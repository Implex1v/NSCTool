FROM node:16-bullseye AS base
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

ARG BUILD_VERSION=0.1.0
ARG GIT_COMMIT=unknown

LABEL org.opencontainers.image.version="${BUILD_VERSION}"\
      org.opencontainers.image.revision="${GIT_COMMIT}"

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=build /app/public ./public
COPY --from=build /app/package.json ./package.json
COPY --from=build /app/entrypoint.sh ./

COPY --from=build --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=build --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs
EXPOSE 3000

ENV PORT 3000
ENV APP_AUTH_URL unset
ENV NEXTAUTH_URL unset
ENV NEXTAUTH_SECRET unset
ENV APP_AUTH_CLIENT_ID unset
ENV APP_AUTH_CLIENT_SECRET unset

CMD ["./entrypoint.sh"]