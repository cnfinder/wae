package cn.finder.wae.api.sdk.test;
import org.junit.Test;

public class Test01 {
	
	/*@Test
	public void test01(){
		ServiceInterfaceConfig.setContextRootUrl("http://localhost:8080/iv");
		ApiCommonService api = new ApiCommonService();
		User user = api.userLogin("王进喜", "123456");
		
		System.out.println("u:"+user.getName());
		
		
	}

	@Test
	public void testFindFlowTaskVariables(){
		ServiceInterfaceConfig.setContextRootUrl("http://localhost:8080/iv");
		
		ApiCommonService service=new ApiCommonService();
		FindFlowTaskListResponse taskResp=	service.findFlowTasks("config_admin");
	    
	    Task task= taskResp.getEntities().get(0);
	    
	    boolean claim= service.flowTaskClaim("config_admin", task.getId());
	    if(claim){
	        Map<String,Object> variables=service.findFlowTaskVariable("3055099");
	        
	        System.out.println(variables.size());
	        // }
		
	}*/
	
	@Test
	public void testLogin(){
		/*ApiConfig.ServiceInterfaceConfig.setContextRootUrl("http://iv.cwintop.com/iv");
		ApiCommonService service=new ApiCommonService();
		
		User user= service.userLogin("whl-test", "123456");
		
		ObjectWrapper wrapper = new ObjectWrapper();
		cn.finder.wae.api.sdk.test.User u= wrapper.wrapper(cn.finder.wae.api.sdk.test.User.class, null, user);
		
		System.out.println(u);*/
		
	}
}
