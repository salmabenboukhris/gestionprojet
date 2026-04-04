import React from 'react';
import { Card, Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';

const UnauthorizedPage = () => {
  const navigate = useNavigate();
  return (
    <Card style={{ margin: 24 }}>
      <Result
        status="403"
        title="403"
        subTitle="Désolé, vous n'êtes pas autorisé à accéder à cette page."
        extra={<Button type="primary" onClick={() => navigate('/')}>Retour au Dashboard</Button>}
      />
    </Card>
  );
};

export default UnauthorizedPage;
