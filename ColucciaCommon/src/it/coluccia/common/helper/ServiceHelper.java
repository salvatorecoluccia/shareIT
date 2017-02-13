package it.coluccia.common.helper;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import it.coluccia.common.constants.CommonConstants;

public class ServiceHelper {

	protected static final Logger log = LoggerFactory.getLogger(ServiceHelper.class);
	
	
	private static final String RESOURCE_CONFIG_MYBATIS = "it/coluccia/shareit/dao/config/shareitdb/myBatisConfig.xml";

	private static SqlSessionFactory sqlMapper;
	
	protected  static ThreadLocal<Map<String,Object>> header = new ThreadLocal<Map<String,Object>>();
	protected  static ThreadLocal<SqlSession> session = new ThreadLocal<SqlSession>();

	
	
	public static void openDataBaseSession() throws IOException {
		session.set(openSession());
	}
	
	private static SqlSession openSession() throws IOException {		
		return getSqlFactoryMapper().openSession();
	}
	
	private synchronized static SqlSessionFactory getSqlFactoryMapper() throws IOException {
		if (getSqlMapper() != null) {
			return getSqlMapper();
		} else {

			Reader reader = Resources.getResourceAsReader(RESOURCE_CONFIG_MYBATIS);
			sqlMapper = new SqlSessionFactoryBuilder().build(reader);
			log.debug("DAO MAPPER JNDI settato");
			
			return getSqlMapper();
		}
	}
	
	public static SqlSessionFactory getSqlMapper() {
		return sqlMapper;
	}
	
	
	public static <T> T getMapper(Class<T> clazz) throws Exception {
		SqlSession mySession = session.get();
		if (mySession == null) {
			throw new Exception("Sessione verso il DB non aperta. Probabilmente il corrente EJB non ha un interceptor che apre connessioni verso il DB oppure non è stata aperta una connessione");
		}
		T mapper = mySession.getMapper(clazz);
		if (mapper == null) {
			throw new Exception("Mapper per la classe " + clazz.getCanonicalName() + " non definita");
		}
		return mapper;
	}
	
	public static final void closeDataBaseSession(){
		SqlSession mySession = session.get();
		if(null != mySession){
			mySession.close();
		}
		session.remove();
	}
	
	
	

	
	
}