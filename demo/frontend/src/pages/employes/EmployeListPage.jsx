import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Input, Select,
  Tooltip, message, Row, Col, Popconfirm, Avatar, Tag
} from 'antd';
import {
  PlusOutlined, EditOutlined, DeleteOutlined, 
  SearchOutlined, ClearOutlined, TeamOutlined,
  MailOutlined, IdcardOutlined, UserOutlined
} from '@ant-design/icons';
import { employeService } from '../../services/modules/employeService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';

const { Option } = Select;

// Hardcoded profils since no ProfilController exists yet,
// but mapped to the standard IDs from DataTestConfig/Database
const PROFILS = [
  { id: 1, code: 'ADMIN', label: 'Administrateur' },
  { id: 2, code: 'SECRETAIRE', label: 'Secrétaire' },
  { id: 3, code: 'CHEF_PROJET', label: 'Chef de Projet' },
  { id: 4, code: 'COMPTABLE', label: 'Comptable' },
  { id: 5, code: 'DIRECTEUR', label: 'Directeur' },
];

/* ===================================================
   Employe Form Modal
   =================================================== */
const EmployeModal = ({ open, onCancel, onSubmit, employe, confirmLoading }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      if (employe) {
        form.setFieldsValue({
          matricule: employe.matricule,
          nom:       employe.nom,
          prenom:    employe.prenom,
          email:     employe.email,
          login:     employe.login,
          profilId:  employe.profilId || PROFILS.find(p => p.code === employe.profilCode)?.id,
          password:  '', 
        });
      } else {
        form.resetFields();
      }
    }
  }, [open, employe, form]);

  const handleOk = () => {
    form.validateFields().then(values => {
      onSubmit(values);
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>{employe ? '✏️ Modifier l\'employé' : '+ Nouvel employé'}</div>}
      open={open} onCancel={onCancel} onOk={handleOk}
      okText={employe ? 'Mettre à jour' : 'Créer'} cancelText="Annuler"
      confirmLoading={confirmLoading} width={640}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="matricule" label="Matricule" rules={[{ required: true, message: 'Requis' }]}>
              <Input prefix={<IdcardOutlined style={{ color: '#94a3b8' }} />} placeholder="EMP-001" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="profilId" label="Rôle / Profil" rules={[{ required: true, message: 'Requis' }]}>
              <Select placeholder="Sélectionner un rôle">
                {PROFILS.map(p => <Option key={p.id} value={p.id}>{p.label}</Option>)}
              </Select>
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="nom" label="Nom" rules={[{ required: true, message: 'Requis' }]}>
              <Input placeholder="Nom" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="prenom" label="Prénom" rules={[{ required: true, message: 'Requis' }]}>
              <Input placeholder="Prénom" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        <Form.Item name="email" label="Email" rules={[{ required: true, message: 'Requis', type: 'email' }]}>
          <Input prefix={<MailOutlined style={{ color: '#94a3b8' }} />} placeholder="email@exemple.com" style={{ borderRadius: 8 }} />
        </Form.Item>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="login" label="Login" rules={[{ required: true, message: 'Requis' }]}>
              <Input prefix={<UserOutlined style={{ color: '#94a3b8' }} />} placeholder="login" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="password" label={employe ? "Nouveau Mot de Passe *" : "Mot de Passe"} rules={[{ required: true, message: 'Requis' }]}>
              <Input.Password placeholder="******" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        {employe && (
          <div style={{ background: '#eff6ff', padding: '10px 14px', borderRadius: 8, fontSize: 13, color: '#1e40af', marginTop: -8, marginBottom: 12 }}>
            💡 Le backend exige de saisir le mot de passe lors d'une modification.
          </div>
        )}
      </Form>
    </Modal>
  );
};

/* ===================================================
   MAIN PAGE
   =================================================== */
const EmployeListPage = () => {
  const { user } = useAuth();
  const isAdmin = user?.profilCode === ROLES.ADMIN;

  const [employes, setEmployes] = useState([]);
  const [loading, setLoading]   = useState(false);
  const [searchParams, setSearchParams] = useState({ matricule: '', login: '', email: '', nom: '' });

  const [modalOpen, setModalOpen]           = useState(false);
  const [selectedEmploye, setSelectedEmploye] = useState(null);
  const [submitting, setSubmitting]         = useState(false);

  const fetchEmployes = async () => {
    setLoading(true);
    try {
      const params = {};
      Object.keys(searchParams).forEach(k => { if (searchParams[k]) params[k] = searchParams[k]; });
      
      // FIX: Call getAll(params) because search(params) doesn't exist in service
      const data = await employeService.getAll(params);
      setEmployes(Array.isArray(data) ? data : []);
    } catch (_) { 
      message.error('Erreur lors du chargement des employés'); 
    } finally { 
      setLoading(false); 
    }
  };

  useEffect(() => { fetchEmployes(); }, [searchParams]);

  const handleClearFilters = () => setSearchParams({ matricule: '', login: '', email: '', nom: '' });

  const handleSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedEmploye) {
        await employeService.update(selectedEmploye.id, data);
        message.success('Employé mis à jour');
      } else {
        await employeService.create(data);
        message.success('Employé créé');
      }
      setModalOpen(false);
      fetchEmployes();
    } catch (err) {
      message.error(err.response?.data?.message || 'Erreur technique');
    } finally { setSubmitting(false); }
  };

  const handleDelete = async (id) => {
    try {
      await employeService.delete(id);
      message.success('Employé supprimé');
      fetchEmployes();
    } catch (err) { message.error(err.response?.data?.message || 'Erreur'); }
  };

  const columns = [
    {
      title: 'Collaborateur',
      key: 'name',
      render: (_, rec) => {
        const initials = [rec.prenom?.[0], rec.nom?.[0]].filter(Boolean).join('').toUpperCase();
        return (
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <Avatar style={{ background: '#e0e7ff', color: '#4338ca', fontWeight: 700 }}>{initials}</Avatar>
            <div>
              <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.prenom} {rec.nom}</div>
              <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.matricule}</div>
            </div>
          </div>
        );
      },
    },
    {
      title: 'Identifiants',
      key: 'ids',
      render: (_, rec) => (
        <div>
          <div style={{ fontSize: 13, display: 'flex', alignItems: 'center', gap: 6 }}>
            <UserOutlined style={{ fontSize: 12, color: '#94a3b8' }} /> {rec.login}
          </div>
          <div style={{ fontSize: 12, color: '#64748b' }}>
            <MailOutlined style={{ fontSize: 11 }} /> {rec.email}
          </div>
        </div>
      ),
    },
    {
       title: 'Rôle',
       dataIndex: 'profilLibelle',
       key: 'role',
       render: (lib, rec) => (
         <Tag color="blue" style={{ borderRadius: 12, padding: '0 10px', fontWeight: 600 }}>{lib || rec.profilCode || 'Utilisateur'}</Tag>
       )
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 100,
      align: 'right',
      render: (_, rec) => (
        <Space size="small">
          <Tooltip title="Modifier">
            <Button type="text" size="small" icon={<EditOutlined style={{ color: '#f59e0b' }} />} onClick={() => { setSelectedEmploye(rec); setModalOpen(true); }} />
          </Tooltip>
          <Popconfirm title="Supprimer ?" onConfirm={() => handleDelete(rec.id)} okText="Oui" cancelText="Non" okButtonProps={{ danger: true }}>
            <Button type="text" size="small" danger icon={<DeleteOutlined />} />
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <h1 style={{ fontSize: 26, fontWeight: 800, margin: 0, color: '#0f172a' }}>Gestion des Employés</h1>
          <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>Consultez et gérez les accès des collaborateurs du système.</p>
        </div>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => { setSelectedEmploye(null); setModalOpen(true); }} size="large" style={{ borderRadius: 8, fontWeight: 600 }}>
          Nouvel Employé
        </Button>
      </div>

      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Row gutter={12} align="middle">
          <Col span={5}>
            <Input placeholder="Matricule" prefix={<SearchOutlined style={{ color: '#94a3b8' }} />} value={searchParams.matricule} onChange={e => setSearchParams(p => ({ ...p, matricule: e.target.value }))} style={{ borderRadius: 8 }} />
          </Col>
          <Col span={5}>
            <Input placeholder="Nom" prefix={<SearchOutlined style={{ color: '#94a3b8' }} />} value={searchParams.nom} onChange={e => setSearchParams(p => ({ ...p, nom: e.target.value }))} style={{ borderRadius: 8 }} />
          </Col>
          <Col span={5}>
            <Input placeholder="Email" prefix={<SearchOutlined style={{ color: '#94a3b8' }} />} value={searchParams.email} onChange={e => setSearchParams(p => ({ ...p, email: e.target.value }))} style={{ borderRadius: 8 }} />
          </Col>
          <Col span={5}>
            <Input placeholder="Login" prefix={<SearchOutlined style={{ color: '#94a3b8' }} />} value={searchParams.login} onChange={e => setSearchParams(p => ({ ...p, login: e.target.value }))} style={{ borderRadius: 8 }} />
          </Col>
          <Col span={4}>
            <Button icon={<ClearOutlined />} onClick={handleClearFilters} block style={{ borderRadius: 8 }}>Vider</Button>
          </Col>
        </Row>
      </Card>

      <Card style={{ borderRadius: 12 }} bodyStyle={{ padding: 0 }}>
        <Table
          dataSource={employes}
          columns={columns}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 8, showTotal: t => `${t} employés`, showSizeChanger: false }}
          size="middle"
        />
      </Card>

      <EmployeModal open={modalOpen} onCancel={() => !submitting && setModalOpen(false)} onSubmit={handleSubmit} employe={selectedEmploye} confirmLoading={submitting} />
    </div>
  );
};

export default EmployeListPage;
