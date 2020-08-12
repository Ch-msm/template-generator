package generator;

import util.Ini;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议生成接口
 * time: 2020/8/3 15:39
 *
 * @author msm
 */
public class ProtoGenerator implements Generator {
  public Ini ini;

  /**
   * 模板生成
   *
   * @param ini  配置文件
   * @param path 模板路径
   */
  @Override
  public void generator(Ini ini, String path) {
    try {
      this.ini = ini;
      Path target = Paths.get( "generator",ini.get("common.dir"),ini.get("proto.protoName") + ".proto");
      Files.deleteIfExists(target);
      Files.createFile(target);
      List<String> collect = loadResource(path)
          .map(
              o -> {
                String value = replace(o, ini, "proto.protoName", "common.name", "proto.protoClass");
                value = info(value);
                return value;
              }
          ).collect(Collectors.toList());
      Files.write(target, collect);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String info(String value) {
    if (!value.contains("proto.info")) {
      return value;
    }
    value = "";
    if (ini.get("proto.info").isBlank()) {
      return value;
    }

    String[] array = ini.get("proto.info").split("\\|");
    StringBuilder valueBuilder = new StringBuilder(value);
    int i = 3;
    for (String o : array) {
      valueBuilder.append("  ").append(o).append(" = ").append(++i).append(";\n");
    }
    value = valueBuilder.toString().substring(0, valueBuilder.length() - 1);
    return value;
  }
}
