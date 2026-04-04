import React from 'react';
import { Badge } from 'antd';

const AppBadge = ({ status, text, ...props }) => {
  // mapping custom pour le métier
  const getStatus = (val) => {
    switch(val) {
      case 'success':
      case true:
        return 'success';
      case 'error':
      case false:
        return 'error';
      case 'processing':
        return 'processing';
      case 'warning':
        return 'warning';
      default:
        return 'default';
    }
  };

  return <Badge status={getStatus(status)} text={text} {...props} />;
};

export default AppBadge;
