# Hardware-Inventory-Sales

## Overview

The **Hardware Inventory & Sales Report System** is a Java-based
application designed for inventory management and sales reporting
for a rel hardware store. This project is developed as
part of a school assignment to create a practical and scalable
software solution.

This project is under construction and is subject to a lot of refactors and changes.
The setup instructions for this project might change as well.

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
- **Build tool**: Maven, For a universal build tool that IDEs can detect and make use of.

## Project Structure

currently being edited

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/HardwareInventorySystem.git
cd HardwareInventorySystem
```

### 2. Configure the Database
- Install MySQL and create a new database
- Create a database in your MySQL server.
- Apply the server details in [the database module resources.](database/src/main/resources), preferable the `.properties` file;
- Refer to [our database schema](https://dbdocs.io/workemailaaronragudos/murico?table=low_stock_alerts&schema=public&view=table_structure&fbclid=IwY2xjawJPnkJleHRuA2FlbQIxMAABHXk9UHiYvKQmC8NfQNbt8FaTdjT6Q_h-LgpNDXAgp_2SBhyLJrQNFO2YdA_aem_xaf-g4VewS3l02DSjrTvIw) for more details of the schema.

If you run into the error `communications link failure`, please make sure that the application is connecting to the database you want it to connect to with proper `PORT`, `PASSWORD`, `DATABASE NAME` and `USERNAME`.

### 3. Run the Application

**To be made**

## UI Design

The graphical user interface (GUI) of the application is designed using Java Swing.

You can visit the [UI Design](https://www.figma.com/design/yxYlvE85nY8IwhmsykmZXe/Hardware-UI?node-id=0-1&t=8Fw66rAWLIh6rxAr-1) folder to view the mockups and wireframes of the application.

## Code Quality and Refactoring

Please refer to [this document](CODING_STANDARD.md) for more information on code
quality and refactoring.

## Contributors

### Project Maintainers
- [Ragudos, Aaron](https://github.com/Ragudos)
- [Mapua, Hanz](https://github.com/Hanzm10)
- [Dela Cruz, Peter](https://github.com/alyastanga)
- [Remo, Jerick]()
- [Raneses, Kurt]()

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
