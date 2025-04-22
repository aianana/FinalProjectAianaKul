# Customer Order Tracker

## Student Name
**Kulnazarova Aiana**
**COMCEH-24**

---

##  Description

**Customer Order Tracker** is a Java-based application that helps manage customer data and order processing. It features both a **Command Line Interface (CLI)** and a **Graphical User Interface (GUI)** built with Swing. The system uses **SQLite** for persistent data storage and includes a **user authentication system** with role-based access control (Admin/User). It also supports **JSON-based import/export** and **summary report generation**.

---


##  Objectives

- Implement full **CRUD operations** for customers and orders.
- Provide both **CLI and GUI interfaces** for user interaction.
- Use **SQLite with JDBC** for database management.
- Integrate **user authentication** and **role-based access control**.
- Enable **data import/export** via JSON files.
- Allow **report generation** for key statistics and metrics.
- Follow modular and clean code practices.
- Ensure input validation and error handling throughout.

##  Project Requirement List

The following list outlines the core features and deliverables that were implemented to meet the project’s objectives:

- User Authentication System

Users must log in using a valid username and password before accessing application features.

- Role-Based Access Control
  
Users are assigned roles (Admin or User), which define their permissions. Only Admins can delete or clear data.

- CRUD Operations for Customers
  
Users can create, read, and (Admins only) delete customer records.

- CRUD Operations for Orders
  
Users can create, read, update order status, and (Admins only) delete orders.

- Graphical User Interface (GUI)
  
A Swing-based GUI allows users to interact with the system through tabs and forms.

- Command Line Interface (CLI)
  
The application can also be operated through a text-based menu in the terminal.

- Input Validation
  
Email format, numeric IDs, quantity fields, and required fields are all validated to prevent invalid input.

- SQLite Database Integration
  
Data is stored persistently in an SQLite database using JDBC. All core entities have their own tables.

- Data Import and Export (JSON)

Users can export and import customer and order data via JSON files.

- Summary Report Generation

The system can generate a report summarizing the number of customers, number of orders, and order status frequencies.

- Error Handling
  
The system includes appropriate try/catch blocks and user-friendly messages for runtime exceptions and SQL issues.

- Modular Architecture
  
The application is divided into clear modules: managers, models, GUI, and utility classes, promoting reusability and clarity.

##  Technologies Used

- Java 17
- Maven
- SQLite (via JDBC)
- Jackson (for JSON serialization)
- Swing (for GUI)
  
##  Algorithms & Modules Explained

###  Modules

- `CustomerManager`, `OrderManager`: Handle all CRUD operations for respective models
- `UserManager`: Manages login, registration, role checking
- `FileManager`: Manages JSON import/export and file operations
- `DatabaseManager`: Initializes and connects to SQLite database
- `ReportGenerator`: Summarizes customers/orders and status frequency

###  Algorithms & Data Structures

- **HashMap** for counting order statuses
- **ArrayList** for handling lists of customers and orders
- **Regex** for email/ID validation
- **UUID** for generating unique order IDs
- **JDBC** and SQL for data manipulation

---

##  Challenges Faced

- Creating a **dual-interface system (CLI + GUI)** with shared logic
- Maintaining **data consistency** between JSON and database
- Implementing **role-based restrictions** both in CLI and GUI
- Designing GUI with **user-friendly layout** and input validation
- Ensuring **robust error handling** for database and file I/O
##  Project Structure

├── src/ │ 

         ├── models/ # Data classes (Customer, Order, User) │ 
         
         ├── managers/ # Business logic, database access, validation │ 
         
         ├── gui/ # Swing-based GUI interface │ 
         
         └── Main.java # CLI-based entry point 
         
├── data/ # SQLite DB and JSON files 

├── pom.xml # Maven configuration

##  User Roles

- **Admin**: Full access (delete customers/orders, clear data)

![image](https://github.com/user-attachments/assets/414cf359-cbb0-4ddc-a6e1-3310048b2e8b)

![image](https://github.com/user-attachments/assets/ab34eb04-e4eb-4dd9-a208-7f9d8bad8077)

![image](https://github.com/user-attachments/assets/373ca30f-3927-44ec-b673-c213b6725c9f)

![image](https://github.com/user-attachments/assets/e6718b34-7b1f-4b07-98d5-50ca161581f6)

![image](https://github.com/user-attachments/assets/29239b56-ae9c-4787-9859-de9bc5941d3b)


- **User**: Limited access (cannot delete or clear data)

![image](https://github.com/user-attachments/assets/9ab2bd3c-74e2-4f23-bf52-3a074b616468)

![image](https://github.com/user-attachments/assets/c92ec173-20a8-4031-aebf-0f8521893b4a)

![image](https://github.com/user-attachments/assets/6b972432-4be3-4776-b085-9ad318f9b338)

![image](https://github.com/user-attachments/assets/f55899b5-75c5-4425-898d-bd75fa953029)

![image](https://github.com/user-attachments/assets/f0a0f6f8-cb0a-4773-aee3-f6311bd0bd87)



##  Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+


##  How to Run

### CLI:

bash

mvn compile

mvn exec:java -Dexec.mainClass="Main"

GUI:

GUI will launch automatically after successful login.

### Import/Export
Data is saved as customers.json and orders.json

Export to /export directory

Import from /import directory

### Reports
Generates a summary of:

Total number of customers and orders

Frequency of order statuses

### Input Validation
Email format check

![image](https://github.com/user-attachments/assets/bfdb25c8-1ed4-411c-ae49-4077297e9568)

ID length and digit checks

![image](https://github.com/user-attachments/assets/6134d42c-472f-46f5-9306-774a27cba7d6)

Empty field prevention

![image](https://github.com/user-attachments/assets/0abad077-8dd7-4f47-b6e6-c25cee635cc1)

### Authentication

User credentials and roles are stored in the users SQLite table. Passwords are stored in plaintext (not secure for production use).

![image](https://github.com/user-attachments/assets/ea6e7c9d-474f-475d-8af4-bee6abcd3541)

![image](https://github.com/user-attachments/assets/d3f95699-a315-480e-a12d-d45a267008e3)

### Clear All Data

Admin-only option to wipe all customer and order data.

![image](https://github.com/user-attachments/assets/6b6c9f6a-86b7-4842-b196-caef0ea18fe3)
