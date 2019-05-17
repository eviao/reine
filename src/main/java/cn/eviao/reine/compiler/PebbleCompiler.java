package cn.eviao.reine.compiler;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.lexer.Syntax;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import io.reactivex.Single;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PebbleCompiler implements TemplateCompiler {
    
    public final static PebbleCompiler INSTANCE = new PebbleCompiler();
    
    private final PebbleEngine engine;

    public PebbleCompiler() {
        Syntax syntax = new Syntax.Builder().build();
        engine = new PebbleEngine.Builder()
            .loader(new StringLoader())
            //defaultEscapingStrategy("js")
            .syntax(syntax)
            .build();
    }

    @Override
    public Single<String> apply(String source, Object data) {
        PebbleTemplate compiledTemplate = engine.getTemplate(source);
        Writer output = new StringWriter();
        try {
            compiledTemplate.evaluate(output, (Map) data);
        } catch (IOException e) {
            return Single.error(e);
        }
        return Single.just(output.toString());
    }
}
