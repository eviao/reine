package cn.eviao.reine.bean.data;

import java.util.List;
import java.util.Map;

public class MultipleData implements Data {

    public final static MultipleData EMPTY = new MultipleData(null);
    
    private final List<Map<String, Object>> data;

    public MultipleData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @Override
    public List<Map<String, Object>> get() {
        return this.data;
    }
}
