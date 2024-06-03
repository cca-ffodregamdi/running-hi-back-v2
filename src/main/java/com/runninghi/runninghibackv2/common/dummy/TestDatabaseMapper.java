package com.runninghi.runninghibackv2.common.dummy;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestDatabaseMapper {
    void disableForeignKeyChecks();
    List<String> getAllTableList();
    void truncateTable(String tableName);
    void enableForeignKeyChecks();

    void insertPostDummyData();
}
