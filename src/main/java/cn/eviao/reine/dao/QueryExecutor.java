package cn.eviao.reine.dao;

import cn.eviao.reine.bean.Pair;
import cn.eviao.reine.bean.data.Data;
import cn.eviao.reine.bean.data.MultipleData;
import cn.eviao.reine.bean.data.SingleData;
import cn.eviao.reine.bean.definition.Commandable;
import cn.eviao.reine.bean.definition.DataSet;
import cn.eviao.reine.bean.definition.Query;
import cn.eviao.reine.bean.definition.QueryMode;
import cn.eviao.reine.compiler.TemplateCompiler;
import cn.eviao.reine.compiler.TemplateCompilerFactory;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.vertx.ext.sql.ResultSet;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QueryExecutor implements DaoExecutor<Map<String, Map<String, Object>>> {

    private final Logger logger = LogManager.getLogger(QueryExecutor.class);

    private final TemplateCompiler compiler = TemplateCompilerFactory.factory();
    private final Function<DataSet, JDBCClient> clientDiscover;

    public QueryExecutor(Function<DataSet, JDBCClient> clientDiscover) {
        this.clientDiscover = clientDiscover;
    }

    private Map<String, Object> getResultRowByIndex(ResultSet resultSet, int index) {
        return resultSet.getColumnNames().stream().map(column -> {
            Object value = resultSet.getRows(true).get(index).getValue(column);
            return new Pair<String, Object>(column, value);
        }).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Single<Data> transformMultiple(ResultSet resultSet) {
        return Single.just(new MultipleData(IntStream.range(0, resultSet.getNumRows()).mapToObj(index ->
                getResultRowByIndex(resultSet, index)).collect(Collectors.toList())));
    }

    private Single<Data> transformSingle(ResultSet resultSet) {
        if (resultSet.getNumRows() > 1) {
            logger.warn("query returns more than 1 row.");
        } else if (resultSet.getNumRows() == 0) {
            return Single.just(SingleData.EMPTY);
        }
        return Single.just(new SingleData(getResultRowByIndex(resultSet, 0)));
    }

    private Single<Pair<String, Object>> executeCommand(SQLConnection conn, Map params, Commandable command) {
        Query query = (Query) command;

        Function<ResultSet, Single<Data>> transform = query.getModeAs() == QueryMode.single
                ? this::transformSingle : this::transformMultiple;

        Single<String> name = Single.just(query.getName());
        Single<Data> resultset = compiler.apply(query.getCommand(), params)
                .flatMap(conn::rxQuery).flatMap(transform);

        return Single.zip(name, resultset, (n, r) -> {
            return new Pair<String, Object>(n, r.get());
        });
    }

    private Single<Map<String, Object>> queryDataSet(DataSet dataSet, Map params) {
        JDBCClient client = null;
        try {
            client = clientDiscover.apply(dataSet);
        } catch (Exception e) {
            return Single.error(e);
        }

        return client.rxGetConnection().flatMap(conn -> {
            return Observable.fromIterable(dataSet.getCommands())
                    .filter(it -> it instanceof Query)
                    .flatMapSingle(it -> executeCommand(conn, params, it))
                    .toMap(Pair::getKey, Pair::getValue);
        });
    }

    private Single<Map<String, Map<String, Object>>> queryDataSource(List<DataSet> dataSets, Map params) {
        return Observable.fromIterable(dataSets).flatMapSingle(dataSet -> {
            Single<String> name = Single.just(dataSet.getName());
            Single<Map<String, Object>> resultset = queryDataSet(dataSet, params);
            return Single.zip(name, resultset, Pair::new);
        }).toMap(Pair::getKey, Pair::getValue);
    }

    @Override
    public Single<Map<String, Map<String, Object>>> apply(List<DataSet> dataSets, Map params) {
        return queryDataSource(dataSets, params);
    }
}
