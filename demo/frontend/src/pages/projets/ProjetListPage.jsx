import React from 'react';
import { Card, Typography, Button } from 'antd';
import AppTable from '../../components/common/AppTable';

const { Title } = Typography;

const ProjetListPage = () => {
  // Mock data pour affichage
  const columns = [
    { title: 'Code', dataIndex: 'code', key: 'code' },
    { title: 'Nom', dataIndex: 'nom', key: 'nom' },
    { title: 'Date Début', dataIndex: 'dateDebut', key: 'dateDebut' },
    { title: 'Date Fin', dataIndex: 'dateFin', key: 'dateFin' },
    { title: 'Actions', key: 'actions', render: () => <a>Gérer</a> }
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
        <Title level={2} style={{ margin: 0 }}>Projets</Title>
        <Button type="primary">Nouveau Projet</Button>
      </div>
      <Card>
        <AppTable dataSource={[]} columns={columns} />
      </Card>
    </div>
  );
};

export default ProjetListPage;
