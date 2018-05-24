package cn.finder.wae.service.rest.resource.query.impl;

import org.apache.log4j.Logger;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import cn.finder.wae.business.domain.Response;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.facade.rest.PspFacade;
import cn.finder.wae.service.rest.common.RestTemplateExecuter;
import cn.finder.wae.service.rest.factory.BeanFactory;
import cn.finder.wae.service.rest.resource.base.BaseQueryServerResource;
import cn.finder.wae.service.rest.resource.query.CommonQueryResource;

public class CommonQueryResourceImpl extends BaseQueryServerResource implements CommonQueryResource{

	
	private Logger logger  = Logger.getLogger(CommonQueryResourceImpl.class);
	
	@Override
	public Representation get()
			throws ResourceException {
		
		logger.debug(" === CommonQueryResourceImpl.get...");
		
		final Form form=getQuery();
		
		String excludePropertiesString =form.getFirstValue("excludeProperties");
		String includePropertiesString =form.getFirstValue("includeProperties");
		String respString=new RestTemplateExecuter<TableQueryResult>() {
 
			@Override
			protected Response<TableQueryResult> execute() throws Exception {
				
				
				parserRequestParams(form);
				
				long showtableConfigId = Long.parseLong(form.getFirstValue("showtableConfigId"));
				PspFacade psisFacade = BeanFactory.getPspFacade(getRequest());
				
				TableQueryResult queryResult = psisFacade.queryTableQueryResult(showtableConfigId, queryCondition);
				
				resp.setEntity(queryResult);
				resp.setPageIndex(queryResult.getPageIndex());
				resp.setPageSize(queryResult.getPageSize());
				resp.setTotalRecord(queryResult.getCount());
				
				return resp;
			}
		}.responseJsonString(excludePropertiesString, includePropertiesString);
		
		
		
		return toRepresentation(respString);
	}
	
	@Override
	public Representation post(Representation representation) throws ResourceException{
		
		logger.debug(" === CommonQueryResourceImpl.post...");
		
		final Form form=new Form(representation);
		
		
		String excludePropertiesString =form.getFirstValue("excludeProperties");
		String includePropertiesString =form.getFirstValue("includeProperties");
		String respString=new RestTemplateExecuter<TableQueryResult>() {

			@Override
			protected Response<TableQueryResult> execute() throws Exception {
				
				
				parserRequestParams(form);
				
				long showtableConfigId = Long.parseLong(form.getFirstValue("showtableConfigId"));
				PspFacade psisFacade = BeanFactory.getPspFacade(getRequest());
				
				TableQueryResult queryResult = psisFacade.queryTableQueryResult(showtableConfigId, queryCondition);
				
				resp.setEntity(queryResult);
				resp.setPageIndex(queryResult.getPageIndex());
				resp.setPageSize(queryResult.getPageSize());
				resp.setTotalRecord(queryResult.getCount());
				
				return resp;
			}
		}.responseJsonString(excludePropertiesString, includePropertiesString);
		
		
		
		return toRepresentation(respString);
	}

}
