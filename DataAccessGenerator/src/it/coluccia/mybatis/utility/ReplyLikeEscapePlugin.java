package it.coluccia.mybatis.utility;


import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;


public class ReplyLikeEscapePlugin extends PluginAdapter {
	public boolean validate(List<String> warnings) {
		return true;
	}

	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (!addCriterionProperty(topLevelClass)) {
			return true;
		}
		
		if (!addCriterionMethod(topLevelClass)) {
			return true;
		}
		
		return addCriteriaMethod(topLevelClass, introspectedTable);
	}
	
	private boolean addCriterionProperty(TopLevelClass topLevelClass) {
		InnerClass criteria = null;
		for (InnerClass innerClass : topLevelClass.getInnerClasses()) {
			if ("Criterion".equals(innerClass.getType().getShortName())) {
				criteria = innerClass;
				break;
			}
		}
		if (criteria == null) {
			return false;
		}

		Field field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setInitializationString("false");
		field.setName("escape");
		field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
		
		criteria.addField(field);
		
		
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "escape"));

		method.setName("setEscape");
		
		method.addBodyLine("this.escape = escape;");

		criteria.addMethod(method);
		
		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());

		method.setName("isEscape");
		
		method.addBodyLine("return this.escape;");

		criteria.addMethod(method);
		
		return true;
	}
	
	private boolean addCriterionMethod(TopLevelClass topLevelClass) {
		InnerClass criteria = null;
		for (InnerClass innerClass : topLevelClass.getInnerClasses()) {
			if ("GeneratedCriteria".equals(innerClass.getType().getShortName())) {
				criteria = innerClass;
				break;
			}
		}
		if (criteria == null) {
			return true;
		}
		Method method = new Method();
		method.setVisibility(JavaVisibility.PROTECTED);
		method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition"));
		method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "value"));
		method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "property"));
		method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "escape"));

		method.setName("addCriterion");
		
		method.addBodyLine("if (value == null) {");
		method.addBodyLine("throw new RuntimeException(\"Value for \" + property + \" cannot be null\");");
		method.addBodyLine("}");
		method.addBodyLine("Criterion crit = new Criterion(condition, value);");
		method.addBodyLine("crit.setEscape(escape);");
		method.addBodyLine("criteria.add(crit);");

		criteria.addMethod(method);
		return true;
	}
	
	private boolean addCriteriaMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		InnerClass criteria = null;
		for (InnerClass innerClass : topLevelClass.getInnerClasses()) {
			if ("GeneratedCriteria".equals(innerClass.getType().getShortName())) {
				criteria = innerClass;
				break;
			}
		}
		if (criteria == null) {
			return true;
		}
		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
			if ((introspectedColumn.isJdbcCharacterColumn()) && (introspectedColumn.isStringColumn())) {
				//Case insensitive
				Method method = new Method();
				method.setVisibility(JavaVisibility.PUBLIC);
				method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), "value"));

				StringBuilder sb = new StringBuilder();
				sb.append(introspectedColumn.getJavaProperty());
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
				sb.insert(0, "and");
				sb.append("LikeInsensitiveEscape");
				method.setName(sb.toString());
				method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

				sb.setLength(0);
				sb.append("addCriterion(\"upper(");
				sb.append(Ibatis2FormattingUtilities.getAliasedActualColumnName(introspectedColumn));

				sb.append(") like\", value.toUpperCase(), \"");
				sb.append(introspectedColumn.getJavaProperty());
				sb.append("\",true);");
				method.addBodyLine(sb.toString());
				method.addBodyLine("return (Criteria) this;");

				criteria.addMethod(method);
				//Case sensitive
				method = new Method();
				method.setVisibility(JavaVisibility.PUBLIC);
				method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), "value"));

				sb = new StringBuilder();
				sb.append(introspectedColumn.getJavaProperty());
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
				sb.insert(0, "and");
				sb.append("LikeEscape");
				method.setName(sb.toString());
				method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

				sb.setLength(0);
				sb.append("addCriterion(\"");
				sb.append(Ibatis2FormattingUtilities.getAliasedActualColumnName(introspectedColumn));

				sb.append(" like\", value, \"");
				sb.append(introspectedColumn.getJavaProperty());
				sb.append("\",true);");
				method.addBodyLine(sb.toString());
				method.addBodyLine("return (Criteria) this;");

				criteria.addMethod(method);
			}
		}
		return true;
	}
}
