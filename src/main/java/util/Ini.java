package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ini文件读取
 * time: 2020/7/2 17:19
 *
 * @author msm
 */
public class Ini {
  private final Map<String, Map<String, String>> map = new HashMap<>(16);

  public Ini(String url) throws IOException {
    String[] key = new String[1];
    Files.lines(Paths.get(url)).forEach(o -> {
      String s = o.strip();
      int index = s.indexOf("#");
      if (index != -1) {
        s = s.substring(0, index);
      }
      if (s.isBlank()) {
        return;
      }
      Pattern pattern = Pattern.compile("\\[(.*?)]");
      Matcher matcher = pattern.matcher(s);
      if (matcher.find() && "[".equals(s.substring(0, 1))) {
        s = matcher.group(1);
        Map<String, String> tempMap = new HashMap<>(16);
        map.put(s, tempMap);
        key[0] = s;
      } else {
        String[] value = s.split("=");
        getMap(key[0]).put(value[0].strip(), value.length>1?value[1].strip():"");
      }
    });
  }

  private Map<String, String> getMap(String key) {
    return map.get(key);
  }

  public String get(String key) {
    String[] keys = key.split("\\.");
    return getMap(keys[0]).get(keys[1]);
  }

  @Override
  public String toString() {
    return map.toString();
  }
}
