package com.pd.gather.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */
public class ConnectorUtil {

    private Logger logger = LoggerFactory.getLogger(ConnectorUtil.class);
    private Connection con = null;
    private Statement state = null;
    private ResultSet res = null;
    private DatabaseMetaData metaData = null;

    //TODO
    //连接池


    public ConnectorUtil(String driverName, String url, String user, String password) {

        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, user, password);
            state = con.createStatement();
        } catch (ClassNotFoundException e) {
            logger.error("create conn error! {} ", e.getMessage());
            e.printStackTrace();
        } catch (SQLException throwables) {
            logger.error("create conn error! {} ", throwables.getMessage());
            throwables.printStackTrace();
        }

    }

    public boolean exec(String sql) {
        boolean execute = false;
        try {
            logger.debug("exec sql {}", sql);
            execute = state.execute(sql);
        } catch (SQLException throwables) {
            logger.error("exec sql failed! {}", sql);
            throwables.printStackTrace();
        }
        return execute;
    }

    public int execUpdateOrInsert(String sql) {
        int i = 0;
        try {
            logger.debug("exec update or insert sql {}", sql);
            i = state.executeUpdate(sql);
        } catch (SQLException throwables) {
            logger.error("exec update or insert sql failed! {}", sql);
            throwables.printStackTrace();
        }
        return i;
    }

    public List<HashMap<String, String>> execQuery(String sql) {
        ResultSet resultSet = null;
        ResultSetMetaData resultMetaData = null;
        ArrayList<HashMap<String, String>> reseult = new ArrayList<HashMap<String, String>>();
        logger.info("exec query sql : {}", sql);
        try {
            resultSet = state.executeQuery(sql);
            resultMetaData = resultSet.getMetaData();
            Integer colNum = resultMetaData.getColumnCount();
            while (resultSet.next()) {
                HashMap<String, String> hashMap = new HashMap<>();
                for (int j = 1; j <= colNum; j++) {
                    String columnName = resultMetaData.getColumnName(j);
                    String colValue = resultSet.getString(j);
                    hashMap.put(columnName, colValue);
                }
                reseult.add(hashMap);
            }

        } catch (SQLException throwables) {
            logger.error("exec query sql failed! {}", sql);
            throwables.printStackTrace();
        }


        return reseult;
    }


    public List<List<String>> getMetaData(String db, String tableName) {

        List<List<String>> tableMeta = new ArrayList<>();
        String columnName;
        String columnType;
        String comment;
        logger.info("get meta data {}, {}", db, tableName);
        try {
            metaData = con.getMetaData();
            ResultSet colRet = state.executeQuery(String.format(Constants.SHOW_COLUMNS, db, tableName));
            while (colRet.next()) {
                columnName = colRet.getString("Field");
                columnType = colRet.getString("Type");
                comment = colRet.getString("Comment");
//            System.out.println(columnName + " " + columnType + " " + comment);
                List<String> info = new ArrayList<String>();
                info.add(columnName);
                info.add(columnType);
                info.add(comment);
                tableMeta.add(info);
            }
        } catch (SQLException throwables) {
            logger.error("get meta failed! {}", db + "." + tableName);
            throwables.printStackTrace();
        }

        return tableMeta;
    }


    // 释放资源
    public void destory() throws SQLException {
        if (res != null) {
            state.close();
        }
        if (state != null) {
            state.close();
        }
        if (con != null) {
            con.close();
        }
    }
}
