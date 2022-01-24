package io.castled.utils;

import io.castled.ObjectRegistry;
import io.castled.configuration.DocConfiguration;

public class DocUtils {

    public static String constructDocUrl(String docPath) {
        if (docPath == null) {
            return null;
        }
        return String.format("%s/%s", ObjectRegistry.getInstance(DocConfiguration.class).getDocUrl(), docPath);
    }
}
