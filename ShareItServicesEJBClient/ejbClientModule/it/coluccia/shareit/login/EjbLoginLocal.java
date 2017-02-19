package it.coluccia.shareit.login;


import java.util.List;

import javax.ejb.Local;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;

@Local
public interface EjbLoginLocal {
	public boolean validateUsernamePassword(String username, String password) throws ServiceException;
	public void signIn(String username,String password,String firstName,String lastName,String email) throws ServiceException;
	public boolean validateUser(String tokenId) throws ServiceException;
}
