package it.coluccia.shareit.fe.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It is an implementation of javax.servlet.Filter class. It filters all request for an xhtml page
 * of the application
 * @author s.coluccia
 *
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class CustomSecurityFilter implements Filter {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void destroy() {

	}

	/**
	 * It redirect all request that are done from not logged in users. 
	 * It also redirect to home page all request for the loginPage that are done from logged in users.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {

			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);

			String reqURI = reqt.getRequestURI();
			if (reqURI.indexOf("/login.xhtml") >= 0 || (ses != null && ses.getAttribute("username") != null)
					|| reqURI.indexOf("/public/") >= 0 || reqURI.contains("javax.faces.resource")) {
				if (reqURI.indexOf("/login.xhtml") >= 0 && ((ses != null && ses.getAttribute("username") != null)
						|| reqURI.indexOf("/public/") >= 0 || reqURI.contains("javax.faces.resource"))) {
					log.debug("Utente autenticato non pu� accedere alla pagina di login, reindirizzo alla home");
					resp.sendRedirect(reqt.getContextPath() + "/faces/home/home.xhtml");
				} else {
					log.debug("Utente autenticato, pu� accedere alla pagina");
					chain.doFilter(request, response);
				}
			} else {
				log.debug("Utente non autenticato, reinderizzo alla pagina di login");
				resp.sendRedirect(reqt.getContextPath() + "/faces/login.xhtml");
			}
		} catch (Exception e) {
			log.error("Errore nel security filter!", e);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
