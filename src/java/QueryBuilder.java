
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;

public class QueryBuilder {


    public QueryBuilder() {
        _query = new BasicDBObject();
    }

    
    public static QueryBuilder start() {
        return new QueryBuilder();
    }
	
    
    public static QueryBuilder start(String key) {
        return (new QueryBuilder()).put(key);
    }
	
    
    public QueryBuilder put(String key) {
        _currentKey = key;
        if(_query.get(key) == null) {
            _query.put(_currentKey, new NullObject());
        }
        return this;
    }
	
    
    public QueryBuilder and(String key) {
        return put(key);
    }
	
    
    public QueryBuilder greaterThan(Object object) {
        addOperand(QueryOperators.GT, object);
        return this;
    }
	
    
    public QueryBuilder greaterThanEquals(Object object) {
        addOperand(QueryOperators.GTE, object);
        return this;
    }
	
    
    public QueryBuilder lessThan(Object object) {
        addOperand(QueryOperators.LT, object);
        return this;
    }
	
    
    public QueryBuilder lessThanEquals(Object object) {
        addOperand(QueryOperators.LTE, object);
        return this;
    }
	
    
    public QueryBuilder is(Object object) {
        addOperand(null, object);
        return this;
    }
	
    
    public QueryBuilder notEquals(Object object) {
        addOperand(QueryOperators.NE, object);
        return this;
    }
	
    
    public QueryBuilder in(Object object) {
        addOperand(QueryOperators.IN, object);
        return this;
    }
	
    
    public QueryBuilder notIn(Object object) {
        addOperand(QueryOperators.NIN, object);
        return this;
    }
	
    
    public QueryBuilder mod(Object object) {
        addOperand(QueryOperators.MOD, object);
        return this;
    }
	
    
    public QueryBuilder all(Object object) {
        addOperand(QueryOperators.ALL, object);
        return this;
    }
	
	
    
    public QueryBuilder exists(Object object) {
        addOperand(QueryOperators.EXISTS, object);
        return this;
    }

    public QueryBuilder regex(Pattern regex) {
        addOperand(null, regex);
        return this;
    }

   
    @SuppressWarnings("unchecked")
    public QueryBuilder or( DBObject ... ors ){
        List l = (List)_query.get( "$or" );
        if ( l == null ){
            l = new ArrayList();
            _query.put( "$or" , l );
        }
        for ( DBObject o : ors )
            l.add( o );
        return this;
    }

    
    public DBObject get() {
        for(String key : _query.keySet()) {
            if(_query.get(key) instanceof NullObject) {
                throw new QueryBuilderException("No operand for key:" + key);
            }
        }
        return _query;
    }
	
    private void addOperand(String op, Object value) {
        if(op == null) {
            _query.put(_currentKey, value);
            return;
        }

        Object storedValue = _query.get(_currentKey);
        BasicDBObject operand;
        if(!(storedValue instanceof DBObject)) {
            operand = new BasicDBObject();
            _query.put(_currentKey, operand);
        } else {
            operand = (BasicDBObject)_query.get(_currentKey);
        }
        operand.put(op, value);
    }
	
    @SuppressWarnings("serial")
	static class QueryBuilderException extends RuntimeException {
            QueryBuilderException(String message) {
                super(message);
            }
	}
    private static class NullObject {}
	
    private DBObject _query;
    private String _currentKey;    
    
    
}
