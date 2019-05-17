package cn.eviao.reine.bootstrap;

import com.alibaba.fastjson.JSON;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;

import java.nio.file.Paths;

public class Bootstrap {
    public final static String BOOTSTRAP_PATH = Paths.get("bootstrap",
        "dist", "bootstrap.tpl").toString();
    
    public final static String LAYOUT_PLACEHOLDER = "#{> layout}";
    public final static String DATA_PLACEHOLDER = "#{> data}";
    
    private final StringBuffer source;

    private Bootstrap(String source) {
        this.source = new StringBuffer(source);
    }
    
    private void mergeLayout(String layout) {
        int start = source.indexOf(LAYOUT_PLACEHOLDER);
        int end = start + LAYOUT_PLACEHOLDER.length();
        source.replace(start, end, layout);
    }

    private void mergeData(Object data) {
        String json = JSON.toJSONString(data);
        int start = source.indexOf(DATA_PLACEHOLDER);
        int end = start + DATA_PLACEHOLDER.length();
        source.replace(start, end, json);
    }
    
    public Bootstrap merge(String layout, Object data) {
        mergeLayout(layout);
        mergeData(data);
        return this;
    }
    
    public String getSource() {
        return source.toString();
    }
    
    public static Single<Bootstrap> load(Vertx vertx) {
        return vertx.fileSystem().rxReadFile(BOOTSTRAP_PATH)
            .map(it -> new Bootstrap(it.toString()));
    }
}
