import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ROUTES } from '../utils/constants';

/**
 * RoleGuard — Protects routes by user role.
 * @param {string[]} allowedRoles — list of profile codes that can access (e.g. 'ADMIN')
 *
 * NOTE: backend EmployeResponseDto returns a flat field "profilCode" (not profil.code)
 */
const RoleGuard = ({ allowedRoles = [] }) => {
  const { user } = useAuth();

  // If no allowedRoles defined, let everyone through
  if (!allowedRoles.length) return <Outlet />;

  const userRole = user?.profilCode; // e.g. "ADMIN", "SECRETAIRE"

  if (!userRole || !allowedRoles.includes(userRole)) {
    return <Navigate to={ROUTES.UNAUTHORIZED} replace />;
  }

  return <Outlet />;
};

export default RoleGuard;

