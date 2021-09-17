package com.pd.gather.dao.impl;

import com.alibaba.fastjson.JSON;
import com.pd.gather.dao.GatherDataInterface;
import com.pd.gather.entity.GatherDataEntity;
import com.pd.gather.entity.TableMetaDataEntity;
import com.pd.gather.util.ConnectorUtil;
import com.pd.gather.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


/**
 * Description:
 *
 * @author zz
 * @date 2021/9/9
 */
@Repository
public class GatherDataInterfaceImpl implements GatherDataInterface {
    private Logger logger = LoggerFactory.getLogger(GatherDataInterfaceImpl.class);

    private DBToHiveData DBToHiveData = new DBToHiveData();
    private Properties properties = new Properties();

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public ConnectorUtil getConn() {
        String driver = properties.getProperty(Constants.MYSQL_DRIVER_NAME);
        String url = properties.getProperty(Constants.MYSQL_URL);
        String name = properties.getProperty(Constants.MYSQL_USER);
        String passwd = properties.getProperty(Constants.MYSQL_PASSWORD);

        ConnectorUtil conn = new ConnectorUtil(driver, url, name, passwd);
        return conn;
    }

    @Override
    public List<HashMap<String, String>> getGatherData() {

        ConnectorUtil conn = null;
        List<HashMap<String, String>> result = null;
        try {
            logger.info("get gather data");
            String tableName = properties.getProperty(Constants.MYSQL_TABLE_NAME);
            conn = getConn();
            result = conn.execQuery(String.format(Constants.SELECT_ALL_FROM_TABLE, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), tableName));
        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return result;
    }


    @Override
    public List<HashMap<String, String>> getGatherDataById(Integer id) {

        logger.info("get gather data by id: {}", id);
        ConnectorUtil conn = null;
        List<HashMap<String, String>> result = null;


        try {

            conn = getConn();
            result = conn.execQuery(String.format(Constants.SELECT_TABLE_BY_ID, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), id));
        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public List<TableMetaDataEntity> getTablesInGather(List<HashMap<String, String>> gatherData) {

        logger.info("get tables info in gather");

        List<TableMetaDataEntity> resultList = new ArrayList<>();
        ConnectorUtil conn = null;
        try {
            for (int i = 0; i < gatherData.size(); i++) {
                TableMetaDataEntity tableMetaDataEntity = new TableMetaDataEntity();
                HashMap<String, String> colData = gatherData.get(i);
                GatherDataEntity gatherDataEntity = JSON.parseObject(JSON.toJSONString(colData), GatherDataEntity.class);
                // System.out.println(gatherData.toString());
                //[{database_name=test, job_id=test_111, jdbc_url=jdbc:mysql://localhost:3306/gather?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true, table_name=test}]
                String tableName = gatherDataEntity.getTableName();
                String db = gatherDataEntity.getDatabaseName();

                conn = new ConnectorUtil(properties.getProperty(Constants.MYSQL_DRIVER_NAME), gatherDataEntity.getJdbcUrl(), gatherDataEntity.getUserName(), gatherDataEntity.getPasswd());
                List<List<String>> metaData = conn.getMetaData(db, tableName);
                String tableComment = conn.execQuery(String.format(Constants.TABLE_COMMENT, tableName)).get(0).get(Constants.TABLE_COMMENT_KEY);
                tableMetaDataEntity.setGatherDataEntity(gatherDataEntity);
                tableMetaDataEntity.setMetaData(metaData);
                tableMetaDataEntity.setTableComment(tableComment);
                resultList.add(tableMetaDataEntity);

            }
        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return resultList;
    }


    @Override
    public List<HashMap<String, String>> getCreateHiveTableSql(List<TableMetaDataEntity> tableMetaDataEntitys) {

        logger.info("exec create hive sql");

        List<HashMap<String, String>> resultList = new ArrayList<>();
        for (TableMetaDataEntity tableMetaDataEntity : tableMetaDataEntitys) {
            GatherDataEntity gatherDataEntity = tableMetaDataEntity.getGatherDataEntity();
            List<List<String>> metaData = tableMetaDataEntity.getMetaData();
            String tableComment = tableMetaDataEntity.getTableComment();
            HashMap<String, String> map = DBToHiveData.convertCreateTable(gatherDataEntity, metaData, properties, tableComment);
            resultList.add(map);
        }
        return resultList;
    }


    @Override
    public boolean createHiveTable(List<HashMap<String, String>> createHiveTableSqls) {

        boolean exec = false;
        ConnectorUtil conn = null;
        try {

            for (HashMap<String, String> createHiveTableSql : createHiveTableSqls) {
                String stgSql = createHiveTableSql.get("stg");
                String odsSql = createHiveTableSql.get("ods");
                conn = new ConnectorUtil(properties.getProperty(Constants.HIVE_DRIVER_NAME), properties.getProperty(Constants.HIVE_URL), properties.getProperty(Constants.HIVE_USER), properties.getProperty(Constants.HIVE_PASSWORD));
                exec = conn.exec(stgSql);
                exec = conn.exec(odsSql);
            }

            logger.info("create hive table success!");

        } finally {
            try {
                conn.destory();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exec;
    }


    @Override
    public List<String> getDataxJson(List<HashMap<String, String>> createHiveTableSqls, List<TableMetaDataEntity> tableMetaDataEntitys, String jsonFileTempleStatic) {
        List<String> dataxJsonList = new ArrayList<>();
        for (int i = 0; i < createHiveTableSqls.size(); i++) {
            HashMap<String, String> stringStringHashMap = createHiveTableSqls.get(i);
            TableMetaDataEntity tableMetaDataEntity = tableMetaDataEntitys.get(i);
            GatherDataEntity gatherDataEntity = tableMetaDataEntity.getGatherDataEntity();
            String stg = stringStringHashMap.get("stg");
            String dataXJson = DBToHiveData.writeDataXJson(properties, stg, jsonFileTempleStatic, gatherDataEntity);
            dataxJsonList.add(dataXJson.replace("\"", "\\\""));
        }

        logger.info("------------------init datax json------------------");
        logger.info(dataxJsonList.toString());
        return dataxJsonList;
    }

    @Override
    public int insertGatherData(GatherDataEntity gatherDataEntity) {
        ConnectorUtil conn = null;
        try {
            conn = getConn();
            String jdbcUrl = gatherDataEntity.getJdbcUrl();
            String databaseName = gatherDataEntity.getDatabaseName();
            String tableName = gatherDataEntity.getTableName();
            String user = gatherDataEntity.getUserName();
            String passwd = gatherDataEntity.getPasswd();
            String syncType = gatherDataEntity.getSyncType();
            String dolphinProjectName = gatherDataEntity.getDolphinProjectName();
            boolean isCreateHiveTable = gatherDataEntity.getCreateHiveTable();
            boolean online = gatherDataEntity.getIsOnline();
            String crontab = gatherDataEntity.getCrontab();
            int exec = conn.execUpdateOrInsert(String.format(Constants.INSERT_GATHER_DATA, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), jdbcUrl, databaseName, tableName, user, passwd, syncType, dolphinProjectName, isCreateHiveTable, online, crontab));
            logger.info("insert gather's data by id successfully");
            return exec;
        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public int updateGather(GatherDataEntity gatherDataEntity) throws Exception {
        ConnectorUtil conn = null;
        try {
            conn = getConn();
            Integer jobId = gatherDataEntity.getJobId();
            String jdbcUrl = gatherDataEntity.getJdbcUrl();
            String databaseName = gatherDataEntity.getDatabaseName();
            String tableName = gatherDataEntity.getTableName();
            String user = gatherDataEntity.getUserName();
            String passwd = gatherDataEntity.getPasswd();
            String syncType = gatherDataEntity.getSyncType();
            String dolphinProjectName = gatherDataEntity.getDolphinProjectName();
            boolean isCreateHiveTable = gatherDataEntity.getCreateHiveTable();
            boolean online = gatherDataEntity.getIsOnline();
            String crontab = gatherDataEntity.getCrontab();
            int exec = conn.execUpdateOrInsert(String.format(Constants.UPDATE_GATHER_DATA, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), jdbcUrl, databaseName, tableName, user, passwd, syncType, dolphinProjectName, isCreateHiveTable, online, crontab, jobId));
            logger.info("update gather's data by id successfully, jobId : {}", jobId);
            return exec;
        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public int deleteGatherData(Integer id) throws Exception {
        ConnectorUtil conn = null;
        int execSucc = 0;
        try {
            conn = getConn();
            execSucc = conn.execUpdateOrInsert(String.format(Constants.DELETE_GATHER_BY_ID, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), id));

            logger.info("delete gather's data by id successfully, id : {}", id);
        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return execSucc;
    }

    @Override
    public GatherDataEntity findGatherById(Integer id) throws Exception {
        ConnectorUtil conn = null;
        GatherDataEntity gatherDataEntity = null;
        try {
            conn = getConn();

            List<HashMap<String, String>> gatherData = conn.execQuery(String.format(Constants.SELECT_GATHER_BY_ID, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), id));
            for (int i = 0; i < gatherData.size(); i++) {
                HashMap<String, String> colData = gatherData.get(i);
                gatherDataEntity = JSON.parseObject(JSON.toJSONString(colData), GatherDataEntity.class);
            }
            logger.info("find gather's data by id successfully, id: {}", id);

        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return gatherDataEntity;
    }

    @Override
    public List<GatherDataEntity> findGatherByPage(Integer star, Integer end) {
        ConnectorUtil conn = null;
        ArrayList<GatherDataEntity> gatherDataEntities = new ArrayList<>();
        try {
            conn = getConn();

            List<HashMap<String, String>> gatherData = conn.execQuery(String.format(Constants.SELECT_GATHER_PAGE, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), star, end));
            for (int i = 0; i < gatherData.size(); i++) {
                HashMap<String, String> colData = gatherData.get(i);
                GatherDataEntity gatherDataEntity = JSON.parseObject(JSON.toJSONString(colData), GatherDataEntity.class);
                gatherDataEntities.add(gatherDataEntity);
            }

            logger.info("find gather's data by page successfully");

        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return gatherDataEntities;
    }

    @Override
    public int findGatherCount() {
        ConnectorUtil conn = null;
        int count = 0;
        try {
            conn = getConn();
            count = Integer.valueOf(conn.execQuery(String.format(Constants.SELECT_COUNT_FROM_TABLE, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME)))
                    .get(0).values().toArray()[0].toString());

            logger.info("find gather's data count successfully, total count: {}", count);
        } finally {
            try {
                conn.destory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return count;
    }

}
