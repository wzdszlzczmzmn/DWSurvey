package net.diaowen.dwsurvey;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * SpringBoot应用程序的启动类，用于启动SpringBoot应用程序
 */
@SpringBootApplication
@ComponentScan(basePackages = {"net.diaowen.common","net.diaowen.dwsurvey"})
public class DwsurveyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DwsurveyApplication.class, args);
    }

    /**
     * 返回TomcatServletWebServerFactory对象,并对其进行自定义配置,禁用了Tomcat容器对JAR包中的Manifest文件的扫描
     *
     * @return TomcatServletWebServerFactory对象
     */
    @Bean
    public TomcatServletWebServerFactory tomcatFactory(){
        return new TomcatServletWebServerFactory(){

            @Override
            protected void postProcessContext(Context context) {
                // 禁用了Tomcat容器对JAR包中的Manifest文件的扫描
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
            }
        };
    }
}
