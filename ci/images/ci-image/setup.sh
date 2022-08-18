#!/bin/bash
set -ex

###########################################################
# UTILS
###########################################################

export DEBIAN_FRONTEND=noninteractive
apt-get update
apt-get install --no-install-recommends -y git curl jq tzdata ca-certificates net-tools libxml2-utils git curl libudev1 libxml2-utils iptables iproute2 jq gnupg lsb-release software-properties-common python3-pip
apt-get update
ln -fs /usr/share/zoneinfo/UTC /etc/localtime
dpkg-reconfigure --frontend noninteractive tzdata
rm -rf /var/lib/apt/lists/*

# curl https://raw.githubusercontent.com/spring-io/concourse-java-scripts/v0.0.4/concourse-java.sh > /opt/concourse-java.sh


###########################################################
# JAVA
###########################################################
JDK_URL=$( ./get-jdk-url.sh $1 )

mkdir -p /opt/openjdk
cd /opt/openjdk
curl -L ${JDK_URL} | tar zx --strip-components=1
test -f /opt/openjdk/bin/java
test -f /opt/openjdk/bin/javac

if [[ $# -eq 2 ]]; then
	cd /
	TOOLCHAIN_JDK_URL=$( ./get-jdk-url.sh $2 )

	mkdir -p /opt/openjdk-toolchain
	cd /opt/openjdk-toolchain
	curl -L ${TOOLCHAIN_JDK_URL} | tar zx --strip-components=1
	test -f /opt/openjdk-toolchain/bin/java
	test -f /opt/openjdk-toolchain/bin/javac
fi

############################################################
## MAVEN
############################################################

#INSTALL MAVEN
MAVEN_VERSION=3.6.3

# 2- Define a constant with the working directory
USER_HOME_DIR="/root"

# 4- Define the URL where maven can be downloaded from
BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

# 5- Create the directories, download maven, validate the download, install it, remove downloaded file and set links
mkdir -p /usr/share/maven /usr/share/maven/ref \
  && echo "Downlaoding maven" \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  \
  && echo "Unziping maven" \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  \
  && echo "Cleaning and setting links" \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# 6- Define environmental variables required by Maven, like Maven_Home directory and where the maven repo is located


###########################################################
# DOCKER
###########################################################
#mkdir -p /etc/apt/keyrings
#curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
#echo \
#  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
#  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
#sudo apt-get update
#sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
#
#
#curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
#APT_KEY_DONT_WARN_ON_DANGEROUS_USAGE=1
#apt-key fingerprint 0EBFCD88
#add-apt-repository "deb [arch=arm64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
#apt-get update
#apt-cache policy docker-ce
#apt-get -y install docker-ce
#usermod -aG docker root


######

cd /
DOCKER_URL=$( ./get-docker-url.sh )
curl -L ${DOCKER_URL} | tar zx
mv /docker/* /bin/
chmod +x /bin/docker*

export ENTRYKIT_VERSION=0.4.0
curl -L https://github.com/progrium/entrykit/releases/download/v${ENTRYKIT_VERSION}/entrykit_${ENTRYKIT_VERSION}_Linux_x86_64.tgz | tar zx
chmod +x entrykit && \
mv entrykit /bin/entrykit && \
entrykit --symlink

# Docker Compose
pip3 install docker-compose