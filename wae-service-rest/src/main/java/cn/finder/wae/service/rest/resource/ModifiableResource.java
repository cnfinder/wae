package cn.finder.wae.service.rest.resource;

import org.restlet.resource.ResourceException;

import cn.finder.wae.service.rest.resource.base.BaseAuthResource;


public abstract class ModifiableResource extends BaseAuthResource {

	protected boolean modifiable;

	
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		
		if(user==null)
		{
			modifiable=false;
		}
		else {
			modifiable=true;
		}
		
	}
	
	public boolean isModifiable() {
		return modifiable;
	}

	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

	public ModifiableResource() {
		super();
	}
	
	
	
}
