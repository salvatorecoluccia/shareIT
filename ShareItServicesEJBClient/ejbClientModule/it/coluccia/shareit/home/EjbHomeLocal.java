package it.coluccia.shareit.home;

import java.util.List;

import javax.ejb.Local;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.shareit.dto.items.shareitdb.Items;

@Local
public interface EjbHomeLocal {
	public List<Items> retrieveAllActiveItems() throws ServiceException;
	
}
