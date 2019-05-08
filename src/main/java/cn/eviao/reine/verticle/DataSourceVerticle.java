package cn.eviao.reine.verticle;

import cn.eviao.reine.bean.definition.DataSet;
import cn.eviao.reine.bean.definition.Reine;
import cn.eviao.reine.constant.Address;
import cn.eviao.reine.dao.QueryExecutor;
import com.alibaba.fastjson.JSON;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DataSourceVerticle extends AbstractVerticle {

    private final Logger logger = LogManager.getLogger(DataSourceVerticle.class);

    private final QueryExecutor queryExecutor = new QueryExecutor(this::getOrInitClient);

    private final Map<String, JDBCClient> clients = new HashMap();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(Address.DATASOURCE_QUERY, this::executorQuery);
    }

    private void executorQuery(Message<JsonObject> message) {
        Map params = message.body().getJsonObject("params").getMap();
        Reine reine = Reine.of(message.body().getJsonObject("reine"));

        queryExecutor.apply(reine.getDataSets(), params).subscribe(result -> {
            message.reply(JSON.toJSONString(result));
        }, err -> {
            logger.error("execute query command", err);
            message.fail(500, err.getMessage());
        });
    }

    private int calcDataSetHash(DataSet dataSet) {
        return new HashCodeBuilder()
                .append(dataSet.getUrl())
                .append(dataSet.getDriver())
                .append(dataSet.getUsername())
                .append(dataSet.getPassword())
                .toHashCode();
    };

    private JDBCClient getOrInitClient(DataSet dataSet) {
        String key = String.valueOf(calcDataSetHash(dataSet));
        if (clients.containsKey(key)) {
            return clients.get(key);
        } else {
            JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
                    .put("url", dataSet.getUrl())
                    .put("driver_class", dataSet.getDriver())
                    .put("user", dataSet.getUsername())
                    .put("password", dataSet.getPassword()));
            clients.put(key, client);
            return clients.get(key);
        }
    }
}
