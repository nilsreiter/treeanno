# TreeAnno

TreeAnno is an annotation tool develop to annotate trees on top of texts. It 
is designed for annotating longer text segments and allows fast annotation
through the use of keyboard commands.

## User roles
TreeAnno distinguishes several user roles:

- General admin:
  Is allowed to do everything
- Project admin:
  Has admin rights for one or more project(s). This includes uploading documents
  to this project, deleting documents, assigning reviewing tasks and 
  comparing/merging the results. The project admin also has the right to edit 
  the master document, which is used as a basis for the annotators
- Annotator: 
  Can edit (annotate) the documents in the project(s) he/she is assigned to.
  Each annotator gets their own copy upon the first edit.

## Workflow 
TreeAnno was built with the following workflow in mind:

1. Uploading of documents
   Documents are raw plain text and get tokenised and sentence split 
   automatically.
2. Annotation preparation
   The project administrator edits the master document before annotation 
   starts to, e.g., fix the sentence splitting.
3. Annotation
   Each annotator annotates individually, doing segmentation changes and 
   tree changes simultaneously.
4. Annotation comparison and merging
   A project administrator compares the annotation. TreeAnno offers different
   views, focusing on either the segmentation or the tree structure. 
   Ideally, the project admin first merges the segmentation and in a second 
   step the tree structure. 
   There is currently no way of moving tree structure annotations onto 
   files that have been segmented differently, so it might be necessary
   to let annotators go over the merged segmented texts again.
   
   
## Configuration
There are two central pieces that can be configured: Database credentials and app properties.

### Database credentials
TreeAnno makes use of the Tomcat database connection pooling mechanism. The database credentials have to be specified in the context descriptor with the name `jdbc/treeanno`. The following snippet shows a data source declaration for Tomcat 8 and a MySQL database. 

```
<Context>
 ...
 <Resource 
     name="jdbc/treeanno" 
     auth="Container" 
     type="javax.sql.DataSource"
     driverClassName="com.mysql.jdbc.Driver"
     username="USERNAME" password="PASSWORD" 
     url="jdbc:mysql://HOST/DATABASE"/>
 ...
</Context>
```

### App settings
Web app settings are read from a UTF-8 encoded properties file that can be located anywhere on the file system. The path to the configuration file has to be specified as a JNDI resource with the name `treeanno/configurationPath`.

```
<Context>
	...
	<Environment name="treeanno/configurationPath" 
		value="path/to/file.properties" 
		type="java.lang.String"/>
	...
</Context>
```

An example properties file can be found in `/de.unistuttgart.ims.reiter.treeanno.war/src/main/resources/project.properties` in this repository.

## Release Workflow

1. Create a new branch for the release
2. Compile and test
   `mvn test`
3. If dependency fails
   1. Increase version number of dependency
   `mvn versions:set -DnewVersion=0.2-SNAPSHOT`
   2. Compile and test dependency
   `mvn clean test` 
   3. Install dependency
   `mvn install`
4. Update required version of dependency