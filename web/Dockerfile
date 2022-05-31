FROM node:17.4.0-bullseye as build
WORKDIR /app
ENV NODE_ENV production

COPY package.json ./package.json
RUN npm install

COPY . .
RUN npm test -- --watchAll=false --coverage
RUN npm run build

FROM nginx:1.21.6-alpine
ENV NODE_ENV production

COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80