package com.pd.gather.util;


/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */
public interface Constants {
    String GATHER_PROP = "gather.properties";
    String DATAX_TEMPLE_JSON = "dataxJsonTemple.json";
    String SUCCESS = "success";
    String FAILED = "failed";
    String FALSE = "false";
    String TRUE = "true";

    //同步方式，全量/增量/每日快照
    String SYNC_TYPE_FULL = "full";
    String SYNC_TYPE_SNAPSHOT = "snapshot";
    String SYNC_TYPE_INCRE = "incre";

    //mysql db info
    String MYSQL_DRIVER_NAME = "MysqlDriverName";
    String MYSQL_URL = "MysqlUrl";
    String MYSQL_USER = "MysqlUser";
    String MYSQL_PASSWORD = "MysqlPassword";
    String MYSQL_DB = "MysqlDB";
    String MYSQL_TABLE_NAME = "MysqlTableName";

    //mysql curd
    String SHOW_COLUMNS = "show full columns from %s.%s";
    String HIVE_EXTERNAL = "external";
    String SELECT_ALL_FROM_TABLE = "select * from  %s.%s";
    String SELECT_TABLE_BY_ID = "select * from  %s.%s where  job_id=%s";
    String SELECT_COUNT_FROM_TABLE = "select count(1) from  %s.%s";
    String TABLE_COMMENT = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME='%s'";
    String SELECT_GATHER_PAGE = "SELECT * from %s.%s limit %s,%s";
    String SELECT_GATHER_BY_ID = "SELECT * from %s.%s where job_id=%s";
    String DELETE_GATHER_BY_ID = "delete from %s.%s where job_id=%s";
    String INSERT_GATHER_DATA = "INSERT INTO %s.%s (jdbc_url,database_name,table_name,user_name,passwd,sync_type,dolphin_project_name,create_hive_table,is_online,crontab) VALUES" +
            "('%s','%s','%s','%s','%s','%s','%s',%s,%s,'%s')";
    String UPDATE_GATHER_DATA = "update %s.%s set jdbc_url = '%s',database_name='%s',table_name='%s',user_name='%s',passwd='%s',sync_type='%s',dolphin_project_name='%s',create_hive_table=%s,is_online=%s,crontab='%s' " +
            "where job_id=%s";
    String UPDATE_GATHER_CREATE_JOB_AND_ON_LINE = "update %s.%s set is_online=%s  where job_id=%s";
    String UPDATE_GATHER_DELETE_JOB_AND_OFF_LINE = "update %s.%s set is_online=%s  where job_id=%s";
    String TABLE_COMMENT_KEY = "TABLE_COMMENT";


    //hive conn info
    String HIVE_DRIVER_NAME = "hiveDriverName";
    String HIVE_URL = "hiveUrl";
    String HIVE_USER = "hiveUser";
    String HIVE_PASSWORD = "hivePassword";

    //stg and ods table info
    String HIVE_STG_TABLE_PREFIX = "hiveStgTableprefix";
    String HIVE_STG_TABLE_LAST_FIX = "hiveStgTablelastfix";
    String HIVE_STG_TABLE_TYPE = "hiveStgTableType";
    String HIVE_STG_TABLE_PATH = "hiveStgTablePath";
    String HIVE_STG_ROW_FORMAT = "hiveStgRowFormat";
    String HIVE_STG_STORE_TYPE = "hiveStgStoreType";
    String HIVE_STG_QUERY_SQL_PARTATION = "hiveStgQuerySqlPartation";
    String HIVE_STG_QUERY_SQL_WHERE_OF_INCRE = "hiveStgQuerySqlWhereOfIncrement";
    String HIVE_STG_COMPRESSION_TYPE = "hiveStgCompressionType";
    String HIVE_STG_ADD_COL = "hiveStgAddcol";
    String HIVE_PARTITION_INFO = "PARTITIONED BY (%s)";
    String HIVE_PARTITION_COL_NAME = "hivePartitioncol";

    String HIVE_ODS_TABLE_PREFIX = "hiveOdsTableprefix";
    String HIVE_ODS_TABLE_LAST_FIX = "hiveOdsTablelastfix";
    String HIVE_ODS_TABLE_TYPE = "hiveOdsTableType";
    String HIVE_ODS_TABLE_PATH = "hiveOdsTablePath";
    String HIVE_ODS_ROW_FORMAT = "hiveOdsRowFormat";
    String HIVE_ODS_STORE_TYPE = "hiveOdsStoreType";
    String HIVE_ODS_COMPRESSION_TYPE = "hiveOdsCompressionType";
    String HIVE_STGSTORE_TEXT_TYPE = "TEXTFILE";
    String HIVE_ODS_ADD_COL = "hiveOdsAddcol";


    //dolphin info
    String TOKEN = "token";
    String CONNECTIONS = "connects";
    String PROCESS_DEFINITION_JSON = "processDefinitionJson";
    String LOCATIONS = "locations";
    String PROJECT_NAME = "projectName";
    String NAME = "name";
    String DESCRIPTION = "description";
    String DATASOURCE_NAME = "$datasourceName";
    String DOLPHIN_SQL_DATA_SOURCENAME = "dolphinSqlDatasourceName";
    String TENANT_ID_NAME = "$tenantId";
    String TENANT_ID = "tenantId";

    String PROCESS_ID = "processId";
    String RELEASE_STATE = "releaseState";
    String RELEASE_STATE_ON_LINE = "1";
    String RELEASE_STATE_OFF_LINE = "0";

    String STG_TO_ODS_SQL_OF_INCRE = "stgToOdsSqlOfIncre";
    String STG_TO_ODS_SQL_OF_SNAPSHOT = "stgToOdsSqlOfSnapshot";
    String STG_TO_ODS_SQL_OF_FULL = "stgToOdsSqlOfFull";
    String STG_TABLE_NAME = "$stgTableName";
    String ODS_TABLE_NAME = "$odsTableName";
    String ODS_COLS = "$odsCols";
    String STG_T_COLS = "$stg_tCols";
    String STG_COLS = "$stgCols";
    String DATAX_JSON = "$dataxJson";
    String STG_TO_ODS_SQL = "$stgToOdsSql";
    String PARTITION_COL_NAME = "$partition_col_name";

    //scheduler
    String SCHEDULE = "schedule";
    String FAILURE_STRATEGY = "failureStrategy";
    String WARNING_TYPE = "warningType";
    String PROCESS_INSTANCE_PRIORITY = "processInstancePriority";
    String WARNING_GROUP_ID = "warningGroupId";
    String RECEIVERS = "receivers";
    String RECEIVERS_CC = "receiversCc";
    String WORKER_GROUP = "workerGroup";
    String PROCESS_DEFINITION_ID = "processDefinitionId";
    String PROCESS_DELETE_ID = "processDefinitionId";

    String SCHEDULER_ID = "id";

    //dolphin url
    String URL = "url";
    String SAVE_URL = "%s/dolphinscheduler/projects/%s/process/save";
    String SEARCH_URL = "%s/dolphinscheduler/projects/%s/process/list-paging?pageSize=10&pageNo=1&searchVal=%s";
    String SEARCH_SCHEDULE_URL = "%s/dolphinscheduler/projects/%s/schedule/list-paging?processDefinitionId=%s&pageSize=10&pageNo=1&searchVal=";
    String SCHEDULER_URL = "%s/dolphinscheduler/projects/%s/schedule/create";
    String SCHEDULER_ON_LINE_URL = "%s/dolphinscheduler/projects/%s/schedule/online";
    String JOB_ON_lINE_URL = "%s/dolphinscheduler/projects/%s/process/release";
    String DOLPHIN_URL = "%s/dolphinscheduler/ui/#/projects/list";
    String JOB_DELETE_URL = "%s/dolphinscheduler/projects/%s/process/delete?processDefinitionId=%s";

    String PROCESS_DEFINITION_NAME = "processDefinitionName";
    String SCHEDULE_CRONTAB = "scheduleCrontab";
    String SCHEDULE_END_TIME = "scheduleEndTime";
    String SCHEDULE_FAILURE_STRATEGY = "scheduleFailureStrategy";
    String SCHEDULE_PROCESS_INSTANCE_PRIORITY = "scheduleProcessInstancePriority";
    String SCHEDULE_RELEASE_STATE = "scheduleReleaseState";
    String SCHEDULE_START_TIME = "scheduleStartTime";
    String SCHEDULE_WARNING_GROUPID = "scheduleWarningGroupId";
    String SCHEDULE_WARNING_TYPE = "scheduleWarningType";
    String SCHEDULE_WORKERGROUP_NAME = "scheduleWorkerGroupName";


    //mysql hive datax data type
    String MYSQL_BIGINT = "bigint";
    String MYSQL_INT = "int";
    String MYSQL_SMALLINT = "smallint";
    String MYSQL_TINYINT = "tinyint";
    String MYSQL_MEDIUMINT = "mediumint";
    String MYSQL_DECIMAL = "decimal";
    String MYSQL_DECIMALDIGITS = "decimaldigits";
    String MYSQL_DOUBLE = "double";
    String MYSQL_FLOAT = "float";
    String MYSQL_BINARY = "binary";
    String MYSQL_VARBINARY = "varbinary";
    String MYSQL_CHAR = "char";
    String MYSQL_VARCHAR = "varchar";
    String MYSQL_MEDIUMTEXT = "mediumtext";
    String MYSQL_TEXT = "text";
    String MYSQL_DATETIME = "datetime";
    String MYSQL_TIME = "time";
    String MYSQL_TIMESTAMP = "timestamp";
    String MYSQL_DATE = "date";
    String MYSQL_JSON = "json";
    String MYSQL_BLOB = "blob";
    String MYSQL_MEDIUMBLOB = "mediumblob";
    String MYSQL_LONGBLOB = "longblob";
    String MYSQL_BIT = "bit";
    String HIVE_BOOLEAN = "boolean";
    String HIVE_TINYINT = "tinyint";
    String HIVE_SMALLINT = "smallint";
    String HIVE_INT = "int";
    String HIVE_BIGINT = "bigint";
    String HIVE_FLOAT = "float";
    String HIVE_DOUBLE = "double";
    String HIVE_DECIMAL = "double";
    String HIVE_DECIMAL_ORIGIN = "decimal";
    String HIVE_STRING = "string";
    String HIVE_VARCHAR = "varchar";
    String HIVE_CHAR = "char";
    String HIVE_BINARY = "string";
    String HIVE_BINARY_ORIGIN = "binary";
    String HIVE_TIMESTAMP = "timestamp";
    String HIVE_DATE = "date";
    String HIVE_ARRAY = "string";
    String HIVE_ARRAY_ORIGIN = "array";
    String HIVE_STRUCT = "string";
    String HIVE_STRUCT_ORIGIN = "struct";
    String HIVE_UNION = "string";
    String HIVE_UNION_ORIGIN = "union";
    String HIVE_MAP = "string";
    String HIVE_MAP_ORIGIN = "map";

    String DATAX_LONG = "long";
    String DATAX_DOUBLE = "double";
    String DATAX_STRING = "string";
    String DATAX_BOOLEAN = "boolean";
    String DATAX_DATE = "date";

}
