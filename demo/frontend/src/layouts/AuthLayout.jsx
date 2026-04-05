import React from 'react';
import { Outlet } from 'react-router-dom';

const AuthLayout = () => {
  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      background: 'linear-gradient(135deg, #0f172a 0%, #1e3a5f 50%, #1a56db 100%)',
      position: 'relative',
      overflow: 'hidden',
    }}>
      {/* Decorative background circles */}
      <div style={{
        position: 'absolute',
        width: 500,
        height: 500,
        borderRadius: '50%',
        background: 'rgba(26, 86, 219, 0.15)',
        top: -200,
        left: -150,
        pointerEvents: 'none',
      }} />
      <div style={{
        position: 'absolute',
        width: 300,
        height: 300,
        borderRadius: '50%',
        background: 'rgba(26, 86, 219, 0.10)',
        bottom: -100,
        right: 80,
        pointerEvents: 'none',
      }} />

      {/* Left branding panel (desktop only) */}
      <div style={{
        display: 'none',
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        padding: '60px',
        color: '#fff',
        flexDirection: 'column',
        gap: 24,
        // Show on desktop
        '@media (min-width: 1024px)': { display: 'flex' },
      }} className="auth-branding">
        <div>
          <div style={{ fontSize: 22, fontWeight: 800, color: '#fff', letterSpacing: '-0.5px', marginBottom: 6 }}>
            Project Manager
          </div>
          <div style={{ fontSize: 12, color: 'rgba(255,255,255,0.6)', letterSpacing: '0.15em', textTransform: 'uppercase' }}>
            Enterprise ERP
          </div>
        </div>
        <h1 style={{ fontSize: 40, fontWeight: 700, color: '#fff', textAlign: 'center', lineHeight: 1.2, maxWidth: 420 }}>
          Pilotez vos projets avec précision
        </h1>
        <p style={{ color: 'rgba(255,255,255,0.7)', fontSize: 16, textAlign: 'center', maxWidth: 380 }}>
          Suivi complet des projets, phases, affectations et reporting financier pour votre équipe.
        </p>
        <div style={{ display: 'flex', gap: 32, marginTop: 16 }}>
          {[['5+', 'Rôles utilisateurs'], ['12', 'Modules métier'], ['100%', 'Sécurisé JWT']].map(([val, label]) => (
            <div key={label} style={{ textAlign: 'center' }}>
              <div style={{ fontSize: 28, fontWeight: 700, color: '#fff' }}>{val}</div>
              <div style={{ fontSize: 12, color: 'rgba(255,255,255,0.6)', marginTop: 4 }}>{label}</div>
            </div>
          ))}
        </div>
      </div>

      {/* Right — Login Form Panel */}
      <div style={{
        flex: 1,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '40px 24px',
      }}>
        <div style={{
          background: '#fff',
          borderRadius: 16,
          padding: '48px 40px',
          width: '100%',
          maxWidth: 420,
          boxShadow: '0 25px 50px rgba(0,0,0,0.25)',
        }}>
          <Outlet />
        </div>
      </div>

      <style>{`
        @media (min-width: 1024px) {
          .auth-branding { display: flex !important; }
        }
      `}</style>
    </div>
  );
};

export default AuthLayout;
