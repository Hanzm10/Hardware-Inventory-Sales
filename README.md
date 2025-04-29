# Hardware Inventory Sales System

A GUI inventory and sales application for a hardware store. Built by student of Technological Institute of the Philippines - Quezon City (T.I.P. - Q.C.) for their final project in ITE012.

## Project Dependencies
 - Build tool: [Apache Maven](https://maven.apache.org/)
 - Primary database: [MySQL](https://www.mysql.com/)
 - Programming Language: [JavaSE-22](https://www.oracle.com/java/technologies/javase/jdk22-archive-downloads.html)
 - Graphical User Interface: [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/start/index.html)
 - Database Driver (For connection with the database): [MySQL Connector Java](https://github.com/mysql/mysql-connector-j)
 - Look and Feel (For the UI Design): [FlatLaf](https://github.com/JFormDesigner/FlatLaf)
 - Layout Helper: [MigLayout](http://www.miglayout.com/)
 - Annotations: [JetBrains Annotations](https://github.com/JetBrains/java-annotations)

## Database Design

As much as possible, lessons from the video titled, ["How to Fake a Database Design - Curtis Poe (‎Ovid‎)"](https://youtu.be/y1tcbhWLiUM?si=TuftIGFaH6Z4yeMF) are followed.
A few examples are:

 - `id` columns should be prefixed with its singular table name. For example, a `users` table should have `users.user_id` for its primary key id column.
 - using snake_case for more readability
 - an underscore prefix for auto-generated columns.

The database design is fairly straightforward and small. Refer to this [diagram](https://dbdocs.io/workemailaaronragudos/murico) to see how it's structured.

## Code Design Principles

- For design patterns: [Refactoring Guru](https://refactoring.guru/)
- Data Access Objects for database interactions: [Core J2EE Patterns - Data Access Objects](https://www.oracle.com/java/technologies/dataaccessobject.html)
- For Regular Expressions or Pattern matching: [Pattern Matching using Regular Expressions](https://knowledge.square-9.com/gc230/pattern-matching-using-regular-expressions)

## UI/UX Design

Our UI/UX designers worked hard to implement our designs. Refer to this [Figma file](https://www.figma.com/design/yxYlvE85nY8IwhmsykmZXe/Hardware-UI?node-id=0-1&p=f&t=Idpi7TTkhP3COe2l-0) and check it out.

## Project Setup

### Eclipse

 - [Project Setup Guide](https://youtu.be/TVhgt9oEcg0)
 - [Project Workflow Guide](https://youtu.be/6z39OYzFuaQ)


### IntelliJ

### NetBeans
