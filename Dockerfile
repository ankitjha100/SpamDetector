FROM tomcat:11-jdk21

COPY SpamDetector.war /usr/local/tomcat/webapps/

EXPOSE 8080
