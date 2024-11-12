# To-dos Application

## About the project

Task Management System.

This is an application developed to facilitate task management.

The application allows for viewing tasks with pagination, searches, and filters by title, description, client, status, priority, creation date, and delivery date.

The application enables the registration, editing, and deletion of tasks, as well as the management of their status and priority, offering a user-friendly interface on the front end and a robust API on the back end.

Additionally, the API is documented with Swagger API to make the available routes, payloads, and responses easy to understand.

## Entity model
![todos_app drawio](https://github.com/user-attachments/assets/4418b910-998b-4418-bd1e-23fcc7c8a7fe)

## Techs

### Front-end
- **Framework**: Next.js and React.js
- **Language**: TypeScript
- **Styles**: Material UI and Tailwind CSS

### Back-end
- **Framework**: Spring
- **Language**: Java
- **Tests**: JUnit and Mockito

### Database
- **Database**: PostgreSQL

### API Documentation
- **Tool**: Swagger API

### Containers
- **Docker** and **Docker Compose**

## How to run

Follow the steps below to set up and run the project on your local machine.

## Prerequisites

- Git
- Node.js and npm (for front-end)
- java 21 (JDK) e Maven
- Docker and Docker Compose

## Steps

**Make sure you have opened the ports 8080, 3000 and 5432 on your machine locally**

1. **Clone this back-end repo with the name 'todos'':**
   ```bash
   git clone https://github.com/Marcus-Nastasi/java-backend-todos-app.git

- **Configure the 'application.properties' file:**
   ```bash
   spring.application.name=todos

   spring.jpa.hibernate.show-sql=true
   spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=123
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
   
   # Security config
   spring.security.token.secret = [your_token_secret].
  
   # Hibernate properties 
   spring.jpa.hibernate.ddl-auto=update

2. **Clone front-end repo on the same folder, with the name 'todos-front':**
   ```bash
   git clone https://github.com/Marcus-Nastasi/to-dos-app-v2.git
   
3. **On the root folder, create a 'docker' folder, and create the docker-compose.yml file:**
    ```yml
    version: '3.8'

    services:
    postgres:
    image: postgres:15
    environment:
    POSTGRES_USER: postgres
    POSTGRES_PASSWORD: 123
    POSTGRES_DB: postgres
    volumes:
    - postgres-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    
    backend:
    build: ../todos
    environment:
    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/postgres
    SPRING_DATASOURCE_USERNAME: postgres
    SPRING_DATASOURCE_PASSWORD: 123
    depends_on:
    - postgres
    ports:
      - "8080:8080"
    
    frontend:
    build: ../todos-front
    environment:
    API_URL: http://backend:8080
    ports:
    - "3000:3000"
    
    volumes:
    postgres-data:

4. **Run the application with Docker: Ensure you're in the project's root directory and execute Docker Compose to start all services automatically:**
    ```bash
    [sudo] docker-compose up --d

5. **Wait for the build to complete and access the application: Once the build is finished, the application will be available in your browser:**
   ```bash
    http://localhost:3000/

6. **You can access the API documentation created with Swagger at the route:**
   ```bash
    http://localhost:8080/swagger-ui/index.html
   
7. **Once you've built the images for the first time, you can create in the root folder of your computer's user, this bash script to run or stop the app easily (make sure you write your own information on [ ] marked fields):**
    ```bash
    usage() {
        echo "Uso: $0 --q"
        exit 1
    }
    
    if [ "$1" == "--q" ]; then
        action="stop"
    else
        action="start"
        access='Access http://localhost:3000/ or http://localhost:8080/swagger-ui/index.html'
    fi
    
    cd [/your/path/to/the/app/docker/folder/]
    
    echo ''
    echo 'Insert your password to run...'
    echo ''
    
    [sudo] docker-compose $action
    
    echo ''
    echo "Application ${action}ed!"
    
    if [ "$action" != "stop" ]; then
        echo ''
        echo "${access}"
    fi
    
    cd [/your/path/to/user/root/]

8. **Once created the bash script, from your root dir you can start the app just by running:**
    ```bash
    . [bash_file_name].sh

9. **From your root dir you can stop the app just by running:**
    ```bash
    . [bash_file_name].sh --q

## Unit tests

###  Build and execution with unit tests
- **To run the unit tests or build without skipping the tests, make sure you have a postgres database on your 5432 port.**
