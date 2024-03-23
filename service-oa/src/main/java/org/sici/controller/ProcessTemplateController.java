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
import org.springframework.web.bind.annotation.*;

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

}
