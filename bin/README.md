Copyright &copy; Frubana 2020. All rights reserved.

# com.frubana.operations:warehouse-change-me

[Maven](http://maven.apache.org/) base project for change-me using [Spring Boot
](https://projects.spring.io/spring-boot/) and [JDBI](https://jdbi.org/).

## Building and Running

### Build-Time Dependencies

The tool chain required to build this project consists of:

- JDK 11
- Maven
- Postgresql

All other dependencies are resolved using Maven at build time. 


### Configuration files
The project uses Spring application yaml to load the properties, you can use the
j2 as base to create a new application file and replace all the environmental 
properties for your local ones.


### Run-Time Dependencies

The PostgreSQL database must exist for the migrations to run without problems.

### Building and Deploying

To create the package:

    mvn clean package
    
If all you want is to test locally by manually invoking Spring Boot executable JAR's:

    java -jar change-me-0.0.1.jar


The generated POM files implicitly invoke [JUnit](http://junit.org) based unit tests using Maven's _surefire_ plugin.
In other words, a command line like

    mvn install

implicitly invokes unit tests. The build will break if any unit tests fail.

### Coverage

| Element             | Covered percentage |
| ------------------- | ------------------ |
| class               |        `0%`       |
| method              |        `0%`       |
| line                |        `0%`       |


### Version

    yms-0.0.1

