package generator;

import util.Ini;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * 生成接口
 * time: 2020/8/3 15:17
 *
 * @author msm
 */
public interface Generator {
  /**
   * 加载模板
   *
   * @param path 模板路径
   * @return 模板内容字符串流
   * @throws Exception 异常
   */
  default Stream<String> loadResource(String path) throws Exception {
    return Files.lines(Paths.get(path));
  }

  default String replace(String str, Ini ini, String... attr) {
    for (int i = 0; i < attr.length; i++) {
      str = str.replaceAll("\\$\\{" + attr[i] + "}", ini.get(attr[i]));
    }
    return str;
  }

  /**
   * 模板生成
   *
   * @param ini  配置文件
   * @param path 模板路径
   */
  void generator(Ini ini, String path);


}
