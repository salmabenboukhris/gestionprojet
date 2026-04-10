import React, { useState } from 'react';
import { DatePicker, Button, message, Space } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import AppModal from '../common/AppModal';
import AppTable from '../common/AppTable';
import { employeService } from '../../services/modules/employeService';

const { RangePicker } = DatePicker;

const DisponibiliteModal = ({ open, onCancel }) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [dates, setDates] = useState(null);

  const fetchDisponibles = async () => {
    if (!dates || dates.length !== 2) {
      message.warning('Veuillez sélectionner une plage de dates');
      return;
    }
    setLoading(true);
    try {
      const dateDebut = dates[0].format('YYYY-MM-DD');
      const dateFin = dates[1].format('YYYY-MM-DD');
      const response = await employeService.getDisponibles(dateDebut, dateFin);
      setData(response);
    } catch (error) {
      message.error(error.response?.data?.message || 'Erreur lors de la vérification');
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    { title: 'Matricule', dataIndex: 'matricule', key: 'matricule' },
    { title: 'Nom', dataIndex: 'nom', key: 'nom' },
    { title: 'Prénom', dataIndex: 'prenom', key: 'prenom' },
    { title: 'Email', dataIndex: 'email', key: 'email' }
  ];

  return (
    <AppModal
      title="Vérifier les disponibilités"
      open={open}
      onCancel={onCancel}
      footer={null}
      width={700}
    >
      <div style={{ marginBottom: 16 }}>
        <Space>
          <RangePicker onChange={(val) => setDates(val)} />
          <Button type="primary" icon={<SearchOutlined />} onClick={fetchDisponibles} loading={loading}>
            Rechercher
          </Button>
        </Space>
      </div>
      
      <AppTable 
        dataSource={data} 
        columns={columns} 
        rowKey="id" 
        loading={loading}
        pagination={{ pageSize: 5 }}
      />
    </AppModal>
  );
};

export default DisponibiliteModal;
