package com.company.codegenerator;

import java.io.File;
import java.io.IOException;

import com.company.codegenerator.content.html.IndexSingleReplace;
import com.company.codegenerator.content.java.ControllerReplace;
import com.company.codegenerator.content.java.MapperReplace;
import com.company.codegenerator.content.java.MapperXmlReplace;
import com.company.codegenerator.content.java.ModelReplace;
import com.company.codegenerator.content.java.ServiceReplace;
import com.company.codegenerator.content.nav.NavReplace;
import com.company.codegenerator.content.sql.MenuReplace;
import com.company.codegenerator.util.Config;
import com.company.codegenerator.util.FileUtil;
import com.company.codegenerator.util.NameUtil;

public class Main {

	public static void main(String[] args) {
		String table = Config.get("table");
		String[] tables = table.split(",");

		for (String tab : tables) {
			String ModelName = NameUtil.dealClassName(tab);
			String modelName = ModelName.substring(0, 1).toLowerCase() + ModelName.substring(1);

			String path = Config.get("project.path");
			String outPath = path + "/" + modelName;

			File file = new File("src/main/resources/code-generator-template");
			String templatePath = null;
			try {
				templatePath = file.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("模板文件所在目录:" + templatePath);

			String inFile = templatePath + "/jsp/indexSingle.jsp";
			String outFile = outPath + "/" + modelName + ".jsp";
			FileUtil.generalFromTemplateFile(inFile, outFile, new IndexSingleReplace(tab));
			
			/*
			inFile = templatePath + "/jsp/index.jsp";
			outFile = outPath + "/" + modelName + ".jsp";
			FileUtil.generalFromTemplateFile(inFile, outFile, new IndexReplace(tab));

			inFile = templatePath + "/jsp/create.jsp";
			outFile = outPath + "/" + modelName + "Create.jsp";
			FileUtil.generalFromTemplateFile(inFile, outFile, new CreateReplace(tab));
			
			inFile = templatePath + "/jsp/update.jsp";
			outFile = outPath + "/" + modelName + "Update.jsp";
			FileUtil.generalFromTemplateFile(inFile, outFile, new UpdateReplace(tab));
			
			inFile = templatePath + "/jsp/detail.jsp";
			outFile = outPath + "/" + modelName + "Detail.jsp";
			FileUtil.generalFromTemplateFile(inFile, outFile, new DetailReplace(tab));
			*/
			
			inFile = templatePath + "/java/controller.java";
			outFile = outPath + "/" + ModelName + "Controller.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ControllerReplace(tab));

			inFile = templatePath + "/java/model.java";
			outFile = outPath + "/" + ModelName + ".java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ModelReplace(tab));

			inFile = templatePath + "/java/mapper.java";
			outFile = outPath + "/" + ModelName + "Dao.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new MapperReplace(tab));
			
			inFile = templatePath + "/java/mapper.xml";
			outFile = outPath + "/" + ModelName + "Dao.xml";
			FileUtil.generalFromTemplateFile(inFile, outFile, new MapperXmlReplace(tab));

			inFile = templatePath + "/java/service.java";
			outFile = outPath + "/" + ModelName + "Service.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ServiceReplace(tab));

			inFile = templatePath + "/sql/menu.sql";
			outFile = outPath + "/" + ModelName + ".sql";
			FileUtil.generalFromTemplateFile(inFile, outFile, new MenuReplace(tab));
			
			inFile = templatePath + "/nav/nav.jsp";
			outFile = outPath + "/" + "nav.jsp";
			FileUtil.generalFromTemplateFile(inFile, outFile, new NavReplace(tab));
		}
	}
}