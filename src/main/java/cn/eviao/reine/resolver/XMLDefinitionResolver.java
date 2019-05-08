package cn.eviao.reine.resolver;

import cn.eviao.reine.bean.definition.DataSet;
import cn.eviao.reine.bean.definition.Query;
import cn.eviao.reine.bean.definition.Reine;
import io.reactivex.Single;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

public class XMLDefinitionResolver implements DefinitionResolver {

    @Override
    public Single<Reine> apply(String source) {
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("reine", Reine.class);
        digester.addObjectCreate("reine/datasource/dataset", DataSet.class);
        digester.addSetProperties("reine/datasource/dataset");
        digester.addObjectCreate("reine/datasource/dataset/query", Query.class);
        digester.addSetProperties("reine/datasource/dataset/query");
        digester.addBeanPropertySetter("reine/datasource/dataset/query", "command");
        digester.addSetNext("reine/datasource/dataset/query", "addCommand");
        digester.addSetNext("reine/datasource/dataset", "addDataset");
        digester.addBeanPropertySetter("reine/layout", "layout");

        StringReader stream = new StringReader(source);
        try {
            return Single.just(digester.parse(stream));
        } catch (IOException | SAXException e) {
            return Single.error(e);
        }
    }
}
