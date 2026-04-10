import React from 'react';
import { Spin } from 'antd';

const AppLoader = ({ tip = 'Chargement...', size = 'large', fullScreen = false }) => {
  if (fullScreen) {
    return (
      <div style={{ height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <Spin tip={tip} size={size} />
      </div>
    );
  }
  
  return <Spin tip={tip} size={size} />;
};

export default AppLoader;
