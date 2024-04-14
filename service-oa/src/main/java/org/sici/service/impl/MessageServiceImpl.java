package org.sici.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.joda.time.DateTime;
import org.sici.custom.LoginUserInfoHelper;
import org.sici.model.process.Process;
import org.sici.model.process.ProcessTemplate;
import org.sici.model.system.SysUser;
import org.sici.service.MessageService;
import org.sici.service.ProcessService;
import org.sici.service.ProcessTemplateService;
import org.sici.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @projectName: oa-parent
 * @package: org.sici.service.impl
 * @className: MessageServiceImpl
 * @author: 749291
 * @description: TODO
 * @date: 4/13/2024 18:56
 * @version: 1.0
 */

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private ProcessService processService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private WxMpService wxMpService;
    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {
        // 查询流程信息
        Process process = processService.getById(processId);

        // 查询用户信息
        SysUser sysUser = sysUserService.getById(userId);

        // 查询流程模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());

        // 获取提交审批的人的信息
        SysUser submitUser = sysUserService.getById(process.getUserId());

        String openId = sysUser.getOpenId();
        if (openId == null) {
            openId = "oUDs66rC7BxLNo8sKueblFxLxy0A#/";
        }

        // 设置消息发送信息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId("sUZ13vLpwSm5Gu6SWSi1dg437kvo3cuE3_DI8L0D4jg")
                .url(" http://oasici1.viphk.nnhk.cc/#/show/" + processId + "/" + taskId)
                .build();

        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuffer content = new StringBuffer();
        for (Map.Entry entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }

        templateMessage.addData(new WxMpTemplateData("first", submitUser.getName() +
                "提交" + processTemplate.getName() + "请注意查看", "#272727"))
                .addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"))
                .addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString(), "#272727"))
                .addData(new WxMpTemplateData("content", content.toString(), "#272727"));

        // 发送消息
        try {
            String returnValue = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            log.info("推送消息返回：{}", returnValue);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void pushProcessedMessage(Long processId, Long userId, Integer status) {
        Process process = processService.getById(processId);
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        SysUser sysUser = sysUserService.getById(userId);
        SysUser currentSysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        String openId = sysUser.getOpenId();
        if (openId == null) {
            openId = "oUDs66rC7BxLNo8sKueblFxLxy0A#/";
        }

        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)//要推送的用户openid
                .templateId("t9Cu5d2lJVM4QgjJZoyyjOMtXTRt-XBGRkB_KsM_edk")//模板id
                .url("http://oasici1.viphk.nnhk.cc/#/show/"+processId+"/0")//点击模板消息要访问的网址
                .build();
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuffer content = new StringBuffer();
        for (Map.Entry entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }
        templateMessage.addData(new WxMpTemplateData("first", "你发起的"+processTemplate.getName()+"审批申请已经被处理了，请注意查看。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword3", currentSysUser.getName(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword4", status == 1 ? "审批通过" : "审批拒绝", status == 1 ? "#009966" : "#FF0033"));
        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));
        String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        log.info("推送消息返回：{}", msg);
    }
}
