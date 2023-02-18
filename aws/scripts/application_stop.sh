#!/bin/bash

docker compose -f /etc/donas/docker-compose.yaml stop
docker compose -f /etc/donas/docker-compose.yaml rm
