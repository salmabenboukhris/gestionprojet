import React from 'react';
import { Card, Button, Form, Input, Typography } from 'antd';
import { useAuth } from '../../hooks/useAuth';

const { Title } = Typography;

const LoginPage = () => {
  const { login } = useAuth();

  const onFinish = (values) => {
    login(values);
  };

  return (
    <Card style={{ width: 400, boxShadow: '0 4px 12px rgba(0,0,0,0.1)' }}>
      <Title level={3} style={{ textAlign: 'center', marginBottom: 24 }}>Connexion</Title>
      <Form layout="vertical" onFinish={onFinish}>
        <Form.Item label="Identifiant" name="login" rules={[{ required: true, message: 'Champ requis' }]}>
          <Input placeholder="Saisir votre login" />
        </Form.Item>
        <Form.Item label="Mot de passe" name="password" rules={[{ required: true, message: 'Champ requis' }]}>
          <Input.Password placeholder="Saisir votre mot de passe" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" fullWidth block>Se connecter</Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default LoginPage;
