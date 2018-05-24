package cn.finder.wae.waepp.server;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import cn.finder.wae.waepp.server.session.SessionManager;
/***
 *  Waepp(Web Arch Engine Presence Protocol)
 * @author WHL
 *
 */

@Deprecated
public class WaeppServer implements org.springframework.context.ApplicationContextAware  {

	 private static final Logger logger = Logger.getLogger(WaeppServer.class);

	    private static WaeppServer instance;

	    private ApplicationContext context;

	    private String version = "1.0.0";

	    private String serverName;

	    private String serverHomeDir;

	    private boolean shuttingDown;

	    /**
	     * Returns the singleton instance of XmppServer.
	     *
	     * @return the server instance.
	     */
	    public static WaeppServer getInstance() {
	        // return instance;
	        if (instance == null) {
	            synchronized (WaeppServer.class) {
	                instance = new WaeppServer();
	            }
	        }
	        return instance;
	    }

	    /**
	     * Constructor. Creates a server and starts it.
	     */
	    public WaeppServer() {
	        if (instance != null) {
	            throw new IllegalStateException("A server is already running");
	        }
	        instance = this;
	        start();
	    }

	    /**
	     * Starts the server using Spring configuration.
	     */
	    public void start() {
	        try {
	            if (isStandAlone()) {
	                Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
	            }

	          //  locateServer();
	            //serverName = Config.getString("xmpp.domain", "127.0.0.1").toLowerCase();
	                    
	            
	            //updated by dragon
	            //context = new ClassPathXmlApplicationContext("spring-config.xml");
	            logger.debug("Spring Configuration loaded.");

	            logger.debug("Wae Server v" + version);

	        } catch (Exception e) {
	            e.printStackTrace();
	            shutdownServer();
	        }
	    }

	    /**
	     * Stops the server.
	     */
	    public void stop() {
	        shutdownServer();
	        Thread shutdownThread = new ShutdownThread();
	        shutdownThread.setDaemon(true);
	        shutdownThread.start();
	    }

	    /**
	     * Returns a Spring bean that has the given bean name.
	     *  
	     * @param beanName
	     * @return a Srping bean 
	     */
	    public Object getBean(String beanName) {
	        return context.getBean(beanName);
	    }

	    /**
	     * Returns the server name.
	     * 
	     * @return the server name
	     */
	    public String getServerName() {
	        return serverName;
	    }

	    /**
	     * Returns true if the server is being shutdown.
	     * 
	     * @return true if the server is being down, false otherwise. 
	     */
	    public boolean isShuttingDown() {
	        return shuttingDown;
	    }

	    /**
	     * Returns if the server is running in standalone mode.
	     * 
	     * @return true if the server is running in standalone mode, false otherwise.
	     */
	    public boolean isStandAlone() {
	        boolean standalone;
	        try {
	            standalone = Class
	                    .forName("org.androidpn.server.starter.ServerStarter") != null;
	        } catch (ClassNotFoundException e) {
	            standalone = false;
	        }
	        return standalone;
	    }

	/*    private void locateServer() throws FileNotFoundException {
	    	Context context = new WebAppContext(contexts, homeDir + File.separator
	                + );
	        String baseDir = "F:/Java���/Ӧ�����/Tomcat 6.0/webapps/ROOT";
	        log.debug("base.dir=" + baseDir);

	        if (serverHomeDir == null) {
	            try {
	                File confDir = new File(baseDir, "conf");
	                if (confDir.exists()) {
	                    serverHomeDir = confDir.getParentFile().getCanonicalPath();
	                }
	            } catch (FileNotFoundException fe) {
	                // Ignore
	            } catch (IOException ie) {
	                // Ignore
	            }
	        }

	        if (serverHomeDir == null) {
	            System.err.println("Could not locate home.");
	            throw new FileNotFoundException();
	        } else {
	            Config.setProperty("server.home.dir", serverHomeDir);
	            log.debug("server.home.dir=" + serverHomeDir);
	        }
	    }*/

	    private void shutdownServer() {
	        shuttingDown = true;
	        // Close all connections
	        SessionManager.getInstance().closeAllSessions();
	        logger.debug("WaeppServer stopped");
	    }

	    private class ShutdownHookThread extends Thread {
	        public void run() {
	            shutdownServer();
	            logger.debug("Server halted");
	            System.err.println("Server halted");
	        }
	    }

	    private class ShutdownThread extends Thread {
	        public void run() {
	            try {
	                Thread.sleep(5000);
	                System.exit(0);
	            } catch (InterruptedException e) {
	                // Ignore
	            }
	        }
	    }

	    /**
	     * added by dragon
	     */
	    public void setApplicationContext(ApplicationContext context)
	    		throws BeansException
		{
			this.context = context;
		}

	
	
	
}
