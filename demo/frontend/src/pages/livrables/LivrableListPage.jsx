import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Input, Select, Tooltip, message, Popconfirm, Row, Col, Typography, Empty
} from 'antd';
import {
  PlusOutlined, EditOutlined, DeleteOutlined, EyeOutlined, SearchOutlined
} from '@ant-design/icons';
import { livrableService } from '../../services/modules/livrableService';
import { phaseService } from '../../services/modules/phaseService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';

const { Option } = Select;
const { Title, Text } = Typography;

const getLivrableStatus = (status) => {
  switch(status) {
    case 'TERMINE': return { label: 'Terminé', color: '#16a34a', bg: '#dcfce7' };
    case 'EN_COURS': return { label: 'En cours', color: '#f59e0b', bg: '#fffbeb' };
    case 'VALIDE': return { label: 'Validé', color: '#0284c7', bg: '#e0f2fe' };
    default: return { label: status || 'En attente', color: '#64748b', bg: '#f1f5f9' };
  }
};

const StatusBadge = ({ status }) => {
  const s = getLivrableStatus(status);
  return (
    <span style={{ padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: s.bg, color: s.color }}>
      {s.label}
    </span>
  );
};

/* ===================================================
   Livrable Form Modal
   =================================================== */
const LivrableModal = ({ open, onCancel, onSubmit, livrable, phases, confirmLoading, initialPhaseId }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      if (livrable) {
        form.setFieldsValue({
          code: livrable.code,
          libelle: livrable.libelle,
          description: livrable.description,
          statut: livrable.statut || 'EN_ATTENTE',
          phaseId: livrable.phaseId || initialPhaseId,
        });
      } else {
        form.resetFields();
        form.setFieldsValue({ statut: 'EN_ATTENTE', phaseId: initialPhaseId });
      }
    }
  }, [open, livrable, form, initialPhaseId]);

  const handleOk = () => {
    form.validateFields().then(values => {
      onSubmit(values);
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>{livrable ? '✏️ Modifier le livrable' : '+ Nouveau livrable'}</div>}
      open={open}
      onCancel={onCancel}
      onOk={handleOk}
      okText={livrable ? 'Mettre à jour' : 'Créer'}
      cancelText="Annuler"
      confirmLoading={confirmLoading}
      width={600}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Row gutter={16}>
          <Col span={8}>
            <Form.Item name="code" label="Code" rules={[{ required: true, message: 'Requis' }]}>
              <Input placeholder="LIV-001" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={16}>
            <Form.Item name="libelle" label="Libellé" rules={[{ required: true, message: 'Requis' }]}>
              <Input placeholder="Nom du livrable" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        <Form.Item name="description" label="Description">
          <Input.TextArea rows={3} placeholder="Description..." style={{ borderRadius: 8 }} />
        </Form.Item>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="phaseId" label="Phase associée" rules={[{ required: true, message: 'Requis' }]}>
              <Select placeholder="Sélectionner une phase" showSearch optionFilterProp="children" style={{ borderRadius: 8 }}>
                {phases.map(p => <Option key={p.id} value={p.id}>{p.codePhase || p.id} - {p.libelle || `Phase ${p.id}`}</Option>)}
              </Select>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="statut" label="Statut" rules={[{ required: true, message: 'Requis' }]}>
              <Select style={{ borderRadius: 8 }}>
                <Option value="EN_ATTENTE">En attente</Option>
                <Option value="EN_COURS">En cours</Option>
                <Option value="TERMINE">Terminé</Option>
                <Option value="VALIDE">Validé</Option>
              </Select>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

/* ===================================================
   Detail Modal
   =================================================== */
const LivrableDetailModal = ({ open, onClose, livrable, phases }) => {
  if (!livrable) return null;
  const targetPhase = phases.find(p => p.id === livrable.phaseId);
  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18 }}>📋 Détail du livrable</div>}
      open={open} onCancel={onClose} footer={null} width={500}
    >
      <div style={{ padding: '8px 0' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 24 }}>
          <div style={{ width: 52, height: 52, borderRadius: 12, background: '#e8f0fe', color: '#1a56db', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 800, fontSize: 18, flexShrink: 0 }}>
            {livrable.code?.slice(0, 2).toUpperCase()}
          </div>
          <div>
            <div style={{ fontWeight: 700, fontSize: 20 }}>{livrable.libelle}</div>
            <div style={{ color: '#64748b' }}>{livrable.code}</div>
          </div>
        </div>
        {livrable.description && (
          <div style={{ background: '#f8fafc', borderRadius: 8, padding: '12px 16px', color: '#475569', marginBottom: 16 }}>
            {livrable.description}
          </div>
        )}
        <Row gutter={[16, 12]}>
          <Col span={12}>
            <div style={{ fontSize: 12, color: '#94a3b8', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: 2 }}>Statut</div>
            <div><StatusBadge status={livrable.statut} /></div>
          </Col>
          <Col span={12}>
            <div style={{ fontSize: 12, color: '#94a3b8', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: 2 }}>Phase associée</div>
            <div style={{ fontWeight: 600, color: '#0f172a' }}>{targetPhase ? targetPhase.libelle || `Phase ${targetPhase.id}` : '—'}</div>
          </Col>
        </Row>
      </div>
    </Modal>
  );
};

/* ===================================================
   MAIN PAGE
   =================================================== */
const LivrableListPage = () => {
  const { user } = useAuth();
  const userRole = user?.profilCode;
  const canEdit = [ROLES.ADMIN, ROLES.CHEF_PROJET].includes(userRole);

  const [livrables, setLivrables] = useState([]);
  const [phases, setPhases] = useState([]);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  
  const [filterPhaseId, setFilterPhaseId] = useState(null);

  const [modalOpen, setModalOpen] = useState(false);
  const [detailOpen, setDetailOpen] = useState(false);
  const [selectedLivrable, setSelectedLivrable] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const fetchLivrables = async () => {
    if (!filterPhaseId) {
      setLivrables([]);
      return;
    }
    setLoading(true);
    try {
      const data = await livrableService.getByPhase(filterPhaseId);
      console.log('Données livrables brut:', data);
      let list = Array.isArray(data) ? data : [];
      if (search) {
        list = list.filter(l => 
          l.libelle?.toLowerCase().includes(search.toLowerCase()) || 
          l.code?.toLowerCase().includes(search.toLowerCase())
        );
      }
      setLivrables(list);
    } catch (_) { message.error('Erreur lors du chargement des livrables'); }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchLivrables(); }, [search, filterPhaseId]);
  
  useEffect(() => {
    phaseService.getAll().then(d => setPhases(Array.isArray(d) ? d : [])).catch(() => {});
  }, []);

  const handleSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedLivrable) {
        await livrableService.update(selectedLivrable.id, data);
        message.success('Livrable mis à jour');
      } else {
        await livrableService.create(data.phaseId, data);
        message.success('Livrable créé');
        if (!filterPhaseId) setFilterPhaseId(data.phaseId);
      }
      setModalOpen(false);
      fetchLivrables();
    } catch (err) {
      console.error(err);
      message.error(err.response?.data?.message || 'Erreur de sauvegarde');
    } finally { setSubmitting(false); }
  };

  const handleDelete = async (id) => {
    try {
      await livrableService.delete(id);
      message.success('Livrable supprimé');
      fetchLivrables();
    } catch (err) { message.error(err.response?.data?.message || 'Erreur lors de la suppression'); }
  };

  const columns = [
    {
      title: 'Livrable',
      key: 'livrable',
      render: (_, rec) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 38, height: 38, borderRadius: 9, background: '#e8f0fe', color: '#1a56db', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 800, fontSize: 13, flexShrink: 0 }}>
            {rec.code?.slice(0, 2).toUpperCase() || 'LV'}
          </div>
          <div>
            <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.libelle}</div>
            <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.code}</div>
          </div>
        </div>
      )
    },
    {
      title: 'Phase',
      dataIndex: 'phaseId',
      key: 'phase',
      render: (phaseId) => {
        const p = phases.find(ph => ph.id === phaseId);
        return p ? <span style={{ color: '#475569', fontWeight: 500 }}>{p.libelle || `Phase ${p.id}`}</span> : '—';
      }
    },
    {
      title: 'Statut',
      dataIndex: 'statut',
      key: 'statut',
      render: (s) => <StatusBadge status={s} />
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 120,
      render: (_, rec) => (
        <Space size="small">
          <Tooltip title="Voir le détail">
            <Button type="text" size="small" icon={<EyeOutlined style={{ color: '#1a56db' }} />} onClick={() => { setSelectedLivrable(rec); setDetailOpen(true); }} />
          </Tooltip>
          {canEdit && (
            <Tooltip title="Modifier">
              <Button type="text" size="small" icon={<EditOutlined style={{ color: '#f59e0b' }} />} onClick={() => { setSelectedLivrable(rec); setModalOpen(true); }} />
            </Tooltip>
          )}
          {canEdit && (
            <Popconfirm title="Supprimer ce livrable ?" onConfirm={() => handleDelete(rec.id)} okText="Supprimer" cancelText="Annuler" okButtonProps={{ danger: true }}>
              <Button type="text" size="small" danger icon={<DeleteOutlined />} />
            </Popconfirm>
          )}
        </Space>
      )
    }
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <Title level={2} style={{ margin: 0, fontWeight: 800, color: '#0f172a' }}>Livrables</Title>
          <Text style={{ color: '#64748b', fontSize: 14 }}>Garantissez la remise de tous les livrables attendus par phase.</Text>
        </div>
        {canEdit && (
          <Button type="primary" icon={<PlusOutlined />} onClick={() => { setSelectedLivrable(null); setModalOpen(true); }} size="large" style={{ borderRadius: 8, fontWeight: 600 }}>
            Nouveau Livrable
          </Button>
        )}
      </div>

      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Row gutter={16} align="middle">
          <Col flex="auto">
            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <span style={{ fontWeight: 600, color: '#475569' }}>Sélectionnez une phase :</span>
              <Select 
                placeholder="Sélectionner une phase..." 
                value={filterPhaseId} 
                onChange={setFilterPhaseId} 
                showSearch 
                optionFilterProp="children" 
                style={{ width: 300, borderRadius: 8 }}
                allowClear
              >
                {phases.map(p => <Option key={p.id} value={p.id}>{p.codePhase || p.id} - {p.libelle || `Phase ${p.id}`}</Option>)}
              </Select>
            </div>
          </Col>
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
        {filterPhaseId ? (
          <Table
            dataSource={livrables}
            columns={columns}
            rowKey="id"
            loading={loading}
            pagination={{ pageSize: 8, showSizeChanger: false }}
            size="middle"
            locale={{ emptyText: <div style={{ padding: 40, textAlign: 'center', color: '#94a3b8' }}>Aucun livrable trouvé pour cette phase</div> }}
          />
        ) : (
          <Empty 
            description="Veuillez sélectionner une phase pour afficher ses livrables."
            style={{ padding: '60px 0' }} 
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          />
        )}
      </Card>

      <LivrableModal open={modalOpen} onCancel={() => !submitting && setModalOpen(false)} onSubmit={handleSubmit} livrable={selectedLivrable} phases={phases} initialPhaseId={filterPhaseId} confirmLoading={submitting} />
      <LivrableDetailModal open={detailOpen} onClose={() => setDetailOpen(false)} livrable={selectedLivrable} phases={phases} />
    </div>
  );
};

export default LivrableListPage;
