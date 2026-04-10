import React from 'react';
import AppModal from '../common/AppModal';
import EmployeForm from './EmployeForm';

const EmployeModal = ({ open, onCancel, employe, onSubmit, confirmLoading }) => {
  const isEditMode = !!employe;

  const handleOk = () => {
    document.getElementById('employeFormSubmitBtn').click();
  };

  return (
    <AppModal
      title={isEditMode ? 'Modifier l\'employé' : 'Nouvel employé'}
      open={open}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
      width={700}
      okText="Enregistrer"
      cancelText="Annuler"
    >
      <EmployeForm 
        initialValues={employe} 
        onSubmit={onSubmit} 
        isSubmitting={confirmLoading} 
      />
    </AppModal>
  );
};

export default EmployeModal;
