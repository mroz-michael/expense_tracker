## Major Refactoring In Progress
After ~8 months removed from this project, reviewing it showed me several areas desperate for refactoring. Having all the layers use static methods makes the app incredibly inflexible, and necessitated the 'isTest' boolean being passed through each layer to determine the final DB query.

I'm currently working to polish this project up a bit by refactoring out all those static methods. in the current commit the app isn't functional, but I hope to have the refactoring done relatively soon. 

The most recent commit where the app works is 35cd7c007eb410f06763d22e7e06c3308b08831c 

## Overview
This is a personal project to develop and demonstrate skill with Java, Unit Testing, and MySQL in an application that 
attempts to model a professional code structure.





### Usage 

*note from September 2025: The app is currently being refactored and this current commit will not work. The latest working commit is 
35cd7c007eb410f06763d22e7e06c3308b08831c

Currently, the application is still a work in progress and as such, does not have a very high portability. Right now,
in order to use the app locally, the following steps need to be followed:

1) Download this repository
2) Create a mySQL database with tables Users and Transactions (see DbTestHelper.prepareTestTables for table structures)
   - The tables used for unit tests will be added automatically upon running the tests. 
3) create a config.properties file in the resources directory with the following lines (placeholders in brackets):
   - db.url=jdbc:mysql://(host name)/(database name)?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
   - db.username= (username for the database)
   - db.password = (password for the database)
   - table_suffix = ("_test" if using test data, "" for production)
   Note this config.properties file should be added to .gitignore. 

     

### Current Progress:

The application's core functionality works as intended, although I would like to add more features in the future. Specifically, I would like
to implement multi-levels of access control, allowing "owner" or "admin" type users additional menu commands to offer
read/write operations on users of lower privilege ranks. 

Also, I would like to create an ExpenseReport class, that users can generate by passing a List of Transactions; the 
Expense Report would provide additional insights, such as a breakdown of #transactions per category, average transaction
amount, total transaction amounts over time, etc. 

