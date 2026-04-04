import React from 'react';
import { Modal } from 'antd';

const AppModal = ({ title, open, onCancel, onOk, children, confirmLoading, ...props }) => {
  return (
    <Modal
      title={title}
      open={open}
      onCancel={onCancel}
      onOk={onOk}
      confirmLoading={confirmLoading}
      destroyOnClose
      maskClosable={false}
      {...props}
    >
      {children}
    </Modal>
  );
};

export default AppModal;
