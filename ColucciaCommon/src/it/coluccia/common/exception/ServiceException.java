package it.coluccia.common.exception;

/**
 * It is represent an exception that is thrown during a service execution
 * 
 * @author s.coluccia
 *
 */
public class ServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2087478285187228896L;
	
	private String msgKey;

	public ServiceException() {
		super();
	}

	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String msg,boolean isKeyForBundle) {
		super(msg);
		if(isKeyForBundle){
			this.msgKey = msg;
		}
	}
	
	public ServiceException(String msg) {
		this(msg,false);
	}

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
	
	
}
