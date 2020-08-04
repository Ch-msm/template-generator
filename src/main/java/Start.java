import generator.HtmlGenerator;
import generator.JavaGenerator;
import generator.ProtoGenerator;
import generator.RestGenerator;
import util.Ini;

import java.io.File;
import java.io.IOException;


/**
 * time: 2020/7/2 16:33
 *
 * @author msm
 */
public class Start {
  public static void main(String[] args) throws IOException {
    String dir = getResourceDir();
    Ini ini = new Ini(dir + "build/patrol_config.conf");
    //生成协议
    new ProtoGenerator().generator(ini, dir + "template/template.proto");
    //生成java文件
    new JavaGenerator().generator(ini, dir + "template/dy_TemplateService.java");
    //生成rest文件
    new RestGenerator().generator(ini, dir + "template/rest_Template.java");
    //生成html文件
    new HtmlGenerator().generator(ini, dir + "template/dy.html");
  }

  /**
   * 获取资源目录
   */
  public static String getResourceDir() {
    String dir = Start.class.getResource("build").getPath().substring(1);
    return new File(dir).getParent() + File.separator;
  }
}
