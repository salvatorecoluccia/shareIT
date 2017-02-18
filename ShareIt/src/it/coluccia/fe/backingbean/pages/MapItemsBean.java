package it.coluccia.fe.backingbean.pages;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.google.maps.model.GeocodingResult;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.home.EjbHomeLocal;
import it.coluccia.shareit.pages.ShareItEjbLocal;
import it.common.fe.utils.FacesMessageUtils;

/**
 * It a jsf page bean that map the page mapsItem
 * @author s.coluccia
 *
 */
@ViewScoped
@ManagedBean(name = "mapsItemBean")
public class MapItemsBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5753930396222928994L;

	@EJB
	private ShareItEjbLocal serviceLocal;

	@EJB
	private EjbHomeLocal serviceLocalHome;
	
	@ManagedProperty(value = "#{itemDetailBean}")
	private ItemDetailBean itemDetailBean;

	private final String BUNDLE_FILE = "it.coluccia.shareit.resources.PagesResources";
	private final String MAPPED_NAME = "mapsItemBean";
	private final String MAPPED_PAGE = "mapsItem";

	private final String KEY_TITLE_SERVICE_ERROR = "msg.error.service.title";
	private final String KEY_BODY_RETRIEVE_ITEMS_ERROR = "msg.error.service.retrieveAllActiveItems.body";

	private MapModel simpleModel;
	private Marker markerItemSelected;
	private Items itemSelected;
	private List<Items> items;
	private Map<Integer, String> itemsAddress;
	private String initialMapCenter;

	/**
	 * It intialize all data of bean after his creation
	 */
	@PostConstruct
	public void init() {
		logger.debug("MapItemsBean instanziato");
		simpleModel = new DefaultMapModel();
		initialMapCenter = "0,0";
		try {
			items = serviceLocalHome.retrieveAllActiveItems(null);
			itemsAddress = generateMapItemsAddress();
			addMarkersToMap();
			if (simpleModel.getMarkers().isEmpty()) {
				initialMapCenter = "45.07031200, 7.68685640";
			} else {
				initialMapCenter = simpleModel.getMarkers().get(0).getLatlng().getLat() + ","
						+ simpleModel.getMarkers().get(0).getLatlng().getLng();
			}
		} catch (ServiceException e) {
			logger.debug("erore durante retrieveAllActiveItems");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_RETRIEVE_ITEMS_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("MapItemsBean init end");
		}

	}
	
	/**
	 * Return a string that is the outcome specified in faces-config.xml that match with detailsPage
	 * @return
	 */
	public String goToDetailsPage() {
		String itemId = ((Items)markerItemSelected.getData()).getId().toString();
		itemDetailBean.setItemId(itemId);
		itemDetailBean.init();
		return itemDetailBean.getMappedPage();
	}

	private void addMarkersToMap() {
		for (Items item : items) {
			BigDecimal roundLat = item.getLatitude().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal roundLng = item.getLongitude().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			if (roundLat.doubleValue() == new BigDecimal(0.00).doubleValue()
					&& roundLng.doubleValue() == new BigDecimal(0.00).doubleValue()) {
				continue;
			}
			simpleModel.addOverlay(new Marker(
					new LatLng(item.getLatitude().setScale(6, BigDecimal.ROUND_HALF_EVEN).doubleValue(),
							item.getLongitude().setScale(6, BigDecimal.ROUND_HALF_EVEN).doubleValue()),
					item.getName(), item));
		}
	}

	private Map<Integer, String> generateMapItemsAddress() throws ServiceException {
		if (this.items == null) {
			throw new ServiceException("generateMapItemsAddress; items è null");
		}
		Map<Integer, String> result = new HashMap<>();
		for (Items item : items) {
			BigDecimal roundLat = item.getLatitude().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal roundLng = item.getLongitude().setScale(2, BigDecimal.ROUND_HALF_EVEN);
			if (roundLat.doubleValue() == new BigDecimal(0.00).doubleValue()
					&& roundLng.doubleValue() == new BigDecimal(0.00).doubleValue()) {
				// 0,0 è il valore di default se non ci sono le coordinate
				logger.debug("Item " + item + " ha coordinate 0,0. Skippo");
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
	 * It is called every time the user click on a marker
	 * @param event
	 */
	public void onMarkerSelect(OverlaySelectEvent event) {
		markerItemSelected = (Marker) event.getOverlay();
	}
	
	/**
	 * It return a string represent the formatted address for the item of marker selected
	 * @return
	 */
	public String getFormattedAddresMarkerSelected(){
		if(markerItemSelected != null){
			Integer id = ((Items)markerItemSelected.getData()).getId();
			return this.itemsAddress.get(id);
		}
		return "";
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

	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public Marker getMarkerItemSelected() {
		return markerItemSelected;
	}

	public void setMarkerItemSelected(Marker markerItemSelected) {
		this.markerItemSelected = markerItemSelected;
	}

	public Items getItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(Items itemSelected) {
		this.itemSelected = itemSelected;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public Map<Integer, String> getItemsAddress() {
		return itemsAddress;
	}

	public void setItemsAddress(Map<Integer, String> itemsAddress) {
		this.itemsAddress = itemsAddress;
	}

	public String getInitialMapCenter() {
		return initialMapCenter;
	}

	public void setInitialMapCenter(String initialMapCenter) {
		this.initialMapCenter = initialMapCenter;
	}

	public ItemDetailBean getItemDetailBean() {
		return itemDetailBean;
	}

	public void setItemDetailBean(ItemDetailBean itemDetailBean) {
		this.itemDetailBean = itemDetailBean;
	}
	
	
	
	
	
	

}
