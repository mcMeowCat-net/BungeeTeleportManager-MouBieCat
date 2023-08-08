package net.moubiecat.bungeeteleportmanager.data.database.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class LocationTypeHandler implements TypeHandler<Location> {
    private final Gson gson = new GsonBuilder().create();

    @Override
    @SuppressWarnings("ConstantConditions")
    public void setParameter(PreparedStatement ps, int i, Location location, JdbcType jdbcType) throws SQLException {
        // Location 中有 Reference<World>，所以不能直接轉成 JsonObject
        // 這裡我們只需要世界名稱，所以先轉成 JsonObject 再取出世界名稱。
        final JsonObject locationJson = new JsonObject();
        // 添加世界名稱
        locationJson.addProperty("world", location.getWorld().getName());
        // 添加座標
        locationJson.addProperty("x", location.getX());
        locationJson.addProperty("y", location.getY());
        locationJson.addProperty("z", location.getZ());
        // 添加方向
        locationJson.addProperty("yaw", location.getYaw());
        locationJson.addProperty("pitch", location.getPitch());
        ps.setString(i, gson.toJson(locationJson));
    }

    @Override
    public Location getResult(ResultSet resultSet, String s) throws SQLException {
        final JsonObject locationJson = gson.fromJson(resultSet.getString(s), JsonObject.class);
        return new Location(
                // 取出世界
                Bukkit.getWorld(locationJson.get("world").getAsString()),
                // 取出座標
                locationJson.get("x").getAsDouble(),
                locationJson.get("y").getAsDouble(),
                locationJson.get("z").getAsDouble(),
                // 取出方向
                locationJson.get("yaw").getAsFloat(),
                locationJson.get("pitch").getAsFloat());
    }

    @Override
    public Location getResult(ResultSet resultSet, int i) throws SQLException {
        final JsonObject locationJson = gson.fromJson(resultSet.getString(i), JsonObject.class);
        return new Location(
                // 取出世界
                Bukkit.getWorld(locationJson.get("world").getAsString()),
                // 取出座標
                locationJson.get("x").getAsDouble(),
                locationJson.get("y").getAsDouble(),
                locationJson.get("z").getAsDouble(),
                // 取出方向
                locationJson.get("yaw").getAsFloat(),
                locationJson.get("pitch").getAsFloat());
    }

    @Override
    public Location getResult(CallableStatement callableStatement, int i) throws SQLException {
        final JsonObject locationJson = gson.fromJson(callableStatement.getString(i), JsonObject.class);
        return new Location(
                // 取出世界
                Bukkit.getWorld(locationJson.get("world").getAsString()),
                // 取出座標
                locationJson.get("x").getAsDouble(),
                locationJson.get("y").getAsDouble(),
                locationJson.get("z").getAsDouble(),
                // 取出方向
                locationJson.get("yaw").getAsFloat(),
                locationJson.get("pitch").getAsFloat());
    }
}
