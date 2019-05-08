package cn.eviao.reine.bean.definition;

import cn.eviao.reine.bean.Jsonable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataSet implements Jsonable {
    private String name;
    private String url;
    private String driver;
    private String username;
    private String password;

    public DataSet() { }

    public DataSet(JsonObject json) {
        this.name = json.getString("name");
        this.url = json.getString("url");
        this.driver = json.getString("driver");
        this.username = json.getString("username");
        this.password = json.getString("password");

        List<Commandable> commands = json.getJsonArray("commands").stream()
                .map(it -> Query.of((JsonObject) it)).collect(Collectors.<Commandable>toList());
        this.commands = commands;
    }

    private List<Commandable> commands = new ArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Commandable> getCommands() {
        return commands;
    }

    public void addCommand(Commandable command) {
        this.commands.add(command);
    }

    public void setCommands(List<Commandable> commands) {
        this.commands = commands;
    }

    @Override
    public JsonObject toJson() {
        ;JsonArray commands = new JsonArray(
                this.commands.stream().map(it -> ((Jsonable) it).toJson()).collect(Collectors.toList())
        );
        return new JsonObject()
                .put("name", this.name)
                .put("url", this.url)
                .put("driver", this.driver)
                .put("username", this.username)
                .put("password", this.password)
                .put("commands", commands);
    }

    public static DataSet of(JsonObject json) {
        return new DataSet(json);
    }
}
