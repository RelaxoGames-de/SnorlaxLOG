package de.snorlaxlog.mysql;

public enum SQLQuery {

    CREATE_MYSQL_USER_CACHE("CREATE TABLE `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` (\n" +
            "  `user_uuid` VARCHAR(35) NOT NULL,\n" +
            "  `user_name` VARCHAR(16) NOT NULL,\n" +
            "  `user_first_seen` TIMESTAMP NULL,\n" +
            "  `user_last_seen` TIMESTAMP NULL,\n" +
            "  `user_discord_id` VARCHAR(18) NULL,\n" +
            "  `user_forum_id` VARCHAR(18) NULL,\n" +
            "  `user_total_onlinetime` TIMESTAMP GENERATED ALWAYS AS () VIRTUAL,\n" +
            "  `user_language_selected` VARCHAR(10) NULL,\n" +
            "  `user_last_cached_IP` VARCHAR(45) NOT NULL,\n" +
            "  PRIMARY KEY (`user_uuid`, `user_name`, `user_last_cached_IP`),\n" +
            "  UNIQUE INDEX `user_uuid_UNIQUE` (`user_uuid` ASC) VISIBLE,\n" +
            "  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) VISIBLE);"),

    ADD_ENTRY_TO_DATABASE("INSERT IGNORE INTO `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` (`user_uuid`, `user_name`, `user_first_seen`, `user_last_seen`, `user_total_onlinetime`, `user_language_selected`, `user_last_cached_IP`) VALUES ('1', '2', '3', '4', '7', '8', '9');"),
    SELECT_EVERYTHING_WHERE_UUID_A_N("SELECT * FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` WHERE `user_uuid` = ? AND `user_name` = ?;"),
    SELECT_USER_UUID_WITH_NAME("SELECT `user_uuid` FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` WHERE `user_name` = ?;");

    String sql;

    SQLQuery(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
