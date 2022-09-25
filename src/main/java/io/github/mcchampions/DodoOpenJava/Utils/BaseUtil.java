package io.github.mcchampions.DodoOpenJava.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 一些常用的方法
 */
public class BaseUtil {
    /**
     * 拼接 Authorization
     *
     * @param clientId 机器人唯一标示
     * @param token    机器人鉴权Token
     * @return 返回拼接后的文本
     */
    public static String Authorization(String clientId, String token) {
        return "Bot " + clientId + "." + token;
    }

    /**
     * Object集合转String集合
     * @param list Object集合
     * @return String集合
     */
    public static List<String> toStringList(List<Object> list) {
        List<String> returnList = new ArrayList<>();
        for (Object o : list) {
            returnList.add(o.toString());
        }
        return returnList;
    }
}