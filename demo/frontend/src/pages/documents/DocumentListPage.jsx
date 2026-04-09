import React, { useState, useEffect } from 'react';
import {
  Card, Table, Button, Space, Modal, Form, Input, Select, Upload, Tooltip, message, Popconfirm, Row, Col, Typography, Empty
} from 'antd';
import {
  PlusOutlined, UploadOutlined, DeleteOutlined, FileTextOutlined, DownloadOutlined
} from '@ant-design/icons';
import dayjs from 'dayjs';
import { documentService } from '../../services/modules/documentService';
import { projetService } from '../../services/modules/projetService';
import { useAuth } from '../../hooks/useAuth';
import { ROLES } from '../../utils/constants';

const { Option } = Select;
const { Title, Text } = Typography;

const DocumentModal = ({ open, onCancel, onSubmit, projets, confirmLoading, initialProjetId }) => {
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState([]);

  useEffect(() => {
    if (open) {
      form.resetFields();
      form.setFieldsValue({ projetId: initialProjetId });
      setFileList([]);
    }
  }, [open, form, initialProjetId]);

  const beforeUpload = (file) => {
    const LIMIT_5MB = 5 * 1024 * 1024;
    if (file.size > LIMIT_5MB) {
      message.error("Le fichier dépasse la taille limite de 5 Mo");
      return Upload.LIST_IGNORE;
    }
    setFileList([file]);
    return false; // Prevent automatic upload
  };

  const handleRemove = () => {
    setFileList([]);
  };

  const handleOk = () => {
    form.validateFields().then(values => {
      if (fileList.length === 0) {
        message.warning('Veuillez sélectionner un fichier');
        return;
      }
      
      const formData = new FormData();
      // fileList[0].originFileObj contient l'objet File javascript natif parfois englobé par AntD
      const finalFile = fileList[0].originFileObj || fileList[0];
      
      // On attache correctement les données attendues
      formData.append('file', finalFile);
      formData.append('code', values.code);
      formData.append('libelle', values.libelle);
      
      onSubmit({
        projetId: values.projetId,
        formData: formData
      });
    });
  };

  return (
    <Modal
      title={<div style={{ fontWeight: 700, fontSize: 18, color: '#0f172a' }}>+ Nouveau Document</div>}
      open={open}
      onCancel={onCancel}
      onOk={handleOk}
      okText="Uploader"
      cancelText="Annuler"
      confirmLoading={confirmLoading}
      width={500}
      okButtonProps={{ style: { borderRadius: 8 } }}
      cancelButtonProps={{ style: { borderRadius: 8 } }}
    >
      <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
        <Form.Item name="projetId" label="Projet associé" rules={[{ required: true, message: 'Requis' }]}>
          <Select placeholder="Sélectionner un projet" showSearch optionFilterProp="children" style={{ borderRadius: 8 }}>
            {projets.map(p => <Option key={p.id} value={p.id}>{p.codeProjet || p.code || p.id} — {p.nom || p.libelle || `Projet ${p.id}`}</Option>)}
          </Select>
        </Form.Item>
        <Row gutter={16}>
          <Col span={10}>
            <Form.Item name="code" label="Code" rules={[{ required: true, message: 'Le code est obligatoire' }]}>
              <Input placeholder="DOC-001" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
          <Col span={14}>
            <Form.Item name="libelle" label="Libellé" rules={[{ required: true, message: 'Le libellé est obligatoire' }]}>
              <Input placeholder="Nom du document" style={{ borderRadius: 8 }} />
            </Form.Item>
          </Col>
        </Row>
        <Form.Item label="Fichier" required>
          <Upload
            beforeUpload={beforeUpload}
            onRemove={handleRemove}
            fileList={fileList}
            maxCount={1}
          >
            <Button icon={<UploadOutlined />} style={{ borderRadius: 8 }}>Sélectionner un fichier (Max 5Mo)</Button>
          </Upload>
        </Form.Item>
      </Form>
    </Modal>
  );
};

const DocumentListPage = () => {
  const { user } = useAuth();
  const userRole = user?.profilCode;
  const canEdit = [ROLES.ADMIN, ROLES.CHEF_PROJET].includes(userRole);

  const [documents, setDocuments] = useState([]);
  const [projets, setProjets] = useState([]);
  
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [downloadingId, setDownloadingId] = useState(null);

  const [filterProjetId, setFilterProjetId] = useState(null);

  useEffect(() => {
    projetService.getAll().then(data => setProjets(Array.isArray(data) ? data : [])).catch(() => {});
  }, []);

  const fetchDocuments = async () => {
    if (!filterProjetId) {
      setDocuments([]);
      return;
    }
    setLoading(true);
    try {
      const data = await documentService.getByProjet(filterProjetId);
      setDocuments(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
      message.error("Erreur lors du chargement des documents");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDocuments();
  }, [filterProjetId]);

  const handleSubmit = async (submitData) => {
    setSubmitting(true);
    try {
      const { projetId, formData } = submitData;
      await documentService.create(projetId, formData);
      message.success('Document uploadé avec succès');
      setModalOpen(false);
      
      if (filterProjetId === projetId) {
        fetchDocuments();
      } else if (!filterProjetId) {
        setFilterProjetId(projetId);
      }
    } catch (err) {
      console.error('Erreur API Document:', err);
      message.error(err.response?.data?.message || err.message || 'Erreur lors de l\'upload');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await documentService.delete(id);
      message.success('Document supprimé');
      fetchDocuments();
    } catch (err) {
      console.error('Erreur supression:', err);
      message.error(err.response?.data?.message || 'Erreur lors de la suppression');
    }
  };

  const handleDownload = async (doc) => {
    setDownloadingId(doc.id);
    try {
      const response = await documentService.download(doc.id);
      const url = window.URL.createObjectURL(new Blob([response]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', doc.nomFichier || `document_${doc.id}`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      message.error('Erreur lors du téléchargement');
    } finally {
      setDownloadingId(null);
    }
  };

  const columns = [
    {
      title: 'Document',
      key: 'document',
      render: (_, rec) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 36, height: 36, borderRadius: 8, background: '#f8fafc', border: '1px solid #e2e8f0', color: '#64748b', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 16, flexShrink: 0 }}>
            <FileTextOutlined />
          </div>
          <div>
            <div style={{ fontWeight: 600, color: '#0f172a' }}>{rec.libelle}</div>
            <div style={{ fontSize: 12, color: '#94a3b8' }}>
              {rec.code} {rec.projetNom ? `· ${rec.projetNom}` : ''}
            </div>
          </div>
        </div>
      )

    },
    {
      title: 'Type',
      dataIndex: 'typeFichier',
      key: 'typeFichier',
      render: t => <span style={{ color: '#475569', fontSize: 13 }}>{t?.split('/')[1]?.toUpperCase() || t || 'Inconnu'}</span>
    },
    {
      title: 'Taille',
      dataIndex: 'taille',
      key: 'taille',
      render: s => <span style={{ color: '#64748b' }}>{s ? (s / 1024).toFixed(1) + ' KB' : '—'}</span>
    },
    {
      title: 'Date',
      dataIndex: 'dateUpload',
      key: 'dateUpload',
      render: d => <span style={{ color: '#475569' }}>{d ? dayjs(d).format('DD/MM/YYYY HH:mm') : '—'}</span>
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 120,
      render: (_, rec) => (
        <Space size="small">
          <Tooltip title="Télécharger">
            <Button 
              type="text" 
              size="small" 
              icon={<DownloadOutlined style={{ color: '#16a34a' }} />} 
              onClick={() => handleDownload(rec)}
              loading={downloadingId === rec.id}
            />
          </Tooltip>
          {canEdit && (
            <Popconfirm title="Supprimer ce document ?" onConfirm={() => handleDelete(rec.id)} okText="Oui" cancelText="Non" okButtonProps={{ danger: true }}>
              <Button type="text" size="small" danger icon={<DeleteOutlined />} />
            </Popconfirm>
          )}
        </Space>
      )
    }
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 28 }}>
        <div>
          <Title level={2} style={{ margin: 0, fontWeight: 800, color: '#0f172a' }}>Documents</Title>
          <Text style={{ color: '#64748b', fontSize: 14 }}>Gérez les pièces jointes et archives de chaque projet.</Text>
        </div>
        {canEdit && (
          <Button type="primary" icon={<PlusOutlined />} onClick={() => setModalOpen(true)} size="large" style={{ borderRadius: 8, fontWeight: 600 }}>
            Nouveau Document
          </Button>
        )}
      </div>

      <Card style={{ borderRadius: 12, marginBottom: 20 }} bodyStyle={{ padding: '16px 20px' }}>
        <Row gutter={16} align="middle">
          <Col flex="auto">
            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <span style={{ fontWeight: 600, color: '#475569' }}>Projet :</span>
              <Select 
                placeholder="Sélectionner un projet pour afficher ses documents..." 
                value={filterProjetId} 
                onChange={setFilterProjetId} 
                showSearch 
                optionFilterProp="children" 
                style={{ width: 350, borderRadius: 8 }}
                allowClear
              >
                {projets.map(p => <Option key={p.id} value={p.id}>{p.codeProjet || p.code || p.id} — {p.nom || p.libelle || `Projet ${p.id}`}</Option>)}
              </Select>
            </div>
          </Col>
        </Row>
      </Card>

      <Card style={{ borderRadius: 12 }} bodyStyle={{ padding: 0 }}>
        {filterProjetId ? (
           <Table
            dataSource={documents}
            columns={columns}
            rowKey="id"
            loading={loading}
            pagination={{ pageSize: 8, showSizeChanger: false }}
            size="middle"
            locale={{ emptyText: <div style={{ padding: 40, textAlign: 'center', color: '#94a3b8' }}>Aucun document trouvé pour ce projet</div> }}
          />
        ) : (
          <Empty 
            description="Veuillez sélectionner un projet pour afficher ses documents."
            style={{ padding: '60px 0' }} 
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          />
        )}
      </Card>

      <DocumentModal 
        open={modalOpen} 
        onCancel={() => !submitting && setModalOpen(false)} 
        onSubmit={handleSubmit} 
        projets={projets} 
        confirmLoading={submitting}
        initialProjetId={filterProjetId}
      />
    </div>
  );
};
export default DocumentListPage;
