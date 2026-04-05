import React from 'react';
import { Card, Descriptions, Avatar, Tag, Typography, Button } from 'antd';
import { UserOutlined, KeyOutlined } from '@ant-design/icons';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const roleLabel = {
  ADMIN: 'Administrateur',
  SECRETAIRE: 'Secrétaire',
  CHEF_PROJET: 'Chef de Projet',
  COMPTABLE: 'Comptable',
  DIRECTEUR: 'Directeur',
};

const roleColor = {
  ADMIN: 'blue',
  SECRETAIRE: 'green',
  CHEF_PROJET: 'purple',
  COMPTABLE: 'orange',
  DIRECTEUR: 'cyan',
};


const ProfilePage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  if (!user) return null;

  const userRole = user.profilCode; // flat field from EmployeResponseDto

  const initials = [user.prenom?.[0], user.nom?.[0]].filter(Boolean).join('').toUpperCase();

  return (
    <div style={{ maxWidth: 720 }}>
      <div className="page-header">
        <div>
          <h1 className="page-title" style={{ fontSize: 24, fontWeight: 700, margin: 0 }}>Mon Profil</h1>
          <p style={{ color: 'var(--color-text-secondary)', margin: 0, marginTop: 4 }}>
            Informations de votre compte utilisateur.
          </p>
        </div>
        <Button
          icon={<KeyOutlined />}
          onClick={() => navigate('/change-password')}
          style={{ borderRadius: 8 }}
        >
          Changer le mot de passe
        </Button>
      </div>

      <Card style={{ borderRadius: 12 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 20, marginBottom: 32, padding: '16px 0', borderBottom: '1px solid var(--color-border)' }}>
          <Avatar
            size={72}
            style={{ background: 'var(--color-primary)', fontSize: 24, fontWeight: 700, flexShrink: 0 }}
          >
            {initials}
          </Avatar>
          <div>
            <Title level={4} style={{ margin: 0 }}>{user.prenom} {user.nom}</Title>
            <Tag color={roleColor[userRole] || 'default'} style={{ marginTop: 8, borderRadius: 6 }}>
              {roleLabel[userRole] || userRole}
            </Tag>
          </div>
        </div>

        <Descriptions column={2} labelStyle={{ fontWeight: 600, color: 'var(--color-text-secondary)', width: 140 }}>
          <Descriptions.Item label="Prénom">{user.prenom}</Descriptions.Item>
          <Descriptions.Item label="Nom">{user.nom}</Descriptions.Item>
          <Descriptions.Item label="Login">{user.login}</Descriptions.Item>
          <Descriptions.Item label="Matricule">{user.matricule}</Descriptions.Item>
          <Descriptions.Item label="Email">{user.email}</Descriptions.Item>
          <Descriptions.Item label="Téléphone">{user.telephone || '—'}</Descriptions.Item>
          <Descriptions.Item label="Profil">
            <Tag color={roleColor[userRole] || 'default'}>{roleLabel[userRole] || userRole}</Tag>
          </Descriptions.Item>
        </Descriptions>
      </Card>
    </div>
  );
};

export default ProfilePage;
