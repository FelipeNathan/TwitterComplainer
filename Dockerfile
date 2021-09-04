# Builder
FROM gradle:7.2.0-jdk11 AS builder

RUN echo "Creating and Entering on /home/project"
WORKDIR /home/project

RUN echo "Copying project to $PWD"
COPY ./ /home/project

RUN echo "Building project"
CMD gradle clean assemble --debug --info --stacktrace

RUN echo "Unpacking project"
RUN unzip /home/project/build/distributions/TwitterComplainer-1.0.zip -d /home/project/build/distributions/app/

# Application
FROM openjdk:11-jre-slim

RUN echo "Creating and Endering on /opt/app"
WORKDIR /opt/app

RUN echo "Copying unzipped project to /opt/app"
COPY --from=builder /home/project/build/distributions/app/ /opt/app/

CMD ["TwitterComplainer-1.0/bin/TwitterComplainer"]