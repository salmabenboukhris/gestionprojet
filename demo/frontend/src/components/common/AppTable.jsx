import React from 'react';
import { Table } from 'antd';

const AppTable = (props) => {
  return (
    <Table 
      {...props} 
      className="custom-app-table" 
      pagination={props.pagination !== false ? { ...props.pagination, showSizeChanger: true } : false} 
    />
  );
};

export default AppTable;
