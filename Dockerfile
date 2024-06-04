FROM jenkins/jenkins:2.440.3-lts-jdk21

USER root

# Install Docker CLI and sudo
RUN apt-get update && \
    apt-get install -y docker.io sudo && \
    rm -rf /var/lib/apt/lists/*

# Add jenkins user to docker group
RUN usermod -aG docker jenkins

# Allow jenkins user to use sudo without a password
RUN echo "jenkins ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

# Copy the entrypoint script
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

# Set permissions for the entrypoint script
RUN chmod +x /usr/local/bin/entrypoint.sh

USER jenkins

# Set the entrypoint
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
