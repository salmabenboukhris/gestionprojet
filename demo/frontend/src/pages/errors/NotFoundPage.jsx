import React from 'react';
import { Card, Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';

const NotFoundPage = () => {
  const navigate = useNavigate();
  return (
    <Card style={{ margin: 24 }}>
      <Result
        status="404"
        title="404"
        subTitle="Désolé, la page que vous cherchez n'existe pas."
        extra={<Button type="primary" onClick={() => navigate('/')}>Retour à l'accueil</Button>}
      />
    </Card>
  );
};

export default NotFoundPage;
