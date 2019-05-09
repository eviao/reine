package cn.eviao.reine.bean.data;

import java.util.Map;

public class SingleData implements Data {

    public final static SingleData EMPTY = new SingleData(null);

    private final Map<String, Object> data;

    public SingleData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public Map<String, Object> get() {
        return this.data;
    }
}
