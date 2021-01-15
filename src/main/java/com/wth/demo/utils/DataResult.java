package com.wth.demo.utils;


import com.wth.demo.exception.code.BaseResponseCode;
import com.wth.demo.exception.code.ResponseCodeInterface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataResult<T> {
    /**
     * 请求响应code 0为成功 其他为失败
     */
    @ApiModelProperty(value = "请求响应code，0为成功 其他为失败",name = "code")
    private int code = 0;

    /**
     * 响应异常码详细信息
     */
    @ApiModelProperty(value = "响应异常码详细信息",name = "msg")
    private String msg;

    /**
     * 响应内容 code为0 返回数据
     */
    @ApiModelProperty(value = "需要返回的数据", name = "data")
    private T data;

    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.msg = null;
    }


    public  DataResult(ResponseCodeInterface responseCodeInterface){
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
        this.data = null;
    }

    public  DataResult(ResponseCodeInterface responseCodeInterface,T data){
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
        this.data = data;
    }

    /**
     * 自定义返回操作 data 可控
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> DataResult getResult(int code,String msg,T data){
        return new DataResult(code,msg,data);
    }

    /**
     * 自定义返回 data 为空
     * @param code
     * @param msg
     * @return
     */
    public static DataResult getResult(int code,String msg){
        return new DataResult(code,msg);
    }

    public static <T> DataResult getResult(int code,T data){
        return new DataResult(code,data);
    }

    public static DataResult getResult(ResponseCodeInterface responseCodeInterface){
        return new DataResult(responseCodeInterface);
    }

    public static <T>DataResult getResult(ResponseCodeInterface responseCodeInterface,T data){
        return new DataResult(responseCodeInterface,data);
    }

    /**
     * 自定义返回 入参一般是异常code枚举 data为空
     * @param baseResponseCode
     * @return
     */
    public static DataResult getResult(BaseResponseCode baseResponseCode){
        return new DataResult(baseResponseCode);
    }

    /**
     * 自定义返回 入参一般是异常code枚举 data可控
     * @param baseResponseCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T>DataResult getResult(BaseResponseCode baseResponseCode,T data){
        return new DataResult(baseResponseCode,data);
    }


    /**
     * 默认成功的响应
     */
    public DataResult(){
        this.code = BaseResponseCode.SUCCESS.getCode();
        this.msg = BaseResponseCode.SUCCESS.getMsg();
        this.data = null;
    }

    /**
     * 默认成功返回数据
     */
    public DataResult(T data){
        this.code = BaseResponseCode.SUCCESS.getCode();
        this.msg = BaseResponseCode.SUCCESS.getMsg();
        this.data = data;
    }

    /**
     * 默认成功的静态方法
     * 操作成功 data为null
     */
    public static DataResult success(){
        return new DataResult();
    }

    /**
     * 默认成功的静态方法
     * 操作成功 data不为null
     */
    public static <T>DataResult success(T data){
        return new DataResult(data);
    }



}
