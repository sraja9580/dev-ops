## AWS EC2 instance & Linux Basics

https://aws.amazon.com/getting-started/learning-path-full-stack-developer/?e=gs2020&p=gsrc

**EC2**
	EC2 - Elastic Cloud Computing
	Virtual Machine(Ubuntu,Windows,linux) - amazon call them instance
	
	AMI - Amazon Machine Image (VM image(os,sw(java,docker,sqlserver etc))
		amazon provides AMI

	Login to Amazon:
	1. 
	Login in mobaxtream - ssh 
	advanced option -> select private key location
	2. Login as - ec2-user
	3. after login sudo -i (to change to root user it is needed for install software etc)
	
	EC2 Tips:
		Use top command after connected to ec2 instance, this will keep the session alive even if you are working on something else
		30 - Year make sure you are exceeding this, terminate the instance if you are not using
		750hr/Month - is free in a month, so stop  the instance when not required.
**Linux Commands**
	pwd 						-> current working directory.
	mkdir mydir 				-> create folder
	cd mydir					-> changing directory
	cat>myfile.txt  			-> it will create a file, write the content and cnt+c for save and close
	ls 							-> lists files and directories under current directories
	ls -l           			-> list files and dirs with details(permission, owner,date etc)
	ls -a           			-> (includes . and hidden files)
	ls -r           			-> contents of subdir also
	cp myfile.txt myfile1.txt 	-> Copy file
	mv myfile1.txt subdir/		-> move file
	rm myfile1.txt				-> remove file
	
	vi editor shortcuts
		https://alvinalexander.com/linux/vi-vim-editor-end-of-line/#:~:text=Short%20answer%3A%20When%20in%20vi,end%20of%20the%20current%20line.
		
**Important directories**
	ls /bin   -> all binaries(call command line utilities)
	ls /root  ->
	ls /boot  -> all the linux startup files
	ls /etc  ->  config files
	ls /home  -> home directory of use
	ls /proc  -> current running kornal related process	
	ls /lib  ->  libraries 
	ls /dev  -> hardware/sw devices
	ls /mnt  -> mounting removable media(cd,pendrive etc)	 
	ls /usr  -> 
	ls /var  -> log file and other varible data
	ls /sbin -> admin commands
	
**VI Editor**

Software installing
	yum repolist 				-> list the repositories(where sw are available)
	yum list 	 				-> list the already installed softwares
	yum grouplist 				-> list the software installed as group
		Available Groups:
			AWS Tools
			Development Tools
			buildsys-build
	yum install package-name 	-> installing software

	yum remove package-name		-> uninstall
	yum group group-name		-> group install
	tail -100 /var/log/yum.log	-> all the yum logs
	yum history 				-> command execution history
	yum search package-name		-> searching package
	
Install git	
	yum install git
	test by cloning repo

Maven install
	install java
	1. yum install java-11-amazon-corretto.x86_64
	2. yum install maven
	3. java11 as home setup
		/usr/lib/jvm/java-11-amazon-corretto.x86_64
		
	Test - create standalone project
		   mvn archetype:generate -DgroupId="com.first" -DartifactId="hellomaven" -DarchetypeArtifactId="maven-archetype-quickstart" -DinteractiveMode="false"
		
Change JDK and JRE
	sudo alternatives --config java
	sudo alternatives --config javac
	
Deploy Spring boot App Manually in EC2
--------------------------------------
	** YOU SHOULD HAVE JAVA IN ec2
	1. Store fat jar in S3 bucket and provide public access
	https://sraja9580.s3.us-east-2.amazonaws.com/helloworld-0.0.1-SNAPSHOT.jar
	2. login to EC2 instance and go to directory where you want to keep jar files
		/home/ec2-user/workspace
	3.Download JAR from S3 bucket
		wget https://sraja9580.s3.us-east-2.amazonaws.com/helloworld-0.0.1-SNAPSHOT.jar
	4. run the jar and enable port for tcp access(i used 8070)
		java -jar helloworld-0.0.1-SNAPSHOT.jar
		
	
Jenkins installation
--------------------

	https://cloudaffaire.com/how-to-install-jenkins-in-aws-ec2-instance/  -- used yum for practice
	## Add Jenkins repo to your yum repository
	sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat/jenkins.repo
 
	## Import a key file from Jenkins-CI to enable installation from the package
	sudo rpm --import https://pkg.jenkins.io/redhat/jenkins.io.key
 
	## Install Jenkins
	sudo yum install jenkins -y
	
	## Enable port 8080 before starting jenkins
	service jenkins start

	cat /var/lib/jenkins/secrets/initialAdminPassword

	service jenkins start

	Uninstall Jenkins
	------------------
	yum remove jenkins

	rm -rf /var/lib/jenkins

	Global Tool Config update
	---------------------------
	## Java Path
	/lib/jvm/java-1.8.0-openjdk-1.8.0.242.b08-0.amzn2.0.1.x86_64

	## Maven Path
	/usr/share/maven

	## Git Path
	/usr/bin/git

	
	## Assign Root User and Permissions:
	------------------------------------
	Assign root as jenkins user and provide permission to jenkins folder for root.
		JENKINS_USER="jenkins" to JENKINS_USER="root"
	vi /etc/sysconfig/jenkins

	chown -R root:root /var/lib/jenkins
	chown -R root:root /var/cache/jenkins
	chown -R root:root /var/log/jenkins

	service jenkins restart
	
	Jenkins Master & Agent Setup
	----------------------------
		You can use any node with java as agent node (we are going to use below tomcat node as agent for jenkisn)
		also git and maven need if it is used for build and deployment
		1. Generate ssh keys in master mode(private and public)
			[ec2-user@ip-172-31-26-20 ~]$ ssh-keygen				
				Enter passphrase (empty for no passphrase):
				**IF YOU ENTER PHRASE, MAKE A NOT OF IT, YOU WILL NEED IT FOR CONNECTION, IF LEFT IT EMPTY THEN DONT NEED TO REMEMBER
		2. Copy private and and public keys (file path will be shown during creation /home/ec2-user/.ssh/id_rsa)
			private key is used in master jenkins tool
			public key is used in agent
			
		3. Set public key in agent
			/home/ec2-user/.ssh/authorized_keys  --> keep existing keeys and new key in newline
		4. Create Agent in master
			Jenkins->Manage Jenkins->Manage Nodes and Clouds->New Nodes
			Fill the following
			Remote Root Dir -> /home/ec2-user				
			Launch method -> SSH
				fill ssh host and credential(private key) (use ip address for host)
				user-name -> ec2-user 
				pwd->not required
			Host Key Verification Strategy -> Manually trusted key verification
		
		5. test -> Execute 2 jobs -> one job will run in agent
			
		
	
Tomcat Installation and Integration:
------------------------------------
	Create new EC2 instance for tomcat
	install java 11 as we developing app in 11
	
	yum install tomcat

	yum install tomcat-webapps tomcat-admin-webapps 
	
	service tomcat start -> and enable port 8080
	i havce changed port to 8090 /usr/share/tomcat/conf/server.xml
	
	vi /usr/share/tomcat/conf/tomcat-users.xml

	Uncomment Admin roles and user and Add:

	<user username="deployer" password="deployer" roles="manager-script" />
	
	service tomcat restart
 
	Deploy war/ear to a container:

	**/java-web-project.war


First JOB
---------
		shell scrip helloword
		1. create file helloworld.share
				echo hello jenkins user
		2. provide execute permission for jenkins user(root)
			chmod 777 helloworld.sh
		3.Jenkins
			NewItem -> freestyle ->hello-world-scrip-job(name)
			Build->Execute shell ->/root/helloworld.sh
		4. Test job using Build Now

Pipeline
--------
	repeatable, reliable automated CI/CD
	
	first-pipeline
	--------------
		pipeline {
			agent any
			stages {
				stage('Hello') {
					steps {
						echo 'Hello World'
					}
				}
			}
		}
		
	Multi Stage pipeline with reading env variables
	-----------------------------------------------
	pipeline{
		agent any
		stages{
			stage("Check-out"){
				steps{
					echo "downloading code from github"
					echo "$BUILD_NUMBER running in $JENKINS_URL"
				}
			}
			stage("Build"){
				steps{
					echo "Compiling and Packaging"
				}
			}        
		}
`	}

	S3 Storage
	----------
		it is like google drive
		100 buckets we can create
		20000 - get request/month
		1000 - push/moth
		5 GB
		
		

	Stress plug-in(simulate stress in machine)
	------------------------------------------
		it can be used to test load balancing, by increasing the CPU stress in one machine, 
		 we can verify whether traffic switched to next machine
		
		yum install stress -y
		
	AMI -Amazon Machine Image
	-------------------------
		select an image -> action -> image.
		next time you can use your own image(u might have setup java jenkins etc)
		
	AWS Elastic Load Balancer
	-------------------------
	
	AWS Auto scaling Groups
	-----------------------
		stress --cpu 1

	Running Spring boot app in linux start
	--------------------------------------
	cd /usr/lib/systemd/system -> may vary
	create hello-world.service file
		[Unit]
		Description=SPRING BOOT HELLOWORLD APP
		After=syslog.target

		[Service]
		User=root
		ExecStart=/usr/bin/java -jar /root/helloworld-0.0.1-SNAPSHOT.jar
		SuccessExitStatus=143

		[Install]
		WantedBy=multi-user.target
		
	To flag the application to start automatically on system boot, use the following command:
		systemctl enable hello-world.service
	https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html
	https://stackoverflow.com/questions/12102270/run-java-jar-file-on-a-server-as-background-process
	https://stackoverflow.com/questions/42203720/systemctl-strange-error-invalid-arguments
	
	
	Cloud Watch Alarm and Simple Notification Service
	-------------------------------------------------
	SNS -> Create Topic and subscribe
	Select instance -> Action-> Cloud Watch Monitoring -> Create or Edit alarm
		select the previous selected topic in the alarm
		
	RDS - Relational Database Service
	---------------------------------
		Search RDS and create database
		
	AWS Elastic Beanstalk -like PCF
	-------------------------------
		create and upload jar to create application
		** 5000 is default port in EBS configure the same in spring boot app
		
		application.properties RDS and EBS
		#AWS RDS
		spring.datasource.url=jdbc:mysql://product-coupon-service.cd4cboii1qhn.us-east-2.rds.amazonaws.com:3306/raja_practice
		spring.datasource.username=admin
		spring.datasource.password=raja9580
		#Elastic Bean Stack
		server.port = 5000
		
  ## Docker
  	Launch an ec2 instance.and install docker in it.
		yum install docker
* Pull Docker Image <br>
```	
	docker pull imagaeName

```
* **Run Docker container** <br>
```
	docker run imageName
	docker run -i -d -t -p 70:80 nginx
	i - intractive mode
	d - detached mode
	t - opens teriminal
	p - 70:80 (expose container port 80 in host machine 70, nginx default port is 80)
```
* **List Docker running containers** <br>
```
	docker ps
	docker ps -a (included non running container)
```
* **Delete Docker Image and Container** <br>
```
	docker rm containerId -> container
	docker rm -f containerId ->deleting running container	
	docker rmi imageName -> image
	
```
* **Stop Start Pause Unpase**
```
	docker start 6ad8b8306
	docker stop 6ad8b83060c3
	docker pause 6ad8b83060c3
	docker unpause 6ad8b83060c3	
```
* **Get inside container**
```
	docker exec -it a8b98b29759 bash
	once you are inside container you can cd into folders, you can instal software etc
	docker exec -it -u 0 a8b98b297593 bash -> as ROOT user
	docker attache a8b98b297593 -> attaches currant command line to a running container	 
```
* **Docker image from running container using COMMIT** <br>
	_this is not preferred way.Docker File is the best practice to create image_
```
	 docker commit a8b98b297593 jenkis-git-image		 
```
* **Docker Image history**
```	
	docker history cd14cecfdb3a
	tells the each layer history and when it is added to the final image
```
* ***Docker Layers** <br>
	By using **docker info** command you get the docker director<br>
	Docker Root Dir: /var/lib/docker -> this is where images stored, containers run, and image repo infos<br>
```	
	docker history cd14cecfdb3a
	tells the each layer history and when it is added to the final image
```
![docker_history](/images/docker/docker_history.PNG) <br>
![docker-containers](/images/docker/docker-containers.PNG) <br>
![docker-image-folder](/images/docker/docker-image-folder.PNG) <br>

* **Docker Container Creation with Environment Variable**
```	
	docker run -dit -p 6036:3306 --env="MYSQL_ROOT_PASSWORD=test" --env="MYSQL_DATABASE=rajapractice" mysql
	env is used to set environment variables
	here we are setting root password and requesting to create default databse rajapractice
```
![docker_exec_db_container_sql_commands](/images/docker/docker_exec_db_container_sql_commands.PNG) <br>

* **Docker VOLUMES**<br>
	Volumes helps to store data in host instead of container, so data can be retained even if the container crashed.<br>
	We need volumed if we use Jenkins,MySQL of if we store any files in our app space.<br>
```	
	CREATE
	docker volume create 9580_volume
	LIST
	docker volume ls
	MOUNT
	docker run -dit -p 8060:8080 --mount source=9580_volume,destination=/var jenkin
```
	now if you go and check volume folder inside host docker directory you can see all jenkins directory<br>
![docker_volumer_container_data_in_host](/images/docker/docker_volumer_container_data_in_host.PNG)

* **Docker Networks**<br>
```	
	docker network ls	
	
	docker network inspect bridge -> Get the details of existing network
		*IP range in the network - subnet
		*Container attached to the network etc
		
	docker network create 9580
	docker network create 9580-with-IP --subnet 172.19.0.0/16
	
	#RUN CONATAINEW WITH SPECIFIC NETWORK
	docker run --net 9580 -d -p 8080:8080 jenkins
	docker run --net 9580-with-IP --ip 172.19.0.5 -h jenkins.raja.com -d -p 8080:8080 jenkins
	
	#CHANGE CONTAINER NETWORK
	docker network disconnect 9580-with-IP 26a4f3befa26  --> Disconnect from current network
	docker network connect 9580-with-IP 26a4f3befa26     --> Connect to New Network
```
![docker-network-list](/images/docker/docker-network-list.PNG)
![docker-network-inspect](/images/docker/docker-network-inspect.PNG)

* **Docker File- ImageBuild and Push to docker hub**<br>
```
	Docker File:
	------------
		FROM openjdk:8-jdk-alpine	
		ADD target/hello-world-rest-api.jar hello-world-rest-api.jar
		ENTRYPOINT ["sh", "-c", "java -jar /hello-world-rest-api.jar"]
		
	Build Image
	-----------
		docker build -t repo/imagename:taganame . (use . in case if you are running the build from same dir as docker file dir, otherwiser use directory instead .)
	  	docker build -t raja9580/hello-world-rest:0.0.1-SNAPSHOT .
		
	PUSHING DOCKER FILE TO HUB
	--------------------------
	docker login (provide user id and pwd in command prompt)
	docker push raja9580/hello-world-rest:0.0.1-SNAPSHOT
	
```
	
	


