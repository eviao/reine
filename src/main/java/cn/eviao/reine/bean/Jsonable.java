package cn.eviao.reine.bean;

import io.vertx.core.shareddata.impl.ClusterSerializable;

public interface Jsonable {
    ClusterSerializable toJson();
}
