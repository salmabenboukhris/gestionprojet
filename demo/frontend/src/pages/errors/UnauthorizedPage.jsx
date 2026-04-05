import React from 'react';
import { Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';
import { ROUTES } from '../../utils/constants';

const UnauthorizedPage = () => {
  const navigate = useNavigate();
  return (
    <div style={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      minHeight: '70vh',
    }}>
      <Result
        status="403"
        title={<span style={{ fontWeight: 700 }}>Accès Refusé</span>}
        subTitle="Vous ne disposez pas des droits nécessaires pour accéder à cette page. Contactez votre administrateur."
        extra={[
          <Button
            type="primary"
            key="back"
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

export default UnauthorizedPage;
