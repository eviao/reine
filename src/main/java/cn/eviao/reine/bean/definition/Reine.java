package cn.eviao.reine.bean.definition;

import cn.eviao.reine.bean.Jsonable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reine implements Jsonable {
    private List<DataSet> dataSets = new ArrayList();
    private String layout;

    public Reine() { }

    public Reine(JsonObject json) {
        List<DataSet> dataSets = json.getJsonArray("dataSets").stream()
                .map(it -> DataSet.of((JsonObject) it)).collect(Collectors.toList());
        this.dataSets = dataSets;
        this.layout = json.getString("layout");
    }

    public List<DataSet> getDataSets() {
        return dataSets;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void addDataset(DataSet dataset) {
        this.dataSets.add(dataset);
    }

    public void setDataSets(List<DataSet> dataSets) {
        this.dataSets = dataSets;
    }

    @Override
    public JsonObject toJson() {
        JsonArray datasets = new JsonArray(
            this.dataSets.stream().map(it -> ((Jsonable) it).toJson()).collect(Collectors.toList())
        );
        return new JsonObject().put("dataSets", datasets).put("layout", this.layout);
    }

    public static Reine of(JsonObject json) {
        return new Reine(json);
    }
}
