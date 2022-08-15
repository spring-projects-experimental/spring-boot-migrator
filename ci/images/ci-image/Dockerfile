FROM --platform=linux/amd64  ubuntu:focal-20220801

ADD ./setup.sh /setup.sh
ADD get-jdk-url.sh /get-jdk-url.sh
#ADD get-docker-url.sh /get-dockerdocker-url.sh
RUN ./setup.sh java17

ENV JAVA_HOME /opt/openjdk
ENV PATH $JAVA_HOME/bin:$PATH
#ADD docker-lib.sh /docker-lib.sh

#ADD build-release-scripts.sh /build-release-scripts.sh
#ADD releasescripts /release-scripts
#RUN ./build-release-scripts.sh

#INSTALL MAVEN
ARG MAVEN_VERSION=3.6.3

# 2- Define a constant with the working directory
ARG USER_HOME_DIR="/root"

# 4- Define the URL where maven can be downloaded from
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

# 5- Create the directories, download maven, validate the download, install it, remove downloaded file and set links
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
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
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

COPY v1/docker-lib.sh /usr/local/bin/docker-lib.sh
COPY v1/entrypoint.sh /usr/local/bin/entrypoint.sh

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["/bin/bash"]


#ENTRYPOINT [ "switch", "shell=/bin/bash", "--", "codep", "/bin/docker daemon" ]

#CMD ["/bin/bash"]