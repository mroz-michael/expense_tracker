## September 2025 update (10 months after project creation)
After ~8 months removed from this project, reviewing it showed me several areas desperate for refactoring. Having all the layers use static methods makes the app incredibly inflexible, and necessitated the 'isTest' boolean being passed through each layer to determine the final DB query.


The main points of refactoring were:

- Removed the 'isTest' boolean passed across the app to determine which tables to use. 

  Instead, classes such as ConfigLoader and TableNameProvider were created to determine the appropriate table name to use based on
  config.properties files.

- Made almost all of the static methods non-static. 

  Originally, I had (for some reason) made almost each layer entirely static, this made thing incredibly inflexible (which probably caused me to use that 'isTest' boolean to begin with). By removing the static nature of the classes, the app can properly instantiate itself in Main from the bottom layer on upwards. This allowed me to pretty easily separate my test and 'production' databases. 

- Implemented a couple of Design-Patterns (sort of)

  - The ConfigLoader follows a Singleton design, where it only contains a single instance, and simply returns that instance upon request. 

  - The TableNameProvider *sort of* follows Factory pattern by centralizing the logic for producing table names, its always called in the same context, but will return distinct tableNames depending on the ConfigLoader's state upon instantiation. 


## Further Areas for Improvement (September 2025):

- Improve the separation of concerns. Originally, I set out to have a well-structured app with well-defined layers. It was a decent attempt but reviewing the code shows that a lot fo the layers got a bit mixed up, especially the controller level handling a lot of UI and logic that should be service-level. 

- Improve the Single Responsibility Principle. Too many functions are just doing too many things. For example, there are a lot of validation loops in many places. The code modularity could be improved greatly. 

- Unit test coverage has some omissions, such as testing each different field you can update Users or Transactions by. Currently, it only tests an arbitrary field for an existing, and then non-existing User/Transaction.

## Overview (January 2024)
This is a personal project to develop and demonstrate skill with Java, Unit Testing, and MySQL in an application that 
attempts to model a professional code structure.





### Usage 

Currently, the application is still a work in progress and as such, does not have a very high portability. Right now,
in order to use the app locally, the following steps need to be followed:

1) Download this repository
2) Create a mySQL database with tables Users and Transactions (see DbTestHelper.prepareTestTables for table structures)
   - The tables used for unit tests will be added automatically upon running the tests. 
3) create a config.properties file in the db/resources directory with the following lines (placeholders in brackets):
   - db.url=jdbc:mysql://(host name)/(database name)?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
   - db.username= (username for the database)
   - db.password = (password for the database)
   - table_suffix = (_test if using dummy data, leave blank for data you care about)
   Note this config.properties file should be added to .gitignore. 

4) create a test_config.properties file in the db/resources directory with the same lines as config.properties, but always leave table_suffix blank (since its a test db to begin with, _test would be redundant)

  This test_config.properties file should also be added to .gitignore

     

### Current Progress:

September 2025:

The refactoring is done for now. The app's functionality is the same as before, but the code is much cleaner. There are still many areas of improvement (see 'Further Areas of Improvement' above), but it's in a much better state than it was back in January 2024. 

January 2024: 

The application's core functionality works as intended, although I would like to add more features in the future. Specifically, I would like
to implement multi-levels of access control, allowing "owner" or "admin" type users additional menu commands to offer
read/write operations on users of lower privilege ranks. 

Also, I would like to create an ExpenseReport class, that users can generate by passing a List of Transactions; the 
Expense Report would provide additional insights, such as a breakdown of #transactions per category, average transaction
amount, total transaction amounts over time, etc. 

