import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Select, DatePicker, InputNumber, Input, Tooltip, message, Popconfirm, Row, Col, Typography, Tag
} from 'antd';
import {
  PlusOutlined, EditOutlined, FileTextOutlined, SearchOutlined, EuroOutlined
} from '@ant-design/icons';
import dayjs from 'dayjs';
import { factureService } from '../../services/modules/factureService';
import { phaseService } from '../../services/modules/phaseService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';

const { Option } = Select;
const { Title, Text } = Typography;

const getFactureStatus = (status) => {
  switch(status) {
    case 'PAYEE': return { label: 'Payée', color: '#16a34a', bg: '#dcfce7' };
    case 'FACTUREE': return { label: 'Facturée', color: '#0284c7', bg: '#e0f2fe' };
    case 'EN_ATTENTE': return { label: 'En attente', color: '#f59e0b', bg: '#fffbeb' };
    case 'ANNULEE': return { label: 'Annulée', color: '#ef4444', bg: '#fef2f2' };
    default: return { label: status || 'Créée', color: '#64748b', bg: '#f1f5f9' };
  }
};

const StatusBadge = ({ status }) => {
  const s = getFactureStatus(status);
  return (
    <span style={{ padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: s.bg, color: s.color }}>
      {s.label}
    </span>
  );
};

/* ===================================================
   Facture Form Modal
   =================================================== */
const FactureModal = ({ open, onCancel, onSubmit, facture, phases, confirmLoading }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      if (facture) {
        form.setFieldsValue({
          code: facture.code || facture.reference, // Support both during transition
          phaseId: facture.phaseId,
          dateFacture: facture.dateFacture ? dayjs(facture.dateFacture) : null,
          montant: facture.montant,
          statut: facture.statut || 'EN_ATTENTE',
        });
      } else {
        form.resetFields();
        form.setFieldsValue({ statut: 'EN_ATTENTE', dateFacture: dayjs() });
      }
    }
  }, [open, facture, form]);

  const handleOk = () => {
    form.validateFields().then(values => {
      onSubmit({
        code: values.code,
        phaseId: values.phaseId,
        dateFacture: values.dateFacture?.format('YYYY-MM-DD'),
        montant: values.montant,
        statut: values.statut,
      });
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>{facture ? '✏️ Modifier la facture' : '+ Nouvelle facture'}</div>}
      open={open}
      onCancel={onCancel}
      onOk={handleOk}
      okText={facture ? 'Mettre à jour' : 'Créer'}
      cancelText="Annuler"
      confirmLoading={confirmLoading}
      width={600}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="code" label="Code" rules={[{ required: true, message: 'Le code est obligatoire' }]}>
              <Input placeholder="FAC-2023-001" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="phaseId" label="Phase associée" rules={[{ required: true, message: 'Requis' }]}>
              <Select placeholder="Sélectionner une phase" showSearch optionFilterProp="children" style={{ borderRadius: 8 }}>
                {phases.map(p => <Option key={p.id} value={p.id}>{p.codePhase || p.id} - {p.libelle || `Phase ${p.id}`}</Option>)}
              </Select>
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={8}>
            <Form.Item name="dateFacture" label="Date de facturation" rules={[{ required: true, message: 'Requis' }]}>
              <DatePicker format="DD/MM/YYYY" style={{ width: '100%', borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={8}>
            <Form.Item name="montant" label="Montant HT (€)" rules={[{ required: true, message: 'Requis' }]}>
              <InputNumber
                min={0} style={{ width: '100%', borderRadius: 8 }}
                formatter={v => `${v}`.replace(/\B(?=(\d{3})+(?!\d))/g, ' ')}
                placeholder="0"
              />
            </Form.Item>
          </Col>
          <Col span={8}>
            <Form.Item name="statut" label="Statut" rules={[{ required: true, message: 'Requis' }]}>
              <Select style={{ borderRadius: 8 }}>
                <Option value="EN_ATTENTE">En attente</Option>
                <Option value="FACTUREE">Facturée</Option>
                <Option value="PAYEE">Payée</Option>
                <Option value="ANNULEE">Annulée</Option>
              </Select>
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
const FactureListPage = () => {
  const { user } = useAuth();
  const userRole = user?.profilCode;
  const canEdit = [ROLES.ADMIN, ROLES.COMPTABLE].includes(userRole);

  const [factures, setFactures] = useState([]);
  const [phases, setPhases] = useState([]);
  
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedFacture, setSelectedFacture] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Filters
  const [search, setSearch] = useState('');
  const [filterStatut, setFilterStatut] = useState(null);

  const fetchFactures = async () => {
    setLoading(true);
    try {
      const data = await factureService.getAll();
      console.log('Données factures brutes:', data);
      let list = Array.isArray(data) ? data : [];
      
      // Apply local filters
      if (search) {
        list = list.filter(f => (f.code || f.reference)?.toLowerCase().includes(search.toLowerCase()));
      }
      if (filterStatut) {
        list = list.filter(f => f.statut === filterStatut);
      }
      
      setFactures(list);
    } catch (err) { 
        console.error(err);
        message.error('Erreur lors du chargement des factures'); 
    }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchFactures(); }, [search, filterStatut]);
  
  useEffect(() => {
    phaseService.getAll().then(d => setPhases(Array.isArray(d) ? d : [])).catch(() => {});
  }, []);

  const handleSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedFacture) {
        await factureService.update(selectedFacture.id, data);
        message.success('Facture mise à jour');
      } else {
        await factureService.create(data.phaseId, data);
        message.success('Facture créée');
      }
      setModalOpen(false);
      fetchFactures();
    } catch (err) {
      console.error(err);
      message.error(err.response?.data?.message || 'Erreur lors de la sauvegarde');
    } finally { setSubmitting(false); }
  };

  const totalsByStatus = factures.reduce((acc, f) => {
    acc[f.statut || 'EN_ATTENTE'] = (acc[f.statut || 'EN_ATTENTE'] || 0) + (f.montant || 0);
    acc['total'] = (acc['total'] || 0) + (f.montant || 0);
    return acc;
  }, {});

  const columns = [
    {
      title: 'Facture',
      key: 'facture',
      render: (_, rec) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 38, height: 38, borderRadius: 9, background: '#f8fafc', border: '1px solid #e2e8f0', color: '#64748b', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 16, flexShrink: 0 }}>
            <FileTextOutlined />
          </div>
          <div>
            <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.code || rec.reference || `FAC-${rec.id}`}</div>
            <div style={{ fontSize: 12, color: '#94a3b8' }}>
              Phase {rec.phaseId}
            </div>
          </div>
        </div>
      )
    },
    {
      title: 'Date',
      dataIndex: 'dateFacture',
      key: 'dateFacture',
      render: d => <span style={{ color: '#475569' }}>{d ? dayjs(d).format('DD/MM/YYYY') : '—'}</span>
    },
    {
      title: 'Montant HT',
      dataIndex: 'montant',
      key: 'montant',
      align: 'right',
      render: m => m != null ? <span style={{ fontWeight: 700, color: '#0f172a' }}>{Number(m).toLocaleString('fr-FR')} €</span> : '—',
    },
    {
      title: 'Statut',
      dataIndex: 'statut',
      key: 'statut',
      render: s => <StatusBadge status={s} />
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 100,
      render: (_, rec) => (
        <Space size="small">
          {canEdit && (
            <Tooltip title="Modifier">
              <Button type="text" size="small" icon={<EditOutlined style={{ color: '#f59e0b' }} />} onClick={() => { setSelectedFacture(rec); setModalOpen(true); }} />
            </Tooltip>
          )}
        </Space>
      )
    }
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <Title level={2} style={{ margin: 0, fontWeight: 800, color: '#0f172a' }}>Factures</Title>
          <Text style={{ color: '#64748b', fontSize: 14 }}>Suivi financier et encours de facturation des projets.</Text>
        </div>
        {canEdit && (
          <Button type="primary" icon={<PlusOutlined />} onClick={() => { setSelectedFacture(null); setModalOpen(true); }} size="large" style={{ borderRadius: 8, fontWeight: 600 }}>
            Nouvelle Facture
          </Button>
        )}
      </div>
      
      {/* Résumé comptable rapide */}
      <Row gutter={16} style={{ marginBottom: 20 }}>
        <Col span={8}>
          <Card style={{ borderRadius: 12, background: 'linear-gradient(135deg, #0284c7 0%, #1e40af 100%)', color: '#fff' }} bodyStyle={{ padding: '20px' }} bordered={false}>
            <div style={{ color: 'rgba(255,255,255,0.7)', fontSize: 13, textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 4 }}>Total Facturé</div>
            <div style={{ fontSize: 24, fontWeight: 800 }}>{Number(totalsByStatus['FACTUREE'] || 0).toLocaleString('fr-FR')} €</div>
          </Card>
        </Col>
        <Col span={8}>
          <Card style={{ borderRadius: 12, background: 'linear-gradient(135deg, #16a34a 0%, #047857 100%)', color: '#fff' }} bodyStyle={{ padding: '20px' }} bordered={false}>
            <div style={{ color: 'rgba(255,255,255,0.7)', fontSize: 13, textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 4 }}>Total Encaissé</div>
            <div style={{ fontSize: 24, fontWeight: 800 }}>{Number(totalsByStatus['PAYEE'] || 0).toLocaleString('fr-FR')} €</div>
          </Card>
        </Col>
        <Col span={8}>
          <Card style={{ borderRadius: 12, background: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)', color: '#fff' }} bodyStyle={{ padding: '20px' }} bordered={false}>
            <div style={{ color: 'rgba(255,255,255,0.7)', fontSize: 13, textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 4 }}>En Attente</div>
            <div style={{ fontSize: 24, fontWeight: 800 }}>{Number(totalsByStatus['EN_ATTENTE'] || 0).toLocaleString('fr-FR')} €</div>
          </Card>
        </Col>
      </Row>

      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Row gutter={16} align="middle">
          <Col flex="auto">
            <Space>
              <Select 
                placeholder="Filtrer par statut" 
                allowClear 
                style={{ width: 180, borderRadius: 8 }}
                value={filterStatut}
                onChange={setFilterStatut}
              >
                <Option value="EN_ATTENTE">En attente</Option>
                <Option value="FACTUREE">Facturée</Option>
                <Option value="PAYEE">Payée</Option>
                <Option value="ANNULEE">Annulée</Option>
              </Select>
            </Space>
          </Col>
          <Col>
            <Input
              prefix={<SearchOutlined style={{ color: '#94a3b8' }} />}
              placeholder="Rechercher (Code)..."
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
          dataSource={factures}
          columns={columns}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 8, showSizeChanger: false }}
          size="middle"
          locale={{ emptyText: <div style={{ padding: 40, textAlign: 'center', color: '#94a3b8' }}>Aucune facture trouvée</div> }}
        />
      </Card>

      <FactureModal open={modalOpen} onCancel={() => !submitting && setModalOpen(false)} onSubmit={handleSubmit} facture={selectedFacture} phases={phases} confirmLoading={submitting} />
    </div>
  );
};

export default FactureListPage;
