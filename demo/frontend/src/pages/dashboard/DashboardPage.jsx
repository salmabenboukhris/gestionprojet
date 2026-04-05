import React, { useState, useEffect } from 'react';
import {
  Row, Col, Card, Table, Tag, Typography, Statistic, Spin, Button,
  Progress, Empty, Tooltip, Badge
} from 'antd';
import {
  ProjectOutlined, CheckCircleOutlined, ClockCircleOutlined,
  EuroCircleOutlined, RiseOutlined, FallOutlined,
  ArrowRightOutlined, BarChartOutlined, TeamOutlined,
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { reportingService } from '../../services/modules/reportingService';
import { projetService } from '../../services/modules/projetService';
import { useAuth } from '../../hooks/useAuth';
import { ROUTES, ROLES } from '../../utils/constants';

const { Title, Text } = Typography;

/* ===================================================
   KPI Card Component
   =================================================== */
const KpiCard = ({ label, value, prefix, suffix, trend, trendLabel, color = '#1a56db', icon }) => (
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
      {trend !== undefined && (
        <span style={{
          display: 'inline-flex', alignItems: 'center', gap: 4,
          fontSize: 12, fontWeight: 600,
          color: trend >= 0 ? '#10b981' : '#ef4444',
          background: trend >= 0 ? '#ecfdf5' : '#fef2f2',
          padding: '2px 8px', borderRadius: 20,
        }}>
          {trend >= 0 ? <RiseOutlined /> : <FallOutlined />}
          {Math.abs(trend)}%
        </span>
      )}
    </div>
    <div style={{ fontSize: 11, fontWeight: 600, letterSpacing: '0.08em', textTransform: 'uppercase', color: '#64748b', marginBottom: 6 }}>
      {label}
    </div>
    <div style={{ fontSize: 30, fontWeight: 800, color: '#0f172a', lineHeight: 1 }}>
      {prefix}{value ?? '—'}{suffix}
    </div>
    {trendLabel && <div style={{ fontSize: 12, color: '#94a3b8', marginTop: 6 }}>{trendLabel}</div>}
  </Card>
);

/* ===================================================
   Project Status Badge
   =================================================== */
const StatusBadge = ({ status }) => {
  const map = {
    EN_COURS:  { color: '#f59e0b', bg: '#fffbeb', label: 'En cours' },
    TERMINE:   { color: '#10b981', bg: '#ecfdf5', label: 'Terminé' },
    EN_RETARD: { color: '#ef4444', bg: '#fef2f2', label: 'En retard' },
    CLOTURE:   { color: '#8b5cf6', bg: '#f5f3ff', label: 'Clôturé' },
    EN_ATTENTE:{ color: '#64748b', bg: '#f1f5f9', label: 'En attente' },
  };
  const cfg = map[status] || { color: '#64748b', bg: '#f1f5f9', label: status };
  return (
    <span style={{
      padding: '3px 10px', borderRadius: 20, fontSize: 12,
      fontWeight: 600, background: cfg.bg, color: cfg.color,
    }}>
      {cfg.label}
    </span>
  );
};

/* ===================================================
   Main Dashboard Page
   =================================================== */
const DashboardPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const userRole = user?.profilCode; // flat field returned by /api/auth/me

  const isAdmin  = userRole === ROLES.ADMIN;
  const isDir    = userRole === ROLES.DIRECTEUR;

  const [tdb, setTdb]             = useState(null);
  const [projetsEnCours, setProjetsEnCours] = useState([]);
  const [projetsClotures, setProjetsClotures] = useState([]);
  const [allProjets, setAllProjets] = useState([]);
  const [loading, setLoading]     = useState(true);

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      try {
        // Always load projects list as fallback KPI source
        const [projList] = await Promise.allSettled([
          projetService.getAll({}),
        ]);
        if (projList.status === 'fulfilled') setAllProjets(projList.value || []);

        // Load reporting data for Admin and Directeur
        if (isAdmin || isDir) {
          const [tdbRes, enCoursRes, closedRes] = await Promise.allSettled([
            reportingService.getTableauDeBord(),
            reportingService.getProjetsEnCours(),
            reportingService.getProjetsClotures(),
          ]);
          if (tdbRes.status === 'fulfilled') setTdb(tdbRes.value);
          if (enCoursRes.status === 'fulfilled') setProjetsEnCours(enCoursRes.value || []);
          if (closedRes.status === 'fulfilled') setProjetsClotures(closedRes.value || []);
        }
      } catch (_) {}
      finally { setLoading(false); }
    };
    loadData();
  }, [isAdmin, isDir]);

  /* ---- Derived KPIs (from projects list or tdb) ---- */
  const totalProjets  = tdb?.totalProjets      ?? allProjets.length;
  const totalPhases   = tdb?.totalPhases       ?? '—';
  const montantFacture= tdb?.montantFacture    ?? '—';
  const montantPaye   = tdb?.montantPaye       ?? '—';

  /* ---- Projects table (recent 10) ---- */
  const recents = (projetsEnCours.length ? projetsEnCours : allProjets).slice(0, 10);

  const projetColumns = [
    {
      title: 'Projet',
      dataIndex: 'nom',
      key: 'nom',
      render: (nom, rec) => (
        <div>
          <div style={{ fontWeight: 600, color: '#0f172a', marginBottom: 2 }}>{nom}</div>
          <div style={{ fontSize: 12, color: '#94a3b8' }}>{rec.code}</div>
        </div>
      ),
    },
    {
      title: 'Organisme',
      dataIndex: ['organisme', 'nom'],
      key: 'organisme',
      render: (nom) => <Text type="secondary" style={{ fontSize: 13 }}>{nom || '—'}</Text>,
    },
    {
      title: 'Chef de Projet',
      key: 'chef',
      render: (_, rec) => {
        const chef = rec.chefProjet;
        if (!chef) return '—';
        return (
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <div style={{
              width: 28, height: 28, borderRadius: '50%', background: '#1a56db',
              color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontSize: 11, fontWeight: 700, flexShrink: 0,
            }}>
              {(chef.prenom?.[0] || '') + (chef.nom?.[0] || '')}
            </div>
            <span style={{ fontSize: 13 }}>{chef.prenom} {chef.nom}</span>
          </div>
        );
      },
    },
    {
      title: 'Statut',
      dataIndex: 'statut',
      key: 'statut',
      render: (s) => <StatusBadge status={s} />,
    },
    {
      title: 'Budget',
      dataIndex: 'montantGlobal',
      key: 'montantGlobal',
      align: 'right',
      render: (m) => m ? (
        <span style={{ fontWeight: 600, color: '#0f172a' }}>
          {Number(m).toLocaleString('fr-FR')} €
        </span>
      ) : '—',
    },
    {
      title: '',
      key: 'action',
      width: 40,
      render: (_, rec) => (
        <Button
          type="text"
          size="small"
          icon={<ArrowRightOutlined />}
          onClick={() => navigate(ROUTES.PROJETS)}
          style={{ color: '#1a56db' }}
        />
      ),
    },
  ];

  /* ---- Render ---- */
  const firstName = user?.prenom || 'Utilisateur';

  return (
    <div>
      {/* Page Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <h1 style={{ fontSize: 26, fontWeight: 800, margin: 0, color: '#0f172a' }}>
            Tableau de bord général
          </h1>
          <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: 14 }}>
            Bienvenue, {firstName}. Voici l'état actuel de vos projets.
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
          {/* ===== KPI ROW ===== */}
          <Row gutter={[20, 20]} style={{ marginBottom: 28 }}>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Total Projets"
                value={totalProjets}
                icon={<ProjectOutlined />}
                color="#1a56db"
                trendLabel="Tous les projets enregistrés"
              />
            </Col>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Projets en cours"
                value={projetsEnCours.length || '—'}
                icon={<ClockCircleOutlined />}
                color="#f59e0b"
                trendLabel="Actuellement actifs"
              />
            </Col>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Projets clôturés"
                value={projetsClotures.length || '—'}
                icon={<CheckCircleOutlined />}
                color="#10b981"
                trendLabel="Projets terminés"
              />
            </Col>
            <Col xs={24} sm={12} xl={6}>
              <KpiCard
                label="Total Phases"
                value={totalPhases}
                icon={<TeamOutlined />}
                color="#8b5cf6"
                trendLabel="Phases de tous les projets"
              />
            </Col>
          </Row>

          {/* ===== Financial KPIs (Admin / Directeur only) ===== */}
          {(isAdmin || isDir) && (montantFacture !== '—' || montantPaye !== '—') && (
            <Row gutter={[20, 20]} style={{ marginBottom: 28 }}>
              <Col xs={24} sm={12}>
                <KpiCard
                  label="Montant facturé"
                  value={montantFacture !== '—' ? Number(montantFacture).toLocaleString('fr-FR') : '—'}
                  suffix=" €"
                  icon={<EuroCircleOutlined />}
                  color="#1a56db"
                  trendLabel="Cumul des phases facturées"
                />
              </Col>
              <Col xs={24} sm={12}>
                <KpiCard
                  label="Montant payé"
                  value={montantPaye !== '—' ? Number(montantPaye).toLocaleString('fr-FR') : '—'}
                  suffix=" €"
                  icon={<CheckCircleOutlined />}
                  color="#10b981"
                  trendLabel="Paiements reçus"
                />
              </Col>
            </Row>
          )}

          {/* ===== Projects Recent Table ===== */}
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
                rowKey="id"
                pagination={false}
                size="middle"
                style={{ borderRadius: 0 }}
              />
            )}
          </Card>

          {/* ===== Quick Actions ===== */}
          <Row gutter={[20, 20]}>
            <Col xs={24} md={8}>
              <Card
                hoverable
                style={{ borderRadius: 12, cursor: 'pointer', textAlign: 'center' }}
                onClick={() => navigate(ROUTES.PROJETS)}
              >
                <ProjectOutlined style={{ fontSize: 32, color: '#1a56db', marginBottom: 12 }} />
                <div style={{ fontWeight: 700, fontSize: 15, marginBottom: 4 }}>Gérer les Projets</div>
                <div style={{ color: '#64748b', fontSize: 13 }}>Créer, modifier et suivre vos projets</div>
              </Card>
            </Col>
            {(isAdmin || isDir) && (
              <Col xs={24} md={8}>
                <Card
                  hoverable
                  style={{ borderRadius: 12, cursor: 'pointer', textAlign: 'center' }}
                  onClick={() => navigate(ROUTES.REPORTING)}
                >
                  <BarChartOutlined style={{ fontSize: 32, color: '#8b5cf6', marginBottom: 12 }} />
                  <div style={{ fontWeight: 700, fontSize: 15, marginBottom: 4 }}>Reporting</div>
                  <div style={{ color: '#64748b', fontSize: 13 }}>Phases, facturation, paiements</div>
                </Card>
              </Col>
            )}
            {(isAdmin) && (
              <Col xs={24} md={8}>
                <Card
                  hoverable
                  style={{ borderRadius: 12, cursor: 'pointer', textAlign: 'center' }}
                  onClick={() => navigate(ROUTES.EMPLOYES)}
                >
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
