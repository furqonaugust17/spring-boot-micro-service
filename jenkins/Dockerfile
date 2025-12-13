FROM jenkins/jenkins:lts

USER root

# Install dependencies (Existing dependencies kept, plus necessary ones for Kubectl)
RUN apt-get update && apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    git \
    apt-transport-https # Tambahan untuk kubectl

# Add Docker's official GPG key and repository (Existing steps)
RUN mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg

RUN echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" \
  > /etc/apt/sources.list.d/docker.list

# Install Docker CLI + Docker Compose v2 (Existing steps)
RUN apt-get update && apt-get install -y \
    docker-ce-cli \
    docker-compose-plugin


# ==========================================================
# PENAMBAHAN UNTUK INSTALL KUBECTL
# ==========================================================
# 1. Unduh kunci GPG Kubernetes
RUN curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.28/deb/Release.key | gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

# 2. Tambahkan repositori Kubernetes
RUN echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.28/deb/ /' | tee /etc/apt/sources.list.d/kubernetes.list

# 3. Update dan Instal kubectl
RUN apt-get update
RUN apt-get install -y kubectl
# ==========================================================


# Add Jenkins user into docker group (Existing steps)
RUN groupadd -f docker && usermod -aG docker jenkins

USER jenkins