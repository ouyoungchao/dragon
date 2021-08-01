package com.shiliu.dragon.utils.random;

import java.util.UUID;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class RandomUtils {

    public static String getDefaultRandom() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
