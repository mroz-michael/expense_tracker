## Overview
This is a personal project to develop and demonstrate skill with Java, Unit Testing, and MySQL in an application that 
attempts to model a professional code structure.


### Usage 

Currently, the application is still a work in progress and as such, does not have a very high portability. Right now,
in order to use the app locally, the following steps need to be followed:

1) Download this repository
2) Create a mySQL database with tables Users and Transactions (see DbTestHelper.prepareTestTables for table structures)
   - The tables used for unit tests will be added automatically upon running the tests. 
3) create a config.properties file in the resources directory with the following lines (placeholders in brackets):
   - db.url=jdbc:mysql://(host name)/(database name)?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
   - db.username= (username for the database)
   - db.password = (password for the database)
   Note this config.properties file should be added to .gitignore. 

     

### Current Progress:

The application's core functionality works as intended, although I would like to add more features in the future. Specifically, I would like
to implement multi-levels of access control, allowing "owner" or "admin" type users additional menu commands to offer
read/write operations on users of lower privilege ranks. 

Also, I would like to create an ExpenseReport class, that users can generate by passing a List of Transactions; the 
Expense Report would provide additional insights, such as a breakdown of #transactions per category, average transaction
amount, total transaction amounts over time, etc. 

