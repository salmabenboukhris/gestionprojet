# ════════════════════════════════════════════
# Stage 1 — Build React avec Node
# ════════════════════════════════════════════
FROM node:20-alpine AS builder

WORKDIR /app

# Copier les fichiers de dépendances d'abord (cache Docker)
COPY package*.json ./
RUN npm ci --silent

# Copier le code source
COPY . .

# Build de production
RUN npm run build

# ════════════════════════════════════════════
# Stage 2 — Servir avec Nginx (léger)
# ════════════════════════════════════════════
FROM nginx:1.25-alpine

# Copier le build React
COPY --from=builder /app/dist /usr/share/nginx/html

# Copier la configuration Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
