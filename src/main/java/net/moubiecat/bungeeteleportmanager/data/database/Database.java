package net.moubiecat.bungeeteleportmanager.data.database;

import main.java.me.avankziar.general.object.ServerLocation;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

public interface Database {
    String TABLE_NAME = "btmteleporthistory";

    @Update("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (UUID VARCHAR(36) NOT NULL, TIME LONG NOT NULL, FROM_LOCATION JSON, TO_LOCATION JSON)")
    void createTable();

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    @Results({
            @Result(column = "UUID", property = "uuid", javaType = UUID.class),
            @Result(column = "TIME", property = "time", javaType = long.class),
            @Result(column = "FROM_LOCATION", property = "fromLocation", javaType = ServerLocation.class),
            @Result(column = "TO_LOCATION", property = "toLocation", javaType = ServerLocation.class)
    })
    List<HistoryData> selectData(@Param("uuid") UUID uuid);

    @Insert("INSERT INTO " + TABLE_NAME + " (TIME, UUID, FROM_LOCATION, TO_LOCATION) VALUES (#{time}, #{uuid}, #{fromLocation}, #{toLocation})")
    void insertData(HistoryData data);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    void deleteData(@Param("uuid") UUID player);
}
