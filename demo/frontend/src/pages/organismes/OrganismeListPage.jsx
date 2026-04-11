import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Input,
  Tooltip, message, Row, Col, Popconfirm, Avatar
} from 'antd';
import {
  PlusOutlined, EditOutlined, DeleteOutlined, 
  SearchOutlined, ClearOutlined, BankOutlined,
  MailOutlined, PhoneOutlined, UserOutlined,
  IdcardOutlined
} from '@ant-design/icons';
import { organismeService } from '../../services/modules/organismeService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';
import { organismeSchema, yupSync } from '../../utils/validations';

/* ===================================================
   Organisme Form Modal
   =================================================== */
const OrganismeModal = ({ open, onCancel, onSubmit, organisme, confirmLoading }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      if (organisme) {
        form.setFieldsValue(organisme);
      } else {
        form.resetFields();
      }
    }
  }, [open, organisme, form]);

  const handleOk = () => {
    form.validateFields().then(values => {
      onSubmit(values);
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>{organisme ? '✏️ Modifier l\'organisme' : '+ Nouvel organisme'}</div>}
      open={open} onCancel={onCancel} onOk={handleOk}
      okText={organisme ? 'Mettre à jour' : 'Créer'} cancelText="Annuler"
      confirmLoading={confirmLoading} width={600}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Row gutter={16}>
          <Col span={8}>
            <Form.Item name="code" label="Code" rules={[yupSync(organismeSchema, 'code')]}>
              <Input placeholder="ORG-001" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={16}>
            <Form.Item name="nom" label="Nom de l'organisme" rules={[yupSync(organismeSchema, 'nom')]}>
              <Input placeholder="Nom complet" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        <Form.Item name="nomContact" label="Nom du contact" rules={[yupSync(organismeSchema, 'nomContact')]}>
          <Input prefix={<UserOutlined style={{ color: '#94a3b8' }} />} placeholder="Responsable" style={{ borderRadius: 8 }} />
        </Form.Item>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="emailContact" label="Email de contact" rules={[yupSync(organismeSchema, 'emailContact')]}>
              <Input prefix={<MailOutlined style={{ color: '#94a3b8' }} />} placeholder="contact@exemple.com" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="telephone" label="Téléphone" rules={[yupSync(organismeSchema, 'telephone')]}>
              <Input prefix={<PhoneOutlined style={{ color: '#94a3b8' }} />} placeholder="05XXXXXXXX" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

/* ===================================================
   MAIN PAGE
   =================================================== */
const OrganismeListPage = () => {
  const { user } = useAuth();
  const isAdmin = user?.profilCode === ROLES.ADMIN;
  const canEdit = [ROLES.ADMIN, ROLES.SECRETAIRE].includes(user?.profilCode);

  const [organismes, setOrganismes] = useState([]);
  const [loading, setLoading]       = useState(false);
  const [search, setSearch]         = useState('');

  const [modalOpen, setModalOpen]           = useState(false);
  const [selectedOrganisme, setSelectedOrganisme] = useState(null);
  const [submitting, setSubmitting]         = useState(false);

  const fetchOrganismes = async () => {
    setLoading(true);
    try {
      const params = {};
      if (search) params.nom = search;
      const data = await organismeService.getAll(params);
      setOrganismes((Array.isArray(data) ? [...data] : []).sort((a, b) => b.id - a.id));
    } catch (_) { 
      message.error('Erreur lors du chargement des organismes'); 
    } finally { 
      setLoading(false); 
    }
  };

  useEffect(() => { fetchOrganismes(); }, [search]);

  const handleSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedOrganisme) {
        await organismeService.update(selectedOrganisme.id, data);
        message.success('Organisme mis à jour');
      } else {
        await organismeService.create(data);
        message.success('Organisme créé');
      }
      setModalOpen(false);
      fetchOrganismes();
    } catch (err) {
      message.error(err.response?.data?.message || 'Erreur technique');
    } finally { setSubmitting(false); }
  };

  const handleDelete = async (id) => {
    try {
      await organismeService.delete(id);
      message.success('Organisme supprimé');
      fetchOrganismes();
    } catch (err) { message.error(err.response?.data?.message || 'Erreur'); }
  };

  const columns = [
    {
      title: 'Organisme',
      key: 'org',
      render: (_, rec) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 40, height: 40, borderRadius: 10, background: '#f0fdf4', color: '#16a34a', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18 }}>
            <BankOutlined />
          </div>
          <div>
            <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.nom}</div>
            <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.code}</div>
          </div>
        </div>
      ),
    },
    {
      title: 'Contact',
      key: 'contact',
      render: (_, rec) => (
        <div>
          <div style={{ fontWeight: 500, fontSize: 13, color: '#1e293b' }}>{rec.nomContact || '—'}</div>
          {rec.emailContact && <div style={{ fontSize: 12, color: '#64748b' }}><MailOutlined style={{ fontSize: 11 }} /> {rec.emailContact}</div>}
        </div>
      ),
    },
    {
      title: 'Téléphone',
      dataIndex: 'telephone',
      key: 'tel',
      render: t => t ? <span><PhoneOutlined style={{ color: '#94a3b8', marginRight: 4 }} /> {t}</span> : '—',
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 100,
      align: 'right',
      render: (_, rec) => (
        <Space size="small">
          {canEdit && (
            <Tooltip title="Modifier">
              <Button type="text" size="small" icon={<EditOutlined style={{ color: '#f59e0b' }} />} onClick={() => { setSelectedOrganisme(rec); setModalOpen(true); }} />
            </Tooltip>
          )}
          {isAdmin && (
            <Popconfirm title="Supprimer ?" onConfirm={() => handleDelete(rec.id)} okText="Oui" cancelText="Non" okButtonProps={{ danger: true }}>
              <Button type="text" size="small" danger icon={<DeleteOutlined />} />
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <h1 style={{ fontSize: 26, fontWeight: 800, margin: 0, color: '#0f172a' }}>Gestion des Organismes</h1>
          <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>Gérez les clients et partenaires de vos projets.</p>
        </div>
        {canEdit && (
          <Button type="primary" icon={<PlusOutlined />} onClick={() => { setSelectedOrganisme(null); setModalOpen(true); }} size="large" style={{ borderRadius: 8, fontWeight: 600 }}>
            Nouvel Organisme
          </Button>
        )}
      </div>

      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Row gutter={16} align="middle">
          <Col flex="auto">
            <Input 
              placeholder="Rechercher un organisme par nom..." 
              prefix={<SearchOutlined style={{ color: '#94a3b8' }} />} 
              value={search} 
              onChange={e => setSearch(e.target.value)} 
              allowClear
              style={{ borderRadius: 8, maxWidth: 400 }} 
            />
          </Col>
        </Row>
      </Card>

      <Card style={{ borderRadius: 12 }} bodyStyle={{ padding: 0 }}>
        <Table
          dataSource={organismes}
          columns={columns}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 8, showTotal: t => `${t} organismes`, showSizeChanger: false }}
          size="middle"
        />
      </Card>

      <OrganismeModal open={modalOpen} onCancel={() => !submitting && setModalOpen(false)} onSubmit={handleSubmit} organisme={selectedOrganisme} confirmLoading={submitting} />
    </div>
  );
};

export default OrganismeListPage;
