FROM maven:3.5.4-jdk-8
WORKDIR /usr/src/
COPY . .
RUN mvn clean package
EXPOSE 8080
CMD ["java", "-classpath", "target/email-api-1.0-SNAPSHOT.jar", "au.com.company.EntryPoint"]