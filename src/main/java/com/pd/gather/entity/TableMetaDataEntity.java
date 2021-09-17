package com.pd.gather.entity;


import java.util.List;

/**
 * Description:
 *
 * @author zz
 * @date 2021/9/8
 */
public class TableMetaDataEntity {
    private static final long serialVersionUID = 1L;

    private GatherDataEntity gatherDataEntity;
    private List<List<String>> metaData;
    private String tableComment;

    public TableMetaDataEntity() {
    }

    public GatherDataEntity getGatherDataEntity() {
        return gatherDataEntity;
    }

    public void setGatherDataEntity(GatherDataEntity gatherDataEntity) {
        this.gatherDataEntity = gatherDataEntity;
    }

    public List<List<String>> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<List<String>> metaData) {
        this.metaData = metaData;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    @Override
    public String toString() {
        return "TableMetaDataEntity{" +
                "gatherDataEntity=" + gatherDataEntity.toString() +
                ", metaData=" + metaData +
                ", tableComment='" + tableComment + '\'' +
                '}';
    }
}
