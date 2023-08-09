package net.moubiecat.bungeeteleportmanager.data.database;

import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import org.apache.ibatis.annotations.*;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface HistoryTable {
    String TABLE_NAME = "btmteleporthistory";

    @Update("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (UUID VARCHAR(36) NOT NULL, TIME LONG NOT NULL, SERVER VARCHAR(16) NOT NULL, FROM_LOCATION JSON, TO_LOCATION JSON)")
    void createTable();

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    @Results({
            @Result(column = "UUID", property = "uuid", javaType = UUID.class),
            @Result(column = "TIME", property = "time", javaType = long.class),
            @Result(column = "SERVER", property = "server", javaType = String.class),
            @Result(column = "FROM_LOCATION", property = "fromLocation", javaType = Location.class),
            @Result(column = "TO_LOCATION", property = "toLocation", javaType = Location.class)
    })
    List<HistoryData> selectData(UUID uuid);

    @Insert("INSERT INTO " + TABLE_NAME + " (TIME, UUID, SERVER, FROM_LOCATION, TO_LOCATION) VALUES (#{time}, #{uuid}, #{server}, #{fromLocation}, #{toLocation})")
    void insertData(HistoryData data);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    void deleteData(@Param("uuid") UUID player);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE UUID = #{uuid} AND TIME = #{time}")
    void deleteDataForTime(@Param("uuid") UUID player, @Param("time") long time);
}
