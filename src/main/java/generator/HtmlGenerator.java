package generator;

import util.Ini;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

/**
 * html生成
 * time: 2020/8/3 15:39
 *
 * @author msm
 */
public class HtmlGenerator implements Generator {
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
      Path target = Paths.get("generator/web/" + ini.get("html.name") + ".html");
      Files.deleteIfExists(target);
      Files.createFile(target);
      List<String> collect = loadResource(path)
          .map(
              o -> {
                String value = replace(o, ini, "html.name", "html.title", "html.path", "html.jsVarName");
                value = toggle(value);
                value = ifIs(value);
                value = advancedSearchItem(value);
                return !ifIs ? value : "";
              }
          ).filter(o -> !o.isBlank()).collect(Collectors.toList());
      Files.write(target, collect);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 切换按钮
   *
   * @param value 值
   * @return value
   */
  private String toggle(String value) {
    if (!value.contains("html.toggle")) {
      return value;
    }
    if (!ini.get("html.toggle1").isEmpty()) {
      value = value.replaceAll("\\$\\{html.toggle1}", " id=\"" + ini.get("html.toggle1") + "\"");
    } else {
      value = value.replaceAll("\\$\\{html.toggle1}", "");
    }
    if (!ini.get("html.toggle2").isEmpty()) {
      value = value.replaceAll("\\$\\{html.toggle2}", " id=\"" + ini.get("html.toggle2") + "\"");
    } else {
      value = value.replaceAll("\\$\\{html.toggle2}", "");
    }
    return value;
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

  public String advancedSearchItem(String value) {
    if (!value.contains("html.advancedSearchItem")) {
      return value;
    }
    if (!"是".equals(ini.get("html.advancedSearch"))) {
      return value;
    }
    value = "";
    String[] array = ini.get("html.advancedSearchItem").split("\\|");
    StringBuilder valueBuilder = new StringBuilder(value);
    for (String o : array) {
      valueBuilder.append("        <section id=\"").append(o).append("\"></section>\n");
    }
    value = valueBuilder.toString().substring(0, valueBuilder.length() - 1);
    return value;
  }

}
