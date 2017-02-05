package it.coluccia.shareit.pages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
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
import it.coluccia.shareit.dao.transactions.shareitdb.TransactionsMapper;
import it.coluccia.shareit.dao.users.shareitdb.UsersMapper;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.dto.items.shareitdb.ItemsExample;
import it.coluccia.shareit.dto.items.shareitdb.ItemsExample.Criteria;
import it.coluccia.shareit.dto.transactions.shareitdb.Transactions;
import it.coluccia.shareit.dto.transactions.shareitdb.TransactionsExample;
import it.coluccia.shareit.dto.users.shareitdb.Users;
import it.coluccia.shareit.dto.users.shareitdb.UsersExample;

@Stateless
@LocalBean
public class ShareItEjb implements ShareItEjbLocal {
	static {
		String log4jConfPath = "C:/workspaces_personali/local_git_repositories/ShareItRep/ShareIt_Config/log4j/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}

	private Logger log = LogManager.getLogger(this.getClass().getCanonicalName());
	private final String MSG_ERROR_BODY_USER_PASS_NOT_FOUND = "msg.error.body.userPassNotFound";
	private final String MSG_ERROR_BODY_BUYER_OWNER_SAME = "msg.error.body.ownerBuyerSame";
	private final String MSG_ERROR_BODY_BUYER_LOW_CREDITS = "msg.error.body.buyerLowCredits";

	private GeoApiContext geoApiContext;

	/**
	 * Default constructor.
	 */
	public ShareItEjb() {
		geoApiContext = new GeoApiContext().setApiKey(CommonConstants.GOOGLE_API_KEY);
		/*
		 * GeocodingResult[] results = GeocodingApi.geocode(context,
		 * "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
		 */
	}

	public GeocodingResult[] geocodeAddress(String addressString) throws ServiceException {
		log.debug("Service geocodeAddress start");
		GeocodingResult[] result = null;
		try {
			GeocodingApiRequest request = GeocodingApi.newRequest(geoApiContext).address(addressString);
			result = request.await();
			// result = GeocodingApi.geocode(geoApiContext,request).await();
			return result;
		} catch (Exception e) {
			log.error("Errore durante geocodeAddress", e);
			throw new ServiceException("Errore durante geocodeAddress", e);
		} finally {
			log.debug("Service geocodeAddress end");
		}
	}

	public GeocodingResult[] geocodeLatLng(BigDecimal lat, BigDecimal lng) throws ServiceException {
		log.debug("Service geocodeLatLng started");
		GeocodingResult[] result;
		try {
			double latDouble = lat.doubleValue();
			double lngDouble = lng.doubleValue();
			/*
			 * if(latDouble == 0 && lngDouble==0){ GeocodingResult res = new
			 * GeocodingResult(); return res; }
			 */
			GeocodingApiRequest request = GeocodingApi.reverseGeocode(geoApiContext, new LatLng(latDouble, lngDouble));
			result = request.await();
			return result;
		} catch (Exception e) {
			log.error("Errore durante geocodeLatLng", e);
			throw new ServiceException("Errore durante geocodeLatLng", e);
		} finally {
			log.debug("Service geocodeLatLng end");
		}
	}

	public Items retrieveItemById(String id) throws ServiceException {
		log.debug("Service retrieveItemById started");
		Items result;
		try {
			ServiceHelper.openDataBaseSession();
			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			result = mapper.selectByPrimaryKey(Integer.parseInt(id));
		} catch (Exception e) {
			log.error("Errore durante retrieveItemById", e);
			throw new ServiceException("Errore durante retrieveItemById", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveItemById end");
		}
		return result;
	}

	public void buyItem(String username, String password, Items item, String message) throws ServiceException {

		log.debug("Service buyItem started");
		Items result;
		try {
			ServiceHelper.openDataBaseSession();
			UsersMapper userMapper = ServiceHelper.getMapper(UsersMapper.class);
			UsersExample query = new UsersExample();
			query.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
			List<Users> tmpResults = userMapper.selectByExample(query);
			if (tmpResults.isEmpty()) {
				log.error("Errore durante buyItem: non esistono utenti con user e password specificati.(" + username
						+ "," + password + ")");
				throw new ServiceException(MSG_ERROR_BODY_USER_PASS_NOT_FOUND, true);
			}
			Users buyer = tmpResults.get(0);
			// controllo che buyer e owner non coincidano
			if (item.getOwnerId().equals(buyer.getId())) {
				log.error("Errore durante buyItem: buyer e owner sono lo stesso utente.(" + username + "," + password
						+ ")");
				throw new ServiceException(MSG_ERROR_BODY_BUYER_OWNER_SAME, true);
			}

			// controllo che utente abbia i crediti disponibili
			if (item.getPriceCredit().compareTo(buyer.getCredit()) > 0) {
				log.error("Errore durante buyItem: buyer non ha abbastanza crediti.(user:" + username + ",pass:"
						+ password + ",crediti_user:" + buyer.getCredit() + ")");
				throw new ServiceException(MSG_ERROR_BODY_BUYER_LOW_CREDITS, true);
			}

			// registro transazione (con messaggio)
			TransactionsMapper mapper = ServiceHelper.getMapper(TransactionsMapper.class);
			Transactions transaction = new Transactions();
			transaction.setIdItem(item.getId());
			transaction.setIdUser(buyer.getId());
			transaction.setMessage(message);
			mapper.insertSelective(transaction);
			log.debug("Transazione registrata correttamente..");

			// setto sellDate e active=false per l'item
			ItemsMapper itemMapper = ServiceHelper.getMapper(ItemsMapper.class);
			item.setSellDate(new Date());
			item.setActive(Short.parseShort("0"));
			itemMapper.updateByPrimaryKeySelective(item);
			log.debug("Item aggiornato correttamente..");

			// aggiorno crediti al buyer e all'owner
			buyer.setCredit(buyer.getCredit().subtract(item.getPriceCredit()));
			userMapper.updateByPrimaryKeySelective(buyer);
			log.debug("Crediti buyer aggiornati correttamente..");

			Users owner = userMapper.selectByPrimaryKey(item.getOwnerId());
			owner.setCredit(buyer.getCredit().add(item.getPriceCredit()));
			userMapper.updateByPrimaryKeySelective(owner);
			log.debug("Crediti owner aggiornati correttamente..");
		} catch (ServiceException e) {
			log.error("Errore durante buyItem, eccezzione gestita", e);
			throw new ServiceException(e.getMsgKey(), true);
		} catch (Exception e) {
			log.error("Errore durante buyItem, eccezzione non gestita", e);
			throw new ServiceException("Errore durante buyItem", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service buyItem end");
		}

	}

	public List<Categories> retrieveCategories() throws ServiceException {
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

	public void publishItem(Items newItem, String username, String password) throws ServiceException {
		log.debug("Service publishItem started");
		Items result;
		Date date = new Date();
		try {
			ServiceHelper.openDataBaseSession();
			UsersMapper userMapper = ServiceHelper.getMapper(UsersMapper.class);
			UsersExample query = new UsersExample();
			query.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
			List<Users> tmpResults = userMapper.selectByExample(query);
			if (tmpResults.isEmpty()) {
				log.error("Errore durante publishItem: non esistono utenti con user e password specificati.(" + username
						+ "," + password + ")");
				throw new ServiceException(MSG_ERROR_BODY_USER_PASS_NOT_FOUND, true);
			}
			Integer ownerId = tmpResults.get(0).getId();

			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			newItem.setActive(Short.parseShort("1"));
			newItem.setCreationDate(date);
			newItem.setPublishDate(date);
			newItem.setOwnerId(ownerId);
			newItem.setLatitude(newItem.getLatitude().setScale(7, RoundingMode.DOWN));
			newItem.setLongitude(newItem.getLongitude().setScale(7, RoundingMode.DOWN));
			mapper.insertSelective(newItem);
		} catch (Exception e) {
			log.error("Errore durante publishItem", e);
			throw new ServiceException("Errore durante publishItem", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service publishItem end");
		}
	}

	private Users selectUtenteByUsername(String username) throws Exception {
		UsersMapper mapper = ServiceHelper.getMapper(UsersMapper.class);
		UsersExample query = new UsersExample();
		query.createCriteria().andUsernameEqualTo(username);
		List<Users> result = mapper.selectByExample(query);
		if (result.isEmpty()) {
			throw new ServiceException("Nessun utente trovato con username uguale a: " + username);
		}
		return result.get(0);
	}

	public Users retrieveCurrentUserObj(String username) throws ServiceException {
		log.debug("Service retrieveCurrentUserObj started");
		Users result;
		try {
			ServiceHelper.openDataBaseSession();
			result = selectUtenteByUsername(username);
		} catch (Exception e) {
			log.error("Errore durante retrieveCurrentUserObj", e);
			throw new ServiceException("Errore durante retrieveCurrentUserObj", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveCurrentUserObj end");
		}
		return result;
	}

	public List<Transactions> retrieveAllUserBuyerTransactions(Integer userId) throws ServiceException {
		log.debug("Service retrieveAllUserBuyerTransactions started");
		try {
			ServiceHelper.openDataBaseSession();
			TransactionsMapper mapper = ServiceHelper.getMapper(TransactionsMapper.class);
			TransactionsExample example = new TransactionsExample();
			example.createCriteria().andIdUserEqualTo(userId);
			return mapper.selectByExample(example);
		} catch (Exception e) {
			log.error("Errore durante retrieveAllUserBuyerTransactions", e);
			throw new ServiceException("Errore durante retrieveAllUserBuyerTransactions", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveAllUserBuyerTransactions end");
		}
	}

	public List<Transactions> retrieveAllUserTransactions(List<Items> items) throws ServiceException {
		log.debug("Service retrieveAllUserBuyerTransactions started");
		try {
			List<Integer> itemsId = new ArrayList<>();
			if (items.isEmpty()) {
				return new ArrayList<Transactions>();
			}
			for (Items item : items) {
				itemsId.add(item.getId());
			}
			ServiceHelper.openDataBaseSession();
			TransactionsMapper mapper = ServiceHelper.getMapper(TransactionsMapper.class);
			TransactionsExample example = new TransactionsExample();
			example.createCriteria().andIdItemIn(itemsId);
			return mapper.selectByExample(example);
		} catch (Exception e) {
			log.error("Errore durante retrieveAllUserBuyerTransactions", e);
			throw new ServiceException("Errore durante retrieveAllUserBuyerTransactions", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveAllUserBuyerTransactions end");
		}
	}

	public List<Items> retrieveAllSellItems() throws ServiceException {

		log.debug("Service retrieveAllActiveItems started");
		List<Items> result;
		try {
			ServiceHelper.openDataBaseSession();
			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			ItemsExample query = new ItemsExample();
			Criteria criteria = query.createCriteria().andSellDateIsNotNull();
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

}
