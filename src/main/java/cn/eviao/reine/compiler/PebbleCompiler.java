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
    
    private PebbleEngine engine;

    public PebbleCompiler() {
        initPebbleEngine();
    }
    
    private void initPebbleEngine() {
        Syntax.Builder syntaxBuilder = new Syntax.Builder();

        engine = new PebbleEngine.Builder()
            .loader(new StringLoader())
            .defaultEscapingStrategy("js")
            .syntax(syntaxBuilder.build())
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
