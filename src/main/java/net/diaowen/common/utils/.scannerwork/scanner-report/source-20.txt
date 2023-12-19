package net.diaowen.common.utils;

/**
 * 包含切割ip字符串并返回ip数组、将字符串ip转化为长整数的方法
 */
public class IpUtils {
    private IpUtils(){

    }

    /**
     *     // 方法用于将 IP 段字符串切割并返回IP数组
     * @param ips //被切割的ip字符串
     * @return // 返回IP数组，包含原始IP和拼接后的IP
     */
    public static String[] getIps(String ips){
        // 使用"/"将 IP 段切割成两个部分
        String[] ip2 = ips.split("/");
        // 提取IP的第一段，即aaa.bbb.ccc.ddd中的aaa
        String ip0 = ip2[0];
        // 组装新的IP，取IP的第一段和第四段组成一个新的IP
        String ip1 = ip0.substring(0,ip0.lastIndexOf(".")+1)+ip2[1];
        // 返回IP数组，包含原始IP和拼接后的IP
        return new String[]{ip0,ip1};
    }

    /**
     * 方法用于将 IP 地址转换成长整型数字
     * @param ipAddress  //被转化的字符串ip
     * @return // 将各段 IP 转换成长整型后相加，并返回结果
     */
    public static long getIpNum(String ipAddress){
        // 按照"."切割 IP 地址
        String[] ip = ipAddress.split("\\.");
        // 将 IP 地址的各段转换成长整型数字
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        // 将各段 IP 转换成长整型后相加，并返回结果
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }


}
