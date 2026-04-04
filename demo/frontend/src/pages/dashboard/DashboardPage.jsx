import React from 'react';
import { Card, Typography } from 'antd';

const { Title, Paragraph } = Typography;

const DashboardPage = () => {
  return (
    <Card>
      <Title level={2}>Tableau de bord</Title>
      <Paragraph>Bienvenue sur l'application de gestion de projets.</Paragraph>
      {/* Placeholder pour les KPI et charts futures */}
    </Card>
  );
};

export default DashboardPage;
