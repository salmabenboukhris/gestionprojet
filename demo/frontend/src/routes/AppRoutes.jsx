import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES } from '../utils/constants';

// Layouts
import MainLayout from '../layouts/MainLayout';
import AuthLayout from '../layouts/AuthLayout';

// Guards
import AuthGuard from '../guards/AuthGuard';
import RoleGuard from '../guards/RoleGuard';

// Pages
import LoginPage from '../pages/auth/LoginPage';
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
import NotFoundPage from '../pages/errors/NotFoundPage';
import UnauthorizedPage from '../pages/errors/UnauthorizedPage';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Routes Publiques / Auth */}
      <Route element={<AuthLayout />}>
        <Route path={ROUTES.LOGIN} element={<LoginPage />} />
      </Route>

      {/* Routes Protégées (Toute l'application) */}
      <Route element={<AuthGuard />}>
        <Route element={<MainLayout />}>
          
          <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />
          
          {/* Dashboard */}
          <Route path={ROUTES.DASHBOARD} element={<DashboardPage />} />
          
          {/* Projets */}
          <Route path={ROUTES.PROJETS} element={<ProjetListPage />} />
          
          {/* Organismes */}
          <Route path={ROUTES.ORGANISMES} element={<OrganismeListPage />} />
          
          {/* Employes */}
          <Route path={ROUTES.EMPLOYES} element={<EmployeListPage />} />
          
          {/* Phases */}
          <Route path={ROUTES.PHASES} element={<PhaseListPage />} />
          
          {/* Affectations */}
          <Route path={ROUTES.AFFECTATIONS} element={<AffectationListPage />} />
          
          {/* Livrables */}
          <Route path={ROUTES.LIVRABLES} element={<LivrableListPage />} />
          
          {/* Documents */}
          <Route path={ROUTES.DOCUMENTS} element={<DocumentListPage />} />
          
          {/* Factures */}
          <Route path={ROUTES.FACTURES} element={<FactureListPage />} />
          
          {/* Reporting */}
          <Route path={ROUTES.REPORTING} element={<ReportingPage />} />

          {/* Errors */}
          <Route path={ROUTES.UNAUTHORIZED} element={<UnauthorizedPage />} />
        </Route>
      </Route>

      {/* Not Found */}
      <Route path="*" element={<Navigate to={ROUTES.NOT_FOUND} replace />} />
      <Route path={ROUTES.NOT_FOUND} element={<AuthLayout />} >
        <Route index element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
