package it.coluccia.fe.backingbean.pages;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

import com.google.maps.model.GeocodingResult;

import it.coluccia.common.constants.CommonConstants;
import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.fe.backingbean.home.HomeBean;
import it.coluccia.fe.backingbean.login.LoginBean;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.pages.ShareItEjbLocal;
import it.common.fe.utils.FacesMessageUtils;

/**
 * It is a jsf bean that maps the page addItem
 * @author s.coluccia
 *
 */
@ViewScoped
@ManagedBean(name = "addItemBean")
public class AddItemBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4405637333092455408L;
	private final String CONFIRM_PHASE_ID = "confirmTab";
	private final String LOCATION_PHASE_ID = "locationTab";
	private final String DESCRIPTION_PHASE_ID = "descriptionTab";

	private final String KEY_TITLE_SERVICE_ERROR = "msg.title.error.service";
	private final String KEY_BODY_RETRIEVE_CATEGORIES_ERROR = "msg.body.error.retrieve.categories";
	private final String KEY_BODY_PUBLISH_ERROR = "msg.body.error.publish";
	private final String KEY_BODY_PUBLISH_ERROR_NOT_COMPLETE = "msg.body.error.publish.not.complete";
	private final String KEY_BODY_UPLOAD_ERROR = "msg.body.error.upload";
	private final String KEY_BODY_COMPILE_WARNING = "msg.body.warning.compile.incomplete";
	private final String KEY_TITLE_SERVICE_WARNING = "msg.title.warning.incomplete";
	private final String KEY_TITLE_SERVICE_SUCCESS = "msg.title.service.success";
	private final String KEY_BODY_PUBLISH_SUCCESS = "msg.body.publish.success";
	private final String KEY_BODY_GEOCODING_SERVICE_ERROR = "msg.body.geocoding.error";
	private final String KEY_BODY_GEOCODING_COMPILE_ERROR = "msg.body.geocoding.compile.error";

	private Items newItem;
	private String categoryNameSelected;
	private List<Categories> categories;
	private List<String> categoriesName;
	private UploadedFile imageFile;
	private String selectedLocation;

	@EJB
	private ShareItEjbLocal serviceLocal;

	@ManagedProperty(value = "#{loginBean}")
	private LoginBean loginBean;

	private final String BUNDLE_FILE = "it.coluccia.shareit.resources.PagesResources";
	private final String MAPPED_NAME = "addItemBean";
	private final String MAPPED_PAGE = "addItem";

	/**
	 * Initialize the page data after the bean creation
	 */
	@PostConstruct
	public void init() {
		logger.debug("AddItemBean instanziato");
		try {
			logger.debug("retrieveCategories bean start");
			categories = serviceLocal.retrieveCategories();
			categoriesName = extractCategoriesName(categories);
			this.newItem = new Items();
		} catch (ServiceException e) {
			logger.debug("erore durante retrieveCategories");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_RETRIEVE_CATEGORIES_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("retrieveCategories bean end");
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

	private void uploadImage() {
		logger.debug("uploadImage start");
		if (imageFile != null) {
			try {
				InputStream fileInputStream = imageFile.getInputstream();
				String imageUriSuffix = UUID.randomUUID().toString();
				String extensionFile = imageFile.getFileName();
				File fileDestination = new File(CommonConstants.UPLOAD_IMAGE_PATH + imageFile.getFileName()
						+ imageUriSuffix + "." + extensionFile);
				Files.copy(fileInputStream, fileDestination.toPath());
				this.newItem.setImageUri(fileDestination.getName());
			} catch (Exception e) {
				logger.debug("erore durante uploadImage");
				FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_UPLOAD_ERROR,
						getResourceBundle());
			}
		}
	}

	/**
	 * It call the geocodeService of GoogleServices suite to translate an userInput string into a latitude and longitude coordinates
	 */
	public void geocodeAddress() {
		logger.debug("geocodeAddress start");
		try {
			GeocodingResult[] result = serviceLocal.geocodeAddress(selectedLocation);
			if (result != null && result.length > 0 && result[0] != null) {
				selectedLocation = result[0].formattedAddress;
				newItem.setLatitude(new BigDecimal(result[0].geometry.location.lat));
				newItem.setLongitude(new BigDecimal(result[0].geometry.location.lng));
			} else {
				newItem.setLatitude(null);
				newItem.setLongitude(null);
				logger.error("Nessun risultato dal servizio di geolocalizzazione google");
				FacesMessageUtils.addMessageWarningFromBundle(KEY_TITLE_SERVICE_WARNING,
						KEY_BODY_GEOCODING_COMPILE_ERROR, getResourceBundle());
			}
		} catch (ServiceException e) {
			logger.error("Errore durante geocodeAddress");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_GEOCODING_SERVICE_ERROR,
					getResourceBundle());
		} finally {
			logger.debug("geocodeAddress end");
		}
	}

	/**
	 * It execute the publish of new item. If not all required information are set in the bean, it return and not insert new item
	 */
	public void publish() {
		logger.debug("publish start");

		// prima eseguo upload immagine
		this.uploadImage();

		if (checkIfFieldAreConsistent()) {
			try {
				serviceLocal.publishItem(newItem, loginBean.getUsername(), loginBean.getPassword());
				RequestContext.getCurrentInstance().addCallbackParam("insertSuccess", true);
				FacesMessageUtils.addMessageInfoFromBundle(KEY_TITLE_SERVICE_SUCCESS, KEY_BODY_PUBLISH_SUCCESS,
						getResourceBundle());
			} catch (ServiceException e) {
				logger.error("erore durante publish");
				FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_PUBLISH_ERROR,
						getResourceBundle());
			} finally {
				logger.debug("publish end");
			}
		} else {
			logger.debug("erore durante publish: newItem non è settato correttamente");
			FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_PUBLISH_ERROR_NOT_COMPLETE,
					getResourceBundle());
		}
	}

	/**
	 * Return a string that represent the outcome match specified in faces-config.xml to go to homePage
	 * @return
	 */
	public String goToHome() {
		logger.debug("..redirecting to Home page");
		return HomeBean.getStaticMappedPage();
	}

	private boolean checkIfFieldAreConsistent() {
		if (this.newItem == null) {
			return false;
		}
		if (this.newItem.getCategoryCode() == null || StringUtils.isEmpty(newItem.getDescription())
				|| StringUtils.isEmpty(newItem.getName()) || newItem.getPriceCredit() == null
				|| StringUtils.isEmpty(newItem.getImageUri()) || newItem.getLatitude() == null
				|| newItem.getLongitude() == null) {
			return false;
		}
		return true;
	}

	private List<String> extractCategoriesName(List<Categories> cat) {
		List<String> result = new ArrayList<>();
		if (cat == null) {
			return result;
		}

		for (Categories category : cat) {
			result.add(category.getName());
		}
		return result;
	}

	/**
	 * It is fired when the user upload a new image
	 * @param event
	 */
	public void handleUploadEvent(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		this.setImageFile(event.getFile());
	}

	/**
	 * This is called every time the user move in the additem wizard.
	 * It blocks the navigation if not all required information are set
	 * @param event
	 * @return
	 */
	public String onFlowProcess(FlowEvent event) {
		logger.debug("onFlowProcess start, eventPhase: " + event.getNewStep());
		if (event.getNewStep().equals(CONFIRM_PHASE_ID)) {
			if (this.newItem == null || this.newItem.getLatitude() == null || this.newItem.getLongitude() == null) {
				FacesMessageUtils.addMessageWarningFromBundle(KEY_TITLE_SERVICE_WARNING,
						KEY_BODY_GEOCODING_COMPILE_ERROR, getResourceBundle());
				return event.getOldStep();
			}
		} else if (event.getNewStep().equals(LOCATION_PHASE_ID)) {
			if (this.newItem == null || StringUtils.isBlank(this.newItem.getDescription())) {
				FacesMessageUtils.addMessageWarningFromBundle(KEY_TITLE_SERVICE_WARNING,
						KEY_BODY_COMPILE_WARNING, getResourceBundle());
				return event.getOldStep();
			}
		}else if (event.getNewStep().equals(DESCRIPTION_PHASE_ID)) {
			if (this.newItem == null || StringUtils.isBlank(this.newItem.getName()) || this.newItem.getPriceCredit() == null || this.categoryNameSelected == null || this.imageFile == null) {
				FacesMessageUtils.addMessageWarningFromBundle(KEY_TITLE_SERVICE_WARNING,
						KEY_BODY_COMPILE_WARNING, getResourceBundle());
				return event.getOldStep();
			}
		}
		
		return event.getNewStep();
	}

	public Items getNewItem() {
		return newItem;
	}

	public void setNewItem(Items newItem) {
		this.newItem = newItem;
	}

	public String getCategoryNameSelected() {
		return categoryNameSelected;
	}

	public void setCategoryNameSelected(String categoryNameSelected) {
		this.categoryNameSelected = categoryNameSelected;
		for (Categories cat : categories) {
			if (cat.getName().equalsIgnoreCase(categoryNameSelected)) {
				newItem.setCategoryCode(cat.getId());
			}
		}
	}

	public List<Categories> getCategories() {
		return categories;
	}

	public void setCategories(List<Categories> categories) {
		this.categories = categories;
	}

	public List<String> getCategoriesName() {
		return categoriesName;
	}

	public void setCategoriesName(List<String> categoriesName) {
		this.categoriesName = categoriesName;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public UploadedFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(UploadedFile imageFile) {
		this.imageFile = imageFile;
	}

	public String getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(String selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

}
