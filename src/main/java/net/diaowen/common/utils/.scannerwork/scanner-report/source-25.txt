package net.diaowen.common.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

/**
 * UserAgentUtils类包含两个方法：userAgent和userAgentInt。
 * userAgent方法接受一个HttpServletRequest对象作为参数，并从中获取"User-Agent"请求头。
 * 然后，它使用UserAgentUtils.parseUserAgentString()方法解析用户代理字符串，并将解析后的UserAgent对象返回。
 * userAgentInt方法也接受一个HttpServletRequest对象作为参数，并从中获取"User-Agent"请求头。
 * 然后，它使用UserAgentUtils.parseUserAgentString()方法解析用户代理字符串，并根据解析结果将操作系统、浏览器和设备类型转换为整数数组。
 */
public class UserAgentUtils {
    private UserAgentUtils(){

    }

    /**
     * userAgent方法接受一个HttpServletRequest对象作为参数，并从中获取"User-Agent"请求头。
     * 然后，它使用UserAgentUtils.parseUserAgentString()方法解析用户代理字符串，并将解析后的UserAgent对象返回。
     * @param request HttpServletRequest对象
     * @return UserAgent对象
     */
   public static UserAgent userAgent(HttpServletRequest request){
//        如下，我们获取了一个agent的字符串：
//        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36"
//        由此，通过User-agent-utils解析：
        String agent=request.getHeader("User-Agent");
        //解析agent字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        return userAgent;
    }



    /**
     * userAgentInt方法也接受一个HttpServletRequest对象作为参数，并从中获取"User-Agent"请求头。
     * 然后，它使用UserAgentUtils.parseUserAgentString()方法解析用户代理字符串，并根据解析结果将操作系统、浏览器和设备类型转换为整数数组。
     * @param request HttpServletRequest对象
     * @return 整数数组，表示操作系统、浏览器和设备类型
     */
   public static int[] userAgentInt(HttpServletRequest request){
        int[] result = new int[]{0,0,0};
        try{
            //获取请求头中的User-Agent
            String agent=request.getHeader("User-Agent");
            if(agent!=null){
                //解析User-Agent字符串
                UserAgent userAgentObj = UserAgent.parseUserAgentString(agent);
                //获取浏览器
                Browser browser = userAgentObj.getBrowser();
                //获取操作系统
                OperatingSystem operatingSystem = userAgentObj.getOperatingSystem();

                //获取浏览器组
                Browser browserGroup = browser.getGroup();
                //获取操作系统组
                OperatingSystem sysGroup = operatingSystem.getGroup();
                //获取设备类型
                DeviceType deviceType = operatingSystem.getDeviceType();

                Integer sys = 0;
                //判断操作系统
                if(OperatingSystem.ANDROID == sysGroup){
                    sys=1;
                }else if(OperatingSystem.WINDOWS == sysGroup ){
                    sys=2;
                }else if(OperatingSystem.IOS == sysGroup){
                    sys=3;
                }else if(OperatingSystem.MAC_OS == sysGroup || OperatingSystem.MAC_OS_X == sysGroup){
                    sys=4;
                }
                result[0] = sys;

                Integer bro = 0;
                //判断浏览器
                if(browserGroup.IE == browserGroup){
                    bro=1;
                }else if(browserGroup.SAFARI == browserGroup){
                    bro=2;
                }else if(browserGroup.FIREFOX == browserGroup){
                    bro=3;
                }else if(browserGroup.OPERA == browserGroup){
                    bro=4;
                }else if(browserGroup.CHROME == browserGroup){
                    bro=5;
                }
                result[1] = bro;

                Integer dt=0;
                //判断设备类型
                if(DeviceType.COMPUTER == deviceType){
                    dt=1;
                }else if(DeviceType.MOBILE == deviceType){
                    dt=2;
                }
                result[2] = dt;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
