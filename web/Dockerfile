FROM node:17.4.0-bullseye AS base
WORKDIR /app
ENV NEXT_TELEMETRY_DISABLED 1

FROM base AS build

COPY package.json ./package.json
COPY package-lock.json ./package-lock.json
RUN npm ci

COPY . .
RUN npm test -- --watchAll=false --coverage
# RUN npm run lint # should be
RUN npm run build

FROM base AS prod
ENV NODE_ENV production

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=build /app/public ./public
COPY --from=build /app/package.json ./package.json

COPY --from=build --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=build --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs
EXPOSE 3000
ENV PORT 3000

CMD ["node", "server.js"]