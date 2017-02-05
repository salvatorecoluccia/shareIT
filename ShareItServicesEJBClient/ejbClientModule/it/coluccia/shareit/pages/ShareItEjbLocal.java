package it.coluccia.shareit.pages;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import com.google.maps.model.GeocodingResult;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.dto.transactions.shareitdb.Transactions;
import it.coluccia.shareit.dto.users.shareitdb.Users;

@Local
public interface ShareItEjbLocal {
	public Items retrieveItemById(String id) throws ServiceException;
	public void buyItem(String username,String password,Items item, String message)throws ServiceException;
	public List<Categories> retrieveCategories() throws ServiceException;
	public void publishItem(Items newItem, String username, String password) throws ServiceException;
	public Users retrieveCurrentUserObj(String username)throws ServiceException;
	public GeocodingResult[] geocodeAddress(String addressString) throws ServiceException;
	public GeocodingResult[] geocodeLatLng(BigDecimal lat, BigDecimal lng) throws ServiceException;
	public List<Transactions> retrieveAllUserBuyerTransactions(Integer userId) throws ServiceException;
	public List<Transactions> retrieveAllUserTransactions(List<Items> items)throws ServiceException;
	public List<Items> retrieveAllSellItems() throws ServiceException;
}
