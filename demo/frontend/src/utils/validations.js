/**
 * Schémas de validation Yup
 * Centralise toutes les règles de validation des formulaires de l'application.
 * Chaque schéma est utilisé dans les Form.Item rules via yupSync().
 */
import * as Yup from 'yup';

// ── Helpers ──────────────────────────────────────────────────────────────────

/**
 * Convertit un schéma Yup en règle Ant Design compatible.
 * Usage: rules={[yupSync(schema, 'fieldName')]}
 */
export function yupSync(schema, field) {
  return {
    async validator(_, value) {
      try {
        await schema.validateAt(field, { [field]: value });
      } catch (err) {
        throw new Error(err.message);
      }
    },
  };
}

/**
 * Convertit un schéma Yup entier en objet de règles Ant Design.
 * Usage: rules={getFieldRules(schema, 'fieldName')}
 */
export function getFieldRules(schema, field) {
  return [yupSync(schema, field)];
}

// ── Schéma Login ─────────────────────────────────────────────────────────────

export const loginSchema = Yup.object({
  login: Yup.string()
    .required('Veuillez saisir votre identifiant')
    .min(3, 'L\'identifiant doit contenir au moins 3 caractères')
    .max(50, 'L\'identifiant ne doit pas dépasser 50 caractères'),

  password: Yup.string()
    .required('Veuillez saisir votre mot de passe')
    .min(4, 'Le mot de passe doit contenir au moins 4 caractères'),
});

// ── Schéma Employé ───────────────────────────────────────────────────────────

export const employeSchema = Yup.object({
  matricule: Yup.string()
    .required('Le matricule est requis')
    .max(20, 'Maximum 20 caractères')
    .matches(/^[A-Za-z0-9\-]+$/, 'Matricule invalide (lettres, chiffres, tirets)'),

  nom: Yup.string()
    .required('Le nom est requis')
    .min(2, 'Minimum 2 caractères')
    .max(100, 'Maximum 100 caractères'),

  prenom: Yup.string()
    .required('Le prénom est requis')
    .min(2, 'Minimum 2 caractères')
    .max(100, 'Maximum 100 caractères'),

  email: Yup.string()
    .required('L\'email est requis')
    .email('Format email invalide (ex: nom@domaine.com)')
    .max(150, 'Maximum 150 caractères'),

  login: Yup.string()
    .required('Le login est requis')
    .min(3, 'Minimum 3 caractères')
    .max(50, 'Maximum 50 caractères')
    .matches(/^[a-zA-Z0-9._]+$/, 'Login invalide (lettres, chiffres, point, underscore)'),

  password: Yup.string()
    .required('Le mot de passe est requis')
    .min(6, 'Minimum 6 caractères')
    .max(100, 'Maximum 100 caractères'),

  profilId: Yup.number()
    .required('Veuillez sélectionner un rôle')
    .typeError('Rôle invalide'),
});

// ── Schéma Organisme ─────────────────────────────────────────────────────────

export const organismeSchema = Yup.object({
  code: Yup.string()
    .required('Le code est requis')
    .max(20, 'Maximum 20 caractères')
    .matches(/^[A-Za-z0-9\-_]+$/, 'Code invalide (lettres, chiffres, tirets)'),

  nom: Yup.string()
    .required('Le nom est requis')
    .min(2, 'Minimum 2 caractères')
    .max(200, 'Maximum 200 caractères'),

  nomContact: Yup.string()
    .max(100, 'Maximum 100 caractères')
    .nullable(),

  emailContact: Yup.string()
    .email('Format email invalide')
    .max(150, 'Maximum 150 caractères')
    .nullable(),

  telephone: Yup.string()
    .max(20, 'Maximum 20 caractères')
    .matches(/^[0-9+\s\-().]*$/, 'Numéro de téléphone invalide')
    .nullable(),
});

// ── Schéma Projet ─────────────────────────────────────────────────────────────

export const projetSchema = Yup.object({
  codeProjet: Yup.string()
    .required('Le code projet est requis')
    .max(20, 'Maximum 20 caractères'),

  nom: Yup.string()
    .required('Le nom du projet est requis')
    .min(3, 'Minimum 3 caractères')
    .max(200, 'Maximum 200 caractères'),

  dateDebut: Yup.date()
    .required('La date de début est requise')
    .typeError('Date invalide'),

  dateFin: Yup.date()
    .required('La date de fin est requise')
    .typeError('Date invalide')
    .min(Yup.ref('dateDebut'), 'La date de fin doit être après la date de début'),

  montantGlobal: Yup.number()
    .required('Le montant global est requis')
    .min(0, 'Le montant doit être positif')
    .typeError('Montant invalide'),

  organismeId: Yup.number()
    .required('Veuillez sélectionner un organisme')
    .typeError('Organisme invalide'),

  chefProjetId: Yup.number()
    .required('Veuillez sélectionner un chef de projet')
    .typeError('Chef de projet invalide'),
});

// ── Schéma Phase ─────────────────────────────────────────────────────────────

export const phaseSchema = Yup.object({
  codePhase: Yup.string()
    .required('Le code phase est requis')
    .max(20, 'Maximum 20 caractères'),

  libelle: Yup.string()
    .required('Le libellé est requis')
    .min(2, 'Minimum 2 caractères')
    .max(200, 'Maximum 200 caractères'),

  dateDebut: Yup.date()
    .required('La date de début est requise')
    .typeError('Date invalide'),

  dateFin: Yup.date()
    .required('La date de fin est requise')
    .typeError('Date invalide')
    .min(Yup.ref('dateDebut'), 'La date de fin doit être après la date de début'),

  montant: Yup.number()
    .required('Le montant est requis')
    .min(0, 'Le montant doit être positif')
    .typeError('Montant invalide'),
});

// ── Schéma Livrable ───────────────────────────────────────────────────────────

export const livrableSchema = Yup.object({
  code: Yup.string()
    .required('Le code est requis')
    .max(50, 'Maximum 50 caractères'),

  libelle: Yup.string()
    .required('Le libellé est requis')
    .min(2, 'Minimum 2 caractères')
    .max(150, 'Maximum 150 caractères'),

  description: Yup.string()
    .max(500, 'Maximum 500 caractères')
    .nullable(),

  phaseId: Yup.number()
    .required('Veuillez sélectionner une phase')
    .typeError('Phase invalide'),

  statut: Yup.string()
    .required('Le statut est requis')
    .oneOf(['EN_ATTENTE', 'EN_COURS', 'TERMINE', 'VALIDE'], 'Statut invalide'),
});

// ── Schéma Document ───────────────────────────────────────────────────────────

export const documentSchema = Yup.object({
  code: Yup.string()
    .required('Le code est requis')
    .max(50, 'Maximum 50 caractères'),

  libelle: Yup.string()
    .required('Le libellé est requis')
    .min(2, 'Minimum 2 caractères')
    .max(150, 'Maximum 150 caractères'),

  projetId: Yup.number()
    .required('Veuillez sélectionner un projet')
    .typeError('Projet invalide'),
});

// ── Schéma Changement de mot de passe ────────────────────────────────────────

export const changePasswordSchema = Yup.object({
  currentPassword: Yup.string()
    .required('Le mot de passe actuel est requis'),

  newPassword: Yup.string()
    .required('Le nouveau mot de passe est requis')
    .min(6, 'Minimum 6 caractères')
    .max(100, 'Maximum 100 caractères')
    .notOneOf([Yup.ref('currentPassword')], 'Le nouveau mot de passe doit être différent de l\'actuel'),

  confirmPassword: Yup.string()
    .required('Veuillez confirmer le nouveau mot de passe')
    .oneOf([Yup.ref('newPassword')], 'Les mots de passe ne correspondent pas'),
});

// ── Schéma Facture ────────────────────────────────────────────────────────────

export const factureSchema = Yup.object({
  numeroFacture: Yup.string()
    .required('Le numéro de facture est requis')
    .max(50, 'Maximum 50 caractères'),

  dateFacture: Yup.date()
    .required('La date de facturation est requise')
    .typeError('Date invalide'),

  montant: Yup.number()
    .required('Le montant est requis')
    .min(0.01, 'Le montant doit être supérieur à 0')
    .typeError('Montant invalide'),
});
