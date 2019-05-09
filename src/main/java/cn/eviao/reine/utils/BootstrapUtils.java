package cn.eviao.reine.utils;

import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;

import java.nio.file.Paths;
import java.util.Optional;

public class BootstrapUtils {
    public final static String LAYOUT_PLACEHOLDER = "{{> layout}}";

    public final static String BOOTSTRAP_PATH = Paths.get("bootstrap",
            "dist", "bootstrap.hbs").toString();

    public static Single<String> loadBootstrap(Vertx vertx) {
        return vertx.fileSystem().rxReadFile(BOOTSTRAP_PATH).map(it -> it.toString());
    }

    public static String combineLayout(String bootstrap, String layout) {
        return Optional.ofNullable(bootstrap).map(it -> it.replace(LAYOUT_PLACEHOLDER, layout)).orElse(null);
    }
}
