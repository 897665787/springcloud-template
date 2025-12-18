package com.company.codegenerate.controller;

import com.company.codegenerate.generator.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 代码生成控制器
 */
@Slf4j
@RestController
@RequestMapping("/code/generate")
@RequiredArgsConstructor
public class CodeGeneratorController {
    
    private final CodeGeneratorService codeGeneratorService;
    
    /**
     * 生成指定模块的代码
     *
     * @param moduleName 模块名称
     * @param tableName  表名
     * @return 生成结果
     */
    @PostMapping("/{moduleName}")
    public String generateCode(@PathVariable String moduleName, @RequestParam String tableName) {
        try {
            // 验证模块是否支持
            List<String> supportedModules = Arrays.asList("adminapi", "admin", "system", "order", "user", "tool");
            if (!supportedModules.contains(moduleName.toLowerCase())) {
                return "不支持的模块: " + moduleName;
            }
            
            codeGeneratorService.generateCode(tableName, moduleName);
            return "代码生成成功: 模块=" + moduleName + ", 表=" + tableName;
        } catch (Exception e) {
            log.error("代码生成失败", e);
            return "代码生成失败: " + e.getMessage();
        }
    }
    
    /**
     * 批量生成代码
     *
     * @param moduleName 模块名称
     * @param tableNames 表名列表
     * @return 生成结果
     */
    @PostMapping("/{moduleName}/batch")
    public String generateCodeBatch(@PathVariable String moduleName, @RequestBody List<String> tableNames) {
        try {
            StringBuilder result = new StringBuilder();
            for (String tableName : tableNames) {
                try {
                    codeGeneratorService.generateCode(tableName, moduleName);
                    result.append("代码生成成功: 表=").append(tableName).append("\n");
                } catch (Exception e) {
                    log.error("代码生成失败: " + tableName, e);
                    result.append("代码生成失败: 表=").append(tableName).append(", 错误=").append(e.getMessage()).append("\n");
                }
            }
            return result.toString();
        } catch (Exception e) {
            log.error("批量代码生成失败", e);
            return "批量代码生成失败: " + e.getMessage();
        }
    }
    
    /**
     * 获取支持的模块列表
     *
     * @return 模块列表
     */
    @GetMapping("/modules")
    public List<String> getSupportedModules() {
        return Arrays.asList("adminapi", "admin", "system", "order", "user", "tool");
    }
}