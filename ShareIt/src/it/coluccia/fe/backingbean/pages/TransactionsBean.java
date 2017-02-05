package it.coluccia.fe.backingbean.pages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import it.coluccia.common.exception.ServiceException;
import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.fe.backingbean.login.LoginBean;
import it.coluccia.shareit.dto.items.shareitdb.Items;
import it.coluccia.shareit.dto.transactions.shareitdb.Transactions;
import it.coluccia.shareit.dto.users.shareitdb.Users;
import it.coluccia.shareit.home.EjbHomeLocal;
import it.coluccia.shareit.pages.ShareItEjbLocal;
import it.common.fe.utils.FacesMessageUtils;
import it.common.fe.utils.TransactionFrontEnd;

@ViewScoped
@ManagedBean(name = "transactionsBean")
public class TransactionsBean extends BaseBean{

		@EJB
		private ShareItEjbLocal serviceLocal;

		@EJB
		private EjbHomeLocal serviceLocalHome;
		
		@ManagedProperty(value = "#{itemDetailBean}")
		private ItemDetailBean itemDetailBean;
		
		@ManagedProperty(value = "#{loginBean}")
		private LoginBean loginBean;
		/*
		@ManagedProperty("#{param.itemId}")
		private int itemId;
*/
		private final String BUNDLE_FILE = "it.coluccia.shareit.resources.PagesResources";
		private final String MAPPED_NAME = "transactionsBean";
		private final String MAPPED_PAGE = "transactions";

		private final String KEY_TITLE_SERVICE_ERROR = "msg.error.service.title";
		private final String KEY_BODY_INTERNAL_ERROR = "msg.error.service.internal";
		
		private List<TransactionFrontEnd> transactions;
		private List<TransactionFrontEnd> transactionsUserBuyer;
		private Users user;


		@PostConstruct
		public void init() {
			logger.debug("TransactionsBean instanziato");
			String currentUser = loginBean.getUsername();
			if (StringUtils.isBlank(currentUser)) {
				logger.debug("erore durante init");
				FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_INTERNAL_ERROR,
						getResourceBundle());
				return;
			}
			try {
				user = serviceLocal.retrieveCurrentUserObj(currentUser);
				transactions = retrieveTransactionForUser();
			} catch (ServiceException e) {
				logger.debug("erore durante retrieveCurrentUserObj");
				FacesMessageUtils.addMessageErrorFromBundle(KEY_TITLE_SERVICE_ERROR, KEY_BODY_INTERNAL_ERROR,
						getResourceBundle());
			} finally {
				logger.debug("init bean end");
			}
			

		}
		
		private List<TransactionFrontEnd> retrieveTransactionForUser() throws ServiceException{
			List<Items> itemsActive = serviceLocal.retrieveAllSellItems();
			List<Transactions> transactionsOfUser = serviceLocal.retrieveAllUserBuyerTransactions(user.getId());
			transactionsUserBuyer = associateTransactionsAndItems(itemsActive,transactionsOfUser);
			List<Items> userItems = selectUserItems(itemsActive);
			List<Transactions> transactionsOfUserItems = serviceLocal.retrieveAllUserTransactions(userItems);
			return associateTransactionsAndItems(userItems,transactionsOfUserItems);
		}
		
		private List<Items> selectUserItems(List<Items> items){
			List<Items> result = new ArrayList<>();
			for(Items item : items){
				if(item.getOwnerId().equals(user.getId())){
					result.add(item);
				}
			}
			return result;
		}
		
		private List<TransactionFrontEnd> associateTransactionsAndItems(List<Items> items, List<Transactions> transactions){
			List<TransactionFrontEnd> result = new ArrayList<>();
			for(Items item : items){
				for(Transactions transaction : transactions){
					if(transaction.getIdItem().equals(item.getId())){
						result.add(new TransactionFrontEnd(item,transaction));
					}
				}
			}
			return result;
		}
		
		public String goToDetailsPage(int itemId) {
			itemDetailBean.setItemId(String.valueOf(itemId));
			itemDetailBean.init();
			return itemDetailBean.getMappedPage();
		}


		@Override
		public String getBundleName() {
			return BUNDLE_FILE;
		}

		@Override
		public String getMappedName() {
			return MAPPED_NAME;
		}

		@Override
		public String getMappedPage() {
			return MAPPED_PAGE;
		}

		public ItemDetailBean getItemDetailBean() {
			return itemDetailBean;
		}

		public void setItemDetailBean(ItemDetailBean itemDetailBean) {
			this.itemDetailBean = itemDetailBean;
		}


		public List<TransactionFrontEnd> getTransactions() {
			return transactions;
		}

		public void setTransactions(List<TransactionFrontEnd> transactions) {
			this.transactions = transactions;
		}

		public List<TransactionFrontEnd> getTransactionsUserBuyer() {
			return transactionsUserBuyer;
		}

		public void setTransactionsUserBuyer(List<TransactionFrontEnd> transactionsUserBuyer) {
			this.transactionsUserBuyer = transactionsUserBuyer;
		}

		public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}

		public LoginBean getLoginBean() {
			return loginBean;
		}

		public void setLoginBean(LoginBean loginBean) {
			this.loginBean = loginBean;
		}
		
		
		
		
		
		
		
		
		
		

	

}
