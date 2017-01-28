package it.coluccia.fe.backingbean.home;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import it.coluccia.fe.backingbean.base.BaseBean;
import it.coluccia.shareit.home.EjbHomeLocal;

@ViewScoped
@ManagedBean(name="homeBean")
public class HomeBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 259735089225645905L;

	private static final String BUNDLE_FILE = "it.coluccia.shareit.resources.HomeResources";
	private static final String MAPPED_NAME = "homeBean";
	private static final String MAPPED_PAGE = "home";
	@EJB
	private EjbHomeLocal serviceLocal;
	
	
	@PostConstruct
	public void init() {
		logger.debug("HomeBean instanziato");
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
	
	public static String getStaticMappedPage(){
		return MAPPED_PAGE;
	}

}
