package it.coluccia.common.constants;

public class ErrorConstants {

	private ErrorConstants() {

	}

	public class PageSuffixException {

	}

	public static final int MAX_MSG_LEN = 500;

	public static final int GENERIC_ERROR_CODE = 0;
	public static final int DB_ERROR_CODE = 1;
	public static final int MESSAGE_TO_SHORT_ERROR_CODE = 2;
	public static final int PARSER_ERROR_CODE = 3;
	public static final int PARSER_DOMAIN_ERROR_CODE = 4;
	public static final int DATABASE_SESSION_ERROR_CODE = 5;
	public static final int ENGINE_RULES_ERROR_CODE = 6;

	public static final String GENERIC_ERROR_MSG = "Errore generico, non specificato.";
	public static final String EMPTY_MESSAGE_ERROR_MSG = "Impossibile elaborare il messaggio perchè vuoto";
	public static final String MESSAGE_TO_SHORT_ERROR_MSG = "Impossibile elaborare il messaggio perchè troncato";
}
