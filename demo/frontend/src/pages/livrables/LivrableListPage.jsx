import React from 'react';
import { Card, Typography, Button } from 'antd';
import AppTable from '../../components/common/AppTable';

const { Title } = Typography;

const LivrableListPage = () => {
  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
        <Title level={2} style={{ margin: 0 }}>Livrables</Title>
        <Button type="primary">Ajouter Livrable</Button>
      </div>
      <Card>
        <AppTable dataSource={[]} columns={[]} />
      </Card>
    </div>
  );
};

export default LivrableListPage;
