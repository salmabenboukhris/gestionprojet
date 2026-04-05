import React from 'react';
import { ROUTES, ROLES } from '../utils/constants';
import { 
  DashboardOutlined, 
  ProjectOutlined, 
  TeamOutlined, 
  BankOutlined, 
  FileTextOutlined, 
  EuroCircleOutlined,
  BarChartOutlined 
} from '@ant-design/icons';

/**
 * Menu configuration strictly aligned with Plan Section 14.5:
 * 
 * - Administrateur: gestion employés, accès global.
 * - Secrétaire: organismes, projets.
 * - Directeur: dashboard, reporting.
 * - Chef de projet: phases, affectations, livrables, documents.
 * - Comptable: factures, reporting.
 */
export const menuConfig = [
  {
    key: ROUTES.DASHBOARD,
    icon: <DashboardOutlined />,
    label: 'Tableau de bord',
    roles: [ROLES.ADMIN, ROLES.DIRECTEUR, ROLES.CHEF_PROJET], // Plan: Directeur + Global
  },
  {
    key: ROUTES.ORGANISMES,
    icon: <BankOutlined />,
    label: 'Organismes',
    roles: [ROLES.ADMIN, ROLES.SECRETAIRE], // Plan: Secrétaire
  },
  {
    key: ROUTES.EMPLOYES,
    icon: <TeamOutlined />,
    label: 'Employés',
    roles: [ROLES.ADMIN], // Plan: Administrateur
  },
  {
    key: ROUTES.PROJETS,
    icon: <ProjectOutlined />,
    label: 'Projets',
    roles: [ROLES.ADMIN, ROLES.SECRETAIRE, ROLES.CHEF_PROJET], // Plan: Secrétaire + Chef
  },
  {
    key: ROUTES.PHASES,
    icon: <ProjectOutlined />,
    label: 'Phases',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET], // Plan: Chef de projet
  },
  {
    key: ROUTES.AFFECTATIONS,
    icon: <TeamOutlined />,
    label: 'Affectations',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET], // Plan: Chef de projet
  },
  {
    key: ROUTES.DOCUMENTS,
    icon: <FileTextOutlined />,
    label: 'Documents',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET], // Plan: Chef de projet
  },
  {
    key: ROUTES.LIVRABLES,
    icon: <FileTextOutlined />,
    label: 'Livrables',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET], // Plan: Chef de projet
  },
  {
    key: ROUTES.FACTURES,
    icon: <EuroCircleOutlined />,
    label: 'Factures',
    roles: [ROLES.ADMIN, ROLES.COMPTABLE], // Plan: Comptable
  },
  {
    key: ROUTES.REPORTING,
    icon: <BarChartOutlined />,
    label: 'Reporting',
    roles: [ROLES.ADMIN, ROLES.DIRECTEUR, ROLES.COMPTABLE], // Plan: Directeur + Comptable
  }
];
