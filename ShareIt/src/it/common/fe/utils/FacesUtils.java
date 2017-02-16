package it.common.fe.utils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * It is an utility class that is used principally from jsf pages
 * @author s.coluccia
 *
 */
public class FacesUtils {

	/**
	 * Return an HttpSession object that represent the current session instance.
	 * If no session instance are presents, it returns null
	 * @return
	 */
	public static HttpSession getSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		return (HttpSession) externalContext.getSession(false);
	}

	/**
	 * Return current HttpServletRequest instance
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	/**
	 * Return a the value of session attribute username
	 * @return
	 */
	public static String getUserName() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session.getAttribute("username").toString();
	}

	/**
	 * It returns current value for the parameter userid in current session.
	 * If ther is no session created, it returns null
	 * @return
	 */
	public static String getUserId() {
		HttpSession session = getSession();
		if (session != null)
			return (String) session.getAttribute("userid");
		else
			return null;
	}
	
	/**
	 * Return current FacesContext object
	 * @return
	 */
	public static FacesContext getFacesContext(){
		return FacesContext.getCurrentInstance();
	}
}
