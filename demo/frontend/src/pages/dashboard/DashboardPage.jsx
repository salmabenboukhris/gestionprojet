import React, { useState, useEffect } from 'react';
import {
  Row, Col, Card, Table, Typography, Spin, Button, Empty,
} from 'antd';
import {
  ProjectOutlined, CheckCircleOutlined, ClockCircleOutlined,
  EuroCircleOutlined, RiseOutlined, FallOutlined,
  ArrowRightOutlined, BarChartOutlined, TeamOutlined,
} from '@ant-design/icons';
import {
  PieChart, Pie, Cell, Tooltip as ReTooltip, Legend,
  BarChart, Bar, XAxis, YAxis, CartesianGrid, ResponsiveContainer,
} from 'recharts';
import { useNavigate } from 'react-router-dom';
import { reportingService } from '../../services/modules/reportingService';
import { projetService } from '../../services/modules/projetService';
import { useAuth } from '../../hooks/useAuth';
import { ROUTES, ROLES } from '../../utils/constants';

const { Text } = Typography;

/* ===================================================
   KPI Card
   =================================================== */
const KpiCard = ({ label, value, suffix, trendLabel, color = '#1a56db', icon }) => (
  <Card hoverable style={{ borderRadius: 12, height: '100%' }}>
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
      <div style={{
        width: 42, height: 42, borderRadius: 10,
        background: color + '18',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        fontSize: 20, color,
      }}>
        {icon}
      </div>
    </div>
    <div style={{ fontSize: 11, fontWeight: 600, letterSpacing: '0.08em', textTransform: 'uppercase', color: '#64748b', marginBottom: 6 }}>
      {label}
    </div>
    <div style={{ fontSize: 30, fontWeight: 800, color: '#0f172a', lineHeight: 1 }}>
      {value ?? '—'}{suffix}
    </div>
    {trendLabel && <div style={{ fontSize: 12, color: '#94a3b8', marginTop: 6 }}>{trendLabel}</div>}
  </Card>
);

/* ===================================================
   Custom Donut Label
   =================================================== */
const renderCustomLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent }) => {
  if (percent < 0.06) return null;
  const RADIAN = Math.PI / 180;
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);
  return (
    <text x={x} y={y} fill="white" textAnchor="middle" dominantBaseline="central" fontSize={13} fontWeight={700}>
      {`${(percent * 100).toFixed(0)}%`}
    </text>
  );
};

/* ===================================================
   Main Dashboard Page
   =================================================== */
const DashboardPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const userRole = user?.profilCode;

  const isAdmin = userRole === ROLES.ADMIN;
  const isDir   = userRole === ROLES.DIRECTEUR;

  const [tdb, setTdb]                         = useState(null);
  const [projetsEnCours, setProjetsEnCours]   = useState([]);
  const [projetsClotures, setProjetsClotures] = useState([]);
  const [allProjets, setAllProjets]           = useState([]);
  const [loading, setLoading]                 = useState(true);

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      try {
        const [projList] = await Promise.allSettled([projetService.getAll({})]);
        if (projList.status === 'fulfilled') setAllProjets(projList.value || []);

        if (isAdmin || isDir) {
          const [tdbRes, enCoursRes, closedRes] = await Promise.allSettled([
            reportingService.getTableauDeBord(),
            reportingService.getProjetsEnCours(),
            reportingService.getProjetsClotures(),
          ]);
          if (tdbRes.status === 'fulfilled')    setTdb(tdbRes.value);
          if (enCoursRes.status === 'fulfilled') setProjetsEnCours(enCoursRes.value || []);
          if (closedRes.status === 'fulfilled')  setProjetsClotures(closedRes.value || []);
        }
      } catch (_) {}
      finally { setLoading(false); }
    };
    loadData();
  }, [isAdmin, isDir]);

  /* ---- Derived values ---- */
  const totalProjets = tdb?.nombreProjets       ?? allProjets.length;
  const totalPhases  = tdb?.nombrePhases        ?? '—';
  const montantTotal = tdb?.montantTotalProjets ?? null;
  const recents      = (projetsEnCours.length ? projetsEnCours : allProjets).slice(0, 8);

  /* ---- Charts data ---- */
  const projetsChartData = [
    { name: 'En cours', value: tdb?.nombreProjetsEnCours  ?? projetsEnCours.length,  fill: '#f59e0b' },
    { name: 'Clôturés', value: tdb?.nombreProjetsClotures ?? projetsClotures.length, fill: '#10b981' },
  ].filter(d => d.value > 0);

  const phasesChartData = [
    { name: 'Terminées\nnon facturées', value: tdb?.nombrePhasesTermineesNonFacturees ?? 0, fill: '#ef4444' },
    { name: 'Facturées\nnon payées',    value: tdb?.nombrePhasesFactureesNonPayees    ?? 0, fill: '#f59e0b' },
    { name: 'Payées',                   value: tdb?.nombrePhasesPayees                ?? 0, fill: '#10b981' },
  ];

  /* ---- Table columns ---- */
  const projetColumns = [
    {
      title: 'Projet',
      key: 'nom',
      render: (_, rec) => (
        <div>
          <div style={{ fontWeight: 600, color: '#0f172a', marginBottom: 2 }}>{rec.nom || rec.projetNom}</div>
          <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.code || rec.projetCode}</div>
        </div>
      ),
    },
    {
      title: 'Chef de Projet',
      key: 'chef',
      render: (_, rec) => {
        const nom = rec.chefProjetNomComplet
          || (rec.chefProjet ? `${rec.chefProjet.prenom || ''} ${rec.chefProjet.nom || ''}`.trim() : null);
        if (!nom) return <Text type="secondary">—</Text>;
        return (
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <div style={{
              width: 28, height: 28, borderRadius: '50%', background: '#1a56db',
              color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontSize: 11, fontWeight: 700,
            }}>
              {nom.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase()}
            </div>
            <span style={{ fontSize: 13 }}>{nom}</span>
          </div>
        );
      },
    },
    {
      title: 'Budget',
      key: 'montant',
      align: 'right',
      render: (_, rec) => {
        const m = rec.montant ?? rec.montantGlobal;
        return m
          ? <span style={{ fontWeight: 600, color: '#0f172a' }}>{Number(m).toLocaleString('fr-FR')} MAD</span>
          : '—';
      },
    },
    {
      title: '',
      key: 'action',
      width: 40,
      render: () => (
        <Button
          type="text" size="small"
          icon={<ArrowRightOutlined />}
          onClick={() => navigate(ROUTES.PROJETS)}
          style={{ color: '#1a56db' }}
        />
      ),
    },
  ];

  return (
    <div>
      {/* ===== Page Header ===== */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <h1 style={{ fontSize: 26, fontWeight: 800, margin: 0, color: '#0f172a' }}>
            Tableau de bord
          </h1>
          <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>
            Bienvenue, {user?.prenom}. Voici l'état actuel de vos projets.
          </p>
        </div>
        {(isAdmin || isDir) && (
          <Button
            type="primary"
            icon={<BarChartOutlined />}
            onClick={() => navigate(ROUTES.REPORTING)}
            style={{ borderRadius: 8 }}
          >
            Reporting complet
          </Button>
        )}
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '80px 0' }}>
          <Spin size="large" tip="Chargement des données..." />
        </div>
      ) : (
        <>
          {/* ===== KPI Row ===== */}
          <Row gutter={[20, 20]} style={{ marginBottom: 28 }}>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Total Projets" value={totalProjets}
                icon={<ProjectOutlined />} color="#1a56db"
                trendLabel="Tous les projets enregistrés"
              />
            </Col>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Projets en cours"
                value={tdb?.nombreProjetsEnCours ?? projetsEnCours.length}
                icon={<ClockCircleOutlined />} color="#f59e0b"
                trendLabel="Actuellement actifs"
              />
            </Col>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Projets clôturés"
                value={tdb?.nombreProjetsClotures ?? projetsClotures.length}
                icon={<CheckCircleOutlined />} color="#10b981"
                trendLabel="Projets terminés"
              />
            </Col>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Total Phases" value={totalPhases}
                icon={<TeamOutlined />} color="#8b5cf6"
                trendLabel="Phases de tous les projets"
              />
            </Col>
          </Row>

          {/* ===== Montant + Phases payées (Admin/Directeur) ===== */}
          {(isAdmin || isDir) && montantTotal !== null && (
            <Row gutter={[20, 20]} style={{ marginBottom: 28 }}>
              <Col xs={24} sm={12}>
                <KpiCard
                  label="Montant total projets"
                  value={Number(montantTotal).toLocaleString('fr-FR')}
                  suffix=" MAD"
                  icon={<EuroCircleOutlined />} color="#1a56db"
                  trendLabel="Valeur contractuelle globale"
                />
              </Col>
              <Col xs={24} sm={12}>
                <KpiCard
                  label="Phases payées"
                  value={tdb?.nombrePhasesPayees ?? '—'}
                  icon={<CheckCircleOutlined />} color="#10b981"
                  trendLabel="Paiements reçus"
                />
              </Col>
            </Row>
          )}

          {/* ===== Charts (Admin / Directeur) ===== */}
          {(isAdmin || isDir) && tdb && (
            <Row gutter={[20, 20]} style={{ marginBottom: 28 }}>

              {/* Donut — Répartition projets */}
              <Col xs={24} md={12}>
                <Card
                  title={<span style={{ fontWeight: 700, fontSize: 15 }}>📊 Répartition des projets</span>}
                  style={{ borderRadius: 12 }}
                  bodyStyle={{ padding: '8px 16px 16px' }}
                >
                  {projetsChartData.length > 0 ? (
                    <ResponsiveContainer width="100%" height={240}>
                      <PieChart>
                        <Pie
                          data={projetsChartData}
                          cx="50%" cy="50%"
                          innerRadius={60} outerRadius={95}
                          paddingAngle={3}
                          dataKey="value"
                          labelLine={false}
                          label={renderCustomLabel}
                        >
                          {projetsChartData.map((entry, i) => (
                            <Cell key={i} fill={entry.fill} />
                          ))}
                        </Pie>
                        <ReTooltip formatter={(val, name) => [`${val} projet(s)`, name]} />
                        <Legend iconType="circle" iconSize={10} />
                      </PieChart>
                    </ResponsiveContainer>
                  ) : (
                    <Empty description="Aucune donnée" style={{ padding: 40 }} />
                  )}
                </Card>
              </Col>

              {/* Bar — État financier des phases */}
              <Col xs={24} md={12}>
                <Card
                  title={<span style={{ fontWeight: 700, fontSize: 15 }}>📈 État financier des phases</span>}
                  style={{ borderRadius: 12 }}
                  bodyStyle={{ padding: '8px 16px 16px' }}
                >
                  <ResponsiveContainer width="100%" height={240}>
                    <BarChart data={phasesChartData} margin={{ top: 10, right: 10, left: -10, bottom: 0 }}>
                      <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                      <XAxis
                        dataKey="name"
                        tick={{ fontSize: 10, fill: '#64748b' }}
                        axisLine={false} tickLine={false}
                      />
                      <YAxis
                        allowDecimals={false}
                        tick={{ fontSize: 11, fill: '#64748b' }}
                        axisLine={false} tickLine={false}
                      />
                      <ReTooltip
                        formatter={(val) => [`${val} phase(s)`]}
                        contentStyle={{ borderRadius: 8, border: '1px solid #e2e8f0', fontSize: 13 }}
                      />
                      <Bar dataKey="value" radius={[6, 6, 0, 0]}>
                        {phasesChartData.map((entry, i) => (
                          <Cell key={i} fill={entry.fill} />
                        ))}
                      </Bar>
                    </BarChart>
                  </ResponsiveContainer>
                </Card>
              </Col>

            </Row>
          )}

          {/* ===== Projets récents table ===== */}
          <Card
            style={{ borderRadius: 12, marginBottom: 24 }}
            title={
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{ fontWeight: 700, fontSize: 16 }}>
                  {projetsEnCours.length ? 'Projets en cours' : 'Projets récents'}
                </span>
                <Button
                  type="link"
                  icon={<ArrowRightOutlined />}
                  onClick={() => navigate(ROUTES.PROJETS)}
                  style={{ color: '#1a56db', fontWeight: 600, paddingRight: 0 }}
                >
                  Voir tout
                </Button>
              </div>
            }
            bodyStyle={{ padding: '0 0 8px' }}
          >
            {recents.length === 0 ? (
              <div style={{ padding: 40 }}>
                <Empty description="Aucun projet trouvé" />
              </div>
            ) : (
              <Table
                dataSource={recents}
                columns={projetColumns}
                rowKey={(r) => r.id ?? r.projetId}
                pagination={{ pageSize: 5, showSizeChanger: false, showTotal: (t) => `${t} projets` }}
                size="middle"
              />
            )}
          </Card>

          {/* ===== Quick Actions ===== */}
          <Row gutter={[20, 20]}>
            <Col xs={24} md={8}>
              <Card hoverable style={{ borderRadius: 12, textAlign: 'center' }} onClick={() => navigate(ROUTES.PROJETS)}>
                <ProjectOutlined style={{ fontSize: 32, color: '#1a56db', marginBottom: 12 }} />
                <div style={{ fontWeight: 700, fontSize: 15, marginBottom: 4 }}>Gérer les Projets</div>
                <div style={{ color: '#64748b', fontSize: 13 }}>Créer, modifier et suivre vos projets</div>
              </Card>
            </Col>
            {(isAdmin || isDir) && (
              <Col xs={24} md={8}>
                <Card hoverable style={{ borderRadius: 12, textAlign: 'center' }} onClick={() => navigate(ROUTES.REPORTING)}>
                  <BarChartOutlined style={{ fontSize: 32, color: '#8b5cf6', marginBottom: 12 }} />
                  <div style={{ fontWeight: 700, fontSize: 15, marginBottom: 4 }}>Reporting</div>
                  <div style={{ color: '#64748b', fontSize: 13 }}>Phases, facturation, paiements</div>
                </Card>
              </Col>
            )}
            {isAdmin && (
              <Col xs={24} md={8}>
                <Card hoverable style={{ borderRadius: 12, textAlign: 'center' }} onClick={() => navigate(ROUTES.EMPLOYES)}>
                  <TeamOutlined style={{ fontSize: 32, color: '#10b981', marginBottom: 12 }} />
                  <div style={{ fontWeight: 700, fontSize: 15, marginBottom: 4 }}>Employés</div>
                  <div style={{ color: '#64748b', fontSize: 13 }}>Gérer les membres de l'équipe</div>
                </Card>
              </Col>
            )}
          </Row>
        </>
      )}
    </div>
  );
};

export default DashboardPage;
