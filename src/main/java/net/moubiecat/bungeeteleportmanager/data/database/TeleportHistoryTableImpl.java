package net.moubiecat.bungeeteleportmanager.data.database;

import net.moubiecat.bungeeteleportmanager.MouBieCat;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.UUID;

public final class TeleportHistoryTableImpl implements TeleportHistoryTable {
    @Override
    public List<TeleportHistoryData> selectTeleportInformation(UUID uuid) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final TeleportHistoryTable mapper = sqlSession.getMapper(TeleportHistoryTable.class);
            return mapper.selectTeleportInformation(uuid);
        }
    }

    @Override
    public void insertData(TeleportHistoryData data) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final TeleportHistoryTable dataMapper = sqlSession.getMapper(TeleportHistoryTable.class);
            dataMapper.insertData(data);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteData(UUID player) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final TeleportHistoryTable dataMapper = sqlSession.getMapper(TeleportHistoryTable.class);
            dataMapper.deleteData(player);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteDataForTime(UUID player, long time) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final TeleportHistoryTable dataMapper = sqlSession.getMapper(TeleportHistoryTable.class);
            dataMapper.deleteDataForTime(player, time);
            sqlSession.commit();
        }
    }
}
