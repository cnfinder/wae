package cn.finder.wae.service.rest.resource.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.engine.adapter.HttpRequest;
import org.restlet.engine.adapter.HttpResponse;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import cn.finder.wae.service.rest.common.CommonUtil;
import cn.finder.wae.service.rest.model.Response;
/**
 *@author wu hualong
 */
public abstract class BaseServerResource extends ServerResource {

	private final static Logger logger=Logger.getLogger(BaseServerResource.class);
	
	protected Representation representation=null;
	
	protected Variant variant=new Variant(MediaType.TEXT_HTML);
	
	protected HttpServletRequest httpServletRequest;
	protected HttpRequest httpRequest;
	@Override
	protected void doInit() throws ResourceException {
		// TODO Auto-generated method stub
		//super.doInit();
		//Reference reference=getReference();

		httpRequest=(HttpRequest)this.getRequest();
		HttpResponse httpResponse=(HttpResponse)this.getResponse();
		Reference reference=httpRequest.getResourceRef();
		
		httpServletRequest=CommonUtil.getRequest(httpRequest);
		
		String path=reference.getPath();
		
		logger.debug("=========:path"+path);
		
		String returnFormat="json";
		try {
			returnFormat = path.substring(path.indexOf(".")+1);
			
		} catch (Exception e) {
			logger.error(e.toString());
			Response<Object> respEntity=new Response<Object>();  
			respEntity.getMessage().setStatusCode(Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			httpResponse.setEntity(toRepresentation(respEntity, new Variant(MediaType.APPLICATION_XML)));
			return;
		}
		
		logger.debug("=========format:"+returnFormat);
		if("json".equalsIgnoreCase(returnFormat))
		{
			variant=new Variant(MediaType.APPLICATION_JSON);
		}
		else if("xml".equalsIgnoreCase(returnFormat))
		{
			variant=new Variant(MediaType.APPLICATION_XML);
		}
		else if("html".equalsIgnoreCase(returnFormat))
		{
			variant=new Variant(MediaType.TEXT_HTML);
		}
		else if("jpg".equalsIgnoreCase(returnFormat))
		{
			variant=new Variant(MediaType.IMAGE_JPEG);
		}
		else if("gif".equalsIgnoreCase(returnFormat))
		{
			variant=new Variant(MediaType.IMAGE_GIF);
		}
		/*if(variant!=null)
		{	
			getVariants().add(variant);
		}
		else
			getVariants().add(new Variant(MediaType.TEXT_HTML));//
*/		
	}
	
	public BaseServerResource()
	{
		
		//Reference reference=getRequest().getResourceRef();
		//String path=reference.getPath();
		
		//System.out.println("path:"+path);
		
		//HttpServletRequest hsr = ServletCall.getRequest(getRequest());
		
		//Reference reference=hsr.getResourceRef();
		//Reference reference2=hsr.getOriginalRef();
		//HttpSession session = hsr.getSession();
		
	}
	
	protected void logInfo(String str)
	{
		logger.debug(str);
	}

	@Override
	public <T> T toObject(Representation source, Class<T> target)
			throws ResourceException {
		// TODO Auto-generated method stub
		return super.toObject(source, target);
	}

	public Representation toRepresentation(Object source) {
		// TODO Auto-generated method stub
		return super.toRepresentation(source, variant);
	}
	
	
	
	
}
