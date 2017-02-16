package it.coluccia.fe.backingbean.login;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;


import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.shareit.login.EjbLoginLocal;
import it.common.fe.utils.FacesMessageUtils;
import it.common.fe.utils.FacesUtils;

/**
 * It is a jsfBean that maps the login page. It is sessionScoped to permit the use in all moments of navigation
 * @author s.coluccia
 *
 */
@SessionScoped
@ManagedBean(name = "loginBean")
public class LoginBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -473186166766576662L;

	private final String KEY_TITLE_SERVICE_ERROR = "msg.title.login.service.error";
	private final String KEY_BODY_VALIDATE_USER = "msg.body.login.validate.user";
	private final String KEY_TITLE_LOGIN_WRONG = "msg.title.login.wrong";
	private final String KEY_BODY_LOGIN_WRONG = "msg.body.login.wrong";
	private final String KEY_TITLE_REGISTER_WRONG = "msg.title.register.wrong";
	private final String KEY_BODY_REGISTER_WRONG_FILED_EMPTY = "msg.body.register.wrong.filedsempty";

	private final String BUNDLE_FILE = "it.coluccia.shareit.resources.LoginResources";
	private final String MAPPED_NAME = "loginBean";
	private final String MAPPED_PAGE = "login";

	/* Questi sono i campi per il login */
	private String username;
	private String password;
	private String idToken;

	/*
	 * Questi sono i campi per la registrazione
	 */
	private String usernameRegister;
	private String passwordRegister;
	private String firstNameRegister;
	private String lastNameRegister;
	private String emailRegister;

	private String bundleName = BUNDLE_FILE;

	@EJB
	private EjbLoginLocal serviceLocal;

	@PostConstruct
	public void init() {
		logger.debug("LoginBean instanziato");
	}

	@Override
	public String getBundleName() {
		return bundleName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsernameRegister() {
		return usernameRegister;
	}

	public void setUsernameRegister(String usernameRegister) {
		this.usernameRegister = usernameRegister;
	}

	public String getPasswordRegister() {
		return passwordRegister;
	}

	public void setPasswordRegister(String passwordRegister) {
		this.passwordRegister = passwordRegister;
	}

	public String getFirstNameRegister() {
		return firstNameRegister;
	}

	public void setFirstNameRegister(String firstNameRegister) {
		this.firstNameRegister = firstNameRegister;
	}

	public String getLastNameRegister() {
		return lastNameRegister;
	}

	public void setLastNameRegister(String lastNameRegister) {
		this.lastNameRegister = lastNameRegister;
	}

	public String getEmailRegister() {
		return emailRegister;
	}

	public void setEmailRegister(String emailRegister) {
		this.emailRegister = emailRegister;
	}

	/**
	 * It log in a user if the username and password that the user has inserted match with a record in database
	 * @return
	 */
	public String validateUserPassword() {
		logger.debug("Start validatePassword");
		RequestContext context = RequestContext.getCurrentInstance();
		String returnValue = "";
		boolean result = false;
		try {
			if(StringUtils.isBlank(idToken)){
				result = serviceLocal.validateUsernamePassword(username, password);
			}
			else{
				result = serviceLocal.validateUser(idToken);
			}
		} catch (ServiceException e) {
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_VALIDATE_USER,
					getResourceBundle());
			return "";
		}
		if (result) {
			logger.debug("Utente registrato");
			HttpSession session = FacesUtils.getSession();
			session.setAttribute("username", username);
			returnValue = "home";
		} else {
			logger.debug("Credenziali errate");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_LOGIN_WRONG, KEY_BODY_LOGIN_WRONG,
					getResourceBundle());
			returnValue = "errorUnauthorized";
		}
		context.addCallbackParam("logged", result);
		logger.debug("End validatePassword correctly");
		return returnValue;
	}

	/**
	 * It is used to execute the registration of new user.
	 * TODO: non presente nella demo
	 */
	public void signIn() {
		logger.debug("SignIn bean started");
		try {
			if (StringUtils.isEmpty(usernameRegister) || StringUtils.isEmpty(passwordRegister)
					|| StringUtils.isEmpty(firstNameRegister) || StringUtils.isEmpty(lastNameRegister)
					|| StringUtils.isEmpty(emailRegister)) {
				logger.debug("Alcuni campi obbligatori sono nulli!");
				FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_REGISTER_WRONG,
						KEY_BODY_REGISTER_WRONG_FILED_EMPTY, getResourceBundle());
				return;
			}
			serviceLocal.signIn(usernameRegister, passwordRegister, firstNameRegister, lastNameRegister, emailRegister);
		} catch (Exception e) {
			logger.debug("SignIn bean exception! " + e);

		} finally {
			logger.debug("SignIn bean end");
		}
	}

	/**
	 * It invalidate the current user session and redirect the user to loginPage by implicit jsf navigation
	 * @return
	 */
	public String logout() {
		logger.debug("Start logout");
		HttpSession session = FacesUtils.getSession();
		session.invalidate();
		this.username = null;
		this.password = null;
		logger.debug("End logout");
		return "login";
	}

	@Override
	public String getMappedName() {
		return MAPPED_NAME;
	}

	@Override
	public String getMappedPage() {
		return MAPPED_PAGE;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

}
