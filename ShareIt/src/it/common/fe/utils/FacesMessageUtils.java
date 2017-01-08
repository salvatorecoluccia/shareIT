package it.common.fe.utils;


import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public class FacesMessageUtils {
	private FacesMessageUtils() {
		
	}
	
	public static void addMessageWarningFromBundle(String keyTitle, String keyMessage, ResourceBundle rb) {
		addMessage(rb.getString(keyTitle), rb.getString(keyMessage), FacesMessage.SEVERITY_WARN);
	}
	
	public static void addMessageWarning(String title, String message) {
		addMessage(title, message, FacesMessage.SEVERITY_WARN);
	}
	
	public static void addMessageInfoFromBundle(String keyTitle, String keyMessage, ResourceBundle rb) {
		addMessage(rb.getString(keyTitle), rb.getString(keyMessage), FacesMessage.SEVERITY_INFO);
	}
	
	public static void addMessageInfo(String title, String message) {
		addMessage(title, message, FacesMessage.SEVERITY_INFO);
	}
	
	public static void addMessageErrorFromBundle(String keyTitle, String keyMessage, ResourceBundle rb) {
		addMessage(rb.getString(keyTitle), rb.getString(keyMessage), FacesMessage.SEVERITY_ERROR);
	}
	
	public static void addMessageError(String title, String message) {
		addMessage(title, message, FacesMessage.SEVERITY_ERROR);
	}
	
	private static void addMessage(String title, String message, Severity severity) {
		FacesMessage msg = new FacesMessage();
		msg.setSummary(title);
		msg.setDetail(message);
		msg.setSeverity(severity);
		FacesUtils.getFacesContext().addMessage(null, msg);
	}
	
}
