package cn.eviao.reine;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        var router = Router.router(vertx);

        router.route().handler( routingContext -> {
            routingContext.response()
                .putHeader("content-type", "text/plain")
                .end("hello, world!");
        });

        vertx.createHttpServer().requestHandler(router)
                .rxListen(config().getInteger("http.port", 8080))
                .subscribe(serv -> {
                    startFuture.complete();
                }, err -> {
                    startFuture.fail(err);
                });
    }
}
