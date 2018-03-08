package org.lordstar.learning.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lordstar.learning.zookeeper.ZookeeperTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static Logger logger = Logger.getLogger(ConfigUtil.class);

    private ConfigUtil() {}

    private static ConfigUtil util = new ConfigUtil();

    public static ConfigUtil getInstance() {
        return util;
    }

    public String getConfigValue(String key) {
        Properties pro = new Properties();
        InputStream in = null;
        in = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            pro.load(in);
        } catch (IOException e) {
            logger.error("读取配置文件错误: " + e);
        }
        String value = pro.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            logger.error("配置文件" + key + "读取失败为空");
            return "";
        }
        return value;
    }
}
