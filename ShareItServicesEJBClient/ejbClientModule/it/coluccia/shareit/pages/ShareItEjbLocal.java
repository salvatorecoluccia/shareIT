package it.coluccia.shareit.pages;

import javax.ejb.Local;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.shareit.dto.items.shareitdb.Items;

@Local
public interface ShareItEjbLocal {
	public Items retrieveItemById(String id) throws ServiceException;
	public void buyItem(String username,String password,Items item, String message)throws ServiceException;
}
