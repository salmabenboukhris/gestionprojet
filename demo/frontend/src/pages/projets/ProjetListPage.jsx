import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Input, Select,
  DatePicker, InputNumber, Tooltip, message, Row, Col,
  Popconfirm
} from 'antd';
import {
  PlusOutlined, EditOutlined, DeleteOutlined, EyeOutlined,
  SearchOutlined, FilterOutlined,
} from '@ant-design/icons';
import dayjs from 'dayjs';
import { projetService } from '../../services/modules/projetService';
import { organismeService } from '../../services/modules/organismeService';
import { employeService } from '../../services/modules/employeService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';

const { Option } = Select;

/* ===================================================
   Status config — Projets don't have a statut field in DTO.
   We derive it from dates (Phase 6 Requirement).
   =================================================== */
const getProjetStatus = (prj) => {
  const today = dayjs().startOf('day');
  const start = dayjs(prj.dateDebut).startOf('day');
  const end   = dayjs(prj.dateFin).endOf('day');

  if (today.isAfter(end)) return { label: 'En retard', color: '#ef4444', bg: '#fef2f2' };
  if (today.isBefore(start)) return { label: 'En attente', color: '#64748b', bg: '#f1f5f9' };
  return { label: 'En cours', color: '#f59e0b', bg: '#fffbeb' };
};

const StatusBadge = ({ projet }) => {
  if (!projet) return null;
  const s = getProjetStatus(projet);
  return (
    <span style={{ padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: s.bg, color: s.color }}>
      {s.label}
    </span>
  );
};


/* ===================================================
   Projet Form Modal
   =================================================== */
const ProjetModal = ({ open, onCancel, onSubmit, projet, organismes, employes, confirmLoading }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      if (projet) {
        form.setFieldsValue({
          code:         projet.code,
          nom:          projet.nom,
          description:  projet.description,
          dateDebut:    projet.dateDebut ? dayjs(projet.dateDebut) : null,
          dateFin:      projet.dateFin   ? dayjs(projet.dateFin)   : null,
          montantGlobal: projet.montant,
          organismeId:  projet.organismeId,
          chefProjetId: projet.chefProjetId,
        });
      } else {
        form.resetFields();
      }
    }
  }, [open, projet, form]);

  const handleOk = () => {
    form.validateFields().then(values => {
      onSubmit({
        code:         values.code,
        nom:          values.nom,
        description:  values.description,
        dateDebut:    values.dateDebut?.format('YYYY-MM-DD'),
        dateFin:      values.dateFin?.format('YYYY-MM-DD'),
        montant:      values.montantGlobal,
        organismeId:  values.organismeId,
        chefProjetId: values.chefProjetId,
      });
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>{projet ? '✏️ Modifier le projet' : '+ Nouveau projet'}</div>}
      open={open}
      onCancel={onCancel}
      onOk={handleOk}
      okText={projet ? 'Mettre à jour' : 'Créer'}
      cancelText="Annuler"
      confirmLoading={confirmLoading}
      width={680}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Row gutter={16}>
          <Col span={8}>
            <Form.Item name="code" label="Code" rules={[{ required: true, message: 'Requis' }]}>
              <Input placeholder="PRJ-001" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={16}>
            <Form.Item name="nom" label="Nom du projet" rules={[{ required: true, message: 'Requis' }]}>
              <Input placeholder="Nom du projet" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        <Form.Item name="description" label="Description">
          <Input.TextArea rows={2} placeholder="Description..." style={{ borderRadius: 8 }} />
        </Form.Item>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="organismeId" label="Organisme" rules={[{ required: true, message: 'Requis' }]}>
              <Select placeholder="Sélectionner un organisme" showSearch optionFilterProp="children">
                {organismes.map(o => <Option key={o.id} value={o.id}>{o.nom}</Option>)}
              </Select>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="chefProjetId" label="Chef de projet" rules={[{ required: true, message: 'Requis' }]}>
              <Select placeholder="Sélectionner un chef" showSearch optionFilterProp="children">
                {employes.map(e => <Option key={e.id} value={e.id}>{e.prenom} {e.nom}</Option>)}
              </Select>
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="dateDebut" label="Date de début" rules={[{ required: true, message: 'Requis' }]}>
              <DatePicker format="DD/MM/YYYY" style={{ width: '100%', borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="dateFin" label="Date de fin" rules={[{ required: true, message: 'Requis' }]}>
              <DatePicker format="DD/MM/YYYY" style={{ width: '100%', borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        <Form.Item name="montantGlobal" label="Montant global (€)">
          <InputNumber
            min={0} style={{ width: '100%', borderRadius: 8 }}
            formatter={v => `${v}`.replace(/\B(?=(\d{3})+(?!\d))/g, ' ')}
            placeholder="0"
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

/* ===================================================
   Detail Modal
   =================================================== */
const ProjetDetailModal = ({ open, onClose, projet }) => {
  if (!projet) return null;
  const fmtDate = d => d ? new Date(d).toLocaleDateString('fr-FR') : '—';
  const fmtEur  = n => n ? Number(n).toLocaleString('fr-FR') + ' €' : '—';
  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18 }}>📋 Détail du projet</div>}
      open={open} onCancel={onClose} footer={null} width={580}
    >
      <div style={{ padding: '8px 0' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 24 }}>
          <div style={{ width: 52, height: 52, borderRadius: 12, background: '#e8f0fe', color: '#1a56db', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 800, fontSize: 18, flexShrink: 0 }}>
            {projet.code?.slice(0, 2).toUpperCase()}
          </div>
          <div>
            <div style={{ fontWeight: 700, fontSize: 20 }}>{projet.nom}</div>
            <div style={{ color: '#64748b' }}>{projet.code}</div>
          </div>
        </div>
        {projet.description && (
          <div style={{ background: '#f8fafc', borderRadius: 8, padding: '12px 16px', color: '#475569', marginBottom: 16 }}>
            {projet.description}
          </div>
        )}
        <Row gutter={[16, 12]}>
          {[
            ['Organisme', projet.organismeNom || '—'],
            ['Chef de projet', projet.chefProjetNomComplet || '—'],
            ['Date début', fmtDate(projet.dateDebut)],
            ['Date fin', fmtDate(projet.dateFin)],
            ['Budget total', fmtEur(projet.montant)],
          ].map(([label, val]) => (
            <Col span={12} key={label}>
              <div style={{ fontSize: 12, color: '#94a3b8', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: 2 }}>{label}</div>
              <div style={{ fontWeight: 600, color: '#0f172a' }}>{val}</div>
            </Col>
          ))}
        </Row>
      </div>
    </Modal>
  );
};

/* ===================================================
   MAIN PAGE
   =================================================== */
const ProjetListPage = () => {
  const { user } = useAuth();
  const userRole = user?.profilCode;
  const canEdit  = [ROLES.ADMIN, ROLES.SECRETAIRE].includes(userRole);

  const [projets, setProjets]       = useState([]);
  const [organismes, setOrganismes] = useState([]);
  const [employes, setEmployes]     = useState([]);
  const [loading, setLoading]       = useState(false);
  const [search, setSearch]         = useState('');

  const [modalOpen, setModalOpen]           = useState(false);
  const [detailOpen, setDetailOpen]         = useState(false);
  const [selectedProjet, setSelectedProjet] = useState(null);
  const [submitting, setSubmitting]         = useState(false);

  const fetchProjets = async () => {
    setLoading(true);
    try {
      const params = {};
      if (search) params.nom = search;
      const data = await projetService.getAll(params);
      setProjets(Array.isArray(data) ? data : []);
    } catch (_) { message.error('Erreur lors du chargement des projets'); }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchProjets(); }, [search]);
  useEffect(() => {
    organismeService.getAll({}).then(d => setOrganismes(Array.isArray(d) ? d : [])).catch(() => {});
    employeService.getAll({}).then(d => setEmployes(Array.isArray(d) ? d : [])).catch(() => {});
  }, []);

  const handleSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedProjet) {
        await projetService.update(selectedProjet.id, data);
        message.success('Projet mis à jour');
      } else {
        await projetService.create(data);
        message.success('Projet créé');
      }
      setModalOpen(false);
      fetchProjets();
    } catch (err) {
      message.error(err.response?.data?.message || 'Erreur');
    } finally { setSubmitting(false); }
  };

  const handleDelete = async (id) => {
    try {
      await projetService.delete(id);
      message.success('Projet supprimé');
      fetchProjets();
    } catch (err) { message.error(err.response?.data?.message || 'Erreur'); }
  };

  /* ---- Columns aligned with ProjetResponseDto ---- */
  const columns = [
    {
      title: 'Projet',
      key: 'projet',
      render: (_, rec) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 38, height: 38, borderRadius: 9, background: '#e8f0fe', color: '#1a56db', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 800, fontSize: 13, flexShrink: 0 }}>
            {rec.code?.slice(0, 2).toUpperCase()}
          </div>
          <div>
            <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.nom}</div>
            <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.code}</div>
          </div>
        </div>
      ),
    },
    {
      title: 'Organisme',
      dataIndex: 'organismeNom', // ← correct flat field
      key: 'organisme',
      render: v => v ? (
        <span style={{ background: '#dbeafe', color: '#1e40af', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600 }}>{v}</span>
      ) : '—',
    },
    {
      title: 'Chef de Projet',
      dataIndex: 'chefProjetNomComplet', // ← correct flat field
      key: 'chef',
      render: v => {
        if (!v) return '—';
        const parts = v.split(' ');
        const initials = parts.map(p => p[0]).join('').toUpperCase().slice(0, 2);
        return (
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <div style={{ width: 28, height: 28, borderRadius: '50%', background: '#7c3aed', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 11, fontWeight: 700 }}>
              {initials}
            </div>
            <span style={{ fontSize: 13 }}>{v}</span>
          </div>
        );
      },
    },
    {
      title: 'Budget',
      dataIndex: 'montant', // ← correct flat field (not montantGlobal)
      key: 'montant',
      align: 'right',
      render: m => m ? <span style={{ fontWeight: 700 }}>{Number(m).toLocaleString('fr-FR')} €</span> : '—',
    },
    {
      title: 'Fin prévue',
      dataIndex: 'dateFin',
      key: 'dateFin',
      render: d => {
        if (!d) return '—';
        const date = new Date(d);
        const past = date < new Date();
        return <span style={{ color: past ? '#ef4444' : '#0f172a', fontWeight: past ? 600 : 400 }}>{date.toLocaleDateString('fr-FR')}</span>;
      },
    },
    {
      title: 'Statut',
      key: 'statut',
      render: (_, rec) => <StatusBadge projet={rec} />,
    },
    {
      title: 'Actions',

      key: 'actions',
      width: 120,
      render: (_, rec) => (
        <Space size="small">
          <Tooltip title="Voir le détail">
            <Button type="text" size="small" icon={<EyeOutlined style={{ color: '#1a56db' }} />} onClick={() => { setSelectedProjet(rec); setDetailOpen(true); }} />
          </Tooltip>
          {canEdit && (
            <Tooltip title="Modifier">
              <Button type="text" size="small" icon={<EditOutlined style={{ color: '#f59e0b' }} />} onClick={() => { setSelectedProjet(rec); setModalOpen(true); }} />
            </Tooltip>
          )}
          {userRole === ROLES.ADMIN && (
            <Popconfirm title="Supprimer ce projet ?" description="Cette action est irréversible." onConfirm={() => handleDelete(rec.id)} okText="Supprimer" cancelText="Annuler" okButtonProps={{ danger: true }}>
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
          <h1 style={{ fontSize: 26, fontWeight: 800, margin: 0, color: '#0f172a' }}>Liste des Projets</h1>
          <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>Vue d'ensemble de la performance opérationnelle.</p>
        </div>
        {canEdit && (
          <Button type="primary" icon={<PlusOutlined />} onClick={() => { setSelectedProjet(null); setModalOpen(true); }} size="large" style={{ borderRadius: 8, fontWeight: 600 }}>
            Nouveau Projet
          </Button>
        )}
      </div>

      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Row gutter={16} align="middle">
          <Col flex="auto" />
          <Col>
            <Input
              prefix={<SearchOutlined style={{ color: '#94a3b8' }} />}
              placeholder="Rechercher par nom..."
              value={search}
              onChange={e => setSearch(e.target.value)}
              allowClear
              style={{ borderRadius: 8, width: 240 }}
            />
          </Col>
        </Row>
      </Card>

      <Card style={{ borderRadius: 12 }} bodyStyle={{ padding: 0 }}>
        <Table
          dataSource={projets}
          columns={columns}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 8, showTotal: t => `Affichage de ${Math.min(8, t)} sur ${t} projets`, showSizeChanger: false }}
          size="middle"
          locale={{ emptyText: <div style={{ padding: 40, textAlign: 'center', color: '#94a3b8' }}>Aucun projet trouvé</div> }}
        />
      </Card>

      <ProjetModal open={modalOpen} onCancel={() => !submitting && setModalOpen(false)} onSubmit={handleSubmit} projet={selectedProjet} organismes={organismes} employes={employes} confirmLoading={submitting} />
      <ProjetDetailModal open={detailOpen} onClose={() => setDetailOpen(false)} projet={selectedProjet} />
    </div>
  );
};

export default ProjetListPage;
