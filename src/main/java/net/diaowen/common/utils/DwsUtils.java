package net.diaowen.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by keyuan on 2019/8/5.
 * 这个类是一个工具类，包含了获取基础URL、将字符串处理成日期、文件下载的静态方法。
 */
public class DwsUtils {
    private DwsUtils(){

    }

    // 默认的基础URL
    public static String BASEURL_DEFAULT = "http://www.surveyform.cn";

    /**
     * 获取基础URL
     * @param request 服务端请求信息
     * @return baseUrl 获取到的基础URL
     */
    public static String baseUrl(HttpServletRequest request){
        //初始化基础URL为空字符串
        String baseUrl = "";
        //基础URL = 获取请求使用的协议 + :// + 获取请求的服务器名 + 检查请求的端口是否为标准的 HTTP 或 HTTPS 端口（80 或 443），如果不是，会拼接上端口号 +  获取请求的上下文路径
        baseUrl = request.getScheme() +"://" + request.getServerName()
                + (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" +request.getServerPort())
                + request.getContextPath();
        //返回获取到的基础URL
        return baseUrl;
    }

    /**
     * 将字符串表示的日期转换为 Date 对象，即将字符串转换为日期类型。
     * @param date 要转化的日期字符串
     * @return
     */
    public static Date str2Date(String date){
        //生成一个SimpleDateFormat的实例simpleDateFormat，用来将"yyyy-MM-dd HH：mm：ss"格式的日期字符串转换为 Date 对象或者将Data对象转换为"yyyy-MM-dd HH：mm：ss"的字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
        try {
            //将字符串data返回指定格式的日期
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            // 如果日期字符串格式错误，捕获 ParseException 异常并打印异常堆栈信息
            e.printStackTrace();
        }
        // 如果转换失败，则返回 null
        return null;
    }

    /**
     * 下载文件到 HttpServletResponse 中。
     *
     * @param response     HttpServletResponse 对象，用于向浏览器发送文件内容
     * @param fileName     要下载的文件名
     * @param downFilePath 下载文件的完整路径
     * @throws IOException 当发生 I/O 异常时抛出
     */
    public static void downloadFile(HttpServletResponse response, String fileName, String downFilePath) throws IOException {
        downFilePath = downFilePath.replace("/", File.separator);
        downFilePath = downFilePath.replace("\\", File.separator);
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

        try (FileInputStream in = new FileInputStream(downFilePath);
             OutputStream out = response.getOutputStream()) {

            byte buffer[] = new byte[1024];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            // 处理可能的异常
            e.printStackTrace();
        }
    }


}
