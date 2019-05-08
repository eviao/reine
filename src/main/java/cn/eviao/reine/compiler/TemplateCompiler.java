package cn.eviao.reine.compiler;

import io.reactivex.Single;

public interface TemplateCompiler {
    Single<String> apply(String source, Object model);
}
