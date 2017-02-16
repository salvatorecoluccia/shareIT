package it.coluccia.shareit.home;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import it.coluccia.common.constants.CommonConstants;
import it.coluccia.common.exception.ServiceException;
import it.coluccia.common.helper.ServiceHelper;
import it.coluccia.shareit.dao.categories.shareitdb.CategoriesMapper;
import it.coluccia.shareit.dao.items.shareitdb.ItemsMapper;
import it.coluccia.shareit.dao.users.shareitdb.UsersMapper;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.dto.items.shareitdb.ItemsExample;
import it.coluccia.shareit.dto.items.shareitdb.ItemsExample.Criteria;
import it.coluccia.shareit.dto.users.shareitdb.Users;
import it.coluccia.shareit.dto.users.shareitdb.UsersExample;

/**
 * It is the ejb layer specified for the homepage functionality
 * @author s.coluccia
 *
 */
@Stateless
@LocalBean
public class EjbHome implements EjbHomeLocal {
	
	static{
		String log4jConfPath = "C:/workspaces_personali/local_git_repositories/ShareItRep/ShareIt_Config/log4j/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}
	
	private GeoApiContext geoApiContext;
	
	/**
	 * It creates an instance of EjbHome. Initialize the GeoApiContext field of class
	 */
	public EjbHome(){
		geoApiContext = new GeoApiContext().setApiKey(CommonConstants.GOOGLE_API_KEY);
	}
	
	private Logger log = LogManager.getLogger(this.getClass().getCanonicalName());
	
	/**
	 * Retrieve from the databese all the categories
	 */
	public List<Categories> retrieveCategories() throws ServiceException{
		log.debug("Service retrieveCategories started");
		List<Categories> result;
		try {
			ServiceHelper.openDataBaseSession();
			CategoriesMapper mapper = ServiceHelper.getMapper(CategoriesMapper.class);
			result = mapper.selectByExample(null);
		} catch (Exception e) {
			log.error("Errore durante retrieveCategories", e);
			throw new ServiceException("Errore durante retrieveCategories", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveCategories end");
		}
		return result;
	}
	
	/**
	 * It retrieves from database all active items.
	 * If currentUser is specified, it retrieves alla active items that have currentUser like owner
	 */
	public List<Items> retrieveAllActiveItems(String currentUser) throws ServiceException{
		log.debug("Service retrieveAllActiveItems started");
		List<Items> result;
		try {
			ServiceHelper.openDataBaseSession();
			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			ItemsExample query = new ItemsExample();
			Criteria criteria = query.createCriteria().andActiveEqualTo(Short.parseShort("1"));
			if(StringUtils.isNotBlank(currentUser)){
				Users utente = selectUtenteByUsername(currentUser);
				criteria.andOwnerIdEqualTo(utente.getId());
			}
			result = mapper.selectByExample(query);
		} catch (Exception e) {
			log.error("Errore durante retrieveAllActiveItems", e);
			throw new ServiceException("Errore durante retrieveAllActiveItems", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveAllActiveItems end");
		}
		return result;

	}
	
	private Users selectUtenteByUsername(String username) throws Exception{
		UsersMapper mapper = ServiceHelper.getMapper(UsersMapper.class);
		UsersExample query = new UsersExample();
		query.createCriteria().andUsernameEqualTo(username);
		List<Users> result = mapper.selectByExample(query);
		if(result.isEmpty()){
			throw new ServiceException("Nessun utente trovato con username uguale a: "+username);
		}
		return result.get(0);
	}
	
	/**
	 * Retrieves all active items that matches with the filters specified.
	 */
	public List<Items> retrieveFilteredActiveItems(String titleFilter,Integer categoryIdFilter,int minCreditsFilter,int maxCreditsFilter,String currentUser) throws ServiceException{
		log.debug("Service retrieveFilteredActiveItems started");
		List<Items> result;
		try {
			ServiceHelper.openDataBaseSession();
			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			ItemsExample query = new ItemsExample();
			Criteria criteria = query.createCriteria().andActiveEqualTo(Short.parseShort("1"));
			if(!StringUtils.isBlank(titleFilter)){
				criteria.andNameLike("%"+titleFilter+"%");
			}
			if(categoryIdFilter != -1){
				criteria.andCategoryCodeEqualTo(categoryIdFilter);
			}
			if(minCreditsFilter > 0){
				criteria.andPriceCreditGreaterThanOrEqualTo(new BigDecimal(minCreditsFilter));
			}
			if(maxCreditsFilter > 0){
				criteria.andPriceCreditLessThanOrEqualTo(new BigDecimal(maxCreditsFilter));
			}
			if(StringUtils.isNotBlank(currentUser)){
				Users utente = selectUtenteByUsername(currentUser);
				criteria.andOwnerIdEqualTo(utente.getId());
			}
			result = mapper.selectByExample(query);
		} catch (Exception e) {
			log.error("Errore durante retrieveFilteredActiveItems", e);
			throw new ServiceException("Errore durante retrieveFilteredActiveItems", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveFilteredActiveItems end");
		}
		return result;
	}
	
	/**
	 * It deletes the item specified by parameter item
	 */
	public void deleteItem(Items item) throws ServiceException{
		log.debug("Service deleteItem started");
		try {
			ServiceHelper.openDataBaseSession();
			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			ItemsExample query = new ItemsExample();
			query.createCriteria().andIdEqualTo(item.getId());
			item.setActive(Short.parseShort("0"));
			mapper.updateByExampleSelective(item, query);
		} catch (Exception e) {
			log.error("Errore durante deleteItem", e);
			throw new ServiceException("Errore durante deleteItem", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service deleteItem end");
		}
	}
	
	/**
	 * It calls the google geocode service to transform a latitude and longitude into an object that maps information
	 * for the specified place.
	 */
	public GeocodingResult[] geocodeLatLng(BigDecimal lat, BigDecimal lng) throws ServiceException{
		log.debug("Service geocodeLatLng started");
		GeocodingResult[] result;
		try {
			double latDouble = lat.doubleValue();
			double lngDouble = lng.doubleValue();
/*			if(latDouble == 0 && lngDouble==0){
				GeocodingResult res = new GeocodingResult();
				return res;
			}*/
			GeocodingApiRequest request = GeocodingApi.reverseGeocode(geoApiContext, new LatLng(latDouble,lngDouble));
			result = request.await();
			return result;
		} catch (Exception e) {
			log.error("Errore durante geocodeLatLng", e);
			throw new ServiceException("Errore durante geocodeLatLng", e);
		} finally {
			log.debug("Service geocodeLatLng end");
		}
	}


}
