package com.yisen.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.LongByteArray;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author rainluo
 * @version 1.0
 * @description IP地址工具类，集成ip2region实现IP归属地查询，可脱离公网服务高速本地化查询。
 * @date 2025/11/14 10:13
 */
@Slf4j
public class IpUtil {

    private static final Version version = Version.IPv4;
    private static Searcher IP_SEARCHER;

    /**
     * 初始化数据
     *
     * @param filePath 文件路径
     */
    public static void init(String filePath) {

        try {
            LongByteArray longByteArray = Searcher.loadContentFromFile(filePath);
            IP_SEARCHER = Searcher.newWithBuffer(version, longByteArray);

        } catch (Throwable e) {
            log.error("初始化ip2region.xdb文件失败,报错信息:[{}]", e.getMessage(), e);
            throw new RuntimeException("系统异常!");
        }
    }


    /**
     * 自定义解析ip地址
     *
     * @param ipStr ipStr
     * @return 返回结果例 [河南省, 洛阳市, 洛龙区]
     */
    public static List<String> getRegionList(String ipStr) {
        List<String> regionList = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(ipStr)) {
                return regionList;
            }
            ipStr = ipStr.trim();
            String region = IP_SEARCHER.search(ipStr);
            String[] split = region.split("\\|");
            regionList.addAll(Arrays.asList(split));
        } catch (Exception e) {
            log.error("解析ip地址出错", e);
        }
        return regionList;
    }

    /**
     * 自定义解析ip地址
     *
     * @param ipStr ipStr
     * @return 返回结果例 河南省|洛阳市|洛龙区
     */
    public static String getRegion(String ipStr) {
        try {
            if (StringUtils.isEmpty(ipStr)) {
                return "";
            }
            ipStr = ipStr.trim();
            return IP_SEARCHER.search(ipStr);
        } catch (Exception e) {
            log.error("解析ip地址出错", e);
            return "";
        }
    }

    /**
     * 获取本机第一个ip
     *
     * @return
     */
    public static String getLocalFirstIp() {
        List<String> list = getLocalIp();
        return !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 获取本机ip
     *
     * @return
     */
    public static List<String> getLocalIp() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 排除回环地址和IPv6地址
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    /**
     * 获取客户端真实IP地址，考虑多级代理等情况
     *
     * @param request HttpServletRequest对象
     * @return 客户端真实IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        // 优先级依次检测常见代理头
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For 可能有多个ip，取第一个非unknown的
                if (ip.contains(",")) {
                    for (String s : ip.split(",")) {
                        s = s.trim();
                        if (!StringUtils.isBlank(s) && !"unknown".equalsIgnoreCase(s)) {
                            return s;
                        }
                    }
                } else {
                    return ip;
                }
            }
        }

        // 未经过代理，直接取远端ip
        String ip = request.getRemoteAddr();

        // 可能仍然是本地回环地址
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            // 获取本地真正的IP地址
            try {
                InetAddress inet = InetAddress.getLocalHost();
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            } catch (Exception e) {
                log.error("获取本地IP地址异常", e);
            }
        }

        return ip;
    }
}
