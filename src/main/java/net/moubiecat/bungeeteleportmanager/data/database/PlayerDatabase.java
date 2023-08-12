package net.moubiecat.bungeeteleportmanager.data.database;

import net.moubiecat.bungeeteleportmanager.MouBieCat;
import net.moubiecat.bungeeteleportmanager.data.HistoryData;
import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class PlayerDatabase implements Database {
    private static final PlayerDatabase INSTANCE = new PlayerDatabase();

    /**
     * Private constructor
     */
    PlayerDatabase() {
    }

    @Override
    public void createTable() {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database mapper = sqlSession.getMapper(Database.class);
            mapper.createTable();
            sqlSession.commit();
        }
    }

    @Override
    public List<HistoryData> selectData(UUID uuid) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database mapper = sqlSession.getMapper(Database.class);
            return mapper.selectData(uuid);
        }
    }

    @Override
    public void insertData(HistoryData data) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database dataMapper = sqlSession.getMapper(Database.class);
            dataMapper.insertData(data);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteData(UUID player) {
        try (final SqlSession sqlSession = MouBieCat.getSqlSessionFactory().openSession()) {
            final Database dataMapper = sqlSession.getMapper(Database.class);
            dataMapper.deleteData(player);
            sqlSession.commit();
        }
    }

    /**
     * Get the instance of the database
     *
     * @return the instance of the database
     */
    public static @NotNull PlayerDatabase getInstance() {
        return INSTANCE;
    }
}
