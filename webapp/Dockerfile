# Base on offical Node.js Alpine image
FROM node:16.13-alpine as webapp

ARG ARG_API_BASE
ARG ARG_API_BASE_URL

ENV API_BASE=${ARG_API_BASE}
ENV API_BASE_URL=${ARG_API_BASE_URL}

# Set working directory
WORKDIR /home/node/webapp
RUN chown -R node:node /home/node/webapp

RUN apk add --no-cache --upgrade bash

# Run container as non-root (unprivileged) user
# The node user is provided in the Node.js Alpine base image
USER node

# Copy package.json and package-lock.json before other files
# Utilise Docker cache to save re-installing dependencies if unchanged
COPY package.json yarn.lock ./

# Install dependencies
RUN yarn install --immutable --immutable-cache --check-cache

ENV NODE_ENV production

# Copy all files
COPY --chown=node:node . .

# Hack: will be replaced by runtime API_BASE_URL
RUN echo "API_BASE_URL=$ARG_API_BASE_URL" >> .buildenv

RUN yarn build

# Expose the listening port
EXPOSE 3000

ENTRYPOINT ["sh", "-c", "tools/bin/startup.sh"]
