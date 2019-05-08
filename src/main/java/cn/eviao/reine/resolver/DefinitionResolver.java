package cn.eviao.reine.resolver;

import cn.eviao.reine.bean.definition.Reine;
import io.reactivex.Single;

public interface DefinitionResolver {
    Single<Reine> apply(String source);
}
