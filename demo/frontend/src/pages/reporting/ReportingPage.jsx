import React, { useState, useEffect, useCallback } from 'react';
import {
  Card, Table, Tabs, Typography, Select, Button,
  Space, Row, Col, Empty, Spin
} from 'antd';
import {
  EuroCircleOutlined, CheckCircleOutlined, ClockCircleOutlined,
  ProjectOutlined,
} from '@ant-design/icons';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
  Tooltip as ReTooltip, ResponsiveContainer, Cell, Legend,
  AreaChart, Area,
} from 'recharts';
import { reportingService } from '../../services/modules/reportingService';
import { projetService } from '../../services/modules/projetService';

const { Title, Text } = Typography;
const { Option } = Select;

/* -------- helpers -------- */
const fmtEur = (n) => (n ? Number(n).toLocaleString('fr-FR') + ' €' : '—');
const fmtDate = (d) => (d ? new Date(d).toLocaleDateString('fr-FR') : '—');

const SummaryKpi = ({ label, value, color, icon }) => (
  <Card style={{ borderRadius: 10, border: `1px solid ${color}30`, background: color + '08' }}>
    <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
      <div style={{
        width: 40, height: 40, borderRadius: 9, background: color + '18',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        fontSize: 18, color,
      }}>{icon}</div>
      <div>
        <div style={{ fontSize: 11, textTransform: 'uppercase', letterSpacing: '0.08em', color: '#64748b', marginBottom: 2 }}>{label}</div>
        <div style={{ fontSize: 20, fontWeight: 800, color: '#0f172a' }}>{value}</div>
      </div>
    </div>
  </Card>
);

/* -------- Phase table for reporting tabs -------- */
const PhaseTable = ({ data, loading }) => {
  const columns = [
    {
      title: 'Phase',
      dataIndex: 'libelle',
      key: 'libelle',
      render: (libelle, rec) => (
        <div>
          <div style={{ fontWeight: 600, color: '#0f172a', marginBottom: 2 }}>{libelle}</div>
          <div style={{ fontSize: 12, color: '#94a3b8' }}>Phase #{rec.id}</div>
        </div>
      ),
    },
    {
      title: 'Projet',
      key: 'projet',
      render: (_, rec) => (
        <div>
          <div style={{ fontWeight: 500, fontSize: 13 }}>{rec.projet?.nom || '—'}</div>
          <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.projet?.code || ''}</div>
        </div>
      ),
    },
    {
      title: 'Chef de Projet',
      key: 'chef',
      render: (_, rec) => {
        const chef = rec.projet?.chefProjet;
        return chef ? `${chef.prenom} ${chef.nom}` : '—';
      },
    },
    {
      title: 'Début',
      dataIndex: 'dateDebut',
      render: fmtDate,
    },
    {
      title: 'Fin',
      dataIndex: 'dateFin',
      render: fmtDate,
    },
    {
      title: 'Montant',
      dataIndex: 'montant',
      align: 'right',
      render: (m) => <span style={{ fontWeight: 600 }}>{fmtEur(m)}</span>,
    },
    {
      title: 'Statut',
      dataIndex: 'statut',
      render: (s) => {
        const map = {
          TERMINE: { color: '#10b981', bg: '#ecfdf5', label: 'Terminée' },
          EN_COURS: { color: '#f59e0b', bg: '#fffbeb', label: 'En cours' },
          FACTUREE: { color: '#3b82f6', bg: '#eff6ff', label: 'Facturée' },
          PAYEE: { color: '#10b981', bg: '#ecfdf5', label: 'Payée' },
        };
        const cfg = map[s] || { color: '#64748b', bg: '#f1f5f9', label: s };
        return (
          <span style={{ padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: cfg.bg, color: cfg.color }}>
            {cfg.label}
          </span>
        );
      },
    },
  ];

  if (!loading && (!data || data.length === 0)) {
    return <Empty description="Aucune donnée à afficher" style={{ padding: '40px 0' }} />;
  }

  return (
    <Table
      dataSource={data}
      columns={columns}
      rowKey="id"
      loading={loading}
      pagination={{ pageSize: 8, showSizeChanger: false, showTotal: (total) => `${total} phases` }}
      size="middle"
    />
  );
};

/* -------- Projet Table -------- */
const ProjetTable = ({ data, loading }) => {
  const columns = [
    {
      title: 'Projet',
      dataIndex: 'nom',
      key: 'nom',
      render: (nom, rec) => (
        <div>
          <div style={{ fontWeight: 600, marginBottom: 2 }}>{nom}</div>
          <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.code}</div>
        </div>
      ),
    },
    { title: 'Organisme', dataIndex: ['organisme', 'nom'], key: 'org', render: v => v || '—' },
    {
      title: 'Chef de Projet',
      key: 'chef',
      render: (_, r) => r.chefProjet ? `${r.chefProjet.prenom} ${r.chefProjet.nom}` : '—',
    },
    { title: 'Début', dataIndex: 'dateDebut', render: fmtDate },
    { title: 'Fin', dataIndex: 'dateFin', render: fmtDate },
    {
      title: 'Budget',
      dataIndex: 'montantGlobal',
      align: 'right',
      render: m => <span style={{ fontWeight: 600 }}>{fmtEur(m)}</span>,
    },
    {
      title: 'Statut',
      dataIndex: 'statut',
      render: s => {
        const map = {
          EN_COURS:  { color: '#f59e0b', bg: '#fffbeb', label: 'En cours' },
          TERMINE:   { color: '#10b981', bg: '#ecfdf5', label: 'Terminé' },
          CLOTURE:   { color: '#8b5cf6', bg: '#f5f3ff', label: 'Clôturé' },
          EN_RETARD: { color: '#ef4444', bg: '#fef2f2', label: 'En retard' },
        };
        const cfg = map[s] || { color: '#64748b', bg: '#f1f5f9', label: s };
        return (
          <span style={{ padding: '3px 10px', borderRadius: 20, fontSize: 12, fontWeight: 600, background: cfg.bg, color: cfg.color }}>
            {cfg.label}
          </span>
        );
      },
    },
  ];
  return (
    <Table
      dataSource={data}
      columns={columns}
      rowKey="id"
      loading={loading}
      pagination={{ pageSize: 8, showSizeChanger: false }}
      size="middle"
    />
  );
};

/* ===================================================
   MAIN REPORTING PAGE
   =================================================== */
const ReportingPage = () => {
  const [activeTab, setActiveTab] = useState('phases-tnf');

  // Data state
  const [phasesTerminees, setPhasesTerminees] = useState([]);
  const [phasesFacturees, setPhasesFacturees] = useState([]);
  const [phasesPayees, setPhasesPayees]       = useState([]);
  const [projetsEnCours, setProjetsEnCours]   = useState([]);
  const [projetsClotures, setProjetsClotures] = useState([]);
  const [allProjets, setAllProjets]           = useState([]);
  const [loading, setLoading] = useState({});

  // Filters
  const [filterProjet, setFilterProjet] = useState(null);

  const setLoad = (key, val) => setLoading(prev => ({ ...prev, [key]: val }));

  // Lazy-load each tab
  const loadTab = useCallback(async (key) => {
    if (key === 'phases-tnf' && !phasesTerminees.length) {
      setLoad('phases-tnf', true);
      try { setPhasesTerminees(await reportingService.getPhasesTermineesNonFacturees()); } catch (_) {}
      setLoad('phases-tnf', false);
    }
    if (key === 'phases-fnp' && !phasesFacturees.length) {
      setLoad('phases-fnp', true);
      try { setPhasesFacturees(await reportingService.getPhasesFactureesNonPayees()); } catch (_) {}
      setLoad('phases-fnp', false);
    }
    if (key === 'phases-payees' && !phasesPayees.length) {
      setLoad('phases-payees', true);
      try { setPhasesPayees(await reportingService.getPhasesPayees()); } catch (_) {}
      setLoad('phases-payees', false);
    }
    if (key === 'projets-en-cours' && !projetsEnCours.length) {
      setLoad('projets-en-cours', true);
      try { setProjetsEnCours(await reportingService.getProjetsEnCours()); } catch (_) {}
      setLoad('projets-en-cours', false);
    }
    if (key === 'projets-clotures' && !projetsClotures.length) {
      setLoad('projets-clotures', true);
      try { setProjetsClotures(await reportingService.getProjetsClotures()); } catch (_) {}
      setLoad('projets-clotures', false);
    }
  }, [phasesTerminees.length, phasesFacturees.length, phasesPayees.length, projetsEnCours.length, projetsClotures.length]);

  useEffect(() => {
    loadTab('phases-tnf'); // Load first tab on mount
    projetService.getAll({}).then(setAllProjets).catch(() => {});
  }, []);

  const handleTabChange = (key) => {
    setActiveTab(key);
    loadTab(key);
  };

  /* ---- Summary KPIs ---- */
  const totalTNF = phasesTerminees.reduce((s, p) => s + (p.montant || 0), 0);
  const totalFNP = phasesFacturees.reduce((s, p) => s + (p.montant || 0), 0);
  const totalPay = phasesPayees.reduce((s, p) => s + (p.montant || 0), 0);

  const tabItems = [
    {
      key: 'phases-tnf',
      label: (
        <span>
          <ClockCircleOutlined style={{ color: '#f59e0b', marginRight: 6 }} />
          Terminées non facturées
          {phasesTerminees.length > 0 && (
            <span style={{ marginLeft: 8, background: '#fef3c7', color: '#92400e', padding: '1px 7px', borderRadius: 10, fontSize: 11, fontWeight: 600 }}>
              {phasesTerminees.length}
            </span>
          )}
        </span>
      ),
      children: <PhaseTable data={phasesTerminees} loading={loading['phases-tnf']} />,
    },
    {
      key: 'phases-fnp',
      label: (
        <span>
          <EuroCircleOutlined style={{ color: '#3b82f6', marginRight: 6 }} />
          Facturées non payées
          {phasesFacturees.length > 0 && (
            <span style={{ marginLeft: 8, background: '#dbeafe', color: '#1e40af', padding: '1px 7px', borderRadius: 10, fontSize: 11, fontWeight: 600 }}>
              {phasesFacturees.length}
            </span>
          )}
        </span>
      ),
      children: <PhaseTable data={phasesFacturees} loading={loading['phases-fnp']} />,
    },
    {
      key: 'phases-payees',
      label: (
        <span>
          <CheckCircleOutlined style={{ color: '#10b981', marginRight: 6 }} />
          Phases payées
        </span>
      ),
      children: <PhaseTable data={phasesPayees} loading={loading['phases-payees']} />,
    },
    {
      key: 'projets-en-cours',
      label: (
        <span>
          <ProjectOutlined style={{ color: '#1a56db', marginRight: 6 }} />
          Projets en cours
        </span>
      ),
      children: <ProjetTable data={projetsEnCours} loading={loading['projets-en-cours']} />,
    },
    {
      key: 'projets-clotures',
      label: (
        <span>
          <CheckCircleOutlined style={{ color: '#8b5cf6', marginRight: 6 }} />
          Projets clôturés
        </span>
      ),
      children: <ProjetTable data={projetsClotures} loading={loading['projets-clotures']} />,
    },
  ];

  return (
    <div>
      {/* Page Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <h1 style={{ fontSize: 26, fontWeight: 800, margin: 0, color: '#0f172a' }}>Reporting Général</h1>
          <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>
            Indicateurs de performance, phases facturées, projets clôturés.
          </p>
        </div>
      </div>

      {/* Summary KPI Row */}
      <Row gutter={[20, 20]} style={{ marginBottom: 28 }}>
        <Col xs={24} md={8}>
          <SummaryKpi label="À facturer" value={fmtEur(totalTNF)} color="#f59e0b" icon={<ClockCircleOutlined />} />
        </Col>
        <Col xs={24} md={8}>
          <SummaryKpi label="Facturé non payé" value={fmtEur(totalFNP)} color="#3b82f6" icon={<EuroCircleOutlined />} />
        </Col>
        <Col xs={24} md={8}>
          <SummaryKpi label="Encaissé" value={fmtEur(totalPay)} color="#10b981" icon={<CheckCircleOutlined />} />
        </Col>
      </Row>

      {/* ===== Charts Section ===== */}
      {(totalTNF > 0 || totalFNP > 0 || totalPay > 0) && (
        <Row gutter={[20, 20]} style={{ marginBottom: 28 }}>
          {/* Bar Chart — Montants financiers */}
          <Col xs={24} md={12}>
            <Card
              title={<span style={{ fontWeight: 700, fontSize: 15 }}>💰 Analyse financière des phases</span>}
              style={{ borderRadius: 12 }}
              bodyStyle={{ padding: '8px 16px 16px' }}
            >
              <ResponsiveContainer width="100%" height={220}>
                <BarChart
                  data={[
                    { name: 'À facturer',       value: totalTNF,  fill: '#f59e0b' },
                    { name: 'Facturé non payé', value: totalFNP,  fill: '#3b82f6' },
                    { name: 'Encaissé',         value: totalPay,  fill: '#10b981' },
                  ]}
                  margin={{ top: 10, right: 10, left: 0, bottom: 0 }}
                >
                  <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                  <XAxis dataKey="name" tick={{ fontSize: 11, fill: '#64748b' }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fontSize: 10, fill: '#64748b' }} axisLine={false} tickLine={false}
                    tickFormatter={(v) => v >= 1000 ? `${(v/1000).toFixed(0)}k` : v}
                  />
                  <ReTooltip
                    formatter={(val) => [`${Number(val).toLocaleString('fr-FR')} MAD`]}
                    contentStyle={{ borderRadius: 8, border: '1px solid #e2e8f0', fontSize: 12 }}
                  />
                  <Bar dataKey="value" radius={[6, 6, 0, 0]}>
                    {[
                      { fill: '#f59e0b' }, { fill: '#3b82f6' }, { fill: '#10b981' },
                    ].map((entry, i) => <Cell key={i} fill={entry.fill} />)}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </Card>
          </Col>

          {/* Bar Chart — Nombre de phases par état */}
          <Col xs={24} md={12}>
            <Card
              title={<span style={{ fontWeight: 700, fontSize: 15 }}>📊 Phases par état</span>}
              style={{ borderRadius: 12 }}
              bodyStyle={{ padding: '8px 16px 16px' }}
            >
              <ResponsiveContainer width="100%" height={220}>
                <BarChart
                  layout="vertical"
                  data={[
                    { name: 'Terminées\nnon facturées', value: phasesTerminees.length, fill: '#f59e0b' },
                    { name: 'Facturées\nnon payées',    value: phasesFacturees.length, fill: '#3b82f6' },
                    { name: 'Payées',                  value: phasesPayees.length,    fill: '#10b981' },
                  ]}
                  margin={{ top: 5, right: 20, left: 30, bottom: 5 }}
                >
                  <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" horizontal={false} />
                  <XAxis type="number" allowDecimals={false}
                    tick={{ fontSize: 10, fill: '#64748b' }} axisLine={false} tickLine={false}
                  />
                  <YAxis type="category" dataKey="name"
                    tick={{ fontSize: 10, fill: '#64748b' }} axisLine={false} tickLine={false} width={90}
                  />
                  <ReTooltip
                    formatter={(val) => [`${val} phase(s)`]}
                    contentStyle={{ borderRadius: 8, border: '1px solid #e2e8f0', fontSize: 12 }}
                  />
                  <Bar dataKey="value" radius={[0, 6, 6, 0]}>
                    {[
                      { fill: '#f59e0b' }, { fill: '#3b82f6' }, { fill: '#10b981' },
                    ].map((entry, i) => <Cell key={i} fill={entry.fill} />)}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </Card>
          </Col>
        </Row>
      )}

      <Card style={{ borderRadius: 12 }} bodyStyle={{ padding: '8px 0 0' }}>
        <Tabs
          activeKey={activeTab}
          onChange={handleTabChange}
          items={tabItems}
          style={{ padding: '0 24px' }}
          tabBarStyle={{ marginBottom: 0, borderBottom: '1px solid #e2e8f0' }}
        />
      </Card>
    </div>
  );
};

export default ReportingPage;
