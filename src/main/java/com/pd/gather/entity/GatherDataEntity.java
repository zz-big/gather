package com.pd.gather.entity;


/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */

public class GatherDataEntity {

    private Integer jobId;
    private String jdbcUrl;
    private String databaseName;
    private String tableName;
    private String userName;
    private String passwd;
    private String syncType;
    private String dolphinProjectName;
    private boolean createHiveTable;
    private boolean isOnline;
    private String crontab;

    public GatherDataEntity() {
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getDolphinProjectName() {
        return dolphinProjectName;
    }

    public void setDolphinProjectName(String dolphinProjectName) {
        this.dolphinProjectName = dolphinProjectName;
    }

    public boolean getCreateHiveTable() {
        return createHiveTable;
    }

    public void setCreateHiveTable(boolean createHiveTable) {
        this.createHiveTable = createHiveTable;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean online) {
        isOnline = online;
    }

    public String getCrontab() {
        return crontab;
    }

    public void setCrontab(String crontab) {
        this.crontab = crontab;
    }

    @Override
    public String toString() {
        return "GatherDataEntity{" +
                "jobId=" + jobId +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", userName='" + userName + '\'' +
                ", passwd='" + passwd + '\'' +
                ", syncType='" + syncType + '\'' +
                ", dolphinProjectName='" + dolphinProjectName + '\'' +
                ", createHiveTable=" + createHiveTable +
                ", isOnline=" + isOnline +
                ", crontab='" + crontab + '\'' +
                '}';
    }
}
