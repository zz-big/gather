package com.pd.gather.dao.impl;

import com.pd.gather.entity.GatherDataEntity;
import com.pd.gather.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * description: test <br>
 * date: 2021/8/15 <br>
 * author: zz <br>
 * version: 1.0 <br>
 */
public class DBToHiveData {

    private static Logger logger = LoggerFactory.getLogger(DBToHiveData.class);


    public HashMap<String, String> convertCreateTable(GatherDataEntity gatherDataEntity, List<List<String>> tableInfo, Properties props, String tableComment) {
        String db = gatherDataEntity.getDatabaseName();
        String tableName = gatherDataEntity.getTableName();

        HashMap<String, String> map = new HashMap<String, String>();
        // String stgTableName = props.getProperty(Constants.HIVE_STG_TABLE_PREFIX) + "_" + db + "_" + tableName + "_" + props.getProperty(Constants.HIVE_STG_TABLE_LAST_FIX);
        String stgTableName = db + "_" + tableName + "_" + props.getProperty(Constants.HIVE_STG_TABLE_LAST_FIX);
        // String odsTableName = props.getProperty(Constants.HIVE_ODS_TABLE_PREFIX) + "_" + db + "_" + tableName + "_" + props.getProperty(Constants.HIVE_ODS_TABLE_LAST_FIX);
        String odsTableName = db + "_" + tableName + "_" + props.getProperty(Constants.HIVE_ODS_TABLE_LAST_FIX);
        String convertCreateTableToSTG = convertCreateTableToSTG(stgTableName, tableInfo, gatherDataEntity, props, tableComment);
        String convertCreateTableToOds = convertCreateTableToOds(odsTableName, tableInfo, gatherDataEntity, props, tableComment);

        map.put("stg", convertCreateTableToSTG);
        map.put("ods", convertCreateTableToOds);
        return map;
    }

    private String convertCreateTableToSTG(String tableName, List<List<String>> tableInfo, GatherDataEntity gatherDataEntity, Properties props, String tableComment) {
        Iterator<List<String>> it = tableInfo.iterator();
        String colString = "";

        while (it.hasNext()) {
            List<String> info = it.next();
            String col = info.get(0);
            String col_type = info.get(1);
            String comment = info.get(2);

            String hive_type = "";
            if (col_type.startsWith(Constants.MYSQL_INT) || col_type.startsWith(Constants.MYSQL_SMALLINT) || col_type.startsWith(Constants.MYSQL_MEDIUMINT)) {
                hive_type = Constants.HIVE_INT;
            } else if (col_type.startsWith(Constants.MYSQL_VARCHAR) || col_type.startsWith(Constants.MYSQL_CHAR)) {
                hive_type = Constants.HIVE_STRING;
            } else if (col_type.startsWith(Constants.MYSQL_TINYINT)) {
                hive_type = Constants.HIVE_TINYINT;
            } else if (col_type.startsWith(Constants.MYSQL_DOUBLE)) {
                hive_type = Constants.HIVE_DOUBLE;
            } else if (col_type.startsWith(Constants.MYSQL_DECIMAL)) {
                hive_type = Constants.HIVE_STRING;
            } else if (col_type.startsWith(Constants.MYSQL_FLOAT)) {
                hive_type = Constants.HIVE_FLOAT;
            } else {
                hive_type = Constants.HIVE_STRING;
            }
            if (comment != "") {
                colString += "`" + col + "` " + hive_type + " COMMENT\"" + comment + "\",\n";
            } else {
                colString += "`" + col + "` " + hive_type + ",\n";
            }
        }

        colString += props.getProperty(Constants.HIVE_STG_ADD_COL) + ",\n";

        String external = props.getProperty(Constants.HIVE_STG_TABLE_TYPE);
        String hiveTablePath = props.getProperty(Constants.HIVE_STG_TABLE_PATH);
        String hiveStgRowFormat = props.getProperty(Constants.HIVE_STG_ROW_FORMAT);
        String hiveStgStoreType = props.getProperty(Constants.HIVE_STG_STORE_TYPE);
        String hiveStgCompressionType = props.getProperty(Constants.HIVE_STG_COMPRESSION_TYPE);
        String syncType = gatherDataEntity.getSyncType();
        boolean needPartition = Boolean.valueOf(Constants.FALSE);

        String creataTableString = createTableString(colString, tableName, syncType, external, hiveTablePath, hiveStgRowFormat, hiveStgStoreType, hiveStgCompressionType, tableComment, props, needPartition);

        return creataTableString;
    }

    private String convertCreateTableToOds(String tableName, List<List<String>> tableInfo, GatherDataEntity gatherDataEntity, Properties props, String tableComment) {
        Iterator<List<String>> it = tableInfo.iterator();
        String colString = "";

        while (it.hasNext()) {
            List<String> info = it.next();
            String col = info.get(0);
            String col_type = info.get(1);
            String comment = info.get(2);

            String hive_type = "";
            if (col_type.startsWith(Constants.MYSQL_INT) || col_type.startsWith(Constants.MYSQL_SMALLINT) || col_type.startsWith(Constants.MYSQL_MEDIUMINT)) {
                hive_type = Constants.HIVE_INT;
            } else if (col_type.startsWith(Constants.MYSQL_VARCHAR) || col_type.startsWith(Constants.MYSQL_CHAR)) {
                hive_type = col_type;
            } else if (col_type.startsWith(Constants.MYSQL_TINYINT)) {
                hive_type = Constants.HIVE_TINYINT;
            } else if (col_type.startsWith(Constants.MYSQL_DOUBLE)) {
                hive_type = col_type;
            } else if (col_type.startsWith(Constants.MYSQL_DECIMAL)) {
                hive_type = col_type;
            } else if (col_type.startsWith(Constants.MYSQL_FLOAT)) {
                hive_type = col_type;
            } else {
                hive_type = Constants.HIVE_STRING;
            }
            if (comment != "") {
                colString += "`" + col + "` " + hive_type + " COMMENT\"" + comment + "\",\n";
            } else {
                colString += "`" + col + "` " + hive_type + ",\n";
            }

        }
        colString += props.getProperty(Constants.HIVE_ODS_ADD_COL) + ",\n";

        String external = props.getProperty(Constants.HIVE_ODS_TABLE_TYPE);
        String hiveOdsTablePath = props.getProperty(Constants.HIVE_ODS_TABLE_PATH);
        String hiveOdsRowFormat = props.getProperty(Constants.HIVE_ODS_ROW_FORMAT);
        String hiveOdsStoreType = props.getProperty(Constants.HIVE_ODS_STORE_TYPE);
        String hiveOdsCompressionType = props.getProperty(Constants.HIVE_ODS_COMPRESSION_TYPE);
        String syncType = gatherDataEntity.getSyncType();

        boolean needPartition = Boolean.valueOf(Constants.TRUE);
        String creataTableString = createTableString(colString, tableName, syncType, external, hiveOdsTablePath, hiveOdsRowFormat, hiveOdsStoreType, hiveOdsCompressionType, tableComment, props, needPartition);


        return creataTableString;
    }


    private String createTableString(String colString, String tableName, String syncType, String external, String hiveTablePath, String hiveRowFormat, String hiveStoreType, String hiveCompressionType, String tableComment, Properties props, boolean needPartition) {
        String externalString = "";
        String location = "";
        String db = "";
        String creataTableString = "";
        String partationInfo = "";

        if (hiveTablePath.endsWith("/")) {
            db = hiveTablePath.substring(hiveTablePath.lastIndexOf("/") + 1, hiveTablePath.length());
        } else {
            db = hiveTablePath.substring(hiveTablePath.lastIndexOf("/") + 1);
        }
        if (db.contains(".")) {
            db = db.substring(0, db.indexOf("."));
        }


        if (external.equalsIgnoreCase(Constants.HIVE_EXTERNAL)) {
            externalString = Constants.HIVE_EXTERNAL;
            location = "LOCATION " + hiveTablePath;
        }

        if (!hiveStoreType.equalsIgnoreCase(Constants.HIVE_STGSTORE_TEXT_TYPE)) {
            hiveStoreType += String.format("\n tblproperties " + " (\"%s.compress\"=\"%s\")", hiveStoreType.toLowerCase(), hiveCompressionType);
        }
        switch (syncType) {
            case Constants.SYNC_TYPE_FULL:
                partationInfo = "";
                break;
            case Constants.SYNC_TYPE_SNAPSHOT:
            case Constants.SYNC_TYPE_INCRE:
                if (needPartition) {
                    partationInfo = String.format(Constants.HIVE_PARTITION_INFO, props.getProperty(Constants.HIVE_PARTITION_COL_NAME));
                }
                break;
            default:
                partationInfo = "";
                break;
        }


        if (tableComment == "") {
            tableComment = "COMMENT \"" + tableComment + "\"";
        } else {
            tableComment = "";
        }

        creataTableString = String.format("create %s table  IF NOT EXISTS %s.%s (\n%s\n) %s\n%s\n" +
                        "ROW FORMAT DELIMITED FIELDS TERMINATED BY %s \n" +
                        "STORED AS %s\n%s",
                externalString, db, tableName,
                colString.substring(0, colString.lastIndexOf(",")), tableComment, partationInfo, hiveRowFormat, hiveStoreType, location);

        return creataTableString;

    }


    public String writeDataXJson(Properties props, String stg, String jsonFileTemple, GatherDataEntity gatherDataEntity) {

        BufferedReader bufferedReader = null;
        StringBuilder jsonStringBuilder = new StringBuilder();
        String tableName = gatherDataEntity.getTableName();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(jsonFileTemple)), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String db = gatherDataEntity.getDatabaseName();
        String json = jsonStringBuilder.toString();
        json = json.replace("$username", gatherDataEntity.getUserName());
        json = json.replace("$password", gatherDataEntity.getPasswd());
        json = json.replace("$db", db);
        json = json.replace("$table", tableName);
        json = json.replace("$jdbcUrl", gatherDataEntity.getJdbcUrl());
        String fileType = props.getProperty(Constants.HIVE_STG_STORE_TYPE);
        if (fileType.toLowerCase().contains("text")) {
            fileType = "text";
        }
        json = json.replace("$fileType", fileType);

        String fileName = db + "_" + tableName + "_" + props.getProperty(Constants.HIVE_STG_TABLE_LAST_FIX);
        json = json.replace("$fileName", fileName);

        json = json.replace("$path", props.getProperty(Constants.HIVE_STG_TABLE_PATH) + "/" + fileName);

        String column = "";

        String[] cols = stg.substring(stg.indexOf("(\n") + 2, stg.indexOf("\n)")).split(",\n");
        String mysqlCol = "";

        for (String col : cols) {
            String[] colInfos = col.split(" ");
            String colName = colInfos[0].replaceAll("`", "");
            String colType = colInfos[1];
            column += "              {\n               \"name\": \"" + colName + "\",\n" +
                    "               \"type\": \"" + colType + "\"\n              },\n";
            mysqlCol += colInfos[0] + ", ";

        }

        mysqlCol = mysqlCol.substring(0, mysqlCol.lastIndexOf(","));

        json = json.replace("\"$column\"", column.substring(0, column.lastIndexOf(",")) + "\n");

        json = json.replace("$compress", props.getProperty(Constants.HIVE_STG_COMPRESSION_TYPE));

        String addCol = props.getProperty(Constants.HIVE_STG_ADD_COL).split(" ")[0];

        json = json.replace("$col", mysqlCol.replace(addCol, props.getProperty(Constants.HIVE_STG_QUERY_SQL_PARTATION)));

        switch (gatherDataEntity.getSyncType()) {
            case Constants.SYNC_TYPE_FULL:
            case Constants.SYNC_TYPE_SNAPSHOT:
                json = json.replace("$where", " where 1 = 1 ");
                break;
            case Constants.SYNC_TYPE_INCRE:
                json = json.replace("$where", props.getProperty(Constants.HIVE_STG_QUERY_SQL_WHERE_OF_INCRE));
                break;
            default:
                json = json.replace("$where", " where 1 = 1 ");
                break;
        }

        return json;

    }


}
