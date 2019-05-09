package cn.eviao.reine.utils;

import java.util.Optional;

public class BootstrapUtils {
    public final static String LAYOUT_PLACEHOLDER = "{{> layout}}";

    public static String combineLayout(String bootstrap, String layout) {
        return Optional.ofNullable(bootstrap).map(it -> it.replace(LAYOUT_PLACEHOLDER, layout)).orElse(null);
    }
}
