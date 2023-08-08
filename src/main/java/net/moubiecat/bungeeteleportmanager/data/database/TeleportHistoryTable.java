package net.moubiecat.bungeeteleportmanager.data.database;

import net.moubiecat.bungeeteleportmanager.data.TeleportHistoryData;
import org.apache.ibatis.annotations.*;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.UUID;

public interface TeleportHistoryTable {
    String TABLE_NAME = "btmteleporthistory";

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    @Results({
            @Result(column = "TIME", property = "time", javaType = long.class),
            @Result(column = "UUID", property = "uuid", javaType = UUID.class),
            @Result(column = "SERVER", property = "server", javaType = String.class),
            @Result(column = "CAUSE", property = "cause", javaType = PlayerTeleportEvent.TeleportCause.class),
            @Result(column = "FROM_LOCATION", property = "fromLocation", javaType = Location.class),
            @Result(column = "TO_LOCATION", property = "toLocation", javaType = Location.class)
    })
    List<TeleportHistoryData> selectTeleportInformation(UUID uuid);

    @Insert("INSERT INTO " + TABLE_NAME + " (TIME, UUID, SERVER, CAUSE, FROM_LOCATION, TO_LOCATION) VALUES (#{time}, #{uuid}, #{server}, #{cause}, #{fromLocation}, #{toLocation})")
    void insertData(TeleportHistoryData data);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE UUID = #{uuid}")
    void deleteData(@Param("uuid") UUID player);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE UUID = #{uuid} AND TIME = #{time}")
    void deleteDataForTime(@Param("uuid") UUID player, @Param("time") long time);
}
