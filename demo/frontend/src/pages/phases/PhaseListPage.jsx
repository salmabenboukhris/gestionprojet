import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Input, Select,
  DatePicker, InputNumber, Tooltip, message, Row, Col,
  Popconfirm, Progress, Tag,
} from 'antd';
import {
  PlusOutlined, EditOutlined, DeleteOutlined,
  CheckCircleOutlined, EuroCircleOutlined, DollarOutlined,
  ExclamationCircleOutlined, CheckOutlined, CloseOutlined,
} from '@ant-design/icons';
import dayjs from 'dayjs';
import { phaseService } from '../../services/modules/phaseService';
import { projetService } from '../../services/modules/projetService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';

const { Option } = Select;

/* ===================================================
   Helpers — PhaseResponseDto uses boolean flags, not statut string
   etatRealisation  → true = réalisée
   etatFacturation  → true = facturée
   etatPaiement     → true = payée
   =================================================== */

/** Derive a human status from the three boolean flags */
const getPhaseStatus = (phase) => {
  if (phase.etatPaiement)    return { label: 'Payée',    color: '#8b5cf6', bg: '#f5f3ff' };
  if (phase.etatFacturation) return { label: 'Facturée', color: '#3b82f6', bg: '#eff6ff' };
  if (phase.etatRealisation) return { label: 'Réalisée', color: '#10b981', bg: '#ecfdf5' };
  return                            { label: 'En cours', color: '#f59e0b', bg: '#fffbeb' };
};

const PhaseStatusBadge = ({ phase }) => {
  const s = getPhaseStatus(phase);
  return (
    <span style={{ padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: s.bg, color: s.color }}>
      {s.label}
    </span>
  );
};

const BoolIcon = ({ val }) => val
  ? <CheckOutlined style={{ color: '#10b981', fontWeight: 700 }} />
  : <CloseOutlined style={{ color: '#cbd5e1' }} />;

/* ===================================================
   Phase Form Modal
   =================================================== */
const PhaseModal = ({ open, onCancel, onSubmit, phase, projets, confirmLoading }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      if (phase) {
        form.setFieldsValue({
          projetId:        phase.projetId,
          code:            phase.code,
          libelle:         phase.libelle,
          description:     phase.description,
          dateDebut:       phase.dateDebut ? dayjs(phase.dateDebut) : null,
          dateFin:         phase.dateFin   ? dayjs(phase.dateFin)   : null,
          montant:         phase.montant,
          tauxRealisation: phase.tauxRealisation,
        });
      } else {
        form.resetFields();
      }
    }
  }, [open, phase, form]);

  // Get the selected project's date range to restrict phase dates (Phase 7 Requirement)
  const selectedProjetData = projets.find(p => p.id === form.getFieldValue('projetId'));
  
  const disabledDate = (current) => {
    if (!selectedProjetData) return false;
    const start = dayjs(selectedProjetData.dateDebut).startOf('day');
    const end = dayjs(selectedProjetData.dateFin).endOf('day');
    return current && (current < start || current > end);
  };

  const handleOk = () => {
    form.validateFields().then(values => {
      onSubmit({
        projetId:        values.projetId,
        code:            values.code,
        libelle:         values.libelle,
        description:     values.description,
        dateDebut:       values.dateDebut?.format('YYYY-MM-DD'),
        dateFin:         values.dateFin?.format('YYYY-MM-DD'),
        montant:         values.montant,
        tauxRealisation: values.tauxRealisation ?? 0,
      });
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>{phase ? '✏️ Modifier la phase' : '+ Nouvelle phase'}</div>}
      open={open} onCancel={onCancel} onOk={handleOk}
      okText={phase ? 'Mettre à jour' : 'Créer'} cancelText="Annuler"
      confirmLoading={confirmLoading} width={620}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Form.Item name="projetId" label="Projet" rules={[{ required: true, message: 'Requis' }]}>
          <Select 
            placeholder="Sélectionner le projet" 
            showSearch 
            optionFilterProp="children"
            onChange={() => form.setFieldsValue({ dateDebut: null, dateFin: null })}
          >
            {projets.map(p => <Option key={p.id} value={p.id}>{p.nom} ({p.code})</Option>)}
          </Select>
        </Form.Item>
        <Row gutter={16}>
          <Col span={8}>
            <Form.Item name="code" label="Code">
              <Input placeholder="PH-01" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={16}>
            <Form.Item name="libelle" label="Libellé" rules={[{ required: true, message: 'Requis' }]}>
              <Input placeholder="Description de la phase" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        <Form.Item name="description" label="Description">
          <Input.TextArea rows={2} style={{ borderRadius: 8 }} />
        </Form.Item>
        <Row gutter={16}>
          <Col span={12}>
            <Form.Item name="dateDebut" label="Date de début" rules={[{ required: true, message: 'Requis' }]}>
              <DatePicker format="DD/MM/YYYY" style={{ width: '100%', borderRadius: 8 }} disabledDate={disabledDate} />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="dateFin" label="Date de fin" rules={[{ required: true, message: 'Requis' }]}>
              <DatePicker format="DD/MM/YYYY" style={{ width: '100%', borderRadius: 8 }} disabledDate={disabledDate} />
            </Form.Item>
          </Col>
        </Row>

        <Row gutter={16}>
          <Col span={18}>
            <Form.Item name="montant" label="Montant (€)" rules={[{ required: true, message: 'Requis' }]}>
              <InputNumber min={0} style={{ width: '100%', borderRadius: 8 }} formatter={v => `${v}`.replace(/\B(?=(\d{3})+(?!\d))/g, ' ')} placeholder="0" />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item name="tauxRealisation" label="Taux %">
              <InputNumber min={0} max={100} style={{ width: '100%', borderRadius: 8 }} formatter={v => `${v}%`} parser={v => v.replace('%', '')} placeholder="0" />
            </Form.Item>
          </Col>
        </Row>

      </Form>
    </Modal>
  );
};

/* ===================================================
   Action Button
   =================================================== */
const ActionBtn = ({ icon, label, color, onClick, disabled }) => (
  <Tooltip title={label}>
    <Button
      size="small" icon={icon} disabled={disabled} onClick={onClick}
      style={{ borderRadius: 6, fontSize: 11, fontWeight: 600, color: disabled ? '#cbd5e1' : color, borderColor: disabled ? '#e2e8f0' : color + '40', background: disabled ? 'transparent' : color + '10' }}
    >{label}</Button>
  </Tooltip>
);

/* ===================================================
   MAIN PAGE
   =================================================== */
const PhaseListPage = () => {
  const { user } = useAuth();
  const userRole = user?.profilCode;
  const canEdit  = [ROLES.ADMIN, ROLES.CHEF_PROJET].includes(userRole);

  const [phases, setPhases]   = useState([]);
  const [projets, setProjets] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filterProjet, setFilterProjet] = useState(null);

  const [modalOpen, setModalOpen]         = useState(false);
  const [selectedPhase, setSelectedPhase] = useState(null);
  const [submitting, setSubmitting]       = useState(false);

  const fetchPhases = async () => {
    setLoading(true);
    try {
      const params = {};
      if (filterProjet) params.projetId = filterProjet;
      const data = await phaseService.getAll(params);
      // LIFO — les plus récents en premier
      const sorted = Array.isArray(data) ? [...data].sort((a, b) => b.id - a.id) : [];
      setPhases(sorted);
    } catch (err) {
      message.error('Erreur lors du chargement des phases : ' + (err.response?.data?.message || err.message));
    }
    finally { setLoading(false); }
  };

  useEffect(() => { fetchPhases(); }, [filterProjet]);
  useEffect(() => { projetService.getAll({}).then(d => setProjets(Array.isArray(d) ? d : [])).catch(() => {}); }, []);

  const handleSubmit = async (data) => {
    setSubmitting(true);
    try {
      if (selectedPhase) {
        // Update uses PUT /api/phases/{id} — projetId not needed in URL
        await phaseService.update(selectedPhase.id, data);
        message.success('Phase mise à jour');
      } else {
        // Create uses POST /api/projets/{projetId}/phases
        const { projetId, ...phaseData } = data;
        await phaseService.create(projetId, phaseData);
        message.success('Phase créée');
      }
      setModalOpen(false);
      fetchPhases();
    } catch (err) { message.error(err.response?.data?.message || 'Erreur'); }
    finally { setSubmitting(false); }
  };

  const handleDelete = async (id) => {
    try { await phaseService.delete(id); message.success('Phase supprimée'); fetchPhases(); }
    catch (err) { message.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleAction = async (action, phase) => {
    const labels = { realise: 'Marquer comme réalisée', facture: 'Marquer comme facturée', paye: 'Marquer comme payée' };
    Modal.confirm({
      title: labels[action],
      icon: <ExclamationCircleOutlined />,
      content: `Confirmer le changement pour "${phase.libelle}" ?`,
      okText: 'Confirmer', cancelText: 'Annuler',
      onOk: async () => {
        try {
          if (action === 'realise') await phaseService.markAsRealise(phase.id);
          if (action === 'facture') await phaseService.markAsFacture(phase.id);
          if (action === 'paye')    await phaseService.markAsPaye(phase.id);
          message.success('Statut mis à jour');
          fetchPhases();
        } catch (err) { message.error(err.response?.data?.message || 'Erreur'); }
      },
    });
  };

  const fmtDate = d => d ? new Date(d).toLocaleDateString('fr-FR') : '—';

  const columns = [
    {
      title: 'Phase',
      key: 'phase',
      render: (_, rec) => (
        <div>
          <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.libelle}</div>
          <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.code || `Phase #${rec.id}`}</div>
        </div>
      ),
    },
    {
      title: 'Projet',
      key: 'projet',
      render: (_, rec) => (
        <span style={{ background: '#dbeafe', color: '#1e40af', padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600 }}>
          {rec.projetNom || rec.projetCode || '—'}
        </span>
      ),
    },
    {
      title: 'Période',
      key: 'periode',
      render: (_, rec) => <span style={{ fontSize: 13, color: '#475569' }}>{fmtDate(rec.dateDebut)} → {fmtDate(rec.dateFin)}</span>,
    },
    {
      title: 'Réalisation',
      dataIndex: 'tauxRealisation',
      key: 'taux',
      width: 130,
      render: (taux) => {
        const val = taux ?? 0;
        const color = val >= 100 ? '#10b981' : val >= 50 ? '#1a56db' : '#f59e0b';
        return (
          <div>
            <Progress percent={val} size="small" strokeColor={color} showInfo={false} style={{ marginBottom: 2 }} />
            <span style={{ fontSize: 12, color, fontWeight: 600 }}>{val}%</span>
          </div>
        );
      },
    },
    {
      title: 'Montant',
      dataIndex: 'montant',
      align: 'right',
      render: m => m ? <span style={{ fontWeight: 700 }}>{Number(m).toLocaleString('fr-FR')} €</span> : '—',
    },
    {
      title: 'Statut',
      key: 'statut',
      render: (_, rec) => <PhaseStatusBadge phase={rec} />,
    },
    {
      title: 'Étapes',
      key: 'etapes',
      render: (_, rec) => (
        <Space size={6}>
          <Tooltip title="Réalisée">  <BoolIcon val={rec.etatRealisation} /></Tooltip>
          <Tooltip title="Facturée">  <BoolIcon val={rec.etatFacturation} /></Tooltip>
          <Tooltip title="Payée">     <BoolIcon val={rec.etatPaiement} /></Tooltip>
        </Space>
      ),
    },
    {
      title: 'Actions métier',
      key: 'metier',
      width: 220,
      render: (_, rec) => {
        if (!canEdit) return null;
        return (
          <Space size={4} wrap>
            <ActionBtn icon={<CheckCircleOutlined />} label="Réalisée" color="#10b981" onClick={() => handleAction('realise', rec)} disabled={rec.etatRealisation} />
            <ActionBtn icon={<EuroCircleOutlined />}  label="Facturée" color="#3b82f6" onClick={() => handleAction('facture', rec)} disabled={!rec.etatRealisation || rec.etatFacturation} />
            <ActionBtn icon={<DollarOutlined />}      label="Payée"    color="#8b5cf6" onClick={() => handleAction('paye',    rec)} disabled={!rec.etatFacturation || rec.etatPaiement} />
          </Space>
        );
      },
    },
    {
      title: '',
      key: 'edit',
      width: 70,
      render: (_, rec) => (
        <Space size="small">
          {canEdit && <Button type="text" size="small" icon={<EditOutlined style={{ color: '#f59e0b' }} />} onClick={() => { setSelectedPhase(rec); setModalOpen(true); }} />}
          {userRole === ROLES.ADMIN && (
            <Popconfirm title="Supprimer cette phase ?" onConfirm={() => handleDelete(rec.id)} okText="Supprimer" cancelText="Annuler" okButtonProps={{ danger: true }}>
              <Button type="text" size="small" danger icon={<DeleteOutlined />} />
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  const kpiData = [
    { label: 'Total',     count: phases.length, color: '#1a56db' },
    { label: 'Réalisées', count: phases.filter(p => p.etatRealisation).length, color: '#10b981' },
    { label: 'Facturées', count: phases.filter(p => p.etatFacturation).length, color: '#3b82f6' },
    { label: 'Payées',    count: phases.filter(p => p.etatPaiement).length,    color: '#8b5cf6' },
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <h1 style={{ fontSize: 26, fontWeight: 800, margin: 0, color: '#0f172a' }}>Phases de projet</h1>
          <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>
            Suivez la <span style={{ color: '#10b981', fontWeight: 600 }}>réalisation</span>, <span style={{ color: '#3b82f6', fontWeight: 600 }}>facturation</span> et <span style={{ color: '#8b5cf6', fontWeight: 600 }}>paiement</span> de chaque phase.
          </p>
        </div>
        {canEdit && (
          <Button type="primary" icon={<PlusOutlined />} size="large" onClick={() => { setSelectedPhase(null); setModalOpen(true); }} style={{ borderRadius: 8, fontWeight: 600 }}>
            Nouvelle Phase
          </Button>
        )}
      </div>

      {/* KPI Row */}
      <Row gutter={[16, 16]} style={{ marginBottom: 20 }}>
        {kpiData.map(k => (
          <Col key={k.label} xs={12} md={6}>
            <Card bodyStyle={{ padding: '16px 20px' }} style={{ borderRadius: 10, borderTop: `3px solid ${k.color}` }}>
              <div style={{ fontSize: 11, color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: 4 }}>{k.label}</div>
              <div style={{ fontSize: 26, fontWeight: 800, color: k.color }}>{k.count}</div>
            </Card>
          </Col>
        ))}
      </Row>

      {/* Filter */}
      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Select
          placeholder="Filtrer par projet"
          allowClear
          value={filterProjet}
          onChange={setFilterProjet}
          style={{ width: 260 }}
          showSearch
          optionFilterProp="children"
        >
          {projets.map(p => <Option key={p.id} value={p.id}>{p.nom}</Option>)}
        </Select>
      </Card>

      {/* Table */}
      <Card style={{ borderRadius: 12 }} bodyStyle={{ padding: 0 }}>
        <Table
          dataSource={phases}
          columns={columns}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 8, showTotal: t => `${t} phases`, showSizeChanger: false }}
          size="middle"
          locale={{ emptyText: <div style={{ padding: 40, textAlign: 'center', color: '#94a3b8' }}>Aucune phase trouvée</div> }}
        />
      </Card>

      <PhaseModal open={modalOpen} onCancel={() => !submitting && setModalOpen(false)} onSubmit={handleSubmit} phase={selectedPhase} projets={projets} confirmLoading={submitting} />
    </div>
  );
};

export default PhaseListPage;
