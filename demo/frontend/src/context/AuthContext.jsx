import React, { createContext, useState, useEffect, useCallback, useRef } from 'react';
import { authService } from '../services/modules/authService';
import { storage } from '../utils/storage';
import { message } from 'antd';

// ── Utilitaire : vérification expiration JWT ─────────────────────────────────
const isTokenExpired = (token) => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    // exp est en secondes, Date.now() en millisecondes
    return Date.now() >= payload.exp * 1000;
  } catch {
    return true; // Token malformé → considéré expiré
  }
};

const getTokenExpirationMs = (token) => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.exp * 1000 - Date.now();
  } catch {
    return 0;
  }
};

// ── Context ──────────────────────────────────────────────────────────────────
export const AuthContext = createContext({
  user: null,
  isAuthenticated: false,
  isLoading: true,
  login: async () => {},
  logout: () => {},
});

// ── Provider ─────────────────────────────────────────────────────────────────
export const AuthProvider = ({ children }) => {
  const [user, setUser]       = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const expirationTimerRef    = useRef(null);

  // Déconnexion automatique à l'expiration du token
  const scheduleAutoLogout = useCallback((token) => {
    if (expirationTimerRef.current) clearTimeout(expirationTimerRef.current);

    const msUntilExpiry = getTokenExpirationMs(token);
    if (msUntilExpiry <= 0) return;

    expirationTimerRef.current = setTimeout(() => {
      storage.removeToken();
      storage.removeUser();
      setUser(null);
      message.warning('Votre session a expiré. Veuillez vous reconnecter.', 5);
    }, msUntilExpiry);
  }, []);

  // Au chargement : vérifier le token existant
  useEffect(() => {
    const token = storage.getToken();

    if (!token) {
      setIsLoading(false);
      return;
    }

    // Vérifier l'expiration côté client d'abord
    if (isTokenExpired(token)) {
      storage.removeToken();
      storage.removeUser();
      setIsLoading(false);
      return;
    }

    // Valider le token côté serveur via /api/auth/me
    authService.getCurrentUser()
      .then((currentUser) => {
        storage.setUser(currentUser);
        setUser(currentUser);
        scheduleAutoLogout(token);
      })
      .catch(() => {
        storage.removeToken();
        storage.removeUser();
        setUser(null);
      })
      .finally(() => setIsLoading(false));

    return () => {
      if (expirationTimerRef.current) clearTimeout(expirationTimerRef.current);
    };
  }, [scheduleAutoLogout]);

  // Login
  const login = async (loginData) => {
    try {
      const token = await authService.login(loginData);
      storage.setToken(token);

      const currentUser = await authService.getCurrentUser();
      storage.setUser(currentUser);
      setUser(currentUser);
      scheduleAutoLogout(token);

      return { success: true };
    } catch (error) {
      const msg =
        error.response?.status === 401 || error.response?.status === 403
          ? 'Identifiant ou mot de passe incorrect.'
          : 'Erreur de connexion. Veuillez réessayer.';
      return { success: false, message: msg };
    }
  };

  // Logout
  const logout = useCallback(() => {
    if (expirationTimerRef.current) clearTimeout(expirationTimerRef.current);
    storage.removeToken();
    storage.removeUser();
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};
