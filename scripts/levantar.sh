#!/bin/bash
if [ "$CODESPACES" = "true" ]; then
  export VITE_API_URL=https://${CODESPACE_NAME}-8080.${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN}
  echo "Detecté Codespaces, usando VITE_API_URL=$VITE_API_URL"
fi
docker compose up --build -d
