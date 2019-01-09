package org.fine.springboot.multipartfile;


/**
 * 异常描述：
 * The temporary upload location [/tmp/tomcat.135042057.80/work/Tomcat/localhost/ROOT] is not valid
 * <p>
 * 原因：
 * 在linux系统中，springboot应用服务再启动（java -jar 命令启动服务）的时候，
 * 会在操作系统的/tmp目录下生成一个tomcat*的文件目录，上传的文件先要转换成临时文件保存在这个文件夹下面。
 * 由于临时/tmp目录下的文件，在长时间（10天）没有使用的情况下，就会被系统机制自动删除掉。
 * 所以如果系统长时间无人问津的话，就可能导致上面这个问题。
 * <p>
 * 解释: /tmp文件夹的有自动cleanup机制，/tmp文件夹的文件10天未更新会被移除，/var/tmp文件夹的文件是30天
 */
public class ErrorProcessor {


    /**
     * 解决办法一：
     * 启服务，临时方案：会重新生成tomcat目录，但是生产环境不建议如此操作；
     *
     * 解决办法二：
     * 注入一个Bean,手动配置临时目录，这个location可以理解为临时文件目录，
     * 我们可以通过配置location的值，使其指向我们的项目需要的临时文件的目录，
     * 文件上传临时路径
     * 在Spring Boot下配置location，可以在main()方法所在文件中添加如下代码：
     *
     * 或者配置
     * #指定一个上传文件的中间目录，如果没有指定该目录，中间目录默认会使用linux下的tmp目录
     * spring.servlet.multipart.location=/opt/data/tmp 并且创建该目录
     */
//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation("data/tmp");
//        return factory.createMultipartConfig();
//    }

}
