What is this project? 
the program represents a library, where user can find out information
about books and authors.

Entities:

Book entity:

title - title of a book: String
yearPublished - a year, when book was published: String
publicationHouse - a name of publication house: String
genre - a field, thar represents the book genres. It can have multiple values separated by commas: String
author - the reference to author entity: Author

Author entity:

name - a name of author: String
birthYear - a birth year: Integer
country - a country from which the author come from: String


How to start a project?

You should write the following commands in terminal of a root project folder:

mvn dependency:resolve 
mvn install
mvn package
java -jar target/book-project-1.0-SNAPSHOT-jar-with0dependencies.jar folder-name* attribute-name*

where:
folder-name: the name of a folder, from which a program should read files. Generally,
all test data included in recources folder, so you should just write the name of the folders from 
recources. For instance: all-files-not-correct, five-correct-files.

attribute-name: the name of a attribure, which should be used for stats calculation.
There are 3 possible options for attribute-name parameter:
published_year: Int
genre: String (multiple value separated by commas)
publication_house: String

