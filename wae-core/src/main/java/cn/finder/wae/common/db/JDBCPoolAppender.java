package cn.finder.wae.common.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;

public class JDBCPoolAppender extends JDBCAppender {

	
	
	
	protected int bufferSize = 10;

	public JDBCPoolAppender() {
		super();
	}

	@Override 
    protected String getLogStatement(LoggingEvent event) {  
        String fqnOfCategoryClass = event.fqnOfCategoryClass;  
        Category logger = Logger.getRootLogger();  
        Priority level = event.getLevel();  
        Object message = event.getMessage();  
        Throwable throwable = null;  
        LoggingEventUtil bEvent = new LoggingEventUtil(fqnOfCategoryClass, logger,  
                level, message, throwable);  
        return super.getLogStatement(bEvent);  
    }

	 protected Connection getConnection() throws SQLException {
	     
	      if (connection == null) {
	    	  ConnectionPool pool = ConnectionPool.getInstance();
	    	  connection = pool.getConnection();
	      }
	      return connection;
	  }

	protected void execute(String sql) throws SQLException {
		Connection con = null;
	    Statement stmt = null;
	    try {
	        con = getConnection();
	        stmt = con.createStatement();
	       
	        
	        stmt.executeUpdate(sql);
	    } catch (SQLException e) {
	       if (stmt != null)
		     stmt.close();
	       throw e;
	    }
	    stmt.close();
	    closeConnection(con);
	}
	
	

	  protected void closeConnection(Connection con) {
		  ConnectionPool pool = ConnectionPool.getInstance();
		  pool.release(con);

	  }
}
