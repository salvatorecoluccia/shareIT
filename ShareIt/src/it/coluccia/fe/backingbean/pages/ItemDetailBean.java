package it.coluccia.fe.backingbean.pages;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.fe.backingbean.login.LoginBean;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.home.EjbHomeLocal;
import it.coluccia.shareit.pages.ShareItEjbLocal;
import it.common.fe.utils.FacesMessageUtils;

@SessionScoped
@ManagedBean(name = "itemDetailBean")
public class ItemDetailBean extends BaseBean {

	private final String KEY_TITLE_SERVICE_ERROR = "msg.title.error.service";
	private final String KEY_BODY_RETRIEVE_ITEM_ERROR = "msg.body.error.service.retrieveItem";
	private final String KEY_TITLE_BUY_OK  = "msg.title.success.buy";
	private final String KEY_BODY_BUY_OK = "msg.body.success.buy";

	private final String BUNDLE_FILE = "it.coluccia.shareit.resources.PagesResources";
	private final String MAPPED_NAME = "itemDetailBean";
	private final String MAPPED_PAGE = "itemDetail";

	private String itemId;
	private String messageToBuy;

	private Items item;
	
    @ManagedProperty(value="#{loginBean}")
    private LoginBean loginBean;

	@EJB
	private ShareItEjbLocal serviceLocal;

	@PostConstruct
	public void init() {
		logger.debug("ItemDetailBean instanziato con itemId= " + itemId);
		if (itemId == null) {
			logger.debug(
					"itemId è null (probabilmente sei ancora nella pagina home) quindi non eseguo retrieveItemById");
			return;
		}
		try {
			logger.debug("retrieveItem bean start");
			item = serviceLocal.retrieveItemById(itemId);
		} catch (ServiceException e) {
			logger.debug("erore durante retrieveItem");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_RETRIEVE_ITEM_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("retrieveItem bean end");
		}
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
	
	
	public void buyItem(){
		try {
			logger.debug("buyItem start");
			serviceLocal.buyItem(loginBean.getUsername(),loginBean.getPassword(),this.item,messageToBuy);
			FacesMessageUtils.addMessageInfoFromBundle(KEY_TITLE_BUY_OK, KEY_BODY_BUY_OK, getResourceBundle());
		} catch (ServiceException e) {
			logger.debug("erore durante buyItem",e);
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, e.getMsgKey(), getResourceBundle());
		}
		finally{
			logger.debug("buyItem end");
			messageToBuy = "";
		}
	}
	

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Items getItem() {
		return item;
	}

	public void setItem(Items item) {
		this.item = item;
	}

	public String getMessageToBuy() {
		return messageToBuy;
	}

	public void setMessageToBuy(String messageToBuy) {
		this.messageToBuy = messageToBuy;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	
	
	
	

}
