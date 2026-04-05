import React, { createContext, useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/modules/authService';
import { storage } from '../utils/storage';
import { ROUTES } from '../utils/constants';
import { message } from 'antd';

export const AuthContext = createContext({
  user: null,
  isAuthenticated: false,
  isLoading: true,
  login: async () => {},
  logout: () => {},
});

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // On app load, restore session from localStorage
  useEffect(() => {
    const token = storage.getToken();
    const storedUser = storage.getUser();
    if (token && storedUser) {
      setUser(storedUser);
    }
    setIsLoading(false);
  }, []);

  const login = async (loginData) => {
    try {
      // 1. Call POST /api/auth/login → returns JWT string
      const token = await authService.login(loginData);
      storage.setToken(token);

      // 2. Call GET /api/auth/me → returns full Employe DTO
      const currentUser = await authService.getCurrentUser();
      storage.setUser(currentUser);
      setUser(currentUser);

      return { success: true };
    } catch (error) {
      const msg =
        error.response?.status === 401 || error.response?.status === 403
          ? 'Identifiant ou mot de passe incorrect.'
          : 'Erreur de connexion. Veuillez réessayer.';
      return { success: false, message: msg };
    }
  };

  const logout = useCallback(() => {
    storage.removeToken();
    storage.removeUser();
    setUser(null);
    // Navigation handled in MainLayout via onLogout callback
  }, []);

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};
