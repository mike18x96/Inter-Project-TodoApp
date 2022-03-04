FROM openjdk:11
ADD target/todo_app-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar todo_app-0.0.1-SNAPSHOT.jar
