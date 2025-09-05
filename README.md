# 🛡️ LaneHeroes

**LaneHeroes** is a project built with **Java Spring Boot**, designed as a database system to store information about Multiplayer Online Battle Arena genre games (examples: Mobile Legends Bang Bang, Dota 2, League of Legends). It serves as the backend API for managing cross-franchise game data such as heroes, games, platforms and companies.

This repository contains both backend and frontend part of **LaneHeroes**, where the data stored and processed in the backend will be displayed. However, this frontend part is to provide a UI/UX for site admins and developers to manage the data within. This is not yet about what the public will see.

This project is meant as a **proof of backend and frontend engineering skill**, focusing on:

## Backend Skills

- Clean architecture
- DTO & service separation
- Batch initialization
- File-based image handling
- Secure user authentication
- Robust API design

## Frontend Skills

- Responsive UI using React and TailwindCSS
- Modular component design
- Dynamic form handling and validation
- RESTful API integration with Axios
- Simple Login/Logout system
- Smooth user experience with optimistic UI and feedback (Toast, Modal, etc.)

---

## 🚀 Tech Stack -- Backend

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Security (Crypto)**
- **Spring Data JPA + Hibernate**
- **MySQL / PostgreSQL**
- **Lombok**
- **Maven**
- **CORS / Static Resource Serving**

## 🚀 Tech Stack -- Frontend

- **TypeScript**
- **React.js (with Next.js)**
- **Tailwind CSS**
- **Axios**
- **React-Hot-Toast**

---

##  Features -- Backend

- **Initialize Batch when starting app** - Batch initializer for platform, company, callsign, game, and hero entities using JSON
- **Static Image Folders** - Custom static image folders per entity (`/images/game`, `/images/hero`, etc.)
- **Missing Image Fallback** - Graceful fallback for missing images (returns `no-image.png`)
- **User Role Control** - Soft-deletable / editable users with role control
- **Cross-Entity Link** - Cross-entity linking (e.g., hero → game, game → platform)
- **MVC Model** -- Clean separation of Entity, DTO, Service, Controller
- **Pagination & filtering**
- **Unit Testing** - Utilizes  **Mockito** (ADDED at July 4th 2025)
- **Secure login** - JWT + password encoding (ADDED at July 9th 2025) 

##  Features -- Frontend

- **Modular CRUD Interface** - Add, update, and delete entities like Hero, Game, Company, Platform, Callsign, and User with dynamic UI components.
- **Unified Modal System** - Centralized modals for both Add and Edit operations, adapting dynamically based on selected entity.
- **Batch Upload Handling** - File upload UI integrated with backend for Excel-based data population.
- **Input Validation** - Built-in checks for required fields, including proper email format and enum-safe role selection.
- **Responsive Layouts** - Cards and containers automatically adapt to screen size for clean display on both desktop and mobile.
- **Toast Notifications** - Real-time success and error feedback using react-hot-toast.
- **Environment-Based API Configuration** - Switch API base URLs using .env setup via NEXT_PUBLIC_API_BASE_URL.
- **Image Previews** - Game and hero icons rendered through next/image for performance optimization.

##  Features -- Both

- **CORS** - Global CORS configuration for seamless connection between frontend and backend
- **Unified ENV** - One ENV file to encompass variables for both frontend and backend
- **Docker** - Wrap up both backend and frontend in one container. (ADDED September 2nd 2025)

---

##  Planned Improvements -- Backend

- **Swagger/OpenAPI documentation**
- **Role-based endpoint access** - full authorization control
- **Other entities that will be necessary later**
- **'Archetypes'** (Categorizing heroes based on their appearance, weapons, play style, etc)

##  Planned Improvements -- Frontend

- **Theme and UI Enhancements** - Improve visual polish with a design system (e.g., Tailwind components, dark mode support, animations via Framer Motion).
- **Frontend Unit & Integration Tests** - Include Unit Testing
- **Form Validation Library** - Use react-hook-form for more scalable and declarative form validation.
- **Caching & Performance Optimization** - Optimize API interactions by caching GET requests
- **Internationalization (i18n)** - Prepare UI for multi-language support.
- **Public Frontend** - Prepare the frontend that will be displayed for public, non-developer audience.


---

## 📂 Folder Structure (Main)

laneheroes/

├── backend/

├── frontend/

└── docker-compose.yml

## 📂 Folder Structure (Backend)

laneheroes/backend/

├── src/main/java/com/laneheroes

│ ├── config/ (For securities, MVC and CORS)

│ ├── controller/ (Endpoints)

│ ├── dto/ # 

│ ├── entities/ # (JPA Entities)

│ ├── enums/

│ ├── exception/

│ ├── initializer/ # (Initializers: Automate database-filling when starting up)

│ ├── repository/ #

│ ├── response/

│ ├── security/

│ ├── services/ # (Business Logic)

│ ├── specifications/ 

│ ├── utilities/

│ └── LaneHeroesApp.java (Main file)

├── src/main/resources/
│ ├── application.properties

│ └── data/ # JSON files for batch upload

├── Dockerfile

├── docker-init.sh (Initialize docker by creating the necessary folders)

└── pom.xml

## 📂 Folder Structure (Frontend)

laneheroes/frontend/

├── app/

│ ├── login/ (for login page)

│ ├── main/ (for main page)

│ ├── globals.css

│ ├── layout.tsx

│ ├── page.tsx

│ ├── components\ (components that are attached throughout the pages)

│ ├── ├──  cards\

│     ├──  form\

│     ├──  modals\

│     ├──  views\

│     ├──  AdminStats.tsx

│     ├──  Dropdown.tsx

│     ├──  Footer.tsx

│     ├──  Header.tsx

│     ├──  LoginPage.tsx

│     ├──  MainPage.tsx

│     ├──  NavBar.tsx

│     └──  View.tsx

│ ├── contexts/ (Reference Data Contexts)

│ ├── types/ (Equals to backend entities)

│ └── utils/ (Miscellanous functions)

├── next.config.ts

├── package.json

├── README.md

├── tailwind.config.ts

├── Dockerfile

└── tsconfig.json

## ️ Getting Started

###  Requirements

- Java 21+
- Maven
- PostgreSQL
- (Optional) Postman / Insomnia for API testing
- Node.js (v18+ recommended. Made with v22.11.0)
- npm
- TypeScript (pre-configured in project)
- React.js with Next.js
- Installed modules/libraries (automatically installed via npm install):
- - axios — for API requests
- - react-hot-toast — for notifications
- - @tailwindcss — for styling
- Modern Web Browser (e.g., Chrome, Firefox)
- Docker Desktop

### ⚙️ Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/KristianW1234/laneheroes.git
   cd laneheroes

2. **Configure Database/Environment**
   Create an .env file within the folder:
    ```bash
    DB_URL=(Your database here. Make sure the schema already exists!)
    DB_URL_DOCKER=(The database used in Docker Container. Match with the values in docker-compose.yml)
    DB_USER=(Your database user here)
    DB_PASSWORD=(Your database password)
    IMG_DIR=(Any place in your server to store images)
    FRONTEND_URL=(For the Lane Heroes Frontend, but only for the local testing)
    FRONTEND_URL_DOCKER=(For Lane Heroes Frontend, be sure to add both the localhost and what is listed in docker-compose.yml, separated with coma. For example: http://localhost:3000,http://frontend:3000 )
    IMG_DIR=(The directory where you keep the images in your local server)
    IMG_DIR_DOCKER=(The directory within the docker where you keep the images. Use the container path)
    JWT_SECRET=(Secret key to utilize in JWT)
    DDL_AUTO=(For local run only, this will execute the batch initialize, filling the database automatically when there is at least a user. Set this 'none')
    DDL_AUTO_DOCKER=(For Docker run only, this create the database schema from the database put within the Docker container)
    NEXT_PUBLIC_API_BASE_URL=(The API endpoint used to access Lane Heroes Backend for local machines)
    NEXT_PUBLIC_API_BASE_URL_DOCKER=(The API endpoint used to access Lane Heroes Backend for Docker Container)
3. **Run the Application**
    
   Make sure you're within the laneheroes folder. 
    ```bash
    docker-compose up --build

    Once the 
   
4. **Login Mockup**

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
    },
    "token":"42358dhew0inf2008f2nu9bf2u9hd893b"
    }
   
## **Notes**

   My earlier commit messages can look messy. For the commits until July 3rd, please refer to NOTES.MD

## **License**

This backend project is licensed under the MIT License.  
You are free to use, modify, and distribute the code under the conditions stated below.

## Disclaimer

This project is a non-commercial fan initiative.  
It references characters, game names, and assets from multiple commercial video games such as Dota 2, Mobile Legends Bang Bang, and others.  
All trademarks and copyrights belong to their respective owners.

No copyright infringement is intended.



