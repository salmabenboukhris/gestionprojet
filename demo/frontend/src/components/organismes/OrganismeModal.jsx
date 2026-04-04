import React from 'react';
import AppModal from '../common/AppModal';
import OrganismeForm from './OrganismeForm';

const OrganismeModal = ({ open, onCancel, organisme, onSubmit, confirmLoading }) => {
  const isEditMode = !!organisme;

  // Lance la soumission du formulaire interne
  const handleOk = () => {
    document.getElementById('organismeFormSubmitBtn').click();
  };

  return (
    <AppModal
      title={isEditMode ? 'Modifier l\'organisme' : 'Nouvel organisme'}
      open={open}
      onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
      width={700}
      okText="Enregistrer"
      cancelText="Annuler"
    >
      <OrganismeForm 
        initialValues={organisme} 
        onSubmit={onSubmit} 
        isSubmitting={confirmLoading} 
      />
    </AppModal>
  );
};

export default OrganismeModal;
