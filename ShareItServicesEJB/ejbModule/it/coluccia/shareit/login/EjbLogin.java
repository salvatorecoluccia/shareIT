package it.coluccia.shareit.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import org.apache.commons.lang3.StringUtils;
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
	
	private final String TOKEN_VERIFICATION_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";

	static {
		String log4jConfPath = "C:/workspaces_personali/local_git_repositories/ShareItRep/ShareIt_Config/log4j/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}

	private Logger log = LogManager.getLogger(this.getClass().getCanonicalName());

	/**
	 * Default constructor.
	 */
	public EjbLogin() {

	}

	@Override
	public boolean validateUser(String tokenId) throws ServiceException {
		try {
			if (StringUtils.isBlank(tokenId)) {
				log.error("TokenID nullo");
				throw new ServiceException("TokenID nullo");
			}
			
			/*TEST WS GOOGLE MAPS*/
			/*GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAN5XiV8NAFbvb5Pq9VafG3ydsD-Ulz9t0");
			GeocodingResult[] results =  GeocodingApi.geocode(context,
			    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
			log.debug("@@@@@@@@@@@@@@@@@@@@@@@---> "+results[0].formattedAddress+" #######RESULT WEB SERVICES CALL");*/
			
			
			
			ServiceHelper.openDataBaseSession();
			
			String url = TOKEN_VERIFICATION_URL+tokenId;

			final String USER_AGENT = "Mozilla/5.0";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			
			if(responseCode == 200){
				checkIfIsInDB(con);
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("Errore durante validateUser", e);
			throw new ServiceException("Errore durante validateUser", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service validateUser end");
		}
	}
	
	private void checkIfIsInDB(HttpURLConnection con) throws Exception{
		
		ObjectMapper objMapper = new ObjectMapper();
		GoogleAuthResponse response = objMapper.readValue(con.getInputStream(), GoogleAuthResponse.class);
		
		UsersMapper mapper = ServiceHelper.getMapper(UsersMapper.class);
		UsersExample query = new UsersExample();
		query.createCriteria().andUsernameEqualTo(response.getGiven_name()+"_"+response.getEmail()).andEmailEqualTo(response.getEmail());
		List<Users> result = mapper.selectByExample(query);
		if (result.isEmpty()) {
			Users newUser = new Users();
			newUser.setActive(Short.parseShort("1"));
			newUser.setAdmin(Short.parseShort("0"));
			newUser.setCredit(BigDecimal.ZERO);
			newUser.setEmail(response.getEmail());
			newUser.setFirstname(response.getGiven_name());
			newUser.setLastname(response.getFamily_name());
			newUser.setPassword("GOOGLE_STORED");
			newUser.setRegDate(new Date());
			newUser.setUsername(response.getGiven_name()+"_"+response.getEmail());
			newUser.setPicture(response.getPicture());
			mapper.insertSelective(newUser);
		}
		/*Se è già in DB non faccio nulla*/

		
	}
	
	

	public boolean validateUsernamePassword(String username, String password) throws ServiceException {
		log.debug("Service validateUseranamePassword started");
		boolean returnValue = false;
		try {
			if (username == null || password == null) {
				log.error("Parametri nulli: USER=" + username + "; PASS:" + password);
				throw new ServiceException("Parametri nulli: USER=" + username + "; PASS:" + password);
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

	public void signIn(String username, String password, String firstName, String lastName, String email)
			throws ServiceException {
		log.debug("Service signIn started");
		try {
			if (username == null || password == null || firstName == null || lastName == null || email == null) {
				log.error("Parametri nulli: USER=" + username + "; PASS:" + password + ";FIRSTNAME:" + firstName
						+ ";LASTNAME:" + lastName + ";EMAIL:" + email);
				throw new ServiceException("Parametri nulli: USER=" + username + "; PASS:" + password + ";FIRSTNAME:"
						+ firstName + ";LASTNAME:" + lastName + ";EMAIL:" + email);
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
