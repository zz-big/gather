package com.pd.gather.service;

import com.pd.gather.dao.GatherDataInterface;
import com.pd.gather.dao.RequestDolphinInterface;
import com.pd.gather.entity.GatherDataEntity;
import com.pd.gather.entity.TableMetaDataEntity;
import com.pd.gather.util.ConnectorUtil;
import com.pd.gather.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */
@Service
public class GatherDataService {
    private Logger logger = LoggerFactory.getLogger(GatherDataService.class);

    @Autowired
    private GatherDataInterface gatherDataInterface;

    @Autowired
    private RequestDolphinInterface requestDolphinInterface;

    private static String mysql2HivePropertiesStatic = GatherDataService.class.getClassLoader().getResource(Constants.GATHER_PROP).getPath();
    private static String jsonFileTempleStatic = GatherDataService.class.getClassLoader().getResource(Constants.DATAX_TEMPLE_JSON).getPath();
    Properties properties = null;


    @PostConstruct
    public void init() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(mysql2HivePropertiesStatic));
            gatherDataInterface.setProperties(properties);

        } catch (IOException e) {
            logger.error("读取配置文件失败" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }


    public List<HashMap<String, String>> getGatherData() {
        List<HashMap<String, String>> getGatherResult = null;
        try {
            getGatherResult = gatherDataInterface.getGatherData();
        } catch (Exception e) {
            logger.error("get gather data failed!" + e.getMessage());
            e.printStackTrace();
        }
        return getGatherResult;
    }


    public int createAllJobs(List<Integer> ids) {
        int n = 0;
        try {

            for (int id : ids) {
                n = onLineGather(id);
            }
        } catch (Exception e) {
            logger.error("create all jobs failed!" + e.getMessage());
            e.printStackTrace();
        }
        return n;
    }


    public int onLineGather(Integer id) {
        int n = 0;
        try {
            List<HashMap<String, String>> gatherData = gatherDataInterface.getGatherDataById(id);
            List<TableMetaDataEntity> tableInGather = gatherDataInterface.getTablesInGather(gatherData);
            List<HashMap<String, String>> createHiveTableSqls = gatherDataInterface.getCreateHiveTableSql(tableInGather);
            GatherDataEntity gatherDataEntity = tableInGather.get(0).getGatherDataEntity();
            if (gatherDataEntity.getCreateHiveTable() == Boolean.valueOf(Constants.TRUE)) {
                boolean createHiveTableSucc = gatherDataInterface.createHiveTable(createHiveTableSqls);
            }

            List<String> dataxJsonList = gatherDataInterface.getDataxJson(createHiveTableSqls, tableInGather, jsonFileTempleStatic);
            String doSave = requestDolphinInterface.doSave(properties, createHiveTableSqls, dataxJsonList, id, gatherDataEntity);

            if (!doSave.contains(Constants.FAILED)) {

                //更新数据库
                ConnectorUtil conn = gatherDataInterface.getConn();
                String updateCreateJobAndOnLine = String.format(Constants.UPDATE_GATHER_CREATE_JOB_AND_ON_LINE, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), Constants.TRUE, id);
                int result = conn.execUpdateOrInsert(updateCreateJobAndOnLine);

                n = result;
            }

        } catch (Exception e) {
            logger.error("on line job by {} failed! {}", id, e.getMessage());
            e.printStackTrace();
        }
        return n;
    }


    public int insertGatherData(GatherDataEntity gatherDataEntity) {
        int n = 0;
        try {
            n = gatherDataInterface.insertGatherData(gatherDataEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n;
    }

    public List<GatherDataEntity> findGatherByPage(Integer star, Integer end) {
        List<GatherDataEntity> gatherByPage = null;
        try {
            gatherByPage = gatherDataInterface.findGatherByPage(star, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gatherByPage;
    }

    public int findGatherCount() {
        int gatherCount = 0;
        try {
            gatherCount = gatherDataInterface.findGatherCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gatherCount;
    }

    public GatherDataEntity findGatherById(Integer id) {
        GatherDataEntity gatherDataEntity = null;
        try {
            gatherDataEntity = gatherDataInterface.findGatherById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gatherDataEntity;
    }


    public int updateGather(GatherDataEntity gatherDataEntity) {
        int gatherCount = 0;
        try {
            gatherCount = gatherDataInterface.updateGather(gatherDataEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gatherCount;
    }


    public int deleteGatherData(Integer id) {
        int n = 0;
        try {
            n = gatherDataInterface.deleteGatherData(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n;
    }

    public String getDolphinUrl() {
        String dolphinUrl = null;
        try {
            dolphinUrl = requestDolphinInterface.getDolphinUrl(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dolphinUrl;
    }


}
