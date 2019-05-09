package cn.eviao.reine.verticle;

import cn.eviao.reine.bean.definition.Reine;
import cn.eviao.reine.compiler.TemplateCompiler;
import cn.eviao.reine.compiler.TemplateCompilerFactory;
import cn.eviao.reine.constant.Address;
import cn.eviao.reine.resolver.DefinitionResolver;
import cn.eviao.reine.resolver.XMLDefinitionResolver;
import cn.eviao.reine.utils.BootstrapUtils;
import cn.eviao.reine.utils.FileLoaderUtils;
import com.alibaba.fastjson.JSON;
import io.reactivex.Single;
import io.reactivex.internal.functions.Functions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.stream.Collectors;

public class RequestVerticle extends AbstractVerticle {

    private final Logger logger = LogManager.getLogger(RequestVerticle.class);

    private final static String CONTENT_HTML_TYPE = "text/html; charset=utf-8";

    private final TemplateCompiler compiler = TemplateCompilerFactory.factory();
    private final DefinitionResolver resolver = new XMLDefinitionResolver();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);
        router.route("/preview/:template").handler(this::handlePreview);
        router.route().last().handler(routingContext -> routingContext.fail(404));

        vertx.createHttpServer().requestHandler(router)
                .rxListen(config().getInteger("http.port", 8080))
                .subscribe(server -> {
                    logger.info("server has been started.");
                    startFuture.complete();
                }, err -> {
                    logger.error("server startup failure.");
                    startFuture.fail(err);
                });
    }

    private Map<String, String> transformParams(RoutingContext routingContext) {
        return routingContext.request().params().entries().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Single<Reine> resolveDefinition(String name) {
        return FileLoaderUtils.loadTemplate(vertx, name).flatMap(resolver::apply);
    }

    private Single<Map> loadDataSource(Reine reine, Map<String, String> params) {
        JsonObject message = new JsonObject().put("reine", reine.toJson()).put("params", params);
        return vertx.eventBus().<String>rxSend(Address.DATASOURCE_QUERY, message)
                .map(it -> it.body()).map(it -> JSON.parseObject(it, Map.class));
    }

    private Single<String> combineLayout(String layout) {
        return FileLoaderUtils.loadBootstrap(vertx).map(it -> BootstrapUtils.combineLayout(it, layout));
    }

    private void handlePreview(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", CONTENT_HTML_TYPE);

        Map<String, String> params = transformParams(routingContext);

        resolveDefinition(params.get("template"))
                .flatMap(reine -> {
                    Single<String> layout = combineLayout(reine.getLayout());
                    Single<Map> context = loadDataSource(reine, params);
                    return Single.zip(layout, context, compiler::apply);
                })
                .flatMap(Functions.identity())
                .subscribe(result -> {
                    response.end(result.toString());
                }, err -> {
                    response.setStatusCode(500).end(err.getMessage());
                    err.printStackTrace();
                });
    }

}
