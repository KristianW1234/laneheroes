# 🛡️ LaneHeroes Backend

**LaneHeroes** is a backend project built with **Java Spring Boot**, designed as a database system to store information about Multiplayer Online Battle Arena genre games (examples: Mobile Legends Bang Bang, Dota 2, League of Legends). It serves as the backend API for managing cross-franchise game data such as heroes, games, platforms and companies.

This project is meant as a **proof of backend engineering skill**, focusing on:
- Clean architecture
- DTO & service separation
- Batch initialization
- File-based image handling
- Secure user authentication
- Robust API design

---

## 🚀 Tech Stack

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Security (Crypto)**
- **Spring Data JPA + Hibernate**
- **MySQL / PostgreSQL**
- **Lombok**
- **Maven**
- **CORS / Static Resource Serving**

---

##  Features

- Admin user bootstrap with `CommandLineRunner`
- Batch initializer for platform, company, callsign, game, and hero entities using JSON
- Custom static image folders per entity (`/images/game`, `/images/hero`, etc.)
- Graceful fallback for missing images (returns `no-image.png`)
- Soft-deletable / editable users with role control
- Cross-entity linking (e.g., hero → game, game → platform)
- Global CORS configuration
- Clean separation of Entity, DTO, Service, Controller
- Pagination & filtering
- Unit tests using **Mockito** (From July 4th)

---

##  Planned Improvements


- Secure login (JWT + password encoding)
- Swagger/OpenAPI documentation
- Role-based endpoint access (full authorization control)
- Other entities that will be necessary later (Hero Skills)
- 'Archetypes' (Categorizing heroes based on their appearance, weapons, play style, etc)


---

## 📂 Folder Structure (Backend)
laneheroes/backend/

├── src/main/java/com/laneheroes

│ ├── config/ (For securities, MVC and CORS)

│ ├── controller/ (Endpoints)

│ ├── dto/ # 

│ ├── entity/ # (JPA Entities)

│ ├── initializer/ # (Initializers: Automate database-filling when starting up)

│ ├── repository/ # 

│ ├── service/ # (Business Logic)

│ └── LaneHeroesApp.java (Main file)

├── src/main/resources/
│ ├── application.properties

│ └── data/ # JSON files for batch upload

└── pom.xml

## ️ Getting Started

###  Requirements

- Java 21+
- Maven
- PostgreSQL
- (Optional) Postman / Insomnia for API testing

### ⚙️ Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/KristianW1234/laneheroes.git
   cd laneheroes

(It's preferrable if it's put within a 'backend' folder)

2. **Configure Database/Environment**
   Create an .env file within the folder:
    ```bash
    DB_URL=(Your database here. Make sure the schema already exists!)
    DB_USER=(Your database user here)
    DB_PASSWORD=(Your database password)
    IMG_DIR=(Any place in your server to store images)
    FRONTEND_URL=(For the Lane Heroes Frontend, but this is not necessary if you just want the standalone backend)

3. **Run the Application**
    ```bash
    mvn spring-boot:run
   
4. **Endpoints**

- Base API URL: http://localhost:8080/laneHeroes
- Static image access: http://localhost:8080/laneHeroes/images/(hero/game/company)/(imageName)

5. **Login Mockup**

    Use the endpont /api/auth with this:
    ```bash
    {
      "userName": "admin",
      "userPassword": "admin123"
    }
   ```
   Example of success:
    ```bash
   {
    "message": "Login successful",
    "status": "Success",
    "data": {
    "userId": 1,
    "userName": "admin",
    "role": "ADMIN",
    "active": true
    }
   
## **Notes**

   My earlier commit messages can look messy. For the commits until July 3th, please refer to NOTES.MD

## **Frontend Integration**
    
    Check this repository out: https://github.com/KristianW1234/laneheroes-frontend


## **Contact**

    Contact me in: kristian.wijaya1234@gmail.com


## **License**

This backend project is licensed under the MIT License.  
You are free to use, modify, and distribute the code under the conditions stated below.

## Disclaimer

This project is a non-commercial fan initiative.  
It references characters, game names, and assets from multiple commercial video games such as Dota 2, Mobile Legends Bang Bang, and others.  
All trademarks and copyrights belong to their respective owners.

No copyright infringement is intended.
