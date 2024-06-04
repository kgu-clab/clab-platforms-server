#!/bin/bash

# Change permissions for Docker socket
if [ -S /var/run/docker.sock ]; then
  sudo chmod 666 /var/run/docker.sock
fi

# Execute the original entrypoint
exec /usr/local/bin/jenkins.sh "$@"
