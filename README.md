REACT-SPRING-APP

This is a spring boot app with a react front end.

# Setup

Open it in IntelliJ IDEA as a gradle project.

# Run

## With IntelliJ IDEA

Start your IDEA spring run config.  
Start react clientside server:
```
cd <git_repo>/src/main/webapp
npm install
npm start
```

## Command line

Create an application-dev.yml according to application-example.yml with your own ids, secrets and token signing key and execute following commands

```
git clone <url>
./gradlew bootRun -Pdev
# new terminal session
cd <git_repo>/src/main/webapp
npm install
npm start
```
