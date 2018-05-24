package cn.finder.wae.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class BaseJdbc {
	public final static Logger log = Logger.getLogger(BaseJdbc.class);
	Connection conn = null;
	PreparedStatement pstmt = null;
	Statement stmt = null;
	ResultSet rs = null;
	/**
	 * 获取数据连接
	 * 
	 * @return 一个连接
	 */
	public Connection getConnection() {
		try {
			if (conn == null || conn.isClosed()) {
				//驱动已经被加载  不需要此语句
				//Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection("jdbc.properties");
				//conn = getC3POConn();
				//
				 //log.info("*******连接打开了 conn class:"+conn.toString());
			}
			//conncurrentConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
		
		//return  getTestConn();
	}
	
	/**
	 * 根据sql得到该sql的结果集
	 * 
	 * @author
	 * @param 参数
	 *            sql语句
	 * @return 返回该sql的结果集
	 */
	public ResultSet executeQuery(String sql,Object[] params) {
		try {
			//conn = getTestConn();
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		} catch (Exception e) {
			log.info("执行sql语句==" + sql + "==时出错！！");
		}
		return rs;
	}
	
	/**
	 * 
	 * @author 
	 * @param sql 查询的字段类型为字符或数字，暂不支持其它类型
	 * @return 返回List结果集
	 */
	public  List<ArrayList<String>> getTableDataBySql(String sql) {
		List<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.trim());
			rs = pstmt.executeQuery();
			ResultSetMetaData date = rs.getMetaData();
			int colCount = date.getColumnCount();
			while(rs.next()){
				ArrayList<String> row = new ArrayList<String>();
				for(int i=0;i<colCount;i++){
					row.add(rs.getString(i+1));
				}
				all.add(row);
			}
			this.close(rs, pstmt, conn);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return all;
	}

	
	
	
	
	public void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
			//ProxoolConnectionFactory.closeConnection();
			//log.info("close(PreparedStatement pstmt, Connection conn)*******连接关闭了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(PreparedStatement pstmt, Connection conn) {
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
			//ProxoolConnectionFactory.closeConnection();
			//log.info("close(PreparedStatement pstmt, Connection conn)*******连接关闭了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
