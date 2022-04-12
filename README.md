# CS445 Buy Nothing Project

Repository for CS445 Project: 
https://github.com/olemeha/CS445_Project.git

Configuration Instructions:

1. Launch ubuntu virtual machine. 

2. Open the browser and download the project zip file from blackboard. You can also clone the repository of the project shown above. 
	
	To clone the repository:
		
		1. Open terminal
		2. Enter <git clone https://github.com/olemeha/CS445_Project.git>
		3. Access the directory of the git repository by doing <ls>, to check what the file name is, and 			   	then type <cd CS445_Project>.
		4. Access the maven Buy Nothing Project by entering <cd BuyNothingProject>
	
	To get file through browser download:
		
		1. Open the terminal.
		2. Enter the downloads directory by typing: <cd ~/Downloads>. Type <ls> to check the files that 		           	have been installed and find the one that is called "Oleksandr-Lemeha-PR-1.zip".
		3. Unzip the file, by entering: <unzip Oleksandr-Lemeha-PR-1.zip>.
		4. When the file has unzipped, enter the directory of the file by typing: <cd Oleksandr-Lemeha-  			   	PR-1>.
		5. Then, enter type <ls> and see that there is a file called "BuyNothingProject".  
		6. Type: <cd BuyNothing> to enter the maven project directory.

Build and Deploy instructions:

1. Install the Maven project by typing: <mvn clean install>


2. To get a full jacocoReport, type: "mvn site", and you can access it by going through these files: Files>Downloads>Oleksandr-Lemeha-PR-1>BuyNothingProject>target>site>jacoco>index.html

Copyright and Licensing Instructions:

Known bugs:

Credits and acknowledgements:



