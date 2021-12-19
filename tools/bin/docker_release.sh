#!/usr/bin/env bash

set -e

# Docker image release
if [[ -z "${DOCKER_USER}" ]]; then
  echo 'DOCKER_USER not set!';
  exit 1;
fi

if [[ -z "${DOCKER_PASSWORD}" ]]; then
  echo 'DOCKER_PASSWORD not set!';
  exit 1;
fi

docker login -u "$DOCKER_USER" -p "$DOCKER_PASSWORD"

NEW_VERSION=$(grep VERSION .env | cut -d"=" -f2)
GIT_REVISION=$(git rev-parse HEAD)
[[ -z "$GIT_REVISION" ]] && echo "Couldn't get the git revision..." && exit 1

echo "Building and publishing docker images $NEW_VERSION for git revision $GIT_REVISION..."

VERSION=$NEW_VERSION GIT_REVISION=$GIT_REVISION docker-compose -f docker-compose.build.yaml build
VERSION=$NEW_VERSION GIT_REVISION=$GIT_REVISION docker-compose -f docker-compose.build.yaml push
echo "Completed building and publishing images..."
docker logout