import React, { useState } from 'react';
import { Form, Input, Button, Typography, Alert } from 'antd';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import { ROUTES } from '../../utils/constants';
import { loginSchema, yupSync } from '../../utils/validations';

const { Title, Text } = Typography;

const LoginPage = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const onFinish = async (values) => {
    setLoading(true);
    setError(null);
    const result = await login(values);
    setLoading(false);
    if (result.success) {
      navigate(ROUTES.DASHBOARD, { replace: true });
    } else {
      setError(result.message);
    }
  };

  return (
    <div>
      {/* Logo / Brand */}
      <div style={{ textAlign: 'center', marginBottom: 32 }}>
        <div style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 52,
          height: 52,
          borderRadius: 14,
          background: 'linear-gradient(135deg, #1a56db, #3b82f6)',
          marginBottom: 16,
          boxShadow: '0 8px 20px rgba(26,86,219,0.3)',
        }}>
          <span style={{ color: '#fff', fontWeight: 800, fontSize: 22 }}>PM</span>
        </div>
        <Title level={3} style={{ margin: 0, fontWeight: 700, color: '#0f172a' }}>
          Connexion
        </Title>
        <Text type="secondary" style={{ fontSize: 14 }}>
          Project Manager — Enterprise ERP
        </Text>
      </div>

      {error && (
        <Alert
          message={error}
          type="error"
          showIcon
          style={{ marginBottom: 20, borderRadius: 8 }}
          closable
          onClose={() => setError(null)}
        />
      )}

      <Form layout="vertical" onFinish={onFinish} size="large">
        <Form.Item
          label={<span style={{ fontWeight: 500, color: '#0f172a' }}>Identifiant</span>}
          name="login"
          rules={[yupSync(loginSchema, 'login')]}
        >
          <Input
            prefix={<UserOutlined style={{ color: '#94a3b8' }} />}
            placeholder="Votre login (min 3 caractères)"
            style={{ borderRadius: 8 }}
          />
        </Form.Item>

        <Form.Item
          label={<span style={{ fontWeight: 500, color: '#0f172a' }}>Mot de passe</span>}
          name="password"
          rules={[yupSync(loginSchema, 'password')]}
        >
          <Input.Password
            prefix={<LockOutlined style={{ color: '#94a3b8' }} />}
            placeholder="Votre mot de passe (min 4 caractères)"
            style={{ borderRadius: 8 }}
          />
        </Form.Item>

        <Form.Item style={{ marginBottom: 0, marginTop: 8 }}>
          <Button
            type="primary"
            htmlType="submit"
            block
            loading={loading}
            style={{
              height: 46,
              borderRadius: 8,
              fontWeight: 600,
              fontSize: 15,
              background: 'linear-gradient(135deg, #1a56db, #3b82f6)',
              border: 'none',
              boxShadow: '0 4px 12px rgba(26,86,219,0.3)',
            }}
          >
            {loading ? 'Connexion en cours...' : 'Se connecter'}
          </Button>
        </Form.Item>
      </Form>

      <div style={{ textAlign: 'center', marginTop: 24, padding: '16px', background: '#f8fafc', borderRadius: 8 }}>
        <Text type="secondary" style={{ fontSize: 12 }}>
          Accès réservé aux membres de l'équipe.<br />
          Contactez l'administrateur pour obtenir vos accès.
        </Text>
      </div>
    </div>
  );
};

export default LoginPage;
