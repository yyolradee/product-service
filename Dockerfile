FROM --platform=linux/amd64 openjdk:17
EXPOSE 8080
ADD target/ProductServiceProject-0.0.1-SNAPSHOT.jar ProductServiceProject-0.0.1-SNAPSHOT.jar
CMD ["sh","-c","java -jar /ProductServiceProject-0.0.1-SNAPSHOT.jar"]