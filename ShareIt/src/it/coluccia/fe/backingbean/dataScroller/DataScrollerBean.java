package it.coluccia.fe.backingbean.dataScroller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.fe.backingbean.pages.ItemDetailBean;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.home.EjbHomeLocal;
import it.common.fe.utils.FacesMessageUtils;

@ViewScoped
@ManagedBean(name = "dataScrollerBean")
public class DataScrollerBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7104327456634029075L;
	private final String BUNDLE_FILE = "it.coluccia.shareit.resources.DataScrollerResources";
	private final String MAPPED_NAME = "dataScrollerBean";
	private final String MAPPED_PAGE = "home";

	private List<Items> items;
	
	private final String KEY_TITLE_SERVICE_ERROR = "msg.error.service.title";
	private final String KEY_BODY_RETRIEVE_ITEMS_ERROR = "msg.error.service.retrieveAllActiveItems.body";
	
    @ManagedProperty(value="#{itemDetailBean}")
    private ItemDetailBean itemDetailBean;
    
	@EJB
	private EjbHomeLocal serviceLocal;

	@PostConstruct
	public void init() {
		logger.debug("DataScrollerBean instanziato");
		try {
			logger.debug("retrieveAllActiveItems bean start");
			items = serviceLocal.retrieveAllActiveItems();
		} catch (ServiceException e) {
			logger.debug("erore durante retrieveAllActiveItems");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_RETRIEVE_ITEMS_ERROR, getResourceBundle());
		}
		finally{
			logger.debug("retrieveAllActiveItems bean end");
		}
	}

	@Override
	public String getBundleName() {
		return BUNDLE_FILE;
	}
	
	public String goToDetailsPage(String itemId){
		itemDetailBean.setItemId(itemId);
		itemDetailBean.init();
		return itemDetailBean.getMappedPage();
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}
	
	@Override
	public String getMappedName() {
		return MAPPED_NAME;
	}

	public ItemDetailBean getItemDetailBean() {
		return itemDetailBean;
	}

	public void setItemDetailBean(ItemDetailBean itemDetailBean) {
		this.itemDetailBean = itemDetailBean;
	}
	
	@Override
	public String getMappedPage() {
		return MAPPED_PAGE;
	}
	
	
	
	
}
