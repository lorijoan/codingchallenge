***Important to do before running***
- Go into src/main/resources/application.properties and src/test/resources/application.properties
- Update tfl.api.key to have your own tfl.api.key in place of the "<YOUR_KEY_HERE>"

How to run (non-docker):
- to create a jar in the target directory, run the command: mvn clean install -U
- run the command from where you have checked the project out to: mvn spring-boot:start
- open a browser window
- investgiate roads you are interested in, some examples:
    - http://localhost:8080/api/v1/road/a101
    - http://localhost:8080/api/v1/road/a23
    - http://localhost:8080/api/v1/road/a1

How to run (docker) - assumption is that you have a local docker setup:
- run the command: docker build --tag codingchallenge:v1.0.0 .
- run the command: docker run -it --expose 8080 -p 8080:8080 localhost/codingchallenge:v1.0.0
- open a browser window
- investgiate roads you are interested in, some examples:
    - http://localhost:8080/api/v1/road/a101
    - http://localhost:8080/api/v1/road/a23
    - http://localhost:8080/api/v1/road/a1


Notes:
- if you are using intellij, please install the lombok plugin
- tests can be run from command line with: mvn clean install -U
- you can investigate the jacoco file by opening to target/site/jacoco/index.html in a web browser after running the above maven command
- it is known there is an issue running the cucumber acceptance test from command line, context issues are appearing and there is not enough time to work them out just yet - thus test is at the moment incorrectly named to be picked up by maven but it works locally in IDE
- there is a snake.yaml vulnerability that we have excluded as a dependency for now, the impact is that we cannot use application.yaml and instead have to use application.properties for the time being
- it was discovered during creation of this service that app_id is defunct on TfL road api, thus it is not being passed during our calls
- RoadService test coverage is slightly reduced - if more time this could be investigated further

Future potential features:
- fix tfl.api.key substitution - this should most importantly not be clear text
- adding interfaces
- authentication on the microservice api calls themselves
- validation of input data
- cleanup of some nasty code (for example the gets in the BDD steps file)
- logging for feedback to developer
