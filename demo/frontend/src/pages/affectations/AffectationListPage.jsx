import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Select, DatePicker, Tooltip, message, Popconfirm, Row, Col, Typography, Empty
} from 'antd';
import { PlusOutlined, DeleteOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import { affectationService } from '../../services/modules/affectationService';
import { phaseService } from '../../services/modules/phaseService';
import { employeService } from '../../services/modules/employeService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';

const { Option } = Select;
const { Title, Text } = Typography;

const AffectationModal = ({ open, onCancel, onSubmit, phases, employes, confirmLoading }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (open) {
      form.resetFields();
    }
  }, [open, form]);

  const handleOk = () => {
    form.validateFields().then(values => {
      onSubmit({
        employeId: values.employeId,
        phaseId: values.phaseId,
        dateDebut: values.dateDebut?.format('YYYY-MM-DD'),
        dateFin: values.dateFin?.format('YYYY-MM-DD'),
      });
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>+ Nouvelle Affectation</div>}
      open={open}
      onCancel={onCancel}
      onOk={handleOk}
      okText="Affecter"
      cancelText="Annuler"
      confirmLoading={confirmLoading}
      width={500}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Form.Item name="employeId" label="Employé" rules={[{ required: true, message: 'Veuillez sélectionner un employé' }]}>
          <Select placeholder="Sélectionner un employé" showSearch optionFilterProp="children" style={{ borderRadius: 8 }}>
            {employes.map(e => <Option key={e.id} value={e.id}>{e.prenom} {e.nom}</Option>)}
          </Select>
        </Form.Item>
        <Form.Item name="phaseId" label="Phase" rules={[{ required: true, message: 'Veuillez sélectionner une phase' }]}>
          <Select placeholder="Sélectionner une phase" showSearch optionFilterProp="children" style={{ borderRadius: 8 }}>
            {phases.map(p => <Option key={p.id} value={p.id}>{p.codePhase || p.id} - {p.libelle || `Phase ${p.id}`}</Option>)}
          </Select>
        </Form.Item>
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
      </Form>
    </Modal>
  );
};

const AffectationListPage = () => {
  const { user } = useAuth();
  const userRole = user?.profilCode;
  const canEdit = [ROLES.ADMIN, ROLES.CHEF_PROJET].includes(userRole);

  const [affectations, setAffectations] = useState([]);
  const [phases, setPhases] = useState([]);
  const [employes, setEmployes] = useState([]);
  
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  
  const [filterType, setFilterType] = useState('phase');
  const [filterId, setFilterId] = useState(null);

  useEffect(() => {
    phaseService.getAll().then(data => setPhases(Array.isArray(data) ? data : [])).catch(() => {});
    employeService.getAll().then(data => setEmployes(Array.isArray(data) ? data : [])).catch(() => {});
  }, []);

  const fetchAffectations = async () => {
    if (!filterId) {
      setAffectations([]);
      return;
    }
    setLoading(true);
    try {
      let data = [];
      if (filterType === 'phase') {
        data = await affectationService.getByPhase(filterId);
      } else {
        data = await affectationService.getByEmploye(filterId);
      }
      setAffectations(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
      message.error("Erreur lors du chargement des affectations");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAffectations();
  }, [filterId, filterType]);

  const handleSubmit = async (data) => {
    setSubmitting(true);
    try {
      const phaseId = data.phaseId;
      const employeId = data.employeId;
      await affectationService.create(phaseId, employeId, data);
      message.success('Affectation créée avec succès');
      setModalOpen(false);
      if ((filterType === 'phase' && filterId === phaseId) || 
          (filterType === 'employe' && filterId === employeId)) {
        fetchAffectations();
      } else if (!filterId) {
        setFilterType('phase');
        setFilterId(phaseId);
      }
    } catch (err) {
      console.error(err);
      message.error(err.response?.data?.message || 'Erreur lors de la création');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (phaseId, employeId) => {
    try {
      await affectationService.delete(phaseId, employeId);
      message.success('Affectation supprimée');
      fetchAffectations();
    } catch (err) {
      console.error(err);
      message.error(err.response?.data?.message || 'Erreur lors de la suppression');
    }
  };

  const columns = [
    {
      title: 'Employé',
      key: 'employe',
      render: (_, rec) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 32, height: 32, borderRadius: '50%', background: '#7c3aed', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 13, fontWeight: 700, flexShrink: 0 }}>
            {rec.employeNom?.[0]}{rec.employePrenom?.[0] || 'E'}
          </div>
          <div>
            <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.employePrenom} {rec.employeNom}</div>
            <div style={{ fontSize: 12, color: '#94a3b8' }}>Matricule: {rec.employeMatricule || '—'}</div>
          </div>
        </div>
      )
    },
    {
      title: 'Phase',
      key: 'phase',
      render: (_, rec) => (
        <div>
          <div style={{ fontWeight: 600, color: '#1e40af' }}>Phase {rec.phaseId}</div>
          <div style={{ fontSize: 12, color: '#64748b' }}>Affecté sur cette phase</div>
        </div>
      )
    },
    {
      title: 'Période',
      key: 'periode',
      render: (_, rec) => {
        const start = rec.dateDebut ? dayjs(rec.dateDebut).format('DD/MM/YYYY') : '—';
        const end = rec.dateFin ? dayjs(rec.dateFin).format('DD/MM/YYYY') : '—';
        return (
          <span style={{ fontWeight: 500, color: '#475569' }}>
            {start} au {end}
          </span>
        );
      }
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 100,
      render: (_, rec) => (
        <Space size="small">
          {canEdit && (
            <Popconfirm 
              title="Supprimer cette affectation ?" 
              onConfirm={() => handleDelete(rec.phaseId, rec.employeId)} 
              okText="Supprimer" 
              cancelText="Annuler" 
              okButtonProps={{ danger: true }}
            >
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
          <Title level={2} style={{ margin: 0, fontWeight: 800, color: '#0f172a' }}>Affectations</Title>
          <Text style={{ color: '#64748b', fontSize: 14 }}>Gestion des ressources sur vos différentes phases projet.</Text>
        </div>
        {canEdit && (
          <Button type="primary" icon={<PlusOutlined />} onClick={() => setModalOpen(true)} size="large" style={{ borderRadius: 8, fontWeight: 600 }}>
            Nouvelle Affectation
          </Button>
        )}
      </div>

      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Row gutter={16} align="middle">
          <Col>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <Select 
                value={filterType} 
                onChange={(val) => { setFilterType(val); setFilterId(null); }} 
                style={{ width: 140, borderRadius: 8 }}
              >
                <Option value="phase">Filtrer par Phase</Option>
                <Option value="employe">Filtrer par Employé</Option>
              </Select>
              
              {filterType === 'phase' ? (
                <Select 
                  placeholder="Sélectionner une phase..." 
                  value={filterId} 
                  onChange={setFilterId} 
                  showSearch 
                  optionFilterProp="children" 
                  style={{ width: 300, borderRadius: 8 }}
                  allowClear
                >
                  {phases.map(p => <Option key={p.id} value={p.id}>{p.codePhase || p.id} - {p.libelle || `Phase ${p.id}`}</Option>)}
                </Select>
              ) : (
                <Select 
                  placeholder="Sélectionner un employé..." 
                  value={filterId} 
                  onChange={setFilterId} 
                  showSearch 
                  optionFilterProp="children" 
                  style={{ width: 300, borderRadius: 8 }}
                  allowClear
                >
                  {employes.map(e => <Option key={e.id} value={e.id}>{e.prenom} {e.nom}</Option>)}
                </Select>
              )}
            </div>
          </Col>
        </Row>
      </Card>

      <Card style={{ borderRadius: 12 }} bodyStyle={{ padding: 0 }}>
        {filterId ? (
          <Table
            dataSource={affectations}
            columns={columns}
            rowKey={(r) => `${r.employeId}_${r.phaseId}`}
            loading={loading}
            pagination={{ pageSize: 8, showSizeChanger: false }}
            size="middle"
            locale={{ emptyText: <div style={{ padding: 40, textAlign: 'center', color: '#94a3b8' }}>Aucune affectation trouvée</div> }}
          />
        ) : (
          <Empty 
            description="Veuillez sélectionner une phase ou un employé."
            style={{ padding: '60px 0' }} 
          />
        )}
      </Card>

      <AffectationModal 
        open={modalOpen} 
        onCancel={() => !submitting && setModalOpen(false)} 
        onSubmit={handleSubmit} 
        phases={phases} 
        employes={employes} 
        confirmLoading={submitting} 
      />
    </div>
  );
};
export default AffectationListPage;
