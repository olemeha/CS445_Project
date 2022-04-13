# CS445 Buy Nothing Project

Repository for CS445 Project: 
https://github.com/olemeha/CS445_Project.git

Configuration Instructions:

1. Launch ubuntu virtual machine. 

2. Open the browser and download the project zip file from blackboard. You can also clone the repository of the project shown above. 
	
	To clone the repository:
		
		1. Open terminal
		2. Enter <git clone https://github.com/olemeha/CS445_Project.git>
		3. Access the directory of the git repository by doing <ls>, to check what the file name is, and then type <cd CS445_Project>.
		4. Access the maven Buy Nothing Project by entering <cd BuyNothingProject>
	
	To get file through browser download:
		
		1. Open the terminal.
		2. Enter the downloads directory by typing: <cd ~/Downloads>. Type <ls> to check the files that have been installed and find the one that is called "Oleksandr-Lemeha-PR-1.zip".
		3. Unzip the file, by entering: <unzip Oleksandr-Lemeha-PR-1.zip>.
		4. When the file has unzipped, enter the directory of the file by typing: <cd Oleksandr-Lemeha-PR-1>.
		5. Then, enter type <ls> and see that there is a file called "BuyNothingProject".  
		6. Type: <cd BuyNothing> to enter the maven project directory.

Build and Deploy instructions:

For Maven Project Testing:
	
		1. First, install the library in the Maven Dependency Repository by typing:<mvn clean install>

		2. Run <mvn compile> which will run Maven and tell it to execute the compile goal. When it's finished, you should find the compiled .class files in the target/classes directory.
		
		3. Then, run <mvn test> which will run the unit tests created in the Maven project. 
		
		4. To get a full jacocoReport, type: "mvn clean test". 
		
		5. You can access it by going through these files: Files>Downloads>Oleksandr-Lemeha-PR-1>BuyNothingProject>target>site>jacoco>index.html or,Files>Home>Oleksandr-Lemeha-PR-1>BuyNothingProject>target>site>jacoco>index.html
		
		6. To then continue testing in Postman, enter <mvn clean package> which will compile the Java code, run any tests, and finish by packaging the code in a JAR file within the target repository.
		
		7. The name of the JAR file should be "target/BuyNothingProject-1.0-SNAPSHOT.jar".
		
		8. Execute the JAR file by running: <java -jar target/BuyNothingProject-1.0-SNAPSHOT.jar>

		9. Now the project is deployed to test the automated Postman Tests.
  
		
For Postman Testing:

		1. Open Postman in Ubuntu
		
		2. Login with your account, or create a new account.
		
		3. Create a new workspace and name it BuyNothingProjectTests and switch it to personal.
		
		4. Once workspace is created, create an environment by clicking on "Environments" tab and the Plus Sign. 
		5. Name environment to your preference and set the variable to "base_url", type to "default", initial and current variables to "http://localhost:8080/bn/api".
		
		6. In the workspace, import the tests by going to "File>Import", select "Link" and enter https://www.getpostman.com/collections/aa2b389418c634499d30.
		
		7. To run the tests automatically, click on the three dots to the right of the collection and choose "Run Collection".
		8. Make sure that the environment is set to what you called it in step 5. 
		9. Click "Run Buy Nothing Tests". 

Copyright and Licensing Instructions:

		1. Access the Maven Copyright and Licensing by typing : <cat mvnw> in the Maven Project Repository
		2. To get access to the third party license, enter: <mvn license:third-party-report>.
		3. Then, go to the following files: "Files>CS445_Project>BuyNothingProject>target>site>third-party-report". 

Known bugs:
		
		1. There are known bugs with the GET requests. Most of the automated Postman tests that fail are the GET requests. 
Credits and acknowledgements:

1. Credits:
		1. Spring Boot 
		2. Maven Apache
		3. Postman
		4. JUnit 
		
2. Acknowledgements:

		1. https://medium.com/@TimvanBaarsen/maven-cheat-sheet-45942d8c0b86
	
		2. https://maven.apache.org/guides/MavenQuickReferenceCard.pdf
		
		3. https://spring.io/projects/spring-boot
		
		4. https://spring.io/guides/gs/spring-boot/
		
		5. https://youtu.be/9SGDpanrc8U
		
		6. http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/automated-test.html
		



