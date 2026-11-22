<div align="center">

<!-- University Information with Large Fonts -->
<h1>Faculty of Sciences - Kénitra</h1>
<h2>Ibn Tofail University</h2>

<br/>

<h3>LUS Génie Logiciel : Développement Frontend et Backend | 2026/2027</h3>
<p>
  <b>Student:</b> Noura Najy &nbsp;|&nbsp; <b>Encadrant:</b> Pr. Samir Mbarki
</p>

<br/>
<hr style="width: 50%; border: 1px solid #ccc;" />
<br/>

<h1>💼 JavaFX Commercial Project</h1>
<p><b>A professional, enterprise-grade desktop application for multi-vendor business management.</b></p>

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

</div>

---

## 📖 About The Project

This application is a fully functional desktop platform designed for businesses to manage their operations. It features a highly secure authentication system, role-based access control, and a seamless, modern user interface with smooth fade transitions.

### ✨ Key Features
*   **🔐 Secure Authentication**: Passwords are never stored in plain text; they are securely hashed using **BCrypt**.
*   **👥 Role-Based Access Control**:
    *   **`ADMIN`**: The system owner. Full access to manage the platform and users.
    *   **`MANAGER`**: Vendors. They can manage their own specific products without seeing others' data.
    *   **`CLIENT`**: Customers. They can self-register, log in, and browse the platform.
*   **✨ Smooth UI/UX**: Seamless `FadeTransition` animations between the Login, Registration, and Dashboard screens, giving it a modern Single Page Application (SPA) feel.
*   **🔒 Password Toggle**: Users can hide/show their passwords during login and registration.
*   **⚡ High Performance**: Utilizes **HikariCP** for ultra-fast database connection pooling.
*   **🎨 Modern Design**: A professional, split-screen responsive interface with a sleek, corporate color palette.

---

## 🛠️ Technology Stack

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="50" alt="Java" title="Java 21" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" height="50" alt="MySQL" title="MySQL" style="margin-left: 20px;" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" height="50" alt="Maven" title="Maven" style="margin-left: 20px;" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" height="50" alt="Spring" title="Spring Security Crypto" style="margin-left: 20px;" />
</div>

<br>

*   **Language:** Java 21
*   **UI Framework:** JavaFX 21
*   **Database:** MySQL 8
*   **Build Tool:** Maven
*   **Connection Pool:** HikariCP
*   **Security:** Spring Security Crypto (BCrypt)
*   **Environment Management:** Dotenv-Java

---

## ⚙️ Prerequisites

Before running this project, ensure you have the following installed:
1.  **Java JDK 21** or higher.
2.  **MySQL Server** (Running locally on port `3306`).
3.  **Maven** (or use the included `mvnw` wrapper).

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/nouranajy/JavaFX-Commercial-project.git
cd JavaFX-Commercial-project
```
### 2. Set up the Database
1. Open **MySQL Workbench** (or your preferred SQL client).

2. Run the **SQL** script located in `database.sql` to automatically create the database, tables, and default roles.

### 3. Configure Environment Variables
1. Locate the `.env.example` file in the root directory of the project.
2. Duplicate it and rename the copy to `.env`.
3. Open the `.env` file and fill in your local MySQL credentials and default Admin details:

```env
# Database Configuration
DB_HOST=127.0.0.1
DB_PORT=3306
DB_USER=root
DB_PASSWORD=your_mysql_password
DB_NAME=CommercialAppDB

# Default Admin Creation
ADMIN_EMAIL=admin@system.com
ADMIN_PASSWORD=admin123