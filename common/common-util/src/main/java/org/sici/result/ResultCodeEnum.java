package org.sici.result;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),

    LOGIN_AUTH(208, "未登陆"),
    LOGIN_ERROR(208, "登陆失败"),
    PERMISSION(209, "没有权限"),
    GLOBAL_ERROR(210, "全局异常"),
    ARITHMETIC_ERROR(211, "数学运算异常"),
    NULL_POINT(212, "空指针异常"),
    HTTP_CLIENT_ERROR(213, "HTTP请求异常"),
    IO_ERROR(214, "IO异常"),
    PARAM_ERROR(215, "参数错误"),
    CUSTOM_ERROR(216, "自定义异常"),
    ;

    private Integer code;

    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
