package cn.eviao.reine.compiler;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Jackson2Helper;
import io.reactivex.Single;

public class HandlebarsCompiler implements TemplateCompiler {

    public final static HandlebarsCompiler INSTANCE = new HandlebarsCompiler();

    private final Handlebars handlebars = new Handlebars();

    public HandlebarsCompiler() {
        handlebars.registerHelper("json", Jackson2Helper.INSTANCE);
    }

    @Override
    public Single<String> apply(String source, Object data) {
        Context context = Context.newBuilder(data).build();
        try {
            return Single.just(handlebars.compileInline(source).apply(context));
        } catch (Exception e) {
            return Single.error(e);
        }
    }
}