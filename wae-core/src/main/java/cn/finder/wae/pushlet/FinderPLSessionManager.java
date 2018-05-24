package cn.finder.wae.pushlet;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.Session;
import nl.justobjects.pushlet.core.SessionManager;
import nl.justobjects.pushlet.util.PushletException;

public class FinderPLSessionManager extends SessionManager{

	public Session createSession(Event anEvent) throws PushletException {
		return Session.create(anEvent.getField("userid", "finder_unknown")); 
	}
	
	
	
	
}
