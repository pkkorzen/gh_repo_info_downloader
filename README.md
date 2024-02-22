## Introduction 🔔
GitHub repository downloader api allows to retrieve list of repositories with their branches and last commits based on provided username.

## Features 🪶
- retrieve github repositories by given username

## Installation 📦
1. Clone this repo using 
``` git clone https://github.com/pkkorzen/gh_repo_info_downloader.git ```
2. Navigate to the root of the project via command line and execute the command
``` mvn spring-boot:run ```
3. The app is running.

## Usage 🚀
Once application is running type https://localhost:8080/repos/{username) in the browser or run a request using postman. Remember to replace username with existing username of your choice, whose repositories you'd like to receive.

## Endpoints 📡
| Endpoint                                                                                         | Method | Description                               |
| ------------------------------------------------------------------------------------------------ | ------ | ----------------------------------------- |
| `/repos/{username}`                                                                              | GET    | retrieve github repositories by username  |

##  Examples 🦉
### Request (successful) 📝
http://localhost:8080/repos/pkkorzen

### Response (successful) 📝
```json
[{
        "repositoryName": "car-rental-app",
        "ownerLogin": "pkkorzen",
        "branches": [{
                "branchName": "master",
                "lastCommitSha": "995a907c50161e592d27b4c94fd6e7cb7c590e24"
            }
        ]
    }, {
        "repositoryName": "gh_repo_info_downloader",
        "ownerLogin": "pkkorzen",
        "branches": [{
                "branchName": "master",
                "lastCommitSha": "f0a720f821396fdc682d821d2735399ce7dbcf7c"
            }
        ]
    }, {
        "repositoryName": "imdb_client",
        "ownerLogin": "pkkorzen",
        "branches": [{
                "branchName": "main",
                "lastCommitSha": "9cb1a6f6973e426e828781e2c30e32932730cbc5"
            }
        ]
    }, {
        "repositoryName": "pandemic-angular",
        "ownerLogin": "pkkorzen",
        "branches": [{
                "branchName": "master",
                "lastCommitSha": "b7ea446629b5fb4c426acc403de685c71282db5c"
            }, {
                "branchName": "moveService",
                "lastCommitSha": "4359428b15caaf8546f6249269c9df0144dcb7f3"
            }
        ]
    }, {
        "repositoryName": "pandemic-spring-boot",
        "ownerLogin": "pkkorzen",
        "branches": [{
                "branchName": "master",
                "lastCommitSha": "5806f12acd7c1ea5ec84b8ec8a3dc6d692eca5b8"
            }, {
                "branchName": "model_change_simple_factory",
                "lastCommitSha": "2633f877957d128e85e8f8b9c19339150e3688be"
            }
        ]
    }, {
        "repositoryName": "weather-station-esp32",
        "ownerLogin": "pkkorzen",
        "branches": [{
                "branchName": "feature_ota",
                "lastCommitSha": "534dd5b6206d31221303fac83af98be3224ae4b0"
            }, {
                "branchName": "feature_program_enhanced_with_logToFile",
                "lastCommitSha": "8c8bf42d3e6428b94fd24e0ab5409d8bd0147c55"
            }, {
                "branchName": "master",
                "lastCommitSha": "66b4a26b178931fd0b9fc121745aeec51c6e1998"
            }
        ]
    }
]
```

### Request (unsuccessful) 🧱
http://localhost:8080/repos/pkkorzen23

### Response (unsuccessful) 🧱
```json
{
    "statusCode": 404,
    "message": "GitHub user: pkkorzen23 not found"
}
```

## Technologies
<img src="https://img.shields.io/badge/-JAVA 21-red" alt="Java 21" /> <img src="https://img.shields.io/badge/-MAVEN-red" alt="MAVEN" />
<img src="https://img.shields.io/badge/-SPRING BOOT 3-red" alt="Spring Boot 3" /> <img src="https://img.shields.io/badge/-LOMBOK-red" alt="Lombok" />
<img src="https://img.shields.io/badge/-JUNIT-red" alt="JUnit" /> <img src="https://img.shields.io/badge/-MOCKITO-red" alt="Mockito" />
