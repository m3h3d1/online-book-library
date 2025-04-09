# Online Book Library

The **Online Book Library** is a web application that allows users to browse, search, and manage books in an online library. It includes features for user registration, login, book reviews, and administrative management.

## Project Structure

The project is divided into two main parts:

1. **Frontend**: A React-based application located in the `frontend/` directory.
2. **Backend**: A Spring Boot application located in the `src/main/java/` directory.

---

### Frontend

The frontend is built using React and Vite for fast development and optimized builds.

#### Key Features:

- User-friendly interface for browsing and searching books.
- Role-based access control for users and administrators.
- Integration with REST APIs for dynamic data.

#### Scripts:

- `npm run dev`: Start the development server.
- `npm run build`: Build the project for production.
- `npm run preview`: Preview the production build.
- `npm run lint`: Run ESLint to check for code quality.

#### Dependencies:

- React, React Router, Material-UI, Axios, and more. See the [package.json](frontend/package.json) for the full list.

---

### Backend

The backend is a Spring Boot application that provides REST APIs for the frontend.

#### Key Features:

- User authentication and authorization.
- CRUD operations for books, users, and reviews.
- Role-based access control for administrators.

#### Build and Run:

- Use the Gradle wrapper (`gradlew` or `gradlew.bat`) to build and run the backend:
  ```sh
  ./gradlew bootRun
  ```
