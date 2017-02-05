package it.coluccia.shareit.login;


import javax.ejb.Local;

import it.coluccia.common.exception.ServiceException;

@Local
public interface EjbLoginLocal {
	public boolean validateUsernamePassword(String username, String password) throws ServiceException;
	public void signIn(String username,String password,String firstName,String lastName,String email) throws ServiceException;
	public boolean validateUser(String tokenId) throws ServiceException;
}
