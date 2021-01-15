package com.wth.demo.shiro;

import com.alibaba.fastjson.JSON;
import com.wth.demo.constants.Constant;
import com.wth.demo.exception.BusinessException;
import com.wth.demo.exception.code.BaseResponseCode;
import com.wth.demo.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;


@Slf4j
public class CustonAccessControlerFilter extends AccessControlFilter {


    /**
     * 是否允许访问
     * true 允许交给下一个filter处理
     * false 会往下执行onAccessDenied
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }


    /**
     * 表示访问拒绝时是否自己处理
     * 如果返回true表示自己不处理且继续拦截器链执行
     * 返回false表示自己已经处理了（比如直接响应前端）不会流转到链式调用
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("request 接口请求方式： {}",request.getMethod());
        log.info("request 接口地址： {}",request.getRequestURL().toString());
        //判断客户端是否携带token
        try{
            String token = request.getHeader(Constant.ACCESS_TOKEN);
            if(StringUtils.isEmpty(token)){
                throw new BusinessException(BaseResponseCode.TOKEN_NOT_NULL);
            }
            CustomPasswordToken customPasswordToken = new CustomPasswordToken(token);
            //主体提交认证
            getSubject(servletRequest,servletResponse).login(customPasswordToken);
        }catch (BusinessException e){
            customResponse(servletResponse,e.getMessageCode(),e.getDetailMessage());
            return false;
        }catch (AuthenticationException e){
            if (e.getCause() instanceof BusinessException){
                BusinessException exception = (BusinessException) e.getCause();
                customResponse(servletResponse,exception.getMessageCode(),exception.getDetailMessage());
            } else {
                customResponse(servletResponse,BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getCode(),BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getMsg());
            }
            return false;
        }catch (Exception e){
            customResponse(servletResponse,BaseResponseCode.SYSTEM_ERROR.getCode(),BaseResponseCode.SYSTEM_ERROR.getMsg());
            return false;
        }
        return true;
    }


    /**
     * 自定义响应前端方法
     * @param response
     * @param code
     * @param msg
     */
    private void customResponse(ServletResponse response,int code,String msg){
        try {
            DataResult dataResult = DataResult.getResult(code,msg);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setCharacterEncoding("UTF-8");
            String str = JSON.toJSONString(dataResult);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(str.getBytes("UTF-8"));
            outputStream.flush();
        } catch (IOException e) {
           log.error("customResponse...{}",e);
        }
    }

}
