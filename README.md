<h2>What is this project about?</h2>
the program represents a library, where user can find out information
about books and authors.

<h2>Entities:</h2>

<h3>Book entity:</h3>
<dl>
    
<dt>title</dt> 
<dd>title of a book: <i>String</i> </dd>

<dt>yearPublished</dt>
<dd>a year, when book was published: <i>String</i> </dd>

<dt>publicationHouse</dt>
<dd>a name of publication house. In the example there was an author field, but I replaced it, because author is a separate entity: <i>String</i></dd>

<dt>genre</dt> 
<dd>a field, thar represents the book genres. It can have multiple values separated by commas: <i>String</i></dd>

<dt>author</dt> 
<dd>the reference to author entity: <i>Author</i></dd>
</dl>


<h3>Author entity:</h3>

<dl>
<dt>name</dt> 
<dd>a name of author: <i>String</i></dd>

<dt>birthYear</dt> 
<dd>a birth year: <i>Integer</i></dd>

<dt>country</dt> 
<dd>a country from which the author come from: <i>String</i></dd>
</dl>


<h2>How to start a project?</h2>
You should write the following commands in the terminal of a root project folder:
<ul>
<li>
mvn dependency:resolve 
</li>
    <li>
mvn clean compile
</li>
     <li>
mvn test
</li>
    <li>
mvn clean package
</li>
 <li>
java -jar target/book-project-1.0-SNAPSHOT-jar-with-dependencies.jar <b>folder-name*</b> <b>attribute-name*</b>
   </li>
</ul>

where:
<dl>
  
<dt>
folder-name
</dt>
<dd>
    the name of a folder, from which the program should read json files. Generally,
all test data included in resources/json directory inside the project, 
so you should just write the name of the folders from 
there. For instance, as a folder-name argument you can write <b>task-example</b>, <b>five-correct-files</b>.
</dd>   
<dt>
    attribute-name
 </dt>
 <dd>the name of an attribute, which should be used for stats calculation.
There are 3 possible options for attribute-name parameter:
     <ul>
         <li> <b>published_year</b>: Int  </li>
         <li> <b>genre</b>: String (multiple values separated by commas) </li>
         <li>     <b>publication_house</b>: String </li>
    </ul>
     <dd>
</dl>

<h2>Examples of usage</h2>


```
java -jar target/book-project-1.0-SNAPSHOT-jar-with-dependencies.jar task-example genre
```

After this command the program will parse json files from "resources/json/task-example" folder and calculate
the occurrence statistics by genre attribute. As a result, it will create, if it is not created before, 
a new folder in "resources/xml-results" directory with the same name as the one user wrote("task-example" in this case),
and inside this folder will be generated a new file "statistics_by_genre.xml". 

"resources/json/task-example" contains only one file. Its content:
```json
[
  {
    "title": "1984",
    "publication_house": "Folio",
    "year_published": 1949,
    "genre": "Dystopian, Fiction"
  },
  {
    "title": "Pride and Prejudice",
    "publication_house": "ABABAGALAMAGA",
    "year_published": 1813,
    "genre": "Romance, Satire"
  },
  {
    "title": "Romeo and Juliet",
    "publication_house": "Folio",
    "year_published": 1597,
    "genre": "Romance, Tragedy"
  }
]

```

Expected result from "resources/xml-results/task-example/statistics_by_genre.xml":
```xml
<statistics>
  <item>
    <value>Romance</value>
    <count>2</count>
  </item>
  <item>
    <value>Dystopian</value>
    <count>1</count>
  </item>
  <item>
    <value>Satire</value>
    <count>1</count>
  </item>
  <item>
    <value>Tragedy</value>
    <count>1</count>
  </item>
  <item>
    <value>Fiction</value>
    <count>1</count>
  </item>
</statistics>
```

<h3>Some other examples</h3>

```
java -jar target/book-project-1.0-SNAPSHOT-jar-with-dependencies.jar twenty-files year_published
```
Parse 20 json-files by 'year_published' attribute.
<hr/>

```
java -jar target/book-project-1.0-SNAPSHOT-jar-with-dependencies.jar two-correct-two-incorrect publication_house
```
The "two-correct-two-incorrect" directory contains four files, two of them have incorrect format. In case the file format is inappropriate,
it will be ignored and the user will get the following logs in console:
```
13:14:01.561 [pool-2-thread-1] ERROR com.profitsoft.Main - Unexpected character ('{' (code 123)): was expecting comma to separate Object entries
 at [Source: (File); line: 8, column: 4]
13:14:01.564 [pool-2-thread-2] ERROR com.profitsoft.Main - Unrecognized token 'Hamlet': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')
 at [Source: (File); line: 3, column: 21]
13:14:01.564 [main] INFO  com.profitsoft.Main - Amount of threads for parsing: 2
13:14:01.566 [main] INFO  com.profitsoft.Main - Time of parsing execution: 31 ms
13:14:01.567 [main] INFO  com.profitsoft.Main - Analyzed 2 json files with correct form.
13:14:01.567 [main] INFO  com.profitsoft.Main - General amount of files in directory: 4
13:14:01.684 [main] INFO  com.profitsoft.Main - Results are successfully saved

```
Also, the program will inform the user about parsing execution time.
<hr/>

```
java -jar target/book-project-1.0-SNAPSHOT-jar-with-dependencies.jar ten-json-files-and-one-txt genre           
```
"ten-json-files-and-one-txt" contains 10 json-files(2 of them invalid) and one txt-file. The txt file will be ignored and will not interfere with the program flow.
<hr/>

```
java -jar target/book-project-1.0-SNAPSHOT-jar-with-dependencies.jar five-correct-files year_published
```

"five-correct-files" folders' files will be read, then "statistics_by_year_published.xml" file will be generated inside the project "resources/xml-results/five-correct-files" directory.

<h2>Multithreading</h2>

<p>Multithreading is used for the parsing of json-files from a chosen directory. For these purposes, I decided to take advantage of ExecutorService. The initial amount of threads is 4.
The comparison by threads amount (to be completely honest, the real difference between each thread config was about 3, but I run the program 10 times for every option and chose more bright examples):</p>

<p>Single thread</p>
<img src="./src/main/resources/img/1 thread.png" alt="1 threads"/>
<hr/>

<p>2 threads</p>
<img src="./src/main/resources/img/2 threads.png" alt="2 threads"/>
<hr/>

<p>4 threads</p>
<img src="./src/main/resources/img/4 threads.png" alt="4 threads"/>
<hr/>

<p>8 threads</p>
<img src="./src/main/resources/img/8 threads.png" alt="8 threads"/>
<hr/>

<p>
We can see the significant difference in the performance while using various
amount of threads.
</p>

<h2>Details</h2>

<p>
Each class and each important method have javadoc. So more details 
and info can be found there. 
</p>
