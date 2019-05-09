package cn.eviao.reine.utils;

import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;

import java.nio.file.Paths;
import java.util.Optional;

public class TemplateUtils {
    public final static String TEMPLATE_ROOT = "templates";
    public final static String TEMPLATE_SUFFIX = ".reine.xml";

    public static Single<String> loadTemplate(Vertx vertx, String name) {
        String filePath = Optional.of(name).map(it -> it + TEMPLATE_SUFFIX)
                .map(it -> Paths.get(TEMPLATE_ROOT, it).toString()).orElse(null);
        return vertx.fileSystem().rxReadFile(filePath).map(it -> it.toString());
    }
}
