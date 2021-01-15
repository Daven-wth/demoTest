package com.wth.demo.exception;

import com.wth.demo.exception.code.ResponseCodeInterface;

public class BusinessException extends RuntimeException {

    private int messageCode;

    private String detailMessage;

    public BusinessException(int messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
        this.detailMessage = message;
    }

    /**
     * 构造函数
     * @param responseCodeInterface
     */
    public BusinessException(ResponseCodeInterface responseCodeInterface) {
        this(responseCodeInterface.getCode(),responseCodeInterface.getMsg());
    }

    public int getMessageCode() {
        return messageCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
