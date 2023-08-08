package net.moubiecat.bungeeteleportmanager.data.database.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class TeleportCauseTypeHandler implements TypeHandler<PlayerTeleportEvent.TeleportCause> {
    @Override
    public void setParameter(PreparedStatement ps, int i, PlayerTeleportEvent.TeleportCause parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public PlayerTeleportEvent.TeleportCause getResult(ResultSet rs, String columnName) throws SQLException {
        return PlayerTeleportEvent.TeleportCause.valueOf(rs.getString(columnName));
    }

    @Override
    public PlayerTeleportEvent.TeleportCause getResult(ResultSet rs, int columnIndex) throws SQLException {
        return PlayerTeleportEvent.TeleportCause.valueOf(rs.getString(columnIndex));
    }

    @Override
    public PlayerTeleportEvent.TeleportCause getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return PlayerTeleportEvent.TeleportCause.valueOf(cs.getString(columnIndex));
    }
}