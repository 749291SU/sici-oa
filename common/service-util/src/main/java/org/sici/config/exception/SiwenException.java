package org.sici.config.exception;

/**
 * @projectName: oa-parent
 * @package: org.sici.config.exception
 * @className: SiwenException
 * @author: 749291
 * @description: TODO
 * @date: 1/31/2024 4:07 PM
 * @version: 1.0
 */

public class SiwenException extends RuntimeException{
    // code
    private Integer code;
    // message
    private String message;

    // constructor
    public SiwenException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
