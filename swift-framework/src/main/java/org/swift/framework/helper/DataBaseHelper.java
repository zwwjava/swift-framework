package org.swift.framework.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.util.CollectUtil;
import org.swift.framework.util.PropUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DataBaseHelper {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseHelper.class);

    //private static Connection conn;
    //apache提供的一个工具，通过反射把查询出的数据实体化
    private static final QueryRunner queryRunner;
    //每个线程享有独立的连接
    private static final ThreadLocal<Connection> CONNECTION_REGISTER;
    private static final BasicDataSource DATA_SOURCE;

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        queryRunner = new QueryRunner();
        CONNECTION_REGISTER = new ThreadLocal<Connection>();
        DATA_SOURCE = new BasicDataSource();

        Properties conf = PropUtil.loadProps("swift.properties");
        DRIVER = conf.getProperty("swift.framework.jdbc.driver");
        URL = conf.getProperty("swift.framework.jdbc.url");
        USERNAME = conf.getProperty("swift.framework.jdbc.username");
        PASSWORD = conf.getProperty("swift.framework.jdbc.password");
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);


        try {
            Class.forName(DRIVER);
            logger.info("加载jdbc");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 开启事物
     */
    public static void beginTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                logger.error("开启事物处理失败");
            } finally {
                CONNECTION_REGISTER.set(connection);
            }
        }
    }

    /**
     * 提交事物
     */
    public static void commitTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                logger.error("提交事物处理失败");
            } finally {
                CONNECTION_REGISTER.remove();
            }
        }
    }

    /**
     * 回滚事物
     */
    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                logger.error("回滚事物处理失败");
            } finally {
                CONNECTION_REGISTER.remove();
            }
        }
    }

    /**
     * 连接数据库
     * @return
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_REGISTER.get();
        if(conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
                logger.error("连接数据库成功");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CONNECTION_REGISTER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭连接
     * @param
     */
    /*public static void closeConnction() {
        Connection conn = CONNECTION_REGISTER.get();
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("关闭数据库连接出错:" + e.getMessage());
            } finally {
                CONNECTION_REGISTER.remove();
            }
        }
    }*/

    /**
     * 通过反射把查询出的数据实体化  对应List
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        Connection conn = getConnection();
        try {
            entityList = queryRunner.query(conn,sql,new BeanListHandler<T>(entityClass),params);
            logger.info("查询：" + sql);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("查询异常：" + sql);
        } finally {
            //closeConnction();
        }
        return entityList;
    }
    /**
     * DbUtil提供了很多种类Handler
     * BeanHandler  返回Bean对象
     * BeanListHandler  返回List对象
     * BeanMapHandler  返回Map对象
     * ArrayHandler  返回Object[]对象
     * ArrayListHandler  返回List对象
     * MapHandler  返回Map对象
     * MapListHandler  返回List>对象
     * ScalarHandler  返回某列的值
     * KeyedHandler  返回Map>对象，需要指定列名
     * ColumnListHandler  返回某列的值列表
     */
    /**
     * 通过反射把查询出的数据实体化
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity = null;
        Connection conn = getConnection();
        try {
            entity = queryRunner.query(conn,sql,new BeanHandler<T>(entityClass),params);
            logger.info("查询：" + sql);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("查询异常：" + sql);
        } finally {
            //closeConnction();
        }
        return entity;
    }

    /**
     * 返回List<Map<String, Object>>
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuery( String sql, Object... params) {
        List<Map<String, Object>> list = null;
        Connection conn = getConnection();
        try {
            list = queryRunner.query(conn,sql,new MapListHandler(),params);
            logger.info("查询：" + sql);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("查询异常：" + sql);
        } finally {
            //closeConnction();
        }
        return list;
    }

    /**
     * 通用更新处理
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int row = 0;
        Connection conn = getConnection();
        try {
            row = queryRunner.update(conn,sql,params);
            logger.info("查询：" + sql);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("查询异常：" + sql);
        } finally {
            //closeConnction();
        }
        return row;
    }

    /**
     * 通用添加处理
     * @param
     * @param
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entryClass, Map<String, Object> fieldMap) {
        if(CollectUtil.isEmpty(fieldMap)) {
            logger.error("插入数据为空");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entryClass);
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        for (String column : fieldMap.keySet()) {
            columns.append(column).append(" ,");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + "VALUES " + values;
        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql,params) == 1;
    }

    /**
     * 通用更新处理
     * @param
     * @param
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entryClass, long id, Map<String, Object> fieldMap) {
        if(CollectUtil.isEmpty(fieldMap)) {
            logger.error("更新数据为空");
            return false;
        }

        String sql = "UPDATE " + getTableName(entryClass) + " SET ";
        StringBuffer columns = new StringBuffer();
        for (String column : fieldMap.keySet()) {
            columns.append(column).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(",")) + " WHERE id = ?";
        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql,params) == 1;
    }

    /**
     * 通用删除处理
     * @param
     * @param
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entryClass, long id) {
        String sql = "DELETE FROM " + getTableName(entryClass) + " WHERE ID = ?";
        return executeUpdate(sql,id) == 1;
    }

    private static String getTableName(Class<?> entryClass) {
        return entryClass.getSimpleName();
    }

}
