--- Work In Progress  ---

This is a project to develop and demonstrate skill with Java, Unit Testing, and MySQL in an application that models a professional code structure. 
The current goal is to have a fully functional CLI that allows the enduser to register/login, and then create CRUD operations on Transactions with 
User and Transaction data stored in a MySQL database.

Current Progress:

Transaction and User models have QueryExecutor and Service classes that have reasonably high unit test coverage. 
The current focus is on completing the Interface components that handle and validate user input to perform the SQL
queries through the UserService and TransactionService classes acting as an intermediary layer between users and the database. 
