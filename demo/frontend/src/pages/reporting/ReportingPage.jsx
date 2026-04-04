import React from 'react';
import { Card, Typography } from 'antd';

const { Title, Paragraph } = Typography;

const ReportingPage = () => {
  return (
    <Card>
      <Title level={2}>Reporting Général</Title>
      <Paragraph>Indicateurs de performance, phases facturées, projets clôturés.</Paragraph>
    </Card>
  );
};

export default ReportingPage;
