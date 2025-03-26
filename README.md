# Hardware-Inventory-Sales

## Overview

The **Hardware Inventory & Sales Report System** is a Java-based
application designed for inventory management and sales reporting
for a rel hardware store. This project is developed as
part of a school assignment to create a practical and scalable
software solution.

## Features

- **Inventory Management**
  - Add new products
  - Update product details
  - Delete products
  - View all products
  - Search for products
  - Track stock levels
  - Sends notifications for low stock levels
  - Generate inventory reports
- **Sales Reporting**
  - View all sales
  - Search for sales
  - Generate sales reports
  - Statistics on sales
  - Analyze store performance
  - Track sales trends
- **Multi-Branch Support**
  - Manage multiple branches
  - Track inventory and sales per branch
  - Generate branch-specific reports
- **User-Friendly Interface**
  - Easy to use
  - Intuitive design
  - Responsive
  - Customizable

## Technologies Used
- **Programming Language**: Java
- **Database**: MySQL
- **User Interface**: Java Swing
- **Software Architecture**: Model-View-Controller (MVC)

## Project Structure

```
/HardwareInventorySystem
├── src/
│   ├── com/
│   │   ├── murico                # Root package
│   │   │   ├── config/           # Application configuration helper classes
│   │   │   ├── controller/       # Controllers handling UI events
│   │   │   ├── dal/              # Database access layer
│   │   │   ├── model/            # Data models and entities
│   │   │   ├── resources/        # Application resources
│   │   │   ├── service/          # Business logic and services
│   │   │   ├── utils/            # Utility classes
│   │   │   └── view/             # Java Swing UI components
│   └── test/                     # Unit and integration tests
├── lib/                          # External JAR dependencies
├── config/                       # External configuration files (settings, properties, .env)
├── external_resources/           # Application storage (logs, reports, images, etc.)
├── external_config/              # External configuration files (settings, properties, .env)
├── README.md                     # Project documentation
├── LICENSE                       # Project license
└── .gitignore                    # Git ignore file
```

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/HardwareInventorySystem.git
cd HardwareInventorySystem
```

### 2. Configure the Database
- Install MySQL and create a new database
- Import the database schema to your MySQL database. Refer to [this file](/external_resources/db/README.md) for instructions.
- Update the database connection settings in the [src/com/murico/java/resources/application.properties](/com/murico/resources/application.properties) file.
- Refer to [our database schema](https://dbdocs.io/workemailaaronragudos/murico?table=low_stock_alerts&schema=public&view=table_structure&fbclid=IwY2xjawJPnkJleHRuA2FlbQIxMAABHXk9UHiYvKQmC8NfQNbt8FaTdjT6Q_h-LgpNDXAgp_2SBhyLJrQNFO2YdA_aem_xaf-g4VewS3l02DSjrTvIw) for more details of the schema.

### 3. Run the Application

**To be made**

## UI Design

The graphical user interface (GUI) of the application is designed using Java Swing.
The UI components are organized into different panels and frames to provide a user-friendly experience.
The design is responsive and customizable to suit the needs of the user.

You can visit the [UI Design](https://www.figma.com/design/yxYlvE85nY8IwhmsykmZXe/Hardware-UI?node-id=0-1&t=8Fw66rAWLIh6rxAr-1) folder to view the mockups and wireframes of the application.

## Code Quality and Refactoring

For information of the techniques used, refer to [Refactoring Guru](https://refactoring.guru/)

- The codebase is well-structured and follows the Model-View-Controller (MVC) architecture for scalability and maintainability.
- The code is modular and reusable, making it easy to add new features and functionalities.
- The code follows the Java naming conventions and best practices for readability and consistency.
- The code is well-documented with comments and Javadoc for better understanding and maintainability.
- The code is tested with unit and integration tests to ensure reliability and robustness.

## Contributors


### Project Maintainers
- [Ragudos, A.](#)
- [Mapua, H.](#)
- [Dela Cruz, P.](#)
- [Remo, J.](#)
- [Raneses, K.](#)

If you would like to contribute to this project, please contact the project maintainers.

## License

This project is licensed under the CC BY-NC 4.0 License - see the [LICENSE](LICENSE) file for details.
