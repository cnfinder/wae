package cn.finder.wae.common.db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 自定义数据库连接池
 * @author wu hualong
 *
 */
public class ConnectionPool {

	private final static Logger logger = Logger.getLogger(ConnectionPool.class);
    private Vector<Connection> pool;

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    /** *//**
     * 连接池的大小，也就是连接池中有多少个数据库连接。
     */
    private int poolSize = 10;

    private static ConnectionPool instance = null;

    /** *//**
     * 私有的构造方法，禁止外部创建本类的对象，要想获得本类的对象，通过<code>getIstance</code>方法。
     * 使用了设计模式中的单例模式。
     */
    private ConnectionPool() {
        init();
    }

    /** *//**
     * 连接池初始化方法，读取属性文件的内容 建立连接池中的初始连接
     */
    private void init() {
        pool = new Vector<Connection>(poolSize);
        readConfig();
        addConnection();
    }

    /** *//**
     * 返回连接到连接池中
     */
    public synchronized void release(Connection conn) {
        pool.add(conn);

    }

    /** *//**
     * 关闭连接池中的所有数据库连接
     */
    public synchronized void closePool() {
        for (int i = 0; i < pool.size(); i++) {
            try {
                ((Connection) pool.get(i)).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            pool.remove(i);
        }
    }

    /** *//**
     * 返回当前连接池的一个对象
     */
    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    /** *//**
     * 返回连接池中的一个数据库连接
     */
    public synchronized Connection getConnection() { 
        if (pool.size() > 0) {
            Connection conn = pool.get(0);
            pool.remove(conn);
            return conn;
        } else {
        	logger.debug("连接池连接不够用了！！");
            return  createConn();
        }
    }

    /** *//**
     * 在连接池中创建初始设置的的数据库连接
     */
    private void addConnection() {
        Connection conn = null;
        for (int i = 0; i < poolSize; i++) {
                conn = createConn();
                pool.add(conn);
        }
    }
    /**
     * 新建一个连接
     *  @param con
     */
    public Connection createConn()
    {
    	 Connection conn = null;
    	 try {
			Class.forName(driverClassName);
			conn = DriverManager.getConnection(url, username, password);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return conn;
    }

    /** *//**
     * 读取设置连接池的属性文件
     */
    private void readConfig() {
        try {
        	//String webrootpath = ServletActionContext.getServletContext().getRealPath("/");
        	String classPath = this.getClass().getClassLoader().getResource("").toURI().getPath();
        	File file = new File(classPath);
            String path = file.getParent() + "/classes/log4j.properties";
            logger.debug("=======********======="+path);
            FileInputStream is = new FileInputStream(path);
            Properties props = new Properties();
            props.load(is);
            this.driverClassName = props.getProperty("log4j.appender.DATABASE.driver");
            this.username = props.getProperty("log4j.appender.DATABASE.user");
            this.password = props.getProperty("log4j.appender.DATABASE.password");
            this.url = props.getProperty("log4j.appender.DATABASE.URL");
            this.poolSize = Integer.parseInt(props.getProperty("log4j.appender.DATABASE.BufferSize"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("读取属性文件出错. ");        
        }
    }
}
