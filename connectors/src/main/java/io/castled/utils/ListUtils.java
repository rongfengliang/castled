package io.castled.utils;

import java.util.List;

public class ListUtils {

    public static <T> T nullOnIndexOutOfBounds(List<T> list, int index) {
        if (list.size() > index) {
            return list.get(index);
        }
        return null;
    }
}
