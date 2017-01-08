package it.coluccia.common.context;



public class ServiceContext {

	private UserInterface user;
	
	private String classCaller;
	
	private String sessionId;
	
	private ServiceContext() {
		
	}
	
	public static ServiceContext createServiceContext (UserInterface user, Object objectCaller, String sessionId) {
		ServiceContext context = new ServiceContext();
		context.user = user;
		context.classCaller = objectCaller.getClass().getCanonicalName();
		
		context.sessionId = sessionId;
		return context;
	}

	public String getClassCaller() {
		return classCaller;
	}

	public UserInterface getUser() {
		return user;
	}

	public String getSessionId() {
		return sessionId;
	}


	
}
