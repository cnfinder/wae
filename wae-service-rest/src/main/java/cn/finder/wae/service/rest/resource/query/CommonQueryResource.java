package cn.finder.wae.service.rest.resource.query;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

public interface CommonQueryResource {
	/**
	 * 通用查询信息
	 * @return Response<TableQueryResult>
	 * 
	 * @throws ResourceException
	 */
	//  /psis/common/query.json?showtableConfigId=2&pageIndex=1&pageSize=10
	@Get
	@Path("/psis/common/query")
	@Produces({"application/json","application/xml"})
	public Representation get() throws ResourceException;
	
	
	@Post
	@Path("/psis/common/query")
	@Produces({"application/json","application/xml"})
	public Representation post(Representation representation) throws ResourceException;
}
