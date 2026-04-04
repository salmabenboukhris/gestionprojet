import React, { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Form, Input, Button, Row, Col, InputNumber, Alert } from 'antd';
import AppForm from '../common/AppForm';

export const employeSchema = z.object({
  matricule: z.string().min(1, 'Obligatoire').max(50, 'Max 50'),
  nom: z.string().min(1, 'Obligatoire').max(100, 'Max 100'),
  prenom: z.string().min(1, 'Obligatoire').max(100, 'Max 100'),
  telephone: z.string().max(30, 'Max 30').optional().nullable(),
  email: z.string().email('Email invalide').max(150, 'Max 150'),
  login: z.string().min(1, 'Obligatoire').max(100, 'Max 100'),
  password: z.string().min(1, 'Obligatoire').max(255, 'Max 255'),
  profilId: z.number({ required_error: 'Obligatoire', invalid_type_error: 'Doit être un nombre' }),
});

const EmployeForm = ({ initialValues, onSubmit, isSubmitting }) => {
  const isEditMode = !!initialValues;
  const { control, handleSubmit, reset } = useForm({
    resolver: zodResolver(employeSchema),
    defaultValues: {
      matricule: '',
      nom: '',
      prenom: '',
      telephone: '',
      email: '',
      login: '',
      password: '',
      profilId: null,
    }
  });

  useEffect(() => {
    if (initialValues) {
      reset({
        matricule: initialValues.matricule || '',
        nom: initialValues.nom || '',
        prenom: initialValues.prenom || '',
        telephone: initialValues.telephone || '',
        email: initialValues.email || '',
        login: initialValues.login || '',
        password: '', // On force l'utilisateur à redéfinir en cas de modif selon la stricte contrainte @NotBlank du DTO
        profilId: initialValues.profilId || null,
      });
    } else {
      reset();
    }
  }, [initialValues, reset]);

  return (
    <AppForm onSubmit={handleSubmit(onSubmit)}>
      {isEditMode && (
        <Alert 
          message="Info API" 
          description="Le backend exige obligatoirement une saisie de mot de passe à la modification." 
          type="info" 
          showIcon 
          style={{ marginBottom: 16 }} 
        />
      )}
      <Row gutter={16}>
        <Col span={12}>
          <Controller
            name="matricule"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Matricule *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} />
              </Form.Item>
            )}
          />
        </Col>
        <Col span={12}>
          <Controller
            name="profilId"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="ID Profil *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <InputNumber {...field} style={{ width: '100%' }} placeholder="Ex: 1" />
              </Form.Item>
            )}
          />
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Controller
            name="nom"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Nom *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} />
              </Form.Item>
            )}
          />
        </Col>
        <Col span={12}>
          <Controller
            name="prenom"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Prénom *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} />
              </Form.Item>
            )}
          />
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Controller
            name="email"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Email *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} type="email" />
              </Form.Item>
            )}
          />
        </Col>
        <Col span={12}>
          <Controller
            name="telephone"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Téléphone" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} />
              </Form.Item>
            )}
          />
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Controller
            name="login"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Login *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} />
              </Form.Item>
            )}
          />
        </Col>
        <Col span={12}>
          <Controller
            name="password"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Mot de passe *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input.Password {...field} />
              </Form.Item>
            )}
          />
        </Col>
      </Row>

      <Button type="primary" htmlType="submit" id="employeFormSubmitBtn" style={{ display: 'none' }}>Submit</Button>
    </AppForm>
  );
};

export default EmployeForm;
