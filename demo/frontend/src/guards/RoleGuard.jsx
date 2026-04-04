import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ROUTES } from '../utils/constants';

// Placeholder pour la phase 1
const RoleGuard = ({ allowedRoles }) => {
  const { user } = useAuth();

  // Pour la phase 1, on laisse passer pour voir l'UI
  // En phase 2, implémenter la logique :
  // if (!user || !allowedRoles.includes(user.profil?.code)) {
  //   return <Navigate to={ROUTES.UNAUTHORIZED} replace />;
  // }

  return <Outlet />;
};

export default RoleGuard;
