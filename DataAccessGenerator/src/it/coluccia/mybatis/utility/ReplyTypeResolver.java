package it.coluccia.mybatis.utility;



import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class ReplyTypeResolver extends JavaTypeResolverDefaultImpl {

	private static final String PROP_USE_REPLY_TYPE_RESOLVER = "useReplyTypeResolver";
	private static final String PROP_REPLY_TIMESTAMP_COLUMNS = "useReplyTypeResolver.timestampColumns";

	private Map<Integer, Map<String,FullyQualifiedJavaType>> jdbcTypeInformationCustom = new HashMap<Integer, Map<String,FullyQualifiedJavaType>>();
	
	@Override
	public void addConfigurationProperties(Properties properties) {
		super.addConfigurationProperties(properties);

		if ("true".equalsIgnoreCase(properties.getProperty(PROP_USE_REPLY_TYPE_RESOLVER))) {
			
			String columns = properties.getProperty(PROP_REPLY_TIMESTAMP_COLUMNS);
			
			if (columns != null) {
				String[] listColumns = columns.split(",");
				if (listColumns != null) {
					Map<String,FullyQualifiedJavaType> subMap = new HashMap<String, FullyQualifiedJavaType>();
					jdbcTypeInformationCustom.put(Integer.valueOf(Types.TIMESTAMP), subMap);
					for (String column: listColumns) {
						subMap.put(column.trim(), new FullyQualifiedJavaType(java.sql.Timestamp.class.getName()));
					}
				}
			}
		}

	}

	
	public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn){
		if (introspectedColumn == null) {
			return super.calculateJavaType(introspectedColumn);
		}
		
		String nameCol = introspectedColumn.getActualColumnName();
		Integer typeJDBC = introspectedColumn.getJdbcType();
		
		Map<String,FullyQualifiedJavaType> subMap = jdbcTypeInformationCustom.get(typeJDBC);
		
		if (subMap == null) {
			return super.calculateJavaType(introspectedColumn);
		}
		
		FullyQualifiedJavaType javaType = subMap.get(nameCol);
		if (javaType == null) {
			return super.calculateJavaType(introspectedColumn);
		}
		
		return javaType;
	}

}