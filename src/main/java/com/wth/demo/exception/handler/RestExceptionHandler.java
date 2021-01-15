package com.wth.demo.exception.handler;


import com.wth.demo.exception.BusinessException;
import com.wth.demo.exception.code.BaseResponseCode;
import com.wth.demo.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {


    /**
     * 系统异常捕获处理
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler(Exception.class)
    public <T>DataResult<T> handleException(Exception e){
        log.error("handleException...{}",e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_ERROR);
    }

    /**
     * 自定义运行异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public DataResult businessExceptionHandler(BusinessException e){
        log.error("BusinessException,exception{}",e);
        return DataResult.getResult(e.getMessageCode(),e.getDetailMessage());
    }

    /**
     * 处理validation框架异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataResult methodArgumentNotVolidExceptionHandler(MethodArgumentNotValidException e){
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        log.error("MethodArgumentNotValidException allError:{} MethodArgumentNotValidException:{}",e.getBindingResult().getAllErrors(),e);
        return createValidExceptionResp(allErrors);

    }

    private DataResult createValidExceptionResp(List<ObjectError> errors){
        String[] msgs = new String[errors.size()];
        int i = 0;
        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            log.info("msg={}",msgs[i]);
            i++;
        }

        return DataResult.getResult(BaseResponseCode.VALIDATOR_ERROR.getCode(),msgs);
    }

    /**
     * 没有权限的异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public DataResult unauthorizedException(UnauthorizedException e){
        log.error("UnauthorizedException....{}",e.getMessage());
        return DataResult.getResult(BaseResponseCode.NOT_PERMISSION);
    }
}
