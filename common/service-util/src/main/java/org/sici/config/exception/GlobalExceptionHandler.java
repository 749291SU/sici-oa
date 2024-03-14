package org.sici.config.exception;

import org.sici.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @projectName: oa-parent
 * @package: org.sici.handler
 * @className: GlobalExceptionHandler
 * @author: 749291
 * @description: TODO
 * @date: 1/31/2024 3:53 PM
 * @version: 1.0
 */

// global exception handler
@ControllerAdvice
public class GlobalExceptionHandler {
    // handle Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        // print StackTrace
        e.printStackTrace();
        return Result.fail("全局异常");
    }

    // add ArithmeticException handler
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result handleArithmeticException(ArithmeticException e) {
        // print StackTrace
        e.printStackTrace();
        return Result.fail("算术异常");
    }

    // add customer defined exception(SiwenException) handler
    @ExceptionHandler(SiwenException.class)
    @ResponseBody
    public Result handleSiwenException(SiwenException e) {
        // print StackTrace
        e.printStackTrace();
        return Result.fail("自定义异常");
    }
}
