package com.company.user.messagedriven.strategy.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CanalRow {
    private List<Map<String, Object>> data;
    private String database;
    private Long es;
    private Long id;
    private Boolean isDdl;
    private Map<String, String> mysqlType;
    private List<Map<String, Object>> old;
    private List<String> pkNames;
    private String sql;
    private Map<String, Integer> sqlType;
    private String table;
    private Long ts;
    private String type;
}