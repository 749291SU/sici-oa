package org.sici.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sici.model.process.ProcessType;
import org.sici.result.Result;
import org.sici.service.ProcessService;
import org.sici.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller
 * @className: ProcessTypeController
 * @author: 749291
 * @description: TODO
 * @date: 3/23/2024 3:34 PM
 * @version: 1.0
 */

@Api(tags = "process type")
@RestController
@RequestMapping("/admin/process/processType")
public class ProcessTypeController {
    @Autowired
    private ProcessTypeService processTypeService;

    // page query
    @ApiOperation(value = "page query process type")
//    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @GetMapping("{page}/{limit}")
    public Result pageQuery(@ApiParam("page number") @PathVariable Integer page,
                            @ApiParam("number of records per page") @PathVariable Integer limit) {

        IPage<ProcessType> processTypePage = new Page<>();

        processTypePage.setSize(limit);
        processTypePage.setCurrent(page);

        return Result.ok(processTypeService.page(processTypePage));
    }


    // get by id
    @ApiOperation(value = "get process type by id")
//    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @GetMapping("get/{id}")
    public Result getById(@ApiParam("id") @PathVariable Long id) {
        return Result.ok(processTypeService.getById(id));
    }

    // add
    @ApiOperation(value = "add process type")
//    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @PostMapping("save")
    public Result add(@ApiParam("process type") @RequestBody ProcessType processType) {
        return Result.ok(processTypeService.save(processType));
    }

    // delete
    @ApiOperation(value = "delete process type")
//    @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @DeleteMapping("remove/{id}")
    public Result delete(@ApiParam("id") @PathVariable Long id) {
        return Result.ok(processTypeService.removeById(id));
    }

    // update
    @ApiOperation(value = "update process type")
//    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @PutMapping("update")
    public Result update(@ApiParam("process type") @RequestBody ProcessType processType) {
        return Result.ok(processTypeService.updateById(processType));
    }


    // findAll
    @ApiOperation(value = "find all process type")
    @GetMapping("findAll")
    public Result findAll() {
        return Result.ok(processTypeService.list());
    }
}

