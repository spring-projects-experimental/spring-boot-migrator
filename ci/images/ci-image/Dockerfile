FROM ubuntu:focal-20220801
#
#VOLUME /var/run/docker.sock
#
###########################################################
# JAVA
###########################################################

ADD ./setup.sh /setup.sh
ADD ./get-jdk-url.sh /get-jdk-url.sh
ADD ./get-docker-url.sh /get-docker-url.sh
RUN ./setup.sh java17

ENV JAVA_HOME /opt/openjdk
ENV PATH $PATH:$JAVA_HOME/bin
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ADD docker-lib.sh /docker-lib.sh

#ENTRYPOINT ["/usr/local/bin/dockerd-entrypoint.sh"]
#ENTRYPOINT ["/bin/bash"]
ENTRYPOINT [ "switch", "shell=/bin/bash", "--", "codep", "/bin/docker daemon" ]