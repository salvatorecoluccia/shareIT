package it.coluccia.fe.backingbean.dataScroller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.fe.backingbean.pages.ItemDetailBean;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
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
	private List<Categories> categories;
	
	private String titleFilter;
	private String categoryNameFilter;
	private int minCreditsFilter;
	private int maxCreditsFilter;
	
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
			categories = serviceLocal.retrieveCategories();
			titleFilter = null;
			categoryNameFilter = null;
			minCreditsFilter = -1;
			maxCreditsFilter = -1;
			
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
	
	public void doAdvancedSearch(){
		logger.debug("doAdvancedSearch bean start");
		try {
			Integer categoryIdFilter = -1;
			if(StringUtils.isNotBlank(categoryNameFilter)){
				categoryIdFilter = retrieveIdCategoryFromName(categoryNameFilter);
			}
			items = serviceLocal.retrieveFilteredActiveItems(titleFilter,categoryIdFilter,minCreditsFilter,maxCreditsFilter);
			
		} catch (ServiceException e) {
			logger.debug("erore durante retrieveFilteredActiveItems");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_RETRIEVE_ITEMS_ERROR, getResourceBundle());
		}
		finally{
			logger.debug("doAdvancedSearch bean end");
		}
	}
	
	private Integer retrieveIdCategoryFromName(String categoryName){
		if(categories != null){
			for(Categories cat : categories){
				if(cat.getName().contains(categoryName)){
					return cat.getId();
				}
			}
		}
		return -1;
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

	public List<Categories> getCategories() {
		return categories;
	}

	public void setCategories(List<Categories> categories) {
		this.categories = categories;
	}

	public String getTitleFilter() {
		return titleFilter;
	}

	public void setTitleFilter(String titleFilter) {
		this.titleFilter = titleFilter;
	}

	public String getCategoryNameFilter() {
		return categoryNameFilter;
	}

	public void setCategoryNameFilter(String categoryNameFilter) {
		this.categoryNameFilter = categoryNameFilter;
	}

	public int getMinCreditsFilter() {
		return minCreditsFilter;
	}

	public void setMinCreditsFilter(int minCreditsFilter) {
		this.minCreditsFilter = minCreditsFilter;
	}

	public int getMaxCreditsFilter() {
		return maxCreditsFilter;
	}

	public void setMaxCreditsFilter(int maxCreditsFilter) {
		this.maxCreditsFilter = maxCreditsFilter;
	}
	
	
	
	
	
	
}
