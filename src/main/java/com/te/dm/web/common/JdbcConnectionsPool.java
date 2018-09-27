package com.te.dm.web.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @author DM
 */
public class JdbcConnectionsPool {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(JdbcConnectionsPool.class);
    private static final String JDBC_CONF_FILE = "db.properties";
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();
    private static DruidDataSource druidDataSource = null;

    static {
        Properties properties = loadPropertiesFile(JDBC_CONF_FILE);
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            logger.error("[JDBC Exception] --> "
                    + "Failed to configured the Druid DataSource, the exceprion message is:", e);
        }
    }

    public static Connection getConnection() {
        Connection connection = connectionThreadLocal.get();
        try {
            if (null == connection) {
                connection = druidDataSource.getConnection();
                connectionThreadLocal.set(connection);
            }
        } catch (SQLException e) {
            logger.error("[JDBC Exception] --> "
                    + "Failed to create a connection, the exceprion message is:", e);
        }
        return connection;
    }

    public static void closeConnection() {
        Connection connection = connectionThreadLocal.get();
        if (null != connection) {
            try {
                connection.close();
                connectionThreadLocal.remove();
            } catch (SQLException e) {
                logger.error("[JDBC Exception] --> "
                        + "Failed to close the DruidPooledConnection, the exceprion message is:", e);
            }
        }
    }

    public static void startTransaction() {
        Connection conn = connectionThreadLocal.get();

        try {
            if (conn == null) {
                conn = getConnection();
                connectionThreadLocal.set(conn);
            }
            conn.setAutoCommit(false);
        } catch (Exception e) {
            logger.error("[JDBC Exception] --> "
                    + "Failed to start the transaction, the exceprion message is:", e);
        }
    }

    public static void commit() {
        try {
            Connection conn = connectionThreadLocal.get();
            if (null != conn) {
                conn.commit();
            }
        } catch (Exception e) {
            logger.error("[JDBC Exception] --> "
                    + "Failed to commit the transaction, the exceprion message is:", e);
        }
    }

    public static void rollback() {
        try {
            Connection conn = connectionThreadLocal.get();
            if (conn != null) {
                conn.rollback();
                connectionThreadLocal.remove();
            }
        } catch (Exception e) {
            logger.error("[JDBC Exception] --> "
                    + "Failed to rollback the transaction, the exceprion message is:", e);
        }
    }

    private static Properties loadPropertiesFile(String fullFile) {
        if (null == fullFile || fullFile.equals("")) {
            throw new IllegalArgumentException(
                    "Properties file path can not be null" + fullFile);
        }
        Properties prop = new Properties();
        try {
            prop.load(JdbcConnectionsPool.class.getClassLoader().getResourceAsStream(fullFile));
        } catch (IOException e) {
            logger.error("[Properties Exception] --> "
                    + "Can not load jdbc properties, the exceprion message is:", e);
        }
        return prop;
    }
}

