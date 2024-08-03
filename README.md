# C-Lab Server
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Spring Boot Gradle CI](https://github.com/KGU-C-Lab/clab-server/actions/workflows/spring-boot-gradle-ci.yml/badge.svg)](https://github.com/KGU-C-Lab/clab-server/actions/workflows/spring-boot-gradle-ci.yml)

## Project Overview
The C-Lab Server project started as a platform for the C-Lab development and security club at Kyonggi University. However, our goal is to go beyond serving just C-Lab. We aim to create a valuable reference for anyone looking to build a similar internal product. For this reason, we are making all our code and various server-related configurations publicly available. We continuously strive to improve the quality of our project code through various efforts and experiments.

Additionally, we are committed to creating a secure platform that users can trust. To achieve this, we adhere to the '[Software Development Security Guide](https://www.kisa.or.kr/2060204/form?postSeq=5&lang_type=KO&page=1)' provided by the Korea Internet & Security Agency (KISA). We are always open to feedback and actively seek to incorporate diverse opinions and suggestions into our work.

Our backend system, as well as our frontend, are both publicly available. If you're interested, please also check out our frontend repository [here](https://github.com/KGU-C-Lab/clab.page).

## Project Purpose and Features
The C-Lab Server project aims to support club activities and facilitate smooth communication among members by providing integrated services such as:

- **Member Management**: Through the Members Page, it offers essential features for club operations including club management, learning management system, book loans, and financial management.
- **Collaboration and Participation**: It enhances member engagement and collaboration with features like individual and group schedule management, personal cloud storage, community functions, job postings, and performance aggregation.
- **Efficiency and Convenience**: By providing various features that enhance user convenience and operational efficiency in one platform, it simplifies the everyday management of the club.

## Requirements
- [Docker](https://docs.docker.com/engine/install/) and [Docker Compose](https://docs.docker.com/compose/install/) installed
- Linux-based OS is recommended
- For Windows, use [WSL2](https://learn.microsoft.com/en-us/windows/wsl/install)

## Getting Started
### Clone the Repository
First, clone the repository to your local machine:
```bash
git clone https://github.com/KGU-C-Lab/clab-server.git
cd clab-server
```

### Docker Compose Setup
The Docker Compose file is located in the `infra` directory. Follow these steps to set up and run the containers.

### Note
- If you want to persist container data, ensure the necessary directories are created and permissions are set:

```bash
# Only if you want to persist data
sudo mkdir -p /infra/nginx /infra/jenkins /infra/redis/data /infra/postgresql/data
sudo chown -R $(whoami):$(whoami) /infra
```

- Set environment variables (these can also be placed in a .env file):

```bash
# Set environment variables
export REDIS_PASSWORD=your_redis_password
export POSTGRES_USER=your_postgres_username
export POSTGRES_PASSWORD=your_postgres_password
```

### Run Docker Compose
Run Docker Compose to start the containers:

```bash
cd infra
docker-compose up -d # or 'docker compose up -d'
```

### Configure Spring Boot Application
After starting the containers, configure the `src/main/resources/application.yml` file. Replace `${}` placeholders with actual values.

### Run the Application
After all the configurations are done, run the Spring Boot application:

```bash
./gradlew bootRun
```

### Additional Information
- Docker and Docker Compose installation: [Get Docker](https://docs.docker.com/get-docker/)
- WSL2 installation and setup: [Install WSL2](https://learn.microsoft.com/en-us/windows/wsl/install)
- Spring Boot official documentation: [Spring Boot Docs](https://spring.io/projects/spring-boot)

## Project Structure
```markdown
api/
├── category/
│   ├── domain/
│   │   ├── adapter/
│   │   │   ├── in/
│   │   │   │   ├── web/
│   │   │   └── out/
│   │   │       ├── persistence/
│   │   ├── application/
│   │   │   ├── dto/
│   │   │   ├── event/
│   │   │   ├── port/
│   │   │   │   ├── in/
│   │   │   │   └── out/
│   │   │   ├── service/
│   │   └── domain/
└── external/
│   ├── category/
│   │   ├── domain/
│   │   │   ├── application/
│   │   │   │   ├── port/
│   │   │   │   └── service/
└── global/
│   ├── auth/
│   ├── common/
│   ├── config/
│   ├── exception/
│   ├── handler/
│   ├── util/
```

### Architecture
This project is structured according to the port-and-adapter ([hexagonal](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))) architecture pattern. This architecture promotes a clean separation of concerns, making the codebase more modular, testable, and maintainable.

### Package Organization
The packages in this project are organized using the private-package convention. This helps in encapsulating implementation details and exposing only necessary components, leading to better modularity and maintainability.

### `domain`
- **adapter**: Contains adapters that interact with external systems. This includes `in` adapters for handling web interfaces and `out` adapters for interacting with databases.
    - **in**: Handles incoming requests from external sources, such as controllers for handling HTTP requests.
    - **out**: Handles interactions with external systems, such as adapters for database integration.
- **application**: Contains services and ports that handle domain logic. DTOs (Data Transfer Objects) and events are also included in this layer.
    - **dto**: Contains DTO classes for handling requests and responses. DTOs are objects used for data transfer between layers.
    - **event**: Contains classes related to domain events, which represent significant occurrences in the business logic.
    - **port**: Defines interfaces for inbound and outbound ports.
        - **in**: Interfaces for handling incoming requests to the application.
        - **out**: Interfaces for handling requests to external systems or databases.
    - **service**: Contains service classes that implement inbound port interfaces. These classes implement business logic and may call outbound ports as needed.
- **domain**: Contains the domain model, which defines the core business logic and rules of the application. This includes entities, value objects, and aggregates.

### `external`
- **category**: Contains classes related to specific domains, including logic for interacting with external systems.
    - **domain**: Defines the business logic and rules for each domain.
    - **application**: Handles application logic for the domain.
        - **port**: Defines inbound ports for handling external requests and outbound ports for making requests to external systems.
        - **service**: Contains service classes that implement inbound port interfaces.

### `global`
- **auth**: Contains classes for handling authentication-related functionality.
- **common**: Contains utility classes that are commonly used across multiple domains.
- **config**: Contains configuration-related classes for the project.
- **exception**: Contains classes for handling global exception logic.
- **handler**: Contains classes for defining handlers for exceptions and specific situations.
- **util**: Contains general utility classes.

## System Architecture Diagram
![System-Architecture-Diagram](images/System-Architecture-Diagram.png)

## Monitoring Services Diagram
![Monitoring-Services-Diagram](images/Monitoring-Services-Diagram.png)

## Entity-Relationship Diagram
> **Note**: Although the actual database design avoids using foreign keys, the diagram visually represents foreign key connections for illustrative purposes.

![Entity-Relationship-Diagram](images/Entity-Relationship-Diagram.png)

## License
This project is licensed under the GNU General Public License (GPL) v3.0. For more information, see the [LICENSE](https://github.com/KGU-C-Lab/clab-server?tab=GPL-3.0-1-ov-file#readme) file.

## Contributors
<a href="https://github.com/KGU-C-Lab/clab-server/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=KGU-C-Lab/clab-server" />
</a>

## Contributing
To contribute to this project, follow these steps:
[CONTRIBUTING.md](CONTRIBUTING.md)
