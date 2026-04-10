import React, { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Form, Input, Button, Row, Col } from 'antd';
import AppForm from '../common/AppForm';

// Schéma copiant OrganismeRequestDto
export const organismeSchema = z.object({
  code: z.string().min(1, 'Le code est obligatoire').max(50, 'Maximum 50 caractères'),
  nom: z.string().min(1, 'Le nom est obligatoire').max(150, 'Maximum 150 caractères'),
  adresse: z.string().max(255, 'Maximum 255 caractères').optional().nullable(),
  telephone: z.string().max(30, 'Maximum 30 caractères').optional().nullable(),
  nomContact: z.string().max(150, 'Maximum 150 caractères').optional().nullable(),
  emailContact: z.union([z.literal(''), z.literal(null), z.string().email('Email invalide').max(150)]).optional(),
  siteWeb: z.string().max(150, 'Maximum 150 caractères').optional().nullable(),
});

const OrganismeForm = ({ initialValues, onSubmit, isSubmitting }) => {
  const { control, handleSubmit, reset } = useForm({
    resolver: zodResolver(organismeSchema),
    defaultValues: {
      code: '',
      nom: '',
      adresse: '',
      telephone: '',
      nomContact: '',
      emailContact: '',
      siteWeb: '',
    }
  });

  // Injecter les données en mode édition
  useEffect(() => {
    if (initialValues) {
      reset({
        code: initialValues.code || '',
        nom: initialValues.nom || '',
        adresse: initialValues.adresse || '',
        telephone: initialValues.telephone || '',
        nomContact: initialValues.nomContact || '',
        emailContact: initialValues.emailContact || '',
        siteWeb: initialValues.siteWeb || '',
      });
    } else {
      reset(); // Creation
    }
  }, [initialValues, reset]);

  return (
    <AppForm onSubmit={handleSubmit(onSubmit)}>
      <Row gutter={16}>
        <Col span={12}>
          <Controller
            name="code"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Code *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} placeholder="Ex: ORG01" />
              </Form.Item>
            )}
          />
        </Col>
        <Col span={12}>
          <Controller
            name="nom"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Nom *" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} placeholder="Nom de l'organisme" />
              </Form.Item>
            )}
          />
        </Col>
      </Row>

      <Controller
        name="adresse"
        control={control}
        render={({ field, fieldState }) => (
          <Form.Item label="Adresse" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
            <Input.TextArea {...field} rows={2} placeholder="Adresse complète" />
          </Form.Item>
        )}
      />

      <Row gutter={16}>
        <Col span={12}>
          <Controller
            name="nomContact"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Nom du contact" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} placeholder="Contact principal" />
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
                <Input {...field} placeholder="Ex: +33 6 ..." />
              </Form.Item>
            )}
          />
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Controller
            name="emailContact"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Email de contact" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} placeholder="email@exemple.com" />
              </Form.Item>
            )}
          />
        </Col>
        <Col span={12}>
          <Controller
            name="siteWeb"
            control={control}
            render={({ field, fieldState }) => (
              <Form.Item label="Site Web" validateStatus={fieldState.error ? 'error' : ''} help={fieldState.error?.message}>
                <Input {...field} placeholder="http://..." />
              </Form.Item>
            )}
          />
        </Col>
      </Row>

      {/* Reste caché, le Modal Ant Design contrôlera la soumission mais on peut laisser un submit natif via un bouton caché si désiré, ou gérer ça via form */}
      <Button type="primary" htmlType="submit" id="organismeFormSubmitBtn" style={{ display: 'none' }}>Submit</Button>
    </AppForm>
  );
};

export default OrganismeForm;
