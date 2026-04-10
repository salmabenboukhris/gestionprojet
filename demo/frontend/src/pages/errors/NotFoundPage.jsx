import React from 'react';
import { Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';
import { ROUTES } from '../../utils/constants';

const NotFoundPage = () => {
  const navigate = useNavigate();
  return (
    <div style={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      minHeight: '100vh',
      background: 'var(--color-bg)',
    }}>
      <Result
        status="404"
        title={<span style={{ fontWeight: 700 }}>404 — Page Introuvable</span>}
        subTitle="La page que vous cherchez n'existe pas ou a été déplacée."
        extra={[
          <Button
            type="primary"
            key="home"
            onClick={() => navigate(ROUTES.DASHBOARD)}
            style={{ borderRadius: 8 }}
          >
            Retour au Tableau de bord
          </Button>,
        ]}
      />
    </div>
  );
};

export default NotFoundPage;
