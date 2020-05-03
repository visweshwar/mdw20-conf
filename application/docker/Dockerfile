FROM xqdocker/ubuntu-openjdk:8
ENV MASTER_KEY YGNalmAN2M6hOySJuwq1fckWpfb2IZ/6RdtvtubB7qFAq1N+O836D5QD00RCxdin25OEXaCNSviZn6Zu5xEoOep1n1G0wi+lLUZMXqchBjZ7GDQQyb4JuOLsDwXxnoJV
ENV JAVA_OPTS: "-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n -Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m"

#Install base Ubuntu libraries
RUN apt-get update && apt-get install -y curl wget apt-transport-https ca-certificates apt-utils gnupg

#Install libmongocrypt
RUN  sh -c 'curl -s https://www.mongodb.org/static/pgp/libmongocrypt.asc | gpg --dearmor >/etc/apt/trusted.gpg.d/libmongocrypt.gpg'
RUN echo "deb https://libmongocrypt.s3.amazonaws.com/apt/ubuntu xenial/libmongocrypt/1.0 universe" |  tee /etc/apt/sources.list.d/libmongocrypt.list
RUN apt-get update &&  apt-get install -y libmongocrypt-dev

# Install  MongoDB Enterprise cryptd
RUN wget -qO - https://www.mongodb.org/static/pgp/server-4.2.asc |  apt-key add -
RUN echo "deb [ arch=amd64,arm64,s390x ] http://repo.mongodb.com/apt/ubuntu xenial/mongodb-enterprise/4.2 multiverse" |  tee /etc/apt/sources.list.d/mongodb-enterprise.list
RUN apt-get update
RUN apt-get install -y mongodb-enterprise-cryptd

#Copy application JAR
ARG JAR_FILE=libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080

#option a
##Run Supervisor in case you have to run additional processes
#RUN apt-get -y install supervisor && mkdir -p /var/log/supervisor && mkdir -p /etc/supervisor/conf.d
##RUN ls /etc
##copying config file from local to docker image
#COPY supervisor.conf /etc/supervisor.conf
COPY script.sh script.sh
RUN chmod +x script.sh

#CMD ["supervisord", "-c", "/etc/supervisor.conf"]   # starting supervisord service
CMD ["/bin/sh","script.sh"]

#OR
#option b
#ENTRYPOINT ["java","-jar","/app.jar"]
