package net.moubiecat.bungeeteleportmanager.data.database.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.me.avankziar.general.object.ServerLocation;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ServerLocationTypeHandler implements TypeHandler<ServerLocation> {
    private final Gson gson = new GsonBuilder().create();

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, ServerLocation location, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, gson.toJson(location));
    }

    @Override
    public ServerLocation getResult(ResultSet resultSet, String s) throws SQLException {
        return gson.fromJson(resultSet.getString(s), ServerLocation.class);
    }

    @Override
    public ServerLocation getResult(ResultSet resultSet, int i) throws SQLException {
        return gson.fromJson(resultSet.getString(i), ServerLocation.class);
    }

    @Override
    public ServerLocation getResult(CallableStatement callableStatement, int i) throws SQLException {
        return gson.fromJson(callableStatement.getString(i), ServerLocation.class);
    }
}
