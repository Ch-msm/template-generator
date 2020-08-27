package start;

import generator.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import util.Ini;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * time: 2020/7/2 16:33
 *
 * @author msm
 */
@SpringBootApplication
@Controller
public class Start implements WebMvcConfigurer {
  private static String path;

  public static void main(String[] args) throws IOException {
    path = Paths.get("generator").toAbsolutePath().toString();
    String dir = getResourceDir();
    run(dir);
//    SpringApplication.run(Start.class, args);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/file/**").addResourceLocations(
        "file:" + path + "/");
    System.out.println("自定义静态资源目录、此处功能用于文件映射");
  }

  @RequestMapping("hello")
  public String hello() {
    return "/config.js";
  }

  public static void run(String dir) throws IOException {

    Ini ini = new Ini(dir + "build/spare_part/info.conf");
    createDir(ini.get("common.dir"));

    //生成协议
    new ProtoGenerator().generator(ini, dir + "template/template.proto");
    //生成java文件
    new JavaGenerator().generator(ini, dir + "template/dy_TemplateService.java");
    //生成rest文件
    new RestGenerator().generator(ini, dir + "template/rest_Template.java");
    //生成html文件
    new HtmlGenerator().generator(ini, dir + "template/dy.html");
    //生成js文件
    new JsGenerator().generator(ini, dir + "template/dy_js.tp");
  }

  /**
   * 获取资源目录
   */
  public static String getResourceDir() {
    String dir = Start.class.getResource("/build").getPath().substring(1);
    return new File(dir).getParent() + File.separator;
  }

  /**
   * 创建目标目录
   */
  public static void createDir(String dir) {
    try {
      Files.createDirectories(Paths.get("generator", dir));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
