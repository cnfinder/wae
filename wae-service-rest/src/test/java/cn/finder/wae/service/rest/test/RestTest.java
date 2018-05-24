package cn.finder.wae.service.rest.test;

import java.io.IOException;

import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class RestTest {

	

		@Test
		public void testCommonQuery() throws IOException
		{
			String actionUrl="http://localhost:8080/PSIS/services/rest/psis/common/query.json";
			ClientResource clientResource=new ClientResource(actionUrl);
			//showtableConfigId=2&pageIndex=1&pageSize=1
			Form form=new Form();
			form.add("showtableConfigId",2+"");
			form.add("pageIndex",1+"");
			form.add("pageSize",1+"");
	        Representation responseRepresentation= clientResource.post(form.getWebRepresentation());
	        String txt=responseRepresentation.getText();
	        System.out.println(responseRepresentation.getMediaType().getName()+" ****** "+txt);
		}
		
		@Test
		public void testCommonQueryGet() throws IOException
		{
			String actionUrl="http://localhost:8080/PSIS/services/rest/psis/common/query.json?showtableConfigId=2&pageIndex=1&pageSize=1";
			ClientResource clientResource=new ClientResource(actionUrl);
			//showtableConfigId=2&pageIndex=1&pageSize=1
	        Representation responseRepresentation= clientResource.get();
	        String txt=responseRepresentation.getText();
	        System.out.println(responseRepresentation.getMediaType().getName()+" ****** "+txt);
		}
}
