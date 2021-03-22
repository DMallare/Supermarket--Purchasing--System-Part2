import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionDao {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static final ProcessBuilder processBuilder = new ProcessBuilder();
    private static final String HOST_NAME = processBuilder.environment().get("HOST");
    private static final String PORT = processBuilder.environment().get("PORT");
    private static final String DATABASE = processBuilder.environment().get("DATABASE");
    private static final String USERNAME = processBuilder.environment().get("USERNAME");
    private static final String PASSWORD = processBuilder.environment().get("PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
        config.setJdbcUrl(url);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        config.setMaximumPoolSize(20);
        ds = new HikariDataSource(config);
    }

    private ConnectionDao() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}