package com.pd.gather.dao;

import com.pd.gather.entity.GatherDataEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */
public interface RequestDolphinInterface {
    String doSave(Properties properties, List<HashMap<String, String>> createHiveTableSqls, List<String> dataxJsonList, int id, GatherDataEntity gatherDataEntity) throws Exception;

    String doScheduler(Properties properties, int id, String jobId, String projectName, String token) throws Exception;

    String getDolphinUrl(Properties properties) throws Exception;

    String doRemove(Properties properties, int id, GatherDataEntity gatherDataEntity) throws Exception;

}
