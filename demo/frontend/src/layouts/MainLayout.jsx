import React, { useState } from 'react';
import { Layout, Menu, Button, Avatar, Dropdown, Space, Typography } from 'antd';
import {
  Outlet,
  useNavigate,
  useLocation,
} from 'react-router-dom';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  LogoutOutlined,
  UserOutlined,
  KeyOutlined,
  BellOutlined,
  SettingOutlined,
} from '@ant-design/icons';
import { menuConfig } from '../routes/menuConfig';
import { useAuth } from '../hooks/useAuth';
import { ROUTES } from '../utils/constants';

const { Sider, Content } = Layout;
const { Text } = Typography;

const MainLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { logout, user } = useAuth();

  // RBAC: Filter menu items by user role
  const userRole = user?.profilCode; // flat field from EmployeResponseDto

  const menuItems = menuConfig
    .filter(item => !item.roles || !userRole || item.roles.includes(userRole))
    .map(item => ({
      key: item.key,
      icon: item.icon,
      label: item.label,
    }));

  const handleMenuClick = ({ key }) => {
    navigate(key);
  };

  const handleLogout = () => {
    logout();
    navigate(ROUTES.LOGIN, { replace: true });
  };

  // Topbar user dropdown
  const userDropdownItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: 'Mon Profil',
      onClick: () => navigate(ROUTES.PROFILE || '/profile'),
    },
    {
      key: 'change-password',
      icon: <KeyOutlined />,
      label: 'Changer le mot de passe',
      onClick: () => navigate(ROUTES.CHANGE_PASSWORD || '/change-password'),
    },
    { type: 'divider' },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: 'Déconnexion',
      danger: true,
      onClick: handleLogout,
    },
  ];

  const roleLabel = {
    ADMIN: 'Administrateur',
    SECRETAIRE: 'Secrétaire',
    CHEF_PROJET: 'Chef de Projet',
    COMPTABLE: 'Comptable',
    DIRECTEUR: 'Directeur',
  }[userRole] || 'Utilisateur';

  const initials = [user?.prenom?.[0], user?.nom?.[0]].filter(Boolean).join('').toUpperCase() || 'U';

  return (
    <Layout style={{ minHeight: '100vh', background: 'var(--color-bg)' }}>
      {/* ===== SIDEBAR ===== */}
      <Sider
        collapsible
        collapsed={collapsed}
        onCollapse={setCollapsed}
        width={240}
        collapsedWidth={72}
        trigger={null}
        style={{
          background: 'var(--sidebar-bg)',
          position: 'fixed',
          height: '100vh',
          left: 0,
          top: 0,
          bottom: 0,
          zIndex: 100,
          boxShadow: '2px 0 8px rgba(0,0,0,0.15)',
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        {/* Logo */}
        <div style={{
          height: 64,
          display: 'flex',
          alignItems: 'center',
          padding: collapsed ? '0 20px' : '0 20px',
          borderBottom: '1px solid rgba(255,255,255,0.08)',
          cursor: 'pointer',
          gap: 12,
          flexShrink: 0,
        }} onClick={() => navigate(ROUTES.DASHBOARD)}>
          <div style={{
            width: 32,
            height: 32,
            borderRadius: 8,
            background: 'var(--color-primary)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontWeight: 800,
            fontSize: 13,
            color: '#fff',
            flexShrink: 0,
          }}>PM</div>
          {!collapsed && (
            <div>
              <div style={{ color: '#fff', fontWeight: 700, fontSize: 14, lineHeight: 1.2 }}>Project Manager</div>
              <div style={{ color: 'rgba(255,255,255,0.4)', fontSize: 10, letterSpacing: '0.1em', textTransform: 'uppercase' }}>Enterprise ERP</div>
            </div>
          )}
        </div>

        {/* Navigation Menu */}
        <div style={{ flex: 1, overflowY: 'auto', overflowX: 'hidden', paddingTop: 12 }}>
          <Menu
            theme="dark"
            mode="inline"
            selectedKeys={[location.pathname]}
            items={menuItems}
            onClick={handleMenuClick}
            style={{ background: 'transparent', border: 'none', paddingBottom: 16 }}
          />
        </div>

        {/* Sidebar collapse toggle */}
        <div style={{
          padding: '12px 16px',
          borderTop: '1px solid rgba(255,255,255,0.08)',
          display: 'flex',
          justifyContent: collapsed ? 'center' : 'flex-end',
        }}>
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{ color: 'rgba(255,255,255,0.5)' }}
          />
        </div>

        {/* New Project Quick button */}
        {!collapsed && (
          <div style={{ padding: '0 16px 16px' }}>
            <Button
              type="primary"
              block
              icon={<span style={{ marginRight: 6 }}>+</span>}
              onClick={() => navigate(ROUTES.PROJETS)}
              style={{
                borderRadius: 8,
                fontWeight: 600,
                background: 'rgba(26,86,219,0.8)',
                border: 'none',
              }}
            >
              New Project
            </Button>
          </div>
        )}
      </Sider>

      {/* ===== MAIN CONTENT ===== */}
      <Layout style={{ marginLeft: collapsed ? 72 : 240, transition: 'margin 0.2s ease' }}>
        {/* TOPBAR */}
        <div style={{
          height: 64,
          background: '#fff',
          borderBottom: '1px solid var(--color-border)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          padding: '0 24px',
          position: 'sticky',
          top: 0,
          zIndex: 99,
          boxShadow: '0 1px 3px rgba(0,0,0,0.05)',
        }}>
          {/* Search hint */}
          <div style={{
            display: 'flex',
            alignItems: 'center',
            gap: 8,
            background: 'var(--color-bg)',
            border: '1px solid var(--color-border)',
            borderRadius: 8,
            padding: '0 14px',
            height: 36,
            cursor: 'pointer',
            width: 220,
            color: 'var(--color-text-muted)',
            fontSize: 13,
          }}>
            🔍 Rechercher...
          </div>

          {/* Right actions */}
          <Space spacing={16} size="middle">
            <Button type="text" icon={<BellOutlined style={{ fontSize: 18 }} />} style={{ color: '#64748b' }} />
            <Button type="text" icon={<SettingOutlined style={{ fontSize: 18 }} />} style={{ color: '#64748b' }} />

            <Dropdown menu={{ items: userDropdownItems }} placement="bottomRight" trigger={['click']}>
              <Space style={{ cursor: 'pointer', padding: '4px 8px', borderRadius: 8, transition: 'background 0.15s' }}
                className="topbar-user">
                <Avatar
                  style={{ background: 'var(--color-primary)', fontWeight: 700, fontSize: 13 }}
                  size={34}
                >
                  {initials}
                </Avatar>
                <div style={{ lineHeight: 1.2 }}>
                  <div style={{ fontWeight: 600, fontSize: 13, color: 'var(--color-text-primary)' }}>
                    {user?.prenom} {user?.nom}
                  </div>
                  <div style={{ fontSize: 11, color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                    {roleLabel}
                  </div>
                </div>
              </Space>
            </Dropdown>
          </Space>
        </div>

        {/* PAGE CONTENT */}
        <Content style={{ padding: 28, minHeight: 'calc(100vh - 64px)' }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

export default MainLayout;
