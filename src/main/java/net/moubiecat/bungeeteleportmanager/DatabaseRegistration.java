package net.moubiecat.bungeeteleportmanager;

import net.moubiecat.bungeeteleportmanager.settings.ConnectionYaml;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DatabaseRegistration {
    public static SqlSessionFactory SQL_SESSION_FACTORY = null;

    private final List<Class<?>> mapperList = new ArrayList<>();
    private final Map<Class<?>, Class<?>> typeHandler = new HashMap<>();

    /**
     * 註冊 Mapper
     *
     * @param mapper Mapper
     */
    public void registerMapper(@NotNull Class<?> mapper) {
        this.mapperList.add(mapper);
    }

    /**
     * 註冊 TypeHandler
     *
     * @param typeHandler TypeHandler
     */
    public void registerTypeHandler(@NotNull Class<?> clazz, @NotNull Class<?> typeHandler) {
        this.typeHandler.put(clazz, typeHandler);
    }

    /**
     * 綁定 SQL 會話工廠
     */

    public void buildSqlSessionFactory(@NotNull ConnectionYaml connection) {
        final String host = connection.getHost();
        final int port = connection.getPort();
        final String user = connection.getUser();
        final String password = connection.getPassword();
        final String database = connection.getDatabase();
        final boolean sslEnabled = connection.getSSLEnabled();
        final boolean autoReconnect = connection.getAutoReconnect();
        // 建立資料源
        final DataSource dataSource = new PooledDataSource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + sslEnabled + "&autoReconnect=" + autoReconnect,
                user, password);
        // 建立環境
        final Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
        // 建立配置
        final Configuration configuration = new Configuration(environment);
        // 註冊 TypeHandler
        this.typeHandler.forEach(configuration.getTypeHandlerRegistry()::register);
        // 註冊 Mapper
        this.mapperList.forEach(configuration::addMapper);
        // 建立 SqlSessionFactory
        DatabaseRegistration.SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(configuration);
    }
}
