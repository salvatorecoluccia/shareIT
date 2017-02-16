package it.common.fe.utils;


import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

/**
 * It permits to add a jsf front end message from a resurce bundle. It exposes methods to add certain types of jsf messages
 * @author s.coluccia
 *
 */
public class FacesMessageUtils {
	private FacesMessageUtils() {
		
	}
	/**
	 * It add a message to jsf page render that is of warning type and contains a text specified in resource 
	 * bundle rb with title specified by keyTitle and message specified by key keyMessage
	 * @param keyTitle key resource for title
	 * @param keyMessage key resource for message
	 * @param rb resource bundle instance
	 */
	public static void addMessageWarningFromBundle(String keyTitle, String keyMessage, ResourceBundle rb) {
		addMessage(rb.getString(keyTitle), rb.getString(keyMessage), FacesMessage.SEVERITY_WARN);
	}
	
	/**
	 * It add a message to jsf page render that is of warning type and contains a text specified in
	 * message parameter with title specified by title parameter
	 * @param title key resource for title
	 * @param message key resource for message
	 */
	public static void addMessageWarning(String title, String message) {
		addMessage(title, message, FacesMessage.SEVERITY_WARN);
	}
	
	/**
	 * It add a message to jsf page render that is of info type and contains a text specified in resource 
	 * bundle rb with title specified by keyTitle and message specified by key keyMessage
	 * @param keyTitle key resource for title
	 * @param keyMessage key resource for message
	 * @param rb resource bundle instance
	 */
	public static void addMessageInfoFromBundle(String keyTitle, String keyMessage, ResourceBundle rb) {
		addMessage(rb.getString(keyTitle), rb.getString(keyMessage), FacesMessage.SEVERITY_INFO);
	}
	
	/**
	 * It add a message to jsf page render that is of info type and contains a text specified in
	 * message parameter with title specified by title parameter
	 * @param title key resource for title
	 * @param message key resource for message
	 */
	public static void addMessageInfo(String title, String message) {
		addMessage(title, message, FacesMessage.SEVERITY_INFO);
	}
	
	/**
	 * It add a message to jsf page render that is of error type and contains a text specified in resource 
	 * bundle rb with title specified by keyTitle and message specified by key keyMessage
	 * @param keyTitle key resource for title
	 * @param keyMessage key resource for message
	 * @param rb resource bundle instance
	 */
	public static void addMessageErrorFromBundle(String keyTitle, String keyMessage, ResourceBundle rb) {
		addMessage(rb.getString(keyTitle), rb.getString(keyMessage), FacesMessage.SEVERITY_ERROR);
	}
	
	/**
	 * It add a message to jsf page render that is of error type and contains a text specified in
	 * message parameter with title specified by title parameter
	 * @param title key resource for title
	 * @param message key resource for message
	 */
	public static void addMessageError(String title, String message) {
		addMessage(title, message, FacesMessage.SEVERITY_ERROR);
	}
	
	/**
	 * It add a message to jsf page render that is of type specified by severity parameter and contains a
	 * text specified in message parameter with title specified by title parameter
	 * @param title the title of message
	 * @param message the body of message
	 * @param severity type of severity of message
	 */
	private static void addMessage(String title, String message, Severity severity) {
		FacesMessage msg = new FacesMessage();
		msg.setSummary(title);
		msg.setDetail(message);
		msg.setSeverity(severity);
		FacesUtils.getFacesContext().addMessage(null, msg);
	}
	
}
