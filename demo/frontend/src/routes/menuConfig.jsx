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

export const menuConfig = [
  {
    key: ROUTES.DASHBOARD,
    icon: <DashboardOutlined />,
    label: 'Tableau de bord',
    roles: [ROLES.ADMIN, ROLES.DIRECTEUR, ROLES.CHEF_PROJET],
  },
  {
    key: ROUTES.ORGANISMES,
    icon: <BankOutlined />,
    label: 'Organismes',
    roles: [ROLES.ADMIN, ROLES.SECRETAIRE],
  },
  {
    key: ROUTES.EMPLOYES,
    icon: <TeamOutlined />,
    label: 'Employés',
    roles: [ROLES.ADMIN],
  },
  {
    key: ROUTES.PROJETS,
    icon: <ProjectOutlined />,
    label: 'Projets',
    roles: [ROLES.ADMIN, ROLES.SECRETAIRE, ROLES.CHEF_PROJET],
  },
  {
    key: ROUTES.PHASES,
    icon: <ProjectOutlined />,
    label: 'Phases',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET],
  },
  {
    key: ROUTES.AFFECTATIONS,
    icon: <TeamOutlined />,
    label: 'Affectations',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET],
  },
  {
    key: ROUTES.DOCUMENTS,
    icon: <FileTextOutlined />,
    label: 'Documents',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET, ROLES.SECRETAIRE],
  },
  {
    key: ROUTES.LIVRABLES,
    icon: <FileTextOutlined />,
    label: 'Livrables',
    roles: [ROLES.ADMIN, ROLES.CHEF_PROJET],
  },
  {
    key: ROUTES.FACTURES,
    icon: <EuroCircleOutlined />,
    label: 'Factures',
    roles: [ROLES.ADMIN, ROLES.COMPTABLE],
  },
  {
    key: ROUTES.REPORTING,
    icon: <BarChartOutlined />,
    label: 'Reporting',
    roles: [ROLES.ADMIN, ROLES.DIRECTEUR],
  }
];
