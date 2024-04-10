package org.sici.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sici.result.Result;
import org.sici.service.ProcessService;
import org.sici.vo.process.ProcessQueryVo;
import org.sici.vo.process.ProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller
 * @className: OaProcessController
 * @author: 749291
 * @description: TODO
 * @date: 3/26/2024 8:19 PM
 * @version: 1.0
 */

@RestController
@Api(tags = "process")
@RequestMapping("/admin/process")
public class ProcessController {
    @Autowired
    private ProcessService processService;

    // 分页查询
    @PreAuthorize("hasAuthority('bnt.process.list')")
    @ApiOperation(value = "page query process")
    @GetMapping("{page}/{limit}")
    public Result pageQuery(@ApiParam("page number") @PathVariable Integer page,
                            @ApiParam("number of records per page") @PathVariable Integer limit,
                            @ApiParam("process query vo") ProcessQueryVo processQueryVo) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        return Result.ok(processService.selectPage(pageParam, processQueryVo));
    }
}
