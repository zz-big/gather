CREATE DATABASE gather;
CREATE TABLE `gather_data` (
  `job_id` int(8) NOT NULL AUTO_INCREMENT,
  `jdbc_url` varchar(255) NOT NULL,
  `database_name` varchar(255) NOT NULL,
  `table_name` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `passwd` varchar(255) NOT NULL,
  `sync_type` varchar(255) NOT NULL,
  `dolphin_project_name` varchar(255) NOT NULL,
  `create_hive_table` boolean DEFAULT false,
  `is_online` boolean DEFAULT false,
  `crontab` varchar(255) NOT NULL,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

  INSERT INTO gather.gather_data (jdbc_url,database_name,table_name,`user_name`,passwd,sync_type,dolphin_project_name,crontab) VALUES('jdbc:mysql://localhost:3306','gather','gather_data','root','000000','full','Test','0 30 8 * * ? *');