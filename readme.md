# 编译：

mvn clean install

# 项目流程：

数据流向：mysql-->datax-->hive_stg-->hive_ods

整体项目流程图：

![gather](.\img\gather.png)

时序图：

![GatherDataService_onLineGather](.\img\GatherDataService_onLineGather.png)



# 运行

## 创建测试数据：

​     1.运行resource/gather.sql创建mysql测试库和表

​     2.创建hive dc_stg和dc_ods库

`create database dc_stg;`

`create database dc_ods;`

## **gather.properties**需要修改的配置项：

```
#mysql
MysqlUrl=jdbc:mysql://localhost:3306
MysqlUser=xxx
MysqlPassword=xxx
MysqlDB=gather
MysqlTableName=gather_data

#是否创建hive表
createTable=true
#hive jdbc
hiveUrl=jdbc:hive2://pd-cdh-192-168-xx-node:10000
hiveUser=xxx
hivePassword=xxx

#dolphin url
url=http://159.75.252.xxx:xx
#dolphin个人用户token
token=xxx
#dolphin个人用户数据源
dolphinSqlDatasourceName=xxx
#dolphin的个人用户租户id,default租户id为-1
tenantId=-1
```



## 本地idea运行项目

![idea_run_1](.\img\idea_run_1.png)

![idea_run_2](.\img\idea_run_2.png)

![idea_run_3](.\img\idea_run_3.png)

点击run启动项目

![idea_run_4](.\img\idea_run_4.png)

# 使用

## 新增要采集的表信息：

![idea_run_5](.\img\idea_run_5.png)

采集信息显示，新增采集信息默认上线dolphin项目是false，点击上线后会创建dolphin任务流，

并且隐藏掉上线按钮(重复上线会在donphin单个任务中生成多个定时任务)。

## 上线项目

点击上线，会提示上线成功或失败，上线成功后可点击dolphin主页按钮跳转dolphin主页查看任务信息。

![idea_run_6](.\img\idea_run_6.png)





