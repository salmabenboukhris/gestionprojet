import React from 'react';
import { Pagination } from 'antd';

const AppPagination = (props) => {
  return (
    <Pagination
      showSizeChanger
      showTotal={(total, range) => `${range[0]}-${range[1]} sur ${total} éléments`}
      {...props}
    />
  );
};

export default AppPagination;
