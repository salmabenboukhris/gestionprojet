import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ROUTES } from '../utils/constants';

// Placeholder pour la phase 1
const AuthGuard = () => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  // Pour la phase 1, on laisse passer pour voir l'UI
  // En phase 2, décommenter :
  // if (!isAuthenticated) {
  //   return <Navigate to={ROUTES.LOGIN} replace />;
  // }

  return <Outlet />;
};

export default AuthGuard;
