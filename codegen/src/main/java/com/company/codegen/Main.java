package com.company.codegen;

import java.io.File;
import java.io.IOException;

import com.company.codegen.content.java.ControllerReplace;
import com.company.codegen.content.java.EdgeControllerReplace;
import com.company.codegen.content.java.FeignFallbackReplace;
import com.company.codegen.content.java.FeignReplace;
import com.company.codegen.content.java.MapperReplace;
import com.company.codegen.content.java.ModelExcelReplace;
import com.company.codegen.content.java.ModelReplace;
import com.company.codegen.content.java.ModelReqReplace;
import com.company.codegen.content.java.ModelRespReplace;
import com.company.codegen.content.java.ServiceReplace;
import com.company.codegen.content.sql.DictReplace;
import com.company.codegen.content.sql.MenuReplace;
import com.company.codegen.content.vue.IndextsReplace;
import com.company.codegen.content.vue.IndexvueReplace;
import com.company.codegen.content.vue.OperationMaskReplace;
import com.company.codegen.util.Config;
import com.company.codegen.util.FileUtil;
import com.company.codegen.util.NameUtil;

public class Main {

	public static void main(String[] args) {
		String table = Config.get("table");
		String[] tables = table.split(",");

		for (String tab : tables) {
			String ModelName = NameUtil.dealClassName(tab);
			String modelName = ModelName.substring(0, 1).toLowerCase() + ModelName.substring(1);

			String path = Config.get("project.path");

			File file = new File("src/main/resources/code-generator-template");
			String templatePath = null;
			try {
				templatePath = file.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("模板文件所在目录:" + templatePath);
			
//			String outPath = path + "/" + modelName;
			String outPath = toOutPath(path, "model");
			String inFile = templatePath + "/java/model.java";
			String outFile = outPath + "/" + ModelName + ".java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ModelReplace(tab));

			outPath = toOutPath(path, "mapper");
			inFile = templatePath + "/java/mapper.java";
			outFile = outPath + "/" + ModelName + "Mapper.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new MapperReplace(tab));

			outPath = toOutPath(path, "service");
			inFile = templatePath + "/java/service.java";
			outFile = outPath + "/" + ModelName + "Service.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ServiceReplace(tab));

			outPath = toOutPath(path, "controller");
			inFile = templatePath + "/java/controller.java";
			outFile = outPath + "/" + ModelName + "Controller.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ControllerReplace(tab));

			outPath = toOutPath(path, "feign");
			inFile = templatePath + "/java/feign.java";
			outFile = outPath + "/" + ModelName + "Feign.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new FeignReplace(tab));

			outPath = toOutPath(path, "feignFallback");
			inFile = templatePath + "/java/feignFallback.java";
			outFile = outPath + "/" + ModelName + "FeignFallback.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new FeignFallbackReplace(tab));

			outPath = toOutPath(path, "edgecontroller");
			inFile = templatePath + "/java/edgecontroller.java";
			outFile = outPath + "/" + ModelName + "Controller.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new EdgeControllerReplace(tab));

			outPath = toOutPath(path, "modelreq");
			inFile = templatePath + "/java/modelreq.java";
			outFile = outPath + "/" + ModelName + "Req.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ModelReqReplace(tab));

			outPath = toOutPath(path, "modelresp");
			inFile = templatePath + "/java/modelresp.java";
			outFile = outPath + "/" + ModelName + "Resp.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ModelRespReplace(tab));

			outPath = toOutPath(path, "modelexcel");
			inFile = templatePath + "/java/modelexcel.java";
			outFile = outPath + "/" + ModelName + "Excel.java";
			FileUtil.generalFromTemplateFile(inFile, outFile, new ModelExcelReplace(tab));

			outPath = toOutPath(path, "sql");
			inFile = templatePath + "/sql/menu.sql";
			outFile = outPath + "/" + ModelName + ".sql";
			FileUtil.generalFromTemplateFile(inFile, outFile, new MenuReplace(tab));

			outPath = toOutPath(path, "sql");
			inFile = templatePath + "/sql/dict.sql";
			outFile = outPath + "/dict/" + ModelName + ".sql";
			FileUtil.generalFromTemplateFile(inFile, outFile, new DictReplace(tab));
			
			outPath = toOutPath(path, "vue");
			inFile = templatePath + "/vue/index.ts";
			outFile = outPath + "/" + modelName + "/api/index.ts";
			FileUtil.generalFromTemplateFile(inFile, outFile, new IndextsReplace(tab));
			
			outPath = toOutPath(path, "vue");
			inFile = templatePath + "/vue/index.vue";
			outFile = outPath + "/" + modelName + "/index.vue";
			FileUtil.generalFromTemplateFile(inFile, outFile, new IndexvueReplace(tab));
			
			outPath = toOutPath(path, "vue");
			inFile = templatePath + "/vue/OperationMask.vue";
			outFile = outPath + "/" + modelName + "/components/OperationMask.vue";
			FileUtil.generalFromTemplateFile(inFile, outFile, new OperationMaskReplace(tab));
			
		}
	}

	private static String toOutPath(String path, String folder) {
		String outputone = Config.get("output.one");
		if("true".equals(outputone)) {
			return path;
		}
		return path + "/" + folder;
	}
}