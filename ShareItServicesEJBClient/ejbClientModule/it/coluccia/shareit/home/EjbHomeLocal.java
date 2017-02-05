package it.coluccia.shareit.home;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import com.google.maps.model.GeocodingResult;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;

@Local
public interface EjbHomeLocal {
	public List<Items> retrieveAllActiveItems(String currentUser) throws ServiceException;
	public List<Items> retrieveFilteredActiveItems(String titleFilter,Integer categoryIdFilter,int minCreditsFilter,int maxCreditsFilter,String currentUser) throws ServiceException;
	public List<Categories> retrieveCategories() throws ServiceException;
	public void deleteItem(Items item) throws ServiceException;
	public GeocodingResult[] geocodeLatLng(BigDecimal lat, BigDecimal lng) throws ServiceException;
	
}
