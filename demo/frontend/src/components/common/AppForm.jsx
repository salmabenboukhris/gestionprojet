import React from 'react';
import { Form } from 'antd';

const AppForm = ({ children, onSubmit, form, layout = 'vertical', ...props }) => {
  return (
    <Form
      form={form}
      layout={layout}
      onFinish={onSubmit}
      {...props}
    >
      {children}
    </Form>
  );
};

export default AppForm;
