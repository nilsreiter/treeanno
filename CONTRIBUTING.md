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