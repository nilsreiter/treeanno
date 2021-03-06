# Installation

You can run TreeAnno either on a server or locally on a single computer. 
If you want multiple annotators to be able to annotate, you need to
install it on a server. The local version is mainly for demoing and 
testing.

## Tomcat (web-access)

To run TreeAnno via web access, you need to have a tomcat installation 
running. Please check the documentation for your operating system on 
how to do that.

If tomcat is running properly, the only thing you need to configure is 
the database access. In addition, you *can* change some settings via a
properties file.

### Database access

TreeAnno stores all its data in a database, which should be configured 
as a DataSource in the tomcat context. To use a MySQL/MariaDB data 
source, add the following to your `context.xml`, replacing USERNAME, 
PASSWORD, and DATABASE URL with the proper values.

```
<Resource name="treeanno/jdbc" auth="Container" type="javax.sql.DataSource"
               maxActive="100" maxIdle="30" maxWait="10000"
               username="USERNAME" password="PASSWORD" driverClassName="com.mysql.jdbc.Driver"
               url="DATABASE URL"/>
```

Other databases can be used in a similar way, as long as the resource 
name `treeanno/jdbc` is provided. 


### Properties

To supply your own properties file, include the following snippet into 
the `context.xml` of your tomcat installation.

```
<Environment name="treeanno/configurationPath" 
        value="PATH_TO_FILE/treeanno.properties" 
        type="java.lang.String"/>
```

The file should (obviously) exist and be readable by the tomcat user.
The default file, including comments for the properties, can be found
[here](de.unistuttgart.ims.reiter.treeanno.war/src/main/resources/project.properties).

## Standalone (local)



### In Memory database

For testing and evaluation purposes, TreeAnno can be used as a 
standalone app with an in-memory database. To enable this, you either 
[download the appropriate package](https://github.com/nilsreiter/treeanno/releases/latest) or compile it yourself with the `mem`-profile:

```
$ mvn package -Pmem
```

**Be aware that any changes made while using an in-memory database are 
lost if you close the application.**

### File based database

The quickest way to do persistent annotation without doing a proper 
installation is to use an sqlite database. To do this, compile the 
application with the profile `sqlite`:

```
$ mvn package -Psqlite
```

