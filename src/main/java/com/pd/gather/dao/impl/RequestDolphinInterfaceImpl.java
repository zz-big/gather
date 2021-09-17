package com.pd.gather.dao.impl;

import com.alibaba.fastjson.JSON;
import com.pd.gather.dao.GatherDataInterface;
import com.pd.gather.dao.RequestDolphinInterface;
import com.pd.gather.entity.GatherDataEntity;
import com.pd.gather.request.Request;
import com.pd.gather.util.ConnectorUtil;
import com.pd.gather.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/10
 */
@Repository
public class RequestDolphinInterfaceImpl implements RequestDolphinInterface {
    private Logger logger = LoggerFactory.getLogger(RequestDolphinInterfaceImpl.class);

    @Autowired
    private GatherDataInterface gatherDataInterface;


    @Override
    public String doSave(Properties properties, List<HashMap<String, String>> createHiveTableSqls, List<String> dataxJsonList, int id, GatherDataEntity gatherDataEntity) {
        logger.info("---------------create dolphin job---------------");
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> paramsOnLine = new HashMap<String, Object>();

        for (int i = 0; i < createHiveTableSqls.size(); i++) {
            String stgTableName = null;
            String dataxJson = dataxJsonList.get(i).replace("\n", "\\n");
            String stgToOdsSql = "";
            String odsTableName;
            String odsCols = "";
            String stg_tCols = "";
            String stgCols = "";
            HashMap<String, String> stringStringHashMap = createHiveTableSqls.get(i);
            String stg = stringStringHashMap.get("stg");
            String ods = stringStringHashMap.get("ods");
            ArrayList<String> stgColsArr = new ArrayList<String>();

            odsTableName = ods.substring(stg.indexOf(".") + 1, stg.indexOf(" ("));
            stgTableName = stg.substring(stg.indexOf(".") + 1, stg.indexOf(" ("));

            String[] stgArr = stg.substring(stg.indexOf("(\n") + 2, stg.lastIndexOf("\n)")).split(",\n");
            for (int j = 0; j < stgArr.length; j++) {
                stgColsArr.add(stgArr[j].split(" ")[0]);
            }


            for (String stgCol : stgColsArr) {
                stgCols += stgCol + ",\\n   ";
                stg_tCols += "stg_t." + stgCol + ",\\n   ";
                odsCols += "ods_t." + stgCol + ",\\n   ";
            }

            String odsAddcol = properties.getProperty(Constants.HIVE_ODS_ADD_COL).split(" ")[0];
            String stgAddcol = properties.getProperty(Constants.HIVE_STG_ADD_COL).split(" ")[0];

            String partition_col_name = properties.getProperty(Constants.HIVE_PARTITION_COL_NAME).split("`")[1].trim();
            String description = "";
            switch (gatherDataEntity.getSyncType()) {

                case Constants.SYNC_TYPE_FULL:
                    stgToOdsSql = properties.getProperty(Constants.STG_TO_ODS_SQL_OF_FULL).replace(Constants.ODS_TABLE_NAME, odsTableName).replace(Constants.STG_TABLE_NAME, stgTableName)
                            .replace(Constants.STG_COLS, stgCols.substring(0, stgCols.lastIndexOf(",\\n")));
                    description = Constants.SYNC_TYPE_FULL;
                    break;
                case Constants.SYNC_TYPE_SNAPSHOT:
                    stgToOdsSql = properties.getProperty(Constants.STG_TO_ODS_SQL_OF_SNAPSHOT).replace(Constants.ODS_TABLE_NAME, odsTableName).replace(Constants.STG_TABLE_NAME, stgTableName)
                            .replace(Constants.STG_COLS, stgCols).replace(Constants.PARTITION_COL_NAME, partition_col_name);
                    description = Constants.SYNC_TYPE_SNAPSHOT;
                    break;
                case Constants.SYNC_TYPE_INCRE:
                    stgToOdsSql = properties.getProperty(Constants.STG_TO_ODS_SQL_OF_INCRE).replace(Constants.ODS_TABLE_NAME, odsTableName).replace(Constants.STG_TABLE_NAME, stgTableName)
                            .replace(Constants.ODS_COLS, odsCols.replace(stgAddcol, odsAddcol)).replace(Constants.STG_T_COLS, stg_tCols.replace(stgAddcol, odsAddcol)).replace(Constants.STG_COLS, stgCols.substring(0, stgCols.lastIndexOf(",\\n")) + " as " + odsAddcol + ", ").replace(Constants.PARTITION_COL_NAME, partition_col_name);
                    description = Constants.SYNC_TYPE_INCRE;
                    break;
                default:
                    break;

            }
            logger.info("------------------stg to ods sql, tableName: " + stgTableName + "---------------");
            logger.info(stgToOdsSql);

            String processDefinitionJson = properties.getProperty(Constants.PROCESS_DEFINITION_JSON).replace(Constants.STG_TO_ODS_SQL, stgToOdsSql).replace(Constants.STG_TABLE_NAME, stgTableName)
                    .replace(Constants.ODS_TABLE_NAME, odsTableName).replace(Constants.DATAX_JSON, dataxJson).replace(Constants.DATASOURCE_NAME, properties.getProperty(Constants.DOLPHIN_SQL_DATA_SOURCENAME))
                    .replace(Constants.TENANT_ID_NAME, properties.getProperty(Constants.TENANT_ID));

            String dolphinProjectName = gatherDataEntity.getDolphinProjectName();
            logger.info("------------------ dolphinProjectName: " + dolphinProjectName + ",  odsTableName: " + odsTableName + "-------------");
            logger.info(Constants.PROJECT_NAME + ": " + dolphinProjectName);
            logger.info(Constants.NAME + ": " + odsTableName);
            logger.info(Constants.PROCESS_DEFINITION_JSON + ":" + processDefinitionJson);
            logger.info(Constants.LOCATIONS + ": " + properties.getProperty(Constants.LOCATIONS));
            logger.info(Constants.CONNECTIONS + ": " + properties.getProperty(Constants.CONNECTIONS));
            logger.info(Constants.DESCRIPTION + ": " + description);

            processDefinitionJson = Request.encodeURL(processDefinitionJson);

            params.put(Constants.PROJECT_NAME, dolphinProjectName);
            params.put(Constants.NAME, odsTableName);
            params.put(Constants.PROCESS_DEFINITION_JSON, processDefinitionJson);
            params.put(Constants.LOCATIONS, properties.getProperty(Constants.LOCATIONS));
            params.put(Constants.CONNECTIONS, properties.getProperty(Constants.CONNECTIONS));
            params.put(Constants.DESCRIPTION, description);


            String saveUrl = String.format(Constants.SAVE_URL, properties.getProperty(Constants.URL), dolphinProjectName);
            String jobOnLineUrl = String.format(Constants.JOB_ON_lINE_URL, properties.getProperty(Constants.URL), dolphinProjectName);
            String searchUrl = String.format(Constants.SEARCH_URL, properties.getProperty(Constants.URL), dolphinProjectName, odsTableName);

            String saveProcess = null;
            String searchProcess = null;
            String onLineProcess = null;
            String jobId = null;
            String token = properties.getProperty(Constants.TOKEN);

            try {
                //创建工作流
                saveProcess = Request.post(saveUrl).params(params).body(token);

                if (!saveProcess.contains(Constants.FAILED)) {
                    searchProcess = Request.get(searchUrl).body(token);

                    //获取工作流id
                    jobId = JSON.parseObject(JSON.parseObject(searchProcess).getJSONObject("data").getJSONArray("totalList").getString(0)).getString("id");

                    paramsOnLine.put(Constants.PROCESS_ID, jobId);
                    paramsOnLine.put(Constants.RELEASE_STATE, Constants.RELEASE_STATE_ON_LINE);
                    //上线job
                    onLineProcess = Request.post(jobOnLineUrl).params(paramsOnLine).body(token);
                    logger.info("on line job success, id : {}", jobId);

                    //dolphin job上线成功，配置定时任务
                    if (!onLineProcess.contains(Constants.FAILED)) {
                        String doScheduler = doScheduler(properties, id, jobId, dolphinProjectName, token);
                        return doScheduler;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Constants.FAILED;
    }

    @Override
    public String doScheduler(Properties properties, int id, String jobId, String dolphinProjectName, String token) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> schedulerOnLineparams = new HashMap<String, Object>();
        ConnectorUtil conn = null;

        //创建定时任务
        String schedulerUrl = String.format(Constants.SCHEDULER_URL, properties.getProperty(Constants.URL), dolphinProjectName);
        String searchScheduleUrl = String.format(Constants.SEARCH_SCHEDULE_URL, properties.getProperty(Constants.URL), dolphinProjectName, jobId);
        String schedulerOnLineUrl = String.format(Constants.SCHEDULER_ON_LINE_URL, properties.getProperty(Constants.URL), dolphinProjectName);

        try {
            conn = gatherDataInterface.getConn();
            List<HashMap<String, String>> hashMaps = conn.execQuery(String.format(Constants.SELECT_GATHER_BY_ID, properties.getProperty(Constants.MYSQL_DB), properties.getProperty(Constants.MYSQL_TABLE_NAME), id));
            GatherDataEntity gatherDataEntity = JSON.parseObject(JSON.toJSONString(hashMaps.get(0)), GatherDataEntity.class);
            String crontab = gatherDataEntity.getCrontab();
            String schedule = String.format(properties.getProperty(Constants.SCHEDULE), crontab);
            params.put(Constants.SCHEDULE, schedule);
            params.put(Constants.FAILURE_STRATEGY, properties.getProperty(Constants.FAILURE_STRATEGY));
            params.put(Constants.PROCESS_INSTANCE_PRIORITY, properties.getProperty(Constants.PROCESS_INSTANCE_PRIORITY));
            params.put(Constants.WARNING_GROUP_ID, properties.getProperty(Constants.WARNING_GROUP_ID));
            params.put(Constants.RECEIVERS, properties.getProperty(Constants.RECEIVERS));
            params.put(Constants.RECEIVERS_CC, properties.getProperty(Constants.RECEIVERS_CC));
            params.put(Constants.WORKER_GROUP, properties.getProperty(Constants.WORKER_GROUP));
            params.put(Constants.PROCESS_DEFINITION_ID, jobId);

            logger.info("------------------on line scheduler, dolphinProjectName: " + dolphinProjectName + "------------------");
            logger.info(Constants.SCHEDULE + ": " + schedule);
            logger.info(Constants.FAILURE_STRATEGY + ": " + properties.getProperty(Constants.FAILURE_STRATEGY));
            logger.info(Constants.PROCESS_INSTANCE_PRIORITY + ":" + properties.getProperty(Constants.PROCESS_INSTANCE_PRIORITY));
            logger.info(Constants.WARNING_GROUP_ID + ": " + properties.getProperty(Constants.WARNING_GROUP_ID));
            logger.info(Constants.RECEIVERS + ": " + properties.getProperty(Constants.RECEIVERS));
            logger.info(Constants.RECEIVERS_CC + ": " + properties.getProperty(Constants.RECEIVERS_CC));
            logger.info(Constants.WORKER_GROUP + ": " + properties.getProperty(Constants.WORKER_GROUP));
            logger.info(Constants.PROCESS_DEFINITION_ID + ": " + jobId);

            String schedulerProcess = null;
            String searchScheduleProcess = null;
            String schedulerOnLineProcess = null;

            schedulerProcess = Request.post(schedulerUrl).params(params).body(token);

            if (!schedulerProcess.contains(Constants.FAILED)) {
                //获取定时任务data
                searchScheduleProcess = Request.get(searchScheduleUrl).body(token);
                String scheduleId = JSON.parseObject(JSON.parseObject(searchScheduleProcess).getJSONObject("data").getJSONArray("totalList").getString(0)).getString("id");

                //上线定时任务
                schedulerOnLineparams.put(Constants.SCHEDULER_ID, scheduleId);
                schedulerOnLineProcess = Request.post(schedulerOnLineUrl).params(schedulerOnLineparams).body(token);
                logger.info("------------------ on line scheduler success, job id : {}, schedule id : {} ------------------ ", jobId, scheduleId);

                return schedulerOnLineProcess;

            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.destory();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Constants.FAILED;
    }

    @Override
    public String getDolphinUrl(Properties properties) throws Exception {

        String dolphinurl = String.format(Constants.DOLPHIN_URL, properties.getProperty(Constants.URL));

        return dolphinurl;
    }


}
