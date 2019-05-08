package cn.eviao.reine.bean.definition;

import cn.eviao.reine.bean.Jsonable;
import io.vertx.core.json.JsonObject;

import java.util.Optional;

public class Query implements Commandable, Jsonable {

    private String name;
    private String mode;
    private String command;

    public Query() { }

    public Query(JsonObject json) {
        this.name = json.getString("name");
        this.mode = json.getString("mode");
        this.command = json.getString("command");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public QueryMode getModeAs() {
        return Optional.ofNullable(this.mode)
                .map(it -> {
                    try {
                        return QueryMode.valueOf(it);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                }).orElse(QueryMode.defaults);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject().put("name", this.name)
                .put("mode", this.mode)
                .put("command", this.command)
                .put("type", "query");
    }

    public static Query of(JsonObject json) {
        return new Query(json);
    }
}
