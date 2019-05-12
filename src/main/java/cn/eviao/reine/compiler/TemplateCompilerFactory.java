package cn.eviao.reine.compiler;

public class TemplateCompilerFactory {

    public final static TemplateCompiler factory() {
        return PebbleCompiler.INSTANCE;
    }
}
