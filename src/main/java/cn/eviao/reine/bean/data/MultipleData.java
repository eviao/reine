package cn.eviao.reine.bean.data;

import java.util.List;
import java.util.Map;

public class MultipleData implements Data {

    private final List<Map<String, Object>> data;

    public MultipleData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public List<Map<String, Object>> get() {
        return this.data;
    }
}
