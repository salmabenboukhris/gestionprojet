import React, { useState, useEffect } from 'react';
import { Card, Typography, Button, Space, message, Modal, Tooltip, Input, Row, Col } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, CalendarOutlined, SearchOutlined, ClearOutlined } from '@ant-design/icons';
import AppTable from '../../components/common/AppTable';
import EmployeModal from '../../components/employes/EmployeModal';
import DisponibiliteModal from '../../components/employes/DisponibiliteModal';
import { employeService } from '../../services/modules/employeService';

const { Title } = Typography;

const EmployeListPage = () => {
  const [employes, setEmployes] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // États de recherche multicritère
  const [searchParams, setSearchParams] = useState({ matricule: '', login: '', email: '', nom: '' });
  
  // États Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDispoModalOpen, setIsDispoModalOpen] = useState(false);
  const [selectedEmploye, setSelectedEmploye] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Charger la liste
  const fetchEmployes = async () => {
    setLoading(true);
    try {
      // Nettoyage des paramètres vides
      const params = {};
      ["matricule", "login", "email", "nom"].forEach(key => {
        if (searchParams[key]) params[key] = searchParams[key];
      });
      
      const data = await employeService.search(params); // mapping strict vers le search implémenté dans le service
      setEmployes(data);
    } catch (error) {
      if (error.response?.status === 404 || error.response?.status === 403) {
        // En cas de 403/404 on évite un crash global
      } else {
        message.error('Erreur lors du chargement des employés');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployes();
  }, [searchParams]);

  // Handle Search Input Change
  const handleFilterChange = (key, value) => {
    setSearchParams(prev => ({ ...prev, [key]: value }));
  };

  const handleClearFilters = () => {
    setSearchParams({ matricule: '', login: '', email: '', nom: '' });
  };

  // Création
  const handleAdd = () => {
    setSelectedEmploye(null);
    setIsModalOpen(true);
  };

  // Édition
  const handleEdit = (record) => {
    setSelectedEmploye(record);
    setIsModalOpen(true);
  };

  // Soumission (Form)
  const handleFormSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedEmploye) {
        // Mode update
        // Le backend exigera le mot de passe s'il a le validateur @NotBlank
        await employeService.update(selectedEmploye.id, data);
        message.success('Employé mis à jour avec succès');
      } else {
        // Mode création
        await employeService.create(data);
        message.success('Employé créé avec succès');
      }
      setIsModalOpen(false);
      fetchEmployes(); // Refresh global list
    } catch (error) {
      // Affichage des erreurs SQL/Unique Constraints que le DTO va bloquer !
      const errorData = error.response?.data;
      if (typeof errorData === 'object' && errorData !== null) {
        // ex: { matricule: "Le matricule est obligatoire", login: "Ce login existe déjà" }
        const msgs = Object.values(errorData).join(', ');
        message.error(errorData.message || msgs || 'Une erreur est survenue (Unicité ou Donnée invalide)');
      } else {
        message.error(error.message || 'Une erreur système est survenue');
      }
    } finally {
      setSubmitting(false);
    }
  };

  // Suppression
  const handleDelete = (id) => {
    Modal.confirm({
      title: 'Êtes-vous sûr de vouloir supprimer cet employé ?',
      content: 'Cette action est irréversible et pourrait échouer s\'il est assigné à un projet.',
      okText: 'Oui, supprimer',
      okType: 'danger',
      cancelText: 'Annuler',
      onOk: async () => {
        try {
          await employeService.delete(id);
          message.success('Employé supprimé avec succès');
          fetchEmployes();
        } catch (error) {
          const msg = error.response?.data?.message || 'Erreur lors de la suppression. Probablement relié à des Affectations existantes.';
          message.error(msg);
        }
      }
    });
  };

  // Définition des colonnes du tableau
  const columns = [
    { title: 'Matricule', dataIndex: 'matricule', key: 'matricule', width: 120 },
    { title: 'Nom', dataIndex: 'nom', key: 'nom' },
    { title: 'Prénom', dataIndex: 'prenom', key: 'prenom' },
    { title: 'Email', dataIndex: 'email', key: 'email' },
    { title: 'Login', dataIndex: 'login', key: 'login' },
    {
      title: 'Actions',
      key: 'actions',
      width: 120,
      render: (_, record) => (
        <Space size="middle">
          <Tooltip title="Modifier">
            <Button 
              type="text" 
              icon={<EditOutlined style={{ color: '#1890ff' }} />} 
              onClick={() => handleEdit(record)} 
            />
          </Tooltip>
          <Tooltip title="Supprimer">
            <Button 
              type="text" 
              danger 
              icon={<DeleteOutlined />} 
              onClick={() => handleDelete(record.id)} 
            />
          </Tooltip>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <Title level={2} style={{ margin: 0 }}>Employés</Title>
        <Space>
          <Button type="default" icon={<CalendarOutlined />} onClick={() => setIsDispoModalOpen(true)}>
            Vérifier Disponibilités
          </Button>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            Nouvel Employé
          </Button>
        </Space>
      </div>

      <Card style={{ marginBottom: 16 }}>
        <Row gutter={16}>
          <Col span={5}>
            <Input 
              placeholder="Matricule" 
              prefix={<SearchOutlined />} 
              value={searchParams.matricule}
              onChange={(e) => handleFilterChange('matricule', e.target.value)} 
            />
          </Col>
          <Col span={5}>
            <Input 
              placeholder="Nom" 
              prefix={<SearchOutlined />} 
              value={searchParams.nom}
              onChange={(e) => handleFilterChange('nom', e.target.value)} 
            />
          </Col>
          <Col span={5}>
            <Input 
              placeholder="Email" 
              prefix={<SearchOutlined />} 
              value={searchParams.email}
              onChange={(e) => handleFilterChange('email', e.target.value)} 
            />
          </Col>
          <Col span={5}>
            <Input 
              placeholder="Login" 
              prefix={<SearchOutlined />} 
              value={searchParams.login}
              onChange={(e) => handleFilterChange('login', e.target.value)} 
            />
          </Col>
          <Col span={4}>
            <Button icon={<ClearOutlined />} onClick={handleClearFilters} block>
              Effacer
            </Button>
          </Col>
        </Row>
      </Card>

      <Card>
        <AppTable 
          dataSource={employes} 
          columns={columns} 
          rowKey="id" 
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      <EmployeModal 
        open={isModalOpen}
        onCancel={() => !submitting && setIsModalOpen(false)}
        employe={selectedEmploye}
        onSubmit={handleFormSubmit}
        confirmLoading={submitting}
      />

      <DisponibiliteModal 
        open={isDispoModalOpen}
        onCancel={() => setIsDispoModalOpen(false)}
      />
    </div>
  );
};

export default EmployeListPage;
