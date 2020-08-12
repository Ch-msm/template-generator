package generator;

import util.Ini;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

/**
 * js生成
 * time: 2020/8/3 15:39
 *
 * @author msm
 */
public class JsGenerator implements Generator {
  public Ini ini;
  private boolean ifIs = false;

  /**
   * 模板生成
   *
   * @param ini  配置文件
   * @param path 模板路径
   */
  @Override
  public void generator(Ini ini, String path) {
    this.ini = ini;
    try {
      Path target = Paths.get("generator",ini.get("common.dir"), ini.get("html.name") + ".js");
      Files.deleteIfExists(target);
      Files.createFile(target);
      List<String> collect = loadResource(path)
          .map(
              o -> {
                String value = replace(o, ini,
                    "html.name", "html.title", "rest.rootPath", "html.jsVarName",
                    "js.listView","js.listData","html.toggle1Name","html.toggle2Name",
                    "html.toggle1Data","html.toggle2Data",
                    "html.toggle1","html.toggle2"
                );
                value = ifIs(value);
                return !ifIs ? value : "";
              }
          ).filter(o -> !o.isBlank()).collect(Collectors.toList());
      Files.write(target, collect);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String ifIs(String value) {
    if (!value.contains("if:")) {
      return value;
    }
    if (value.contains("</if:")) {
      ifIs = false;
    } else {
      Pattern p = compile(":(.*?)>");
      Matcher m = p.matcher(value);
      while (m.find()) {
        //m.group(1)不包括这两个字符
        if (!"是".equals(ini.get("html." + m.group(1)))) {
          ifIs = true;
        }
      }
    }
    return "";
  }
}
