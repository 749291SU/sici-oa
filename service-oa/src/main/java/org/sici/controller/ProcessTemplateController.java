package org.sici.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sici.model.process.ProcessTemplate;
import org.sici.result.Result;
import org.sici.service.ProcessTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller
 * @className: ProcessTemplateController
 * @author: 749291
 * @description: TODO
 * @date: 3/23/2024 3:46 PM
 * @version: 1.0
 */

@Api(tags = "process template")
@RestController
@RequestMapping("/admin/process/processTemplate")
public class ProcessTemplateController {
    @Autowired
    private ProcessTemplateService processTemplateService;

    // page query
    @ApiOperation(value = "page query process template")
    @GetMapping("{page}/{limit}")
    public Result pageQuery(@ApiParam("page number") @PathVariable Integer page,
                            @ApiParam("number of records per page") @PathVariable Integer limit) {

        return Result.ok(processTemplateService.selectPage(page, limit));
    }

    // get by id
    @ApiOperation(value = "get process template by id")
    @GetMapping("get/{id}")
    public Result getById(@ApiParam("id") @PathVariable Long id) {
        return Result.ok(processTemplateService.getById(id));
    }

    // add
    @ApiOperation(value = "add process template")
    @PostMapping("save")
    public Result add(@ApiParam("process template") @RequestBody ProcessTemplate processTemplate) {
        return Result.ok(processTemplateService.save(processTemplate));
    }

    // delete
    @ApiOperation(value = "delete process template")
    @DeleteMapping("delete/{id}")
    public Result delete(@ApiParam("id") @PathVariable Long id) {
        return Result.ok(processTemplateService.removeById(id));
    }

    // update
    @ApiOperation(value = "update process template")
    @PutMapping("update")
    public Result update(@ApiParam("process template") @RequestBody ProcessTemplate processTemplate) {
        return Result.ok(processTemplateService.updateById(processTemplate));
    }


//    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        // 获取项目路径
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();

        String fileName = file.getOriginalFilename();
        // 上传目录
        File tempFile = new File(path + "/processes/");
        // 判断目录是否存着
        if (!tempFile.exists()) {
            tempFile.mkdirs();//创建目录
        }
        // 创建空文件用于写入文件
        File imageFile = new File(path + "/processes/" + fileName);
        // 保存文件流到本地
        try {
            file.transferTo(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }

        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + fileName);
        map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
        return Result.ok(map);
    }

    // 发布流程定义
//    @PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    @ApiOperation(value = "发布流程定义")
    @GetMapping("/publish/{id}")
    public Result publish(@ApiParam("id") @PathVariable Long id) {
        return Result.ok(processTemplateService.publish(id));
    }


}
