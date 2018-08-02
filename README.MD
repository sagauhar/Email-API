# Email API

This project provides a simple implementation of a Email API.

## Technologies

When selecting technical stack, the idea was to pick such tools and technologies which can provide base for a stable, reliable, scalable and highly available architecture.

I picked [Netflix libraries](https://github.com/Netflix) since those are mature and are being used to serve millions of customers.

I also picked [Java 8](https://www.oracle.com/au/java/index.html) to be the language of choice simply because of the solution requirements.

[Hystrix](https://github.com/Netflix/Hystrix) and [Archaius](https://github.com/Netflix/archaius) were picked to use it for fail-over functionality between internal API calls and for configuration purposes.

Following diagram shows what happens when a function call is wrapped in a Hystrix Command

![diagramm.png](https://bitbucket.org/repo/zq8Kzy/images/2583105901-diagramm.png)

[RxNetty](https://github.com/ReactiveX/RxNetty) was chosen because of its closeness to [Karyon](https://github.com/Netflix/karyon) which is used by Netflix to create their microservices. This server

gives RxJava on top of Netty server which enables the async operations to be done very conveniently.

To decouple the classes, [Guice](https://github.com/google/guice) as a library to provide Dependency Injection, can be used. It is a very light weight Google backed library compared to Spring. Since, the service was very thin, I thought to leave it as simple as possible.

[Maven](https://maven.apache.org/) is used for source project management and build tool. This is also used to build the artifact which can be used for deployment purpose.

[Docker](https://www.docker.com/) is used for creating application containers which can be used to run it locally as well as on a containerized environment such as Kubernetes or ECS.

## Configuration

The configurations are mentioned in the config.properties file.

To setup the project, please update the following properties:

```
sendgrid.api.key: API key of SendGrid
mailgun.api.key: API key of MailGun
mailgun.api.url: URL of MailGun since it has account domain in it
from.address.value: From address to be used when sending out Emails
http.server.port: Port on which this service should run on
```

## Build

In order to build the code, you need to download or clone the [code](https://github.com/sagauhar/Email-API).

To clone the code, you will require Git on your machine. Once installed, you can follow the GitHub instructions on the repository page for further instructions.

To run the project, you need to have Docker installed on the machine.

```
docker build --tag=email-api .
docker run -p 18080:8080 -t -i email-api
```

The above commands should create an image using the statements mentioned in the Dockerfile and will run the service on port 18080.

## Testing

There are unit tests written for the main logic class. The idea was to adapt to TDD approach rather than create the tests after the development of main classes.

## Things to Improve

The unit tests coverage can be improved. There are no tests for individual classes. This was mainly because of shortage of time.

Input validation and error handling can be improved as well. The clients expect to have predefined set of error codes and messages. I looked in to that by going through the SendGrid and MailGun documentation but couldn't find any spec regarding error codes. Only status codes were mentioned.

API documentation is also missing from the code. This has become a standard and was missed because of deadline. This can be easily done by creating JaxRS class with Annotations which can be picked up by the RxNetty as interceptor and generate at runtime.