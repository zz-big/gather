import com.alibaba.fastjson.JSON;
import com.pd.gather.dao.GatherDataInterface;
import com.pd.gather.dao.impl.GatherDataInterfaceImpl;
import com.pd.gather.dao.impl.RequestDolphinInterfaceImpl;
import com.pd.gather.entity.GatherDataEntity;
import com.pd.gather.entity.TableMetaDataEntity;
import com.pd.gather.request.Request;
import com.pd.gather.util.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */

public class GatherTest {

    private GatherDataInterfaceImpl gatherDataInterfaceImpl = new GatherDataInterfaceImpl();
    private RequestDolphinInterfaceImpl RequestDolphinInterfaceImpl;

    private static String mysql2HivePropertiesStatic = GatherDataInterface.class.getClassLoader().getResource(Constants.GATHER_PROP).getPath();
    private static String jsonFileTempleStatic = GatherDataInterface.class.getClassLoader().getResource(Constants.DATAX_TEMPLE_JSON).getPath();

    private Properties properties = null;


    public void init() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(mysql2HivePropertiesStatic));
            gatherDataInterfaceImpl.setProperties(properties);
            System.out.println(mysql2HivePropertiesStatic);
        } catch (IOException e) {
            System.out.println("读取配置文件失败");
        }
    }

    @org.junit.Test
    public void testSql() {
        // init();
        // List<HashMap<String, String>> gatherData = gatherDataInterfaceImpl.getGatherData();
        // List<TableMetaDataEntity> tablesInGather = gatherDataInterfaceImpl.getTablesInGather(gatherData);
        // List<HashMap<String, String>> createHiveTableSql = gatherDataInterfaceImpl.getCreateHiveTableSql(tablesInGather);
        // for (int i = 0; i < createHiveTableSql.size(); i++) {
        //     HashMap<String, String> stringStringHashMap = createHiveTableSql.get(i);
        //     System.out.println(stringStringHashMap.get("stg"));
        //     System.out.println(stringStringHashMap.get("ods"));
        // }
    }

    @org.junit.Test
    public void testDatax() {

        // List<HashMap<String, String>> gatherData = gatherDataInterfaceImpl.getGatherData();
        // List<TableMetaDataEntity> tableInGathers = gatherDataInterfaceImpl.getTablesInGather(gatherData);
        // System.out.println(tableInGathers.get(0).toString());

        //TableMetaDataEntity{gatherDataEntity=GatherDataEntity{jobId=4, jdbcUrl='jdbc:mysql://106.55.175.172:33061', databaseName='device_middle_platform', tableName='temp', userName='read_user', passwd='read_user@pudutech', syncType='full', dolphinProjectName='GatherTest', createHiveTable=true, isOnline=true, crontab='0 12 20 * * ? *'}, metaData=[[code, varchar(255), ], [zh_name, varchar(255), ]], tableComment=''}
        ArrayList<List<String>> meta = new ArrayList<>();
        ArrayList<String> metaTmp1 = new ArrayList<>();
        ArrayList<String> metaTmp2 = new ArrayList<>();
        metaTmp1.add("code");
        metaTmp1.add("varchar(255)");
        metaTmp1.add("");
        metaTmp2.add("zh_name");
        metaTmp2.add("varchar(255)");
        metaTmp2.add("");
        meta.add(metaTmp1);
        meta.add(metaTmp2);

        String tableComment = "";

        TableMetaDataEntity tableMetaDataEntity = new TableMetaDataEntity();

        String gatherDataEntityJson = "{jobId:4, jdbcUrl:'jdbc:mysql://106.55.175.172:33061', databaseName:'device_middle_platform', tableName:'temp', userName:'read_user', passwd:'read_user@pudutech', syncType:'full', dolphinProjectName:'GatherTest', createHiveTable:true, isOnline:true, crontab:'0 12 20 * * ? *'}";
        GatherDataEntity gatherDataEntity = JSON.parseObject(gatherDataEntityJson, GatherDataEntity.class);

        tableMetaDataEntity.setGatherDataEntity(gatherDataEntity);
        tableMetaDataEntity.setMetaData(meta);
        tableMetaDataEntity.setTableComment(tableComment);

        System.out.println(tableMetaDataEntity.toString());

        List<TableMetaDataEntity> gatherDataEntities = new ArrayList<>();
        gatherDataEntities.add(tableMetaDataEntity);

        init();
        gatherDataInterfaceImpl.setProperties(properties);
        System.out.println("------------tableInGathers--------------");
        List<HashMap<String, String>> createHiveTableSqls = gatherDataInterfaceImpl.getCreateHiveTableSql(gatherDataEntities);
        List<String> dataxJsonList = gatherDataInterfaceImpl.getDataxJson(createHiveTableSqls, gatherDataEntities, jsonFileTempleStatic);
        System.out.println("------------dataxJsonList--------------");
        System.out.println(dataxJsonList.get(0));

    }

    @org.junit.Test
    public void testRequest() throws IOException {
        // init();
        // String token = properties.getProperty(Constants.TOKEN);
        // System.out.println(token);
        // String saveProcess = Request.get("http://159.75.252.114:12346/dolphinscheduler/projects/GatherTest/process/list-paging?pageSize=10&pageNo=1&searchVal=&userId=&_t=0.6334133712708736").body(token);
        // System.out.println(saveProcess);

    }

    @org.junit.Test
    public void testSave() {

    }
}
