# conference-app
[![Build Status](https://dev.azure.com/catalin-cretu/conference-app/_apis/build/status/catalin-cretu.conference-app?branchName=master)](https://dev.azure.com/catalin-cretu/conference-app/_build/latest?definitionId=1&branchName=master)

Conference Demo App

## Build Tools & Tech
- mvn 3+
- Java 11
- Lombok
- SpringBoot 2
- jUnit 5
- Liquibase
- Togglz

## URLs
|                        |                                          | 
| ---------------------- | ---------------------------------------- |
| actuators              | http://localhost:8078/actuator           |
| db migrations          | http://localhost:8078/actuator/liquibase |
| h2 console (user/pass) | http://localhost:8078/h2-console         |
| togglz console         | http://localhost:8078/togglz-console     |

### References
- conference example data: https://gotoams.nl/2018

## License
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)