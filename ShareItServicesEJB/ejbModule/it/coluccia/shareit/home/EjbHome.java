package it.coluccia.shareit.home;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.common.helper.ServiceHelper;
import it.coluccia.shareit.dao.items.shareitdb.ItemsMapper;
import it.coluccia.shareit.dao.users.shareitdb.UsersMapper;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.dto.items.shareitdb.ItemsExample;
import it.coluccia.shareit.dto.users.shareitdb.Users;
import it.coluccia.shareit.dto.users.shareitdb.UsersExample;

@Stateless
@LocalBean
public class EjbHome implements EjbHomeLocal {
	
	static{
		String log4jConfPath = "C:/workspaces_personali/Workspace_shareIt/ShareIt_Config/log4j/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}
	
	private Logger log = LogManager.getLogger(this.getClass().getCanonicalName());
	
	public List<Items> retrieveAllActiveItems() throws ServiceException{
		log.debug("Service retrieveAllActiveItems started");
		List<Items> result;
		try {
			ServiceHelper.openDataBaseSession();
			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			ItemsExample query = new ItemsExample();
			query.createCriteria().andActiveEqualTo(Short.parseShort("1"));
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
