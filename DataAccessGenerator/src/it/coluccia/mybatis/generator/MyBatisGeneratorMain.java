package it.coluccia.mybatis.generator;



import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;


public class MyBatisGeneratorMain {

	private static final String[] conf_all = {
		 
		"C:/workspaces_personali/Workspace_shareIt/DataAccessGenerator/FileGenerators/USERS.xml"};
	
	
	
	public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException,
			InterruptedException {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		
		args = new String[]{"ALL"};
		
		if (args.length == 0) {
			System.out.println("### ERRORE generazione codice Mapper. Argument non specificato!!!");
			return;
		}
		if (!args[0].equals("ALL")) {

			System.out.println("#### Lancio generazione codice da: " + args[0]);

			File configFile = new File(args[0]);
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(configFile);
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);

			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
			System.out.println("	### Warning generazione Mapper:");

		} else {
			for (String file : conf_all) {
				System.out.println("#### Lancio generazione codice da: " + file);

				File configFile = new File(file);
				ConfigurationParser cp = new ConfigurationParser(warnings);
				Configuration config = cp.parseConfiguration(configFile);
				DefaultShellCallback callback = new DefaultShellCallback(overwrite);

				MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
				myBatisGenerator.generate(null);
			}

		}
		if (warnings.size() == 0)
			System.out.print("nessuno!");
		else {
			for (String warn : warnings) {
				System.out.println("	-	" + warn);
			}
		}
		System.out.println("### FINE generazione codice Mapper.");
	}
}
