import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ConfigProvider, App as AntApp } from 'antd';
import frFR from 'antd/locale/fr_FR';
import AppRoutes from './routes/AppRoutes';
import { AuthProvider } from './context/AuthContext';
import './index.css';

// Ant Design Theme customisation aligned with our design tokens
const antdTheme = {
  token: {
    colorPrimary: '#1a56db',
    colorSuccess: '#10b981',
    colorWarning: '#f59e0b',
    colorError: '#ef4444',
    colorInfo: '#3b82f6',
    borderRadius: 8,
    fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, sans-serif",
    colorBgContainer: '#ffffff',
    colorBgLayout: '#f8fafc',
  },
  components: {
    Button: {
      borderRadius: 8,
    },
    Input: {
      borderRadius: 8,
    },
    Card: {
      borderRadius: 10,
    },
    Table: {
      borderRadius: 10,
    },
  },
};

function App() {
  return (
    <ConfigProvider locale={frFR} theme={antdTheme}>
      <AntApp>
        <AuthProvider>
          <BrowserRouter>
            <AppRoutes />
          </BrowserRouter>
        </AuthProvider>
      </AntApp>
    </ConfigProvider>
  );
}

export default App;
