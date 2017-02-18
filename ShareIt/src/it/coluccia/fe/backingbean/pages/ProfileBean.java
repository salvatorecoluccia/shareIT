package it.coluccia.fe.backingbean.pages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.fe.backingbean.dataScroller.DataScrollerBean;
import it.coluccia.fe.backingbean.login.LoginBean;
import it.coluccia.shareit.dto.users.shareitdb.Users;
import it.coluccia.shareit.pages.ShareItEjbLocal;
import it.common.fe.utils.FacesMessageUtils;

/**
 * Jsf backing bean that map the page of user profile
 * @author s.coluccia
 *
 */
@ViewScoped
@ManagedBean(name = "profileBean")
public class ProfileBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1951774769542028450L;
	private final String KEY_TITLE_INSTANTIATE_ERROR = "msg.title.profile.error.instantiate";
	private final String KEY_BODY_INSTANTIATE_ERROR = "msg.description.profile.error.instantiate";
	
	
	@EJB
	private ShareItEjbLocal serviceLocal;

	private final String BUNDLE_FILE = "it.coluccia.shareit.resources.PagesResources";
	private final String MAPPED_NAME = "profileBean";
	private final String MAPPED_PAGE = "profile";

	private String currentUser;

	private Users user;

	@ManagedProperty(value = "#{dataScrollerBean}")
	private DataScrollerBean dataScrollerBean;

	@ManagedProperty(value = "#{loginBean}")
	private LoginBean loginBean;

	/**
	 * It is called after bean creation and initialize all data used into bean functions
	 */
	@PostConstruct
	public void init() {
		logger.debug("ProfileBean instanziato");
		currentUser = loginBean.getUsername();
		if (StringUtils.isBlank(currentUser)) {
			logger.debug("erore durante retrieveCategories");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_INSTANTIATE_ERROR, KEY_BODY_INSTANTIATE_ERROR,
					getResourceBundle());
			return;
		}
		try {
			user = serviceLocal.retrieveCurrentUserObj(currentUser);
		} catch (ServiceException e) {
			logger.debug("erore durante retrieveItem");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_INSTANTIATE_ERROR, KEY_BODY_INSTANTIATE_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("retrieveItem bean end");
		}
		dataScrollerBean.setCurrentUser(currentUser);
	}

	@Override
	public String getBundleName() {
		return BUNDLE_FILE;
	}

	@Override
	public String getMappedName() {
		return MAPPED_NAME;
	}

	@Override
	public String getMappedPage() {
		return MAPPED_PAGE;
	}

	public DataScrollerBean getDataScrollerBean() {
		return dataScrollerBean;
	}

	public void setDataScrollerBean(DataScrollerBean dataScrollerBean) {
		this.dataScrollerBean = dataScrollerBean;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	

}
