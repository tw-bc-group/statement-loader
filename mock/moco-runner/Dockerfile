FROM java:alpine
ADD https://repo1.maven.org/maven2/com/github/dreamhead/moco-runner/1.0.0/moco-runner-1.0.0-standalone.jar moco-runner-standalone.jar
VOLUME ["/moco/config"]
EXPOSE 12306
ENTRYPOINT java -jar moco-runner-standalone.jar http -p 12306 -c /moco/config/*.json