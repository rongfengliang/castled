# Base on offical Node.js Alpine image
FROM node:16.13-alpine as webapp

ARG ARG_API_BASE
ARG ARG_API_BASE_URL
ARG ARG_APP_BASE_URL

ENV API_BASE=${ARG_API_BASE}
ENV API_BASE_URL=${ARG_API_BASE_URL}
ENV APP_BASE_URL=${ARG_APP_BASE_URL}

# Set working directory
WORKDIR /usr/webapp

RUN apk add --no-cache --upgrade bash

# Copy package.json and package-lock.json before other files
# Utilise Docker cache to save re-installing dependencies if unchanged
COPY package.json yarn.lock ./

# Install dependencies
RUN yarn --no-cache install

# Copy all files
COPY . .

# Hack: will be replaced by runtime API_BASE_URL
RUN echo "API_BASE_URL=$ARG_API_BASE_URL" >> .buildenv

RUN yarn build

# Expose the listening port
EXPOSE 3000

ENTRYPOINT ["sh", "-c", "tools/bin/startup.sh"]
