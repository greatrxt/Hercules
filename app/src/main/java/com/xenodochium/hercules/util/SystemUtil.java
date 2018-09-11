package com.xenodochium.hercules.util;

import java.util.ArrayList;
import java.util.List;

public class SystemUtil {

    /**
     * Clone a list
     */
    public static <T> List<T> cloneList(List<T> original) {
        List<T> copy = new ArrayList<T>(original);
        return copy;
    }
}
