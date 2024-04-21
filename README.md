<h2>What is this project?</h2>
the program represents a library, where user can find out information
about books and authors.

<h3>Entities:</h3>

<h4>Book entity:</h4>

title - title of a book: String

yearPublished - a year, when book was published: String

publicationHouse - a name of publication house: String

genre - a field, thar represents the book genres. It can have multiple values separated by commas: String

author - the reference to author entity: Author


<h4>Author entity:</h4>

name - a name of author: String

birthYear - a birth year: Integer

country - a country from which the author come from: String


<h3>How to start a project?</h3>
You should write the following commands in terminal of a root project folder:
<ul>
<li>
mvn dependency:resolve 
</li>
    <li>
mvn install
</li>
    <li>
mvn package
</li>
 <li>
java -jar target/book-project-1.0-SNAPSHOT-jar-with-dependencies.jar <b>folder-name*</b> <b>attribute-name*</b>
   </li>
</ul>

where:
<ul>
  
<li>
folder-name: the name of a folder, from which a program should read files. Generally,
  
all test data included in recources folder, so you should just write the name of the folders from 

recources. For instance, as a folder-name you can write task-example, five-correct-files.
   </li>
   <li>
     attribute-name: the name of a attribure, which should be used for stats calculation.
There are 3 possible options for attribute-name parameter:
     <b>published_year</b>: Int; 
      <b>genre</b>: String (multiple value separated by commas); 
      <b>publication_house</b>: String; 
   </li>
</ul>