package cn.eviao.reine.utils;

import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;

import java.nio.file.Paths;
import java.util.Optional;

public class FileLoaderUtils {
    public final static String TEMPLATE_ROOT = "templates";
    public final static String TEMPLATE_SUFFIX = ".reine.xml";

    public final static String BOOTSTRAP_PATH = Paths.get("bootstrap",
            "dist", "index.hbs").toString();

    public static Single<String> loadTemplate(Vertx vertx, String name) {
        String filePath = Optional.of(name).map(it -> it + TEMPLATE_SUFFIX)
                .map(it -> Paths.get(TEMPLATE_ROOT, it).toString()).orElse(null);
        return vertx.fileSystem().rxReadFile(filePath).map(it -> it.toString());
    }

    public static Single<String> loadBootstrap(Vertx vertx) {
        return vertx.fileSystem().rxReadFile(BOOTSTRAP_PATH).map(it -> it.toString());
    }
}
