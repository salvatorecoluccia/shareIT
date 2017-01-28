package it.coluccia.shareit.home;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.common.helper.ServiceHelper;
import it.coluccia.shareit.dao.categories.shareitdb.CategoriesMapper;
import it.coluccia.shareit.dao.items.shareitdb.ItemsMapper;
import it.coluccia.shareit.dao.users.shareitdb.UsersMapper;
import it.coluccia.shareit.dto.categories.shareitdb.Categories;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.dto.items.shareitdb.ItemsExample;
import it.coluccia.shareit.dto.items.shareitdb.ItemsExample.Criteria;
import it.coluccia.shareit.dto.users.shareitdb.Users;
import it.coluccia.shareit.dto.users.shareitdb.UsersExample;

@Stateless
@LocalBean
public class EjbHome implements EjbHomeLocal {
	
	static{
		String log4jConfPath = "C:/workspaces_personali/local_git_repositories/ShareItRep/ShareIt_Config/log4j/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}
	
	private Logger log = LogManager.getLogger(this.getClass().getCanonicalName());
	
	public List<Categories> retrieveCategories() throws ServiceException{
		log.debug("Service retrieveCategories started");
		List<Categories> result;
		try {
			ServiceHelper.openDataBaseSession();
			CategoriesMapper mapper = ServiceHelper.getMapper(CategoriesMapper.class);
			result = mapper.selectByExample(null);
		} catch (Exception e) {
			log.error("Errore durante retrieveCategories", e);
			throw new ServiceException("Errore durante retrieveCategories", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveCategories end");
		}
		return result;
	}
	
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
	
	public List<Items> retrieveFilteredActiveItems(String titleFilter,Integer categoryIdFilter,int minCreditsFilter,int maxCreditsFilter) throws ServiceException{
		log.debug("Service retrieveFilteredActiveItems started");
		List<Items> result;
		try {
			ServiceHelper.openDataBaseSession();
			ItemsMapper mapper = ServiceHelper.getMapper(ItemsMapper.class);
			ItemsExample query = new ItemsExample();
			Criteria criteria = query.createCriteria().andActiveEqualTo(Short.parseShort("1"));
			if(!StringUtils.isBlank(titleFilter)){
				criteria.andNameLike("%"+titleFilter+"%");
			}
			if(categoryIdFilter != -1){
				criteria.andCategoryCodeEqualTo(categoryIdFilter);
			}
			if(minCreditsFilter > 0){
				criteria.andPriceCreditGreaterThanOrEqualTo(new BigDecimal(minCreditsFilter));
			}
			if(maxCreditsFilter > 0){
				criteria.andPriceCreditLessThanOrEqualTo(new BigDecimal(maxCreditsFilter));
			}
			result = mapper.selectByExample(query);
		} catch (Exception e) {
			log.error("Errore durante retrieveFilteredActiveItems", e);
			throw new ServiceException("Errore durante retrieveFilteredActiveItems", e);
		} finally {
			ServiceHelper.closeDataBaseSession();
			log.debug("Service retrieveFilteredActiveItems end");
		}
		return result;
	}


}
