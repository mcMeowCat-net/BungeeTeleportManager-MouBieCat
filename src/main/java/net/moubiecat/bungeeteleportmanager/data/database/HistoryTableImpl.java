package net.moubiecat.bungeeteleportmanager.data.database;

import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.UUID;

public final class HistoryTableImpl implements HistoryTable {
    @Override
    public void createTable() {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final HistoryTable mapper = sqlSession.getMapper(HistoryTable.class);
            mapper.createTable();
            sqlSession.commit();
        }
    }

    @Override
    public List<HistoryData> selectData(UUID uuid) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final HistoryTable mapper = sqlSession.getMapper(HistoryTable.class);
            return mapper.selectData(uuid);
        }
    }

    @Override
    public void insertData(HistoryData data) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final HistoryTable dataMapper = sqlSession.getMapper(HistoryTable.class);
            dataMapper.insertData(data);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteData(UUID player) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final HistoryTable dataMapper = sqlSession.getMapper(HistoryTable.class);
            dataMapper.deleteData(player);
            sqlSession.commit();
        }
    }
}
