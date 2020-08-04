package generator;

import util.Ini;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * rest生成
 * time: 2020/8/3 15:39
 *
 * @author msm
 */
public class RestGenerator implements Generator {

  /**
   * 模板生成
   *
   * @param ini  配置文件
   * @param path 模板路径
   */
  @Override
  public void generator(Ini ini, String path) {
    try {
      Path target = Paths.get("generator/rest/" + ini.get("rest.className") + ".java");
      Files.deleteIfExists(target);
      Files.createFile(target);
      List<String> collect = loadResource(path)
          .map(
              o -> replace(o, ini,
                  "common.author", "common.name", "common.time",
                  "rest.className", "rest.packageName","rest.rootPath",
                  "proto.protoName", "proto.protoClass"
              )
          ).collect(Collectors.toList());
      Files.write(target, collect);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
