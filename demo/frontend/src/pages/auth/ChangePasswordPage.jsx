import React, { useState } from 'react';
import { Card, Form, Input, Button, Alert, Typography } from 'antd';
import { KeyOutlined } from '@ant-design/icons';
import { authService } from '../../services/modules/authService';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const ChangePasswordPage = () => {
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    setLoading(true);
    setError(null);
    try {
      await authService.changePassword({
        oldPassword: values.oldPassword,
        newPassword: values.newPassword,
      });
      setSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors du changement de mot de passe.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 480 }}>
      <div style={{ marginBottom: 24 }}>
        <h1 style={{ fontSize: 24, fontWeight: 700, margin: 0 }}>Changer le mot de passe</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: 0, marginTop: 4 }}>
          Saisissez votre mot de passe actuel et choisissez un nouveau.
        </p>
      </div>

      <Card style={{ borderRadius: 12 }}>
        {success ? (
          <div style={{ textAlign: 'center', padding: '24px 0' }}>
            <Alert
              message="Mot de passe modifié avec succès !"
              type="success"
              showIcon
              style={{ marginBottom: 20, borderRadius: 8 }}
            />
            <Button type="primary" onClick={() => navigate('/profile')} style={{ borderRadius: 8 }}>
              Retour au profil
            </Button>
          </div>
        ) : (
          <Form layout="vertical" onFinish={onFinish} size="large">
            {error && (
              <Alert
                message={error}
                type="error"
                showIcon
                closable
                onClose={() => setError(null)}
                style={{ marginBottom: 20, borderRadius: 8 }}
              />
            )}

            <Form.Item
              label={<span style={{ fontWeight: 500 }}>Mot de passe actuel</span>}
              name="oldPassword"
              rules={[{ required: true, message: 'Champ requis' }]}
            >
              <Input.Password prefix={<KeyOutlined style={{ color: '#94a3b8' }} />} style={{ borderRadius: 8 }} />
            </Form.Item>

            <Form.Item
              label={<span style={{ fontWeight: 500 }}>Nouveau mot de passe</span>}
              name="newPassword"
              rules={[
                { required: true, message: 'Champ requis' },
                { min: 6, message: 'Au moins 6 caractères' },
              ]}
            >
              <Input.Password prefix={<KeyOutlined style={{ color: '#94a3b8' }} />} style={{ borderRadius: 8 }} />
            </Form.Item>

            <Form.Item
              label={<span style={{ fontWeight: 500 }}>Confirmer le nouveau mot de passe</span>}
              name="confirmPassword"
              dependencies={['newPassword']}
              rules={[
                { required: true, message: 'Champ requis' },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue('newPassword') === value) return Promise.resolve();
                    return Promise.reject(new Error('Les mots de passe ne correspondent pas'));
                  },
                }),
              ]}
            >
              <Input.Password prefix={<KeyOutlined style={{ color: '#94a3b8' }} />} style={{ borderRadius: 8 }} />
            </Form.Item>

            <Form.Item style={{ marginBottom: 0 }}>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                block
                style={{ borderRadius: 8, height: 44, fontWeight: 600 }}
              >
                Mettre à jour le mot de passe
              </Button>
            </Form.Item>
          </Form>
        )}
      </Card>
    </div>
  );
};

export default ChangePasswordPage;
