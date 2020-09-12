Setup the mysql container:	
 docker run -dp 1000:3306 --name=9580-mysql-docker --env="MYSQL_ROOT_PASSWORD=root" --env="MYSQL_DATABASE=raja_practice" mysql

Login to DB using work bench and execute queries
	URL: 192.168.99.100:1000 -> as i am using docker tool i cannot use localhost:1000,
	you can find the docker host ip address(192.168.99.100) once you launch docker terminal
	
	use raja_practice;
	create table coupon(
	id int AUTO_INCREMENT PRIMARY KEY,
	code varchar(20) UNIQUE,
	discount decimal(8,3),
	exp_date varchar(100) 
	);

Build Docker Image
------------------
	CD to Dockerfile folder, or instead of . we have to give file path
		 docker build -t raja9580/coupon:0.0.1-SNAPSHOT .
	
Run Coupon Service Container
----------------------------	 
	docker run -it --name=coupon-service -p 1001:8080 raja9580/coupon:0.0.1-SNAPSHOT

Test:


Connect to Database using container name
----------------------------------------
	* Run both containers in custom network
	* URL in coupon-service jdbc:mysql://9580-mysql-docker/raja_practice
	 
	Change MYSQL_DATABASE network
	docker network disconnect bridge  9580-mysql-docker
	docker network connect 9580  9580-mysql-docker
	
	Code change and Image build and docker hub push
		jdbc:mysql://9580-mysql-docker/raja_practice
		docker run -dp 1001:8080 --net 9580  raja9580/coupon-service:0.0.1-SNAPSHOT
		
	Test APP:
	post: http://hostname:1001/coupons
		test data {"code": "FLAT50","discount": 50,"expDate": "20201231"}