package org.sici.controller.api;

import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import org.sici.model.process.Process;
import org.sici.model.system.SysUser;
import org.sici.result.Result;
import org.sici.service.ProcessService;
import org.sici.service.ProcessTemplateService;
import org.sici.service.SysUserService;
import org.sici.vo.process.ApprovalVo;
import org.sici.vo.process.ProcessFormVo;
import org.sici.vo.process.ProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller.api
 * @className: OaProcessController
 * @author: 749291
 * @description: TODO
 * @date: 3/28/2024 4:45 PM
 * @version: 1.0
 */

@Api(tags = "process")
@RestController
@RequestMapping("/admin/process")
@CrossOrigin
public class OaProcessController {
    @Autowired
    private ProcessService processService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private SysUserService sysUserService;

    // findProcessType
    @Operation(summary = "find process type")
    @GetMapping("findProcessType")
    public Result findProcessType() {
        return processService.findProcessType();
    }

    // get ProcessTemplate by id
    @Operation(summary = "get process template by id")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplateById(@ApiParam("processTemplateId") @PathVariable Long processTemplateId) {
        return Result.ok(processTemplateService.getById(processTemplateId));
    }


    // start up a process
    @Operation(summary = "start up a process")
    @PostMapping("startUp")
    public Result startUp(@ApiParam("processFormVo") @RequestBody ProcessFormVo processFormVo) {
        return processService.startUp(processFormVo);
    }


    // find pending process
    @Operation(summary = "find pending process")
    @GetMapping("findPending/{page}/{limit}")
    public Result findPending(@ApiParam("page") @PathVariable Integer page,
                              @ApiParam("limit") @PathVariable Integer limit) {
        return Result.ok(processService.findPending(new Page<>(page, limit)));
    }

    // show process details
    @Operation(summary = "show process details")
    @GetMapping("show/{processId}")
    public Result show(@ApiParam("processId") @PathVariable Long processId) {
        return Result.ok(processService.show(processId));
    }

    // approve a process
    @Operation(summary = "approve a process")
    @PostMapping("approve")
    public Result approve(@ApiParam("approvalVo") @RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }


    // 查询已处理的任务
    @Operation(summary = "find processed process")
    @GetMapping("findProcessed/{page}/{limit}")
    public Result findProcessed(@ApiParam("page") @PathVariable Integer page,
                                @ApiParam("limit") @PathVariable Integer limit) {
        Page<Process> pageParam = new Page<>();
        pageParam.setCurrent(page);
        pageParam.setSize(limit);

        IPage<ProcessVo> processed = processService.findProcessed(pageParam);
        return Result.ok(processed);
    }


    // 查询已发起的申请
    @Operation(summary = "find started process")
    @GetMapping("findStarted/{page}/{limit}")
    public Result findStarted(@ApiParam("page") @PathVariable Integer page,
                              @ApiParam("limit") @PathVariable Integer limit) {
        Page<ProcessVo> pageParam = new Page<>();
        pageParam.setCurrent(page);
        pageParam.setSize(limit);

        IPage<ProcessVo> started = processService.findStarted(pageParam);
        return Result.ok(started);
    }


    // 获取当前用户信息
    @Operation(summary = "get current user info")
    @GetMapping("getCurrentUser")
    public Result getCurrentUser() {
        Map<String, Object> currentUser = sysUserService.getCurrentUser();
        return Result.ok(currentUser);
    }

}
