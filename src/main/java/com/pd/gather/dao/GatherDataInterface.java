package com.pd.gather.dao;

import com.pd.gather.entity.GatherDataEntity;
import com.pd.gather.entity.TableMetaDataEntity;
import com.pd.gather.util.ConnectorUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */
public interface GatherDataInterface {

    ConnectorUtil getConn() throws Exception;

    void setProperties(Properties properties) throws Exception;

    List<HashMap<String, String>> getGatherData() throws Exception;

    List<HashMap<String, String>> getGatherDataById(Integer id) throws Exception;

    List<TableMetaDataEntity> getTablesInGather(List<HashMap<String, String>> gatherData) throws Exception;

    boolean createHiveTable(List<HashMap<String, String>> createHiveTableSqls) throws Exception;

    List<HashMap<String, String>> getCreateHiveTableSql(List<TableMetaDataEntity> tableMetaDataEntity) throws Exception;

    List<String> getDataxJson(List<HashMap<String, String>> createHiveTableSqls, List<TableMetaDataEntity> tableInGathers, String jsonFileTempleStatic) throws Exception;

    int insertGatherData(GatherDataEntity gatherDataEntity) throws Exception;

    int updateGather(GatherDataEntity gatherDataEntity) throws Exception;

    int deleteGatherData(Integer id) throws Exception;

    GatherDataEntity findGatherById(Integer id) throws Exception;

    List<GatherDataEntity> findGatherByPage(Integer star, Integer end) throws Exception;

    int findGatherCount() throws Exception;

}
