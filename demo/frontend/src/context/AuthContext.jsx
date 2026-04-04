import React, { createContext, useState, useEffect } from 'react';
import { storage } from '../utils/storage';

export const AuthContext = createContext({
  user: null,
  isAuthenticated: false,
  login: async () => {},
  logout: () => {},
});

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Phase 1 : Initialisation dummy, pas de vraie vérification auto.
    const token = storage.getToken();
    const storedUser = storage.getUser();
    
    if (token && storedUser) {
      setUser(storedUser);
    }
    
    setIsLoading(false);
  }, []);

  const login = async (loginData) => {
    // Stub Phase 1 - À relier avec authService plus tard
    console.log('Login attempt', loginData);
  };

  const logout = () => {
    storage.removeToken();
    storage.removeUser();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};
