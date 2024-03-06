package com.ari24charles.usercenter.aop;

import cn.hutool.core.util.IdUtil;
import com.ari24charles.usercenter.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志
 *
 * @author ari24charles
 */
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    /**
     * 拦截所有控制层的请求响应，获取请求信息以及方法执行的时间
     */
    @Around("execution(* com.ari24charles.usercenter.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成唯一 id
        String requestId = IdUtil.simpleUUID();
        // 获取客户端 IP
        String ip = NetUtils.getIpAddress(httpServletRequest);
        // 获取请求路径
        String uri = httpServletRequest.getRequestURI();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        String requestParam = "[" + StringUtils.join(args, ", ") + "]";
        // 输出请求日志
        log.info("request <{}> start, ip: {}, path: {}, params: {}", requestId, ip, uri, requestParam);
        // 开始计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            // 执行方法
            return joinPoint.proceed();
        } finally {
            // 结束计时
            stopWatch.stop();
            Long totalTimeMillis = stopWatch.getTotalTimeMillis();
            // 输出响应日志
            log.info("request <{}> end, cost {}ms", requestId, totalTimeMillis);
        }
    }
}
