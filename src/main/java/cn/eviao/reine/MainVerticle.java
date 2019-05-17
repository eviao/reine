package cn.eviao.reine;

import cn.eviao.reine.bean.definition.Reine;
import cn.eviao.reine.codec.ReineMessageCodec;
import cn.eviao.reine.verticle.DataSourceVerticle;
import cn.eviao.reine.verticle.RequestVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class MainVerticle extends AbstractVerticle {

    private final Logger logger = LogManager.getLogger(MainVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.exceptionHandler(err -> logger.error(err));
        vertx.eventBus().registerDefaultCodec(Reine.class, new ReineMessageCodec());

        Function<String, Future<String>> deployRequestVerticle = it ->
                Future.future(future -> vertx.deployVerticle(new RequestVerticle(), new DeploymentOptions(config()), future));
        Function<String, Future<String>> deployDataSourceVerticle = it ->
                Future.future(future -> vertx.deployVerticle(new DataSourceVerticle(), new DeploymentOptions(config()), future));

        Future.<String>succeededFuture()
                .compose(deployRequestVerticle)
                .compose(deployDataSourceVerticle)
                .setHandler(result -> {
                    if (result.succeeded()) {
                        logger.info("starting successful.");
                        startFuture.complete();
                    } else {
                        logger.error("starting failed: {}", result.cause().getMessage());
                        startFuture.fail(result.cause());
                    }
                });
    }
}
