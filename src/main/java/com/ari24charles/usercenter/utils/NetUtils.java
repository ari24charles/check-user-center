package com.ari24charles.usercenter.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * 网络工具类
 *
 * @author ari24charles
 */
public class NetUtils {

    /**
     * 获取客户端的 IP
     *
     * @param request 客户端发起的请求
     * @return IP 地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) { // 获取本机 IP
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (inetAddress != null) {
                    ip = inetAddress.getHostAddress();
                }
            }
        }
        if (ip != null && ip.length() > 15) { // 多代理情况下，第一个 IP 为真实 IP
            // 多个 IP 使用 "," 进行分割
            if (ip.indexOf(",") > 0) {
                // 获取第一个 IP
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        if (ip == null) {
            return "127.0.0.1";
        }
        return ip;
    }
}
