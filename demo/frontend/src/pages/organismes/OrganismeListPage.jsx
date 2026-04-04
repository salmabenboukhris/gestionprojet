import React, { useState, useEffect } from 'react';
import { Card, Typography, Button, Space, message, Modal, Tooltip } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import AppTable from '../../components/common/AppTable';
import AppSearchBar from '../../components/common/AppSearchBar';
import AppLoader from '../../components/common/AppLoader';
import OrganismeModal from '../../components/organismes/OrganismeModal';
import { organismeService } from '../../services/modules/organismeService';

const { Title } = Typography;

const OrganismeListPage = () => {
  const [organismes, setOrganismes] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // États de recherche
  const [searchParams, setSearchParams] = useState({ nom: '' });
  
  // États Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedOrganisme, setSelectedOrganisme] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Charger la liste
  const fetchOrganismes = async () => {
    setLoading(true);
    try {
      // Nettoyage des paramètres vides
      const params = {};
      if (searchParams.nom) params.nom = searchParams.nom;
      
      const data = await organismeService.getAll(params);
      setOrganismes(data);
    } catch (error) {
      message.error('Erreur lors du chargement des organismes');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrganismes();
  }, [searchParams]);

  // Handle Search
  const handleSearch = (value) => {
    setSearchParams({ nom: value });
  };

  // Création
  const handleAdd = () => {
    setSelectedOrganisme(null);
    setIsModalOpen(true);
  };

  // Édition
  const handleEdit = (record) => {
    setSelectedOrganisme(record);
    setIsModalOpen(true);
  };

  // Soumission (Form)
  const handleFormSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedOrganisme) {
        // Mode update
        await organismeService.update(selectedOrganisme.id, data);
        message.success('Organisme mis à jour avec succès');
      } else {
        // Mode création
        await organismeService.create(data);
        message.success('Organisme créé avec succès');
      }
      setIsModalOpen(false);
      fetchOrganismes(); // Refresh global list
    } catch (error) {
      const msg = error.response?.data?.message || 'Une erreur est survenue';
      message.error(msg);
    } finally {
      setSubmitting(false);
    }
  };

  // Suppression
  const handleDelete = (id) => {
    Modal.confirm({
      title: 'Êtes-vous sûr de vouloir supprimer cet organisme ?',
      content: 'Cette action est irréversible.',
      okText: 'Oui, supprimer',
      okType: 'danger',
      cancelText: 'Annuler',
      onOk: async () => {
        try {
          await organismeService.delete(id);
          message.success('Organisme supprimé avec succès');
          fetchOrganismes();
        } catch (error) {
          const msg = error.response?.data?.message || 'Erreur lors de la suppression';
          message.error(msg);
        }
      }
    });
  };

  // Définition des colonnes du tableau
  const columns = [
    { title: 'Code', dataIndex: 'code', key: 'code', width: 120 },
    { title: 'Nom', dataIndex: 'nom', key: 'nom' },
    { title: 'Contact', dataIndex: 'nomContact', key: 'nomContact' },
    { title: 'Téléphone', dataIndex: 'telephone', key: 'telephone' },
    { title: 'Email', dataIndex: 'emailContact', key: 'emailContact' },
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
        <Title level={2} style={{ margin: 0 }}>Organismes</Title>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          Nouvel Organisme
        </Button>
      </div>

      <Card>
        <div style={{ marginBottom: 16, width: 300 }}>
          <AppSearchBar 
            placeholder="Rechercher par nom..." 
            onSearch={handleSearch} 
            enterButton={<SearchOutlined />} 
          />
        </div>

        {loading && !organismes.length ? (
          <AppLoader fullScreen={false} />
        ) : (
          <AppTable 
            dataSource={organismes} 
            columns={columns} 
            rowKey="id" 
            loading={loading}
            pagination={{ pageSize: 10 }}
          />
        )}
      </Card>

      <OrganismeModal 
        open={isModalOpen}
        onCancel={() => !submitting && setIsModalOpen(false)}
        organisme={selectedOrganisme}
        onSubmit={handleFormSubmit}
        confirmLoading={submitting}
      />
    </div>
  );
};

export default OrganismeListPage;
