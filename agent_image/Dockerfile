FROM ubuntu

LABEL maintainer="Mohammed Samsuddin <mshmsudd@gmail.com>"

USER root

# Make sure the package repository is up to date.
RUN apt-get update -y && apt-get install -y curl sudo && \
    # Install maven
    #sudo apt install maven && \
    #sudo apt install default-jdk
    # Install EKSCTL
    #RUN curl --silent –location "https://github.com/weaveworks/eksctl/releases/download/latest_release/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
    # Install Docker
    RUN apt install docker.io
RUN snap install docker
# Install AWS CLI
RUN apt install awscli -y
# Install KUBECTL
#RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl && \
#RUN chmod +x ./kubectl && \

# Install Hadolint for Linting test
#RUN wget -O /bin/hadolint https://github.com/hadolint/hadolint/releases/download/v1.16.3/hadolint-Linux-x86_64 && \
#RUN chmod +x /bin/hadolint

ENTRYPOINT ["jenkins-slave"]