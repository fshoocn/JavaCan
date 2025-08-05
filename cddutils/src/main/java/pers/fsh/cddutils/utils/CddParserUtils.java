package pers.fsh.cddutils.utils;

import lombok.extern.log4j.Log4j2;

import java.util.Map;

/**
 * @author fanshuhua
 * @date 2025/4/28 15:58
 */
@Log4j2
public class CddParserUtils {
    public static <T> T getFromIdMap(Map<String, Object> idMap, String id, Class<T> clazz) {
        if (!idMap.containsKey(id)) {
            log.error("未在idMap中找到对应数据 id:{}", id);
            throw new RuntimeException("未在idMap中找到对应数据 id:" + id);
        }
        Object o = idMap.get(id);
        if (!clazz.isInstance(o)) {
            log.error("id:{},对应数据类型错误,预期是{},实际是{}", id, clazz.getName(), o.getClass().getName());
            throw new RuntimeException();
        }
        return clazz.cast(o);
    }
}
