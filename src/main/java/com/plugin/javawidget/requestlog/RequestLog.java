package com.plugin.javawidget.requestlog;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.plugin.configproperty.Config;
import com.plugin.configproperty.ConfigValue;
import com.plugin.json.Json;
import org.apache.commons.lang3.StringEscapeUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

@Aspect
@Component
public class RequestLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLog.class);

    @ConfigValue(@Value("${SystemMsgEnums.HTTPSERVLETREQUEST.msg}"))
    private transient Config<String> HTTPSERVLETREQUEST;


    private class json extends Json {

        @Override
        protected String toJson(Object src) {
            return super.toJson(src);
        }

    }

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) or "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) or "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) or "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) or "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)  ")
    public Object validIdentityAndSecure(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = null;
        try {
            proceed = pjp.proceed();
        } catch (Exception e) {
            log(start,pjp,proceed);
            LOGGER.error("方法运行失败", e);
            throw e;
        }
        log(start,pjp,proceed);
        return proceed;
    }

    /**
     * 输出日志
     * @param start
     * @param pjp
     * @param proceed
     */
    private void log(long start, ProceedingJoinPoint pjp, Object proceed) {
        long end = System.currentTimeMillis();
        long excuteTime = end - start;
        try {
            getParameter(pjp, proceed, excuteTime);
        } catch (Exception e) {
            LOGGER.error("操作日志失败", e);
        }
    }

    /**
     * 解析参数
     * @param pjp Aop拦截对象
     * @param proceed 方法执行结果
     * @param excuteTime 执行时间
     */
    private void getParameter(ProceedingJoinPoint pjp, Object proceed, Long excuteTime) {
        Map<String, Object> linkMap = new LinkedHashMap<>();
        HttpServletRequest request = getRequest(pjp);
        if (request != null) {
            //当前方法对象描述
            final Signature signature = pjp.getSignature();
            final MethodSignature methodSignature = (MethodSignature) signature;
            String signaturename = pjp.getSignature().toString();
            //class上的注解
            final Annotation[] annotations = methodSignature.getMethod().getDeclaringClass().getAnnotations();
            linkMap.put("DeclaringClassAnnotations", Arrays.toString(annotations));
            //方法上面的注解
            final Annotation[] annotations1 = methodSignature.getMethod().getAnnotations();
            linkMap.put("MethodAnnotations", Arrays.toString(annotations1));
            linkMap.put("signature", signaturename);
            linkMap.put("url",request.getRequestURI());
            // ip
            String ip = request.getHeader("X-real-IP");
            linkMap.put("ip", StringUtils.isEmpty(ip) ? "-" : ip);
            // url参数
            Map<String, String[]> requestParam = request.getParameterMap();
            linkMap.put("requestParam", null == requestParam || requestParam.size() == 0 ? "-" : requestParam);
            /**
             * 原始body
             * {@link com.plugin.javawidget.driven.resolvers.JsonResolverImpl#resolveArgument}
             */
            final String attribute = (String)request.getAttribute(HTTPSERVLETREQUEST.get());
            /**
             * 获取到解析完成的参数，此对象获取值是共享
             */
            //TODO 2018.09.28 注意获取的对象全局共享
            /*Object[] args = pjp.getArgs();
            if (args != null && args.length > 0) {
                for (Object object : args) {
                    //输入
                    linkMap.put("requestBody",object);
                }
            }*/
            JsonObject asJsonObject = null;
            try{
                asJsonObject = new JsonParser().parse(attribute).getAsJsonObject();
                linkMap.put("requestBody",asJsonObject);
            }catch (Exception e){
                linkMap.put("requestBody",attribute);
            }
            linkMap.put("responseBody", proceed);
            String OS = request.getHeader("User-Agent");
            linkMap.put("OS", OS);
        }
        // 操作时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        linkMap.put("operatingTime", simpleDateFormat.format(date));
        // 执行时间
        linkMap.put("excuteTime", excuteTime);
        LOGGER.info(StringEscapeUtils.unescapeJson(new json().toJson(linkMap)));
    }

    /**
     * 获取到 HttpServletRequest 对象
     *
     * @param pjp
     * @return
     */
    private HttpServletRequest getRequest(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        HttpServletRequest request;
        if (args != null) {
            for (Object object : args) {
                if (object instanceof HttpServletRequest) {
                    request = (HttpServletRequest) object;
                    return request;
                }
            }
        }
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            request = sra.getRequest();
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
        return request;
    }
}
