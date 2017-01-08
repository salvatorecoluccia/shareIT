package it.coluccia.shareit.login;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.LogManager;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.common.helper.ServiceHelper;
import it.coluccia.shareit.dao.users.shareitdb.UsersMapper;
import it.coluccia.shareit.dto.users.shareitdb.Users;
import it.coluccia.shareit.dto.users.shareitdb.UsersExample;
import it.coluccia.shareit.login.EjbLoginLocal;

/**
 * Session Bean implementation class TestEJb
 */
@Stateless
@LocalBean
public class EjbLogin implements EjbLoginLocal {
	
	static{
		String log4jConfPath = "C:/workspaces_personali/Workspace_shareIt/ShareIt_Config/log4j/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}

	private Logger log = LogManager.getLogger(this.getClass().getCanonicalName());

	/**
	 * Default constructor.
	 */
	public EjbLogin() {

	}


	public boolean validateUsernamePassword(String username,String password) throws ServiceException{
		log.debug("Service validateUseranamePassword started");
		boolean returnValue = false;
		try {
			if(username == null || password == null){
				log.error("Parametri nulli: USER="+username+"; PASS:"+password);
				throw new ServiceException("Parametri nulli: USER="+username+"; PASS:"+password);
			}
			ServiceHelper.openDataBaseSession();
			UsersMapper mapper = ServiceHelper.getMapper(UsersMapper.class);
			UsersExample query = new UsersExample();
			query.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
			List<Users> result = mapper.selectByExample(query);
			if (!result.isEmpty()) {
				returnValue = true;
			}
		} catch (Exception e) {
			log.error("Errore durante validateUsernamPassword", e);
			throw new ServiceException("Errore durante validateUsernamPassword", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service validateUseranamePassword end");
		}
		return returnValue;

	}
	
	public void signIn(String username,String password,String firstName,String lastName,String email) throws ServiceException{
		log.debug("Service signIn started");
		try {
			if(username == null || password == null || firstName == null || lastName == null || email == null){
				log.error("Parametri nulli: USER="+username+"; PASS:"+password +";FIRSTNAME:"+firstName+";LASTNAME:"+lastName+";EMAIL:"+email);
				throw new ServiceException("Parametri nulli: USER="+username+"; PASS:"+password +";FIRSTNAME:"+firstName+";LASTNAME:"+lastName+";EMAIL:"+email);
			}
			ServiceHelper.openDataBaseSession();
			UsersMapper mapper = ServiceHelper.getMapper(UsersMapper.class);
			Users newUser = new Users();
			newUser.setActive(Short.parseShort("1"));
			newUser.setAdmin(Short.parseShort("0"));
			newUser.setEmail(email);
			newUser.setFirstname(firstName);
			newUser.setLastname(lastName);
			newUser.setPassword(password);
			newUser.setRegDate(new Date());
			newUser.setUsername(username);		
			mapper.insertSelective(newUser);
		} catch (Exception e) {
			log.error("Errore durante signIn", e);
			throw new ServiceException("Errore durante signIn", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service signIn end");
		}

	}


}
