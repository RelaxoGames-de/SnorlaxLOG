package de.snorlaxlog.mysql;

public enum SQLQuery {

    CREATE_MYSQL_USER_CACHE("");

    String sql;

    SQLQuery(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
