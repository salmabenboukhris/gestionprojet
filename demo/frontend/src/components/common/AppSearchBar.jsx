import React from 'react';
import { Input } from 'antd';

const { Search } = Input;

const AppSearchBar = ({ placeholder = 'Rechercher...', onSearch, ...props }) => {
  return (
    <Search
      placeholder={placeholder}
      onSearch={onSearch}
      allowClear
      {...props}
    />
  );
};

export default AppSearchBar;
