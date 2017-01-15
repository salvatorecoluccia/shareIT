package it.coluccia.shareit.pages;

import java.util.List;

import javax.ejb.Local;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;

@Local
public interface ShareItEjbLocal {
	public Items retrieveItemById(String id) throws ServiceException;
	public void buyItem(String username,String password,Items item, String message)throws ServiceException;
	public List<Categories> retrieveCategories() throws ServiceException;
	public void publishItem(Items newItem, String username, String password) throws ServiceException;
}
