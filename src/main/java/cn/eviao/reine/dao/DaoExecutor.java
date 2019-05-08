package cn.eviao.reine.dao;

import cn.eviao.reine.bean.definition.DataSet;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

public interface DaoExecutor<T> {
    Single<T> apply(List<DataSet> dataSets, Map params);
}
