# 🛡️ LaneHeroes Frontend (Admin)

**LaneHeroes** is a backend project built with **Java Spring Boot**, designed as a database system to store information about Multiplayer Online Battle Arena genre games (examples: Mobile Legends Bang Bang, Dota 2, League of Legends). It serves as the backend API for managing cross-franchise game data such as heroes, games, platforms and companies.

This is the Frontend part of **LaneHeroes**, where the data stored and processed in the backend will be displayed. However, this frontend part is to provide a UI/UX for site admins and developers to manage the data within. This is not yet about what the public will see.

This project is meant as a **proof of frontend engineering skill**, focusing on:
- Responsive UI using React and TailwindCSS
- Modular component design
- Dynamic form handling and validation
- RESTful API integration with Axios
- Simple Login/Logout system
- Smooth user experience with optimistic UI and feedback (Toast, Modal, etc.)

----

## 🚀 Tech Stack

- **TypeScript** – for type-safe development
- **React.js (with Next.js)** – frontend framework with server-side rendering capabilities
- **Tailwind CSS** – for utility-first responsive styling
- **Axios** – for HTTP requests to backend APIs
- **React-Hot-Toast** – for instant feedback and notifications

----

## Features

- **Modular CRUD Interface** - Add, update, and delete entities like Hero, Game, Company, Platform, Callsign, and User with dynamic UI components.
- **Unified Modal System** - Centralized modals for both Add and Edit operations, adapting dynamically based on selected entity.
- **Batch Upload Handling** - File upload UI integrated with backend for Excel-based data population.
- **Input Validation** - Built-in checks for required fields, including proper email format and enum-safe role selection.
- **Responsive Layouts** - Cards and containers automatically adapt to screen size for clean display on both desktop and mobile.
- **Toast Notifications** - Real-time success and error feedback using react-hot-toast.
- **Environment-Based API Configuration** - Switch API base URLs using .env setup via NEXT_PUBLIC_API_BASE_URL.
- **Image Previews** - Game and hero icons rendered through next/image for performance optimization.

----

## Future Improvements

- **Theme and UI Enhancements** - Improve visual polish with a design system (e.g., Tailwind components, dark mode support, animations via Framer Motion).
- **Frontend Unit & Integration Tests** - Include Unit Testing
- **Form Validation Library** - Use react-hook-form for more scalable and declarative form validation.
- **Caching & Performance Optimization** - Optimize API interactions by caching GET requests
- **Internationalization (i18n)** - Prepare UI for multi-language support.
- **Public Frontend** - Prepare the frontend that will be displayed for public, non-developer audience.

----

## 📂 Folder Structure (Backend)

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

└── tsconfig.json

## Getting Started

### Requirements

- Node.js (v18+ recommended. Made with v22.11.0)

- npm

- TypeScript (pre-configured in project)

- React.js with Next.js

- Installed modules/libraries (automatically installed via npm install):

- - axios — for API requests

- - react-hot-toast — for notifications

- - @tailwindcss — for styling

- Modern Web Browser (e.g., Chrome, Firefox)

### ⚙️ Setup Instructions

1. **Prepare the backend server**

Go here for more details: https://github.com/KristianW1234/laneheroes/ And make sure they're running.

2. **Clone the repository**

``bash
   git clone https://github.com/KristianW1234/laneheroes-frontend.git
   cd laneheroes-backend

3. **Install the dependencies**

Run this to install all within the package.json:

``bash
   npm install

4. **Setup local environment**

Create a file called .env.local within the src folder, then add the location of the backend server. For example:

```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/laneHeroes
```

5. **Run the Development Server**

``bash
   npm run dev

The server will run in http://localhost:3000

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