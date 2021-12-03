---
sidebar_position: 1
---

# Local Deployment

Deploying Castled locally is fairly straightforward.

1. Make sure you have [docker](https://docs.docker.com/get-docker/) and [docker compose](https://docs.docker.com/compose/install/) installed on your local machine
2. Follow the steps below for cloning the repo and running castled services

```jsx title="Local Deployment"
git clone https://github.com/castledio/castled.git
cd castled
docker-compose up
```

3. Once all the services are up, Castled UI should be available at [http://localhost:3000/](http://localhost:3000/)
4. Let your data activation begin!

:::note
First time you are running docker compose up, it could take a few mins depending on your network bandwidth.
:::
