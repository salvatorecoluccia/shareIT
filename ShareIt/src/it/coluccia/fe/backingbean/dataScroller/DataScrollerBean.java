package it.coluccia.fe.backingbean.dataScroller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import com.google.maps.GeoApiContext;
import com.google.maps.model.GeocodingResult;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.fe.backingbean.pages.ItemDetailBean;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.home.EjbHomeLocal;
import it.common.fe.utils.FacesMessageUtils;


/**
 * It is the jsf bean that map the datascroller component
 * @author s.coluccia
 *
 */
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

	private String currentUser;

	private List<Items> items;
	private Map<Integer, String> itemsAddress;
	private List<Categories> categories;

	private String titleFilter;
	private String categoryNameFilter;
	private int minCreditsFilter;
	private int maxCreditsFilter;

	private Items currentItemSelectedForDelete;

	private final String KEY_TITLE_SERVICE_ERROR = "msg.error.service.title";
	private final String KEY_BODY_RETRIEVE_ITEMS_ERROR = "msg.error.service.retrieveAllActiveItems.body";
	private final String KEY_BODY_DELETE_ERROR = "msg.error.service.deleteItem";
	private final String KEY_TITLE_SERVICE_SUCCESS = "msg.success.title.service.deleteItem";
	private final String KEY_BODY_DELETE_SUCCESS = "msg.success.body.service.deleteItem";

	@ManagedProperty(value = "#{itemDetailBean}")
	private ItemDetailBean itemDetailBean;

	@EJB
	private EjbHomeLocal serviceLocal;

	/**
	 * It initializes the bean data
	 */
	@PostConstruct
	public void init() {
		logger.debug("DataScrollerBean instanziato");
		try {
			logger.debug("retrieveAllActiveItems bean start");
			items = serviceLocal.retrieveAllActiveItems(currentUser);
			itemsAddress = generateMapItemsAddress();
			categories = serviceLocal.retrieveCategories();
			titleFilter = null;
			categoryNameFilter = null;
			minCreditsFilter = -1;
			maxCreditsFilter = -1;

		} catch (ServiceException e) {
			logger.debug("erore durante retrieveAllActiveItems");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_RETRIEVE_ITEMS_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("retrieveAllActiveItems bean end");
		}
	}

	@Override
	public String getBundleName() {
		return BUNDLE_FILE;
	}

	private Map<Integer, String> generateMapItemsAddress() throws ServiceException {
		if (this.items == null) {
			throw new ServiceException("generateMapItemsAddress; items è null");
		}
		Map<Integer, String> result = new HashMap<>();
		for (Items item : items) {
			BigDecimal roundLat = item.getLatitude().setScale(2,BigDecimal.ROUND_HALF_EVEN);
			BigDecimal roundLng = item.getLongitude().setScale(2,BigDecimal.ROUND_HALF_EVEN);
			if (roundLat.doubleValue() == new BigDecimal(0.00).doubleValue() && roundLng.doubleValue() == new BigDecimal(0.00).doubleValue()) {
				// 0,0 è il valore di default se non ci sono le coordinate
				logger.debug("Item "+item+" ha coordinate 0,0. Skippo");
				result.put(item.getId(), "n.d.");
			} else {
				String address = geocodeAddress(item);
				result.put(item.getId(), address);
			}
		}
		return result;

	}

	private String geocodeAddress(Items item) throws ServiceException {
		logger.debug("geocodeAddress start");

		GeocodingResult[] result = serviceLocal.geocodeLatLng(item.getLatitude(), item.getLongitude());
		if (result != null && result.length > 0 && result[0] != null) {
			return result[0].formattedAddress;
		} else {
			logger.error("Nessun risultato dal servizio di reversegeolocalizzazione google");
			throw new ServiceException("Nessun risultato dal servizio di reversegeolocalizzazione google");
		}

	}

	/**
	 * Return a string represent the outcome mapped in faces-config.xml
	 * @param itemId
	 * @return
	 */
	public String goToDetailsPage(String itemId) {
		itemDetailBean.setItemId(itemId);
		itemDetailBean.init();
		return itemDetailBean.getMappedPage();
	}

	/**
	 * It execute an advance items search. It uses the filters specified by user 
	 * in the page before this call
	 */
	public void doAdvancedSearch() {
		logger.debug("doAdvancedSearch bean start");
		try {
			Integer categoryIdFilter = -1;
			if (StringUtils.isNotBlank(categoryNameFilter)) {
				categoryIdFilter = retrieveIdCategoryFromName(categoryNameFilter);
			}
			items = serviceLocal.retrieveFilteredActiveItems(titleFilter, categoryIdFilter, minCreditsFilter,
					maxCreditsFilter, currentUser);

		} catch (ServiceException e) {
			logger.debug("erore durante retrieveFilteredActiveItems");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_RETRIEVE_ITEMS_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("doAdvancedSearch bean end");
		}
	}

	/**
	 * Delete an item. It is a logical delete
	 * The item that want to delete must be selected before otherwise the method return immediatelly
	 */
	public void deleteItem() {
		logger.debug("deleteItem bean start");
		if (currentItemSelectedForDelete == null) {
			logger.error("currentItemSelectedForDelete è null! Termino il metodo");
			return;
		}
		try {
			serviceLocal.deleteItem(currentItemSelectedForDelete);
			FacesMessageUtils.addMessageInfoFromBundle(KEY_TITLE_SERVICE_SUCCESS, KEY_BODY_DELETE_SUCCESS,
					getResourceBundle());
			init();
		} catch (ServiceException e) {
			logger.debug("erore durante deleteItem");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_DELETE_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("deleteItem bean end");
		}

	}

	private Integer retrieveIdCategoryFromName(String categoryName) {
		if (categories != null) {
			for (Categories cat : categories) {
				if (cat.getName().contains(categoryName)) {
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

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public Items getCurrentItemSelectedForDelete() {
		return currentItemSelectedForDelete;
	}

	public void setCurrentItemSelectedForDelete(Items currentItemSelectedForDelete) {
		this.currentItemSelectedForDelete = currentItemSelectedForDelete;
	}

	public Map<Integer, String> getItemsAddress() {
		return itemsAddress;
	}

	public void setItemsAddress(Map<Integer, String> itemsAddress) {
		this.itemsAddress = itemsAddress;
	}

}
