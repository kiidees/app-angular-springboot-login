FROM node:alpine

# 1. Usamos la ruta estándar /app para que coincida con tu Docker Compose
WORKDIR /app

# 2. Copiamos solo los archivos de dependencias primero
COPY package*.json ./

RUN npm install -g @angular/cli@20.0.3

# 3. Instalamos las dependencias locales (incluyendo el CLI de Angular si está en tu package.json)
RUN npm install --legacy-peer-deps

# 4. Copiamos el resto del código fuente
COPY . .

# 5. Corregimos los permisos para que el usuario 'node' pueda modificar node_modules/.cache
RUN chown -R node:node /app

# 6. Cambiamos al usuario 'node' (evita problemas de permisos de root en Linux/WSL2)
USER node

EXPOSE 4200

# 7. Ejecutamos usando el CLI local instalado en node_modules, agregando el --poll obligatorio
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--poll", "1000"]