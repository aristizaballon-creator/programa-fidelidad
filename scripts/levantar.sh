#!/bin/bash

echo "Esperando a que Docker esté listo..."
for i in $(seq 1 30); do
  if docker info > /dev/null 2>&1; then
    echo "Docker listo."
    break
  fi
  sleep 2
done

if [ "$CODESPACES" = "true" ]; then
  export VITE_API_URL=https://${CODESPACE_NAME}-8080.${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN}
  export FRONTEND_ORIGIN=https://${CODESPACE_NAME}-5173.${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN}
  echo "Detecté Codespaces, usando VITE_API_URL=$VITE_API_URL"
fi

docker compose up --build -d
