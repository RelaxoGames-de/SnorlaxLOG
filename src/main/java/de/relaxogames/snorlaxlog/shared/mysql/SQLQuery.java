package de.snorlaxlog.shared.mysql;

public enum SQLQuery {

        CREATE_MYSQL_USER_CACHE("CREATE TABLE IF NOT EXISTS `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` (\n" +
                "    `user_id` INT AUTO_INCREMENT,\n" +
                "    `user_uuid` VARCHAR(40) NOT NULL,\n" +
                "    `user_name` VARCHAR(16) NOT NULL,\n" +
                "    `user_first_seen` TIMESTAMP NULL,\n" +
                "    `user_last_seen` TIMESTAMP NULL,\n" +
                "    `user_discord_id` VARCHAR(18) NULL,\n" +
                "    `user_forum_id` VARCHAR(18) NULL,\n" +
                "    `user_total_onlinetime` BIGINT,\n" +
                "    `user_language_selected` VARCHAR(10) NULL,\n" +
                "    `user_last_cached_IP` VARCHAR(45) NOT NULL,\n" +
                "    PRIMARY KEY (`user_id`, `user_uuid`, `user_name`, `user_last_cached_IP`),\n" +
                "    UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),\n" +
                "    UNIQUE INDEX `user_uuid_UNIQUE` (`user_uuid` ASC),\n" +
                "    UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC)\n" +
                ");"),

    ADD_ENTRY_TO_DATABASE("INSERT INTO `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` (`user_id`, `user_uuid`, `user_name`, `user_first_seen`, `user_last_seen`, `user_discord_id`, `user_forum_id`, `user_total_onlinetime`, `user_language_selected`, `user_last_cached_IP`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"),
    SELECT_EVERYTHING_WHERE_UUID_A_N("SELECT * FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` WHERE `user_uuid` = ?;"),
    SELECT_EVERYTHING_WHERE_NAME_A_N("SELECT * FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` WHERE `user_name` = ?;"),
    SELECT_USER_UUID_WITH_NAME("SELECT * FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` WHERE `user_name` = ?;"),
    SELECT_USER_WITH_L_NAME("SELECT * FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` WHERE LOWER(`user_name`) = ?;"),
    SELECT_USER_ONLINE_TIME("SELECT `user_total_onlinetime` FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` WHERE `user_uuid` = ? AND `user_name` = ?;"),
    SELECT_ALL_ENTRIES("SELECT * FROM `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%`;"),
    UPDATE_USER_ONLINE_TIME("UPDATE `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` SET `user_total_onlinetime` = ? WHERE `user_uuid` = ? AND `user_name` = ?;"),
    UPDATE_USER_PROFILE("UPDATE `%DATABASE_PATH%`.`%TABLE_NAME_STANDARD%` SET `%ENTRY_DATA%` = ? WHERE `user_uuid` = ?;");

    final String sql;

    SQLQuery(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
