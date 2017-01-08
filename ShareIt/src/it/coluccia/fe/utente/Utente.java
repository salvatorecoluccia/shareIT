package it.coluccia.fe.utente;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.coluccia.common.context.UserInterface;


public class Utente implements UserInterface {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());

	private static final long serialVersionUID = 8798384120554414194L;
	
	private String username;
	
	private String nome;
	
	private String cognome;
	
	private Map<String,Boolean> abilitazioni = new HashMap<String, Boolean>();
	
	private String email;
	
	private Locale lingua;
	
	private String tipoUtente;
	
	public Utente() {
	}
	
	public Utente(Utente user) {
		
		if (user == null) {
			return;
		}
		
		try {
			BeanUtils.copyProperties(this, user);
		} catch (IllegalAccessException e) {
			logger.error("Si è verificato un errore durante l'inizializzazione dell'oggetto", e);
		} catch (InvocationTargetException e) {
			logger.error("Si è verificato un errore durante l'inizializzazione dell'oggetto", e);
		}
		
		if (lingua == null) {
			lingua = Locale.ITALIAN;
		}
	}

	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Locale getLingua() {
		return lingua;
	}

	public void setLingua(Locale lingua) {
		this.lingua = lingua;
	}
	
	public void setLingua(String lingua) {
		if (StringUtils.isBlank(lingua)) {
			logger.warn("Il locale è vuoto. Impostato il locale di default");
			return;
		}
		try {
			Locale loc = new Locale(lingua);
			this.lingua = loc;
		} catch (Throwable th) {
			logger.error("Il locale {} non è valido. Impostato il locale di default", lingua, th);
		}
	}

	
	public Map<String,Boolean> getAbilitazioni() {
		return abilitazioni;
	}

	public void setAbilitazioni(Map<String,Boolean> abilitazioni) {
		this.abilitazioni = abilitazioni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNomeCompleto() {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.isNotBlank(this.nome.trim())) {
			builder.append(this.nome);
			builder.append(' ');
		}
		
		if (StringUtils.isNotBlank(this.cognome)) {
			builder.append(this.cognome.trim());
		}
		
		String result = builder.toString().trim();
		
		result = WordUtils.capitalize(result);
		
		return result;
		
	}

	public String getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
	
	public boolean isAbilitato(Set<String> abilitazioni) {
		
		if (this.abilitazioni == null) {
			return false;
		}
		
		if (abilitazioni == null) {
			return true;
		}
		
		for (String ab: abilitazioni) {
			Boolean res = this.abilitazioni.get(ab);
			if (res != null && res) {
				return true;
			}
		}
		
		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
