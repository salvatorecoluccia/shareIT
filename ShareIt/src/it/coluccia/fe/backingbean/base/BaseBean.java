package it.coluccia.fe.backingbean.base;


import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.common.fe.utils.FacesUtils;


public abstract class BaseBean implements Serializable {
	
	
	static{
		String log4jConfPath = "C:/workspaces_personali/Workspace_shareIt/ShareIt_Config/log4j/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}

	private static final long serialVersionUID = -373552439493490466L;

	private static final String COMMON_RESOURCE_BUNDLE = "it.reply.nl.CommonResource";


	protected Logger logger = LogManager.getLogger(this.getClass().getCanonicalName());

	protected ResourceBundle getResourceBundle() {

		return ResourceBundle.getBundle(getBundleName(), getLocale(), Thread.currentThread().getContextClassLoader());
	}

	protected Locale getLocale() {
		return Locale.ITALIAN;
	}

	protected ResourceBundle getCommonResourceBundle() {

		return ResourceBundle.getBundle(COMMON_RESOURCE_BUNDLE, getLocale(),
				Thread.currentThread().getContextClassLoader());
	}

	public abstract String getBundleName();

	protected void resetState() {
		logger.debug("START reset");
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		clearValid(root);
		logger.debug("END reset");
	}

	private void clearValid(UIComponent component) {
		if (component instanceof UIInput) {
			((UIInput) component).resetValue();

		}
		List<UIComponent> children = component.getChildren();
		if (children == null || children.size() == 0) {
			return;
		}

		for (UIComponent child : children) {
			clearValid(child);
		}
	}

	/*protected void resetStateDatatableByComponentId(String componentId) {
		logger.debug("START resetStateByComponentId");
		if (StringUtils.isNotBlank(componentId)) {
			DataTable dataTable = (DataTable) FacesUtils.getView().findComponent(componentId);
			if (dataTable != null) {
				dataTable.reset();
			}
		}

		logger.debug("END resetStateByComponentId");
	}
*/
	protected void addComponentToUpdate(String componentId) {
		FacesContext context = FacesUtils.getFacesContext();
		if (context.isPostback()) {
			if (!RequestContext.getCurrentInstance().isIgnoreAutoUpdate())
				context.getPartialViewContext().getRenderIds().add(componentId);
		} else {
			logger.warn("Attenzione. Impossibile aggiungere ai componenti da renderizzare il componente "+
					componentId);
		}
	}
/*
	protected void clearCacheDataTable(String componentId) {
		logger.debug("START clearDataTable");
		if (StringUtils.isNotBlank(componentId)) {
			DataTable dataTable = (DataTable) FacesUtils.getView().findComponent(componentId);
			if (dataTable != null) {
				dataTable.clearLazyCache();

			}
		}

		logger.debug("END clearDataTable");
	}
*/
	
	public abstract String getMappedName();
	public abstract String getMappedPage();

	

	
	
	

}
