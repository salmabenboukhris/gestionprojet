import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES, ROLES } from '../utils/constants';

// Layouts
import MainLayout from '../layouts/MainLayout';
import AuthLayout from '../layouts/AuthLayout';

// Guards
import AuthGuard from '../guards/AuthGuard';
import RoleGuard from '../guards/RoleGuard';

// Auth Pages
import LoginPage from '../pages/auth/LoginPage';
import ProfilePage from '../pages/auth/ProfilePage';
import ChangePasswordPage from '../pages/auth/ChangePasswordPage';

// Feature Pages
import DashboardPage from '../pages/dashboard/DashboardPage';
import ProjetListPage from '../pages/projets/ProjetListPage';
import EmployeListPage from '../pages/employes/EmployeListPage';
import OrganismeListPage from '../pages/organismes/OrganismeListPage';
import PhaseListPage from '../pages/phases/PhaseListPage';
import AffectationListPage from '../pages/affectations/AffectationListPage';
import LivrableListPage from '../pages/livrables/LivrableListPage';
import DocumentListPage from '../pages/documents/DocumentListPage';
import FactureListPage from '../pages/factures/FactureListPage';
import ReportingPage from '../pages/reporting/ReportingPage';

// Error Pages
import NotFoundPage from '../pages/errors/NotFoundPage';
import UnauthorizedPage from '../pages/errors/UnauthorizedPage';

const { ADMIN, SECRETAIRE, CHEF_PROJET, COMPTABLE, DIRECTEUR } = ROLES;

const AppRoutes = () => {
  return (
    <Routes>
      {/* ============================================
          PUBLIC ROUTES (no authentication required)
          ============================================ */}
      <Route element={<AuthLayout />}>
        <Route path={ROUTES.LOGIN} element={<LoginPage />} />
      </Route>

      {/* ============================================
          PROTECTED ROUTES (authentication required)
          ============================================ */}
      <Route element={<AuthGuard />}>
        <Route element={<MainLayout />}>

          {/* Root → Dashboard */}
          <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />

          {/* Dashboard — visible selon rôle */}
          <Route path={ROUTES.DASHBOARD} element={<DashboardPage />} />

          {/* Profile & Password — tous les utilisateurs connectés */}
          <Route path={ROUTES.PROFILE} element={<ProfilePage />} />
          <Route path={ROUTES.CHANGE_PASSWORD} element={<ChangePasswordPage />} />

          {/* Organismes — Admin, Secrétaire */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, SECRETAIRE]} />}>
            <Route path={ROUTES.ORGANISMES} element={<OrganismeListPage />} />
          </Route>

          {/* Employés — Admin uniquement */}
          <Route element={<RoleGuard allowedRoles={[ADMIN]} />}>
            <Route path={ROUTES.EMPLOYES} element={<EmployeListPage />} />
          </Route>

          {/* Projets — Admin, Secrétaire, Chef de Projet */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, SECRETAIRE, CHEF_PROJET]} />}>
            <Route path={ROUTES.PROJETS} element={<ProjetListPage />} />
          </Route>

          {/* Phases — Admin, Chef de Projet */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, CHEF_PROJET]} />}>
            <Route path={ROUTES.PHASES} element={<PhaseListPage />} />
          </Route>

          {/* Affectations — Admin, Chef de Projet */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, CHEF_PROJET]} />}>
            <Route path={ROUTES.AFFECTATIONS} element={<AffectationListPage />} />
          </Route>

          {/* Livrables — Admin, Chef de Projet */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, CHEF_PROJET]} />}>
            <Route path={ROUTES.LIVRABLES} element={<LivrableListPage />} />
          </Route>

          {/* Documents — Admin, Chef de Projet, Secrétaire */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, CHEF_PROJET, SECRETAIRE]} />}>
            <Route path={ROUTES.DOCUMENTS} element={<DocumentListPage />} />
          </Route>

          {/* Factures — Admin, Comptable */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, COMPTABLE]} />}>
            <Route path={ROUTES.FACTURES} element={<FactureListPage />} />
          </Route>

          {/* Reporting — Admin, Directeur */}
          <Route element={<RoleGuard allowedRoles={[ADMIN, DIRECTEUR]} />}>
            <Route path={ROUTES.REPORTING} element={<ReportingPage />} />
          </Route>

          {/* Unauthorized page — inside main layout */}
          <Route path={ROUTES.UNAUTHORIZED} element={<UnauthorizedPage />} />
        </Route>
      </Route>

      {/* ============================================
          ERROR ROUTES
          ============================================ */}
      <Route path={ROUTES.NOT_FOUND} element={<NotFoundPage />} />
      <Route path="*" element={<Navigate to={ROUTES.NOT_FOUND} replace />} />
    </Routes>
  );
};

export default AppRoutes;
