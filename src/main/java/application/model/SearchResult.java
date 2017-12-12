package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lancec on 14-3-11.
 */
public class SearchResult<T> implements Serializable {
    private static final long serialVersionUID = -6518830586631077440L;
    private SearchCriteria criteria;
    protected long total;
    protected Collection<T> results = new ArrayList<T>();

    public SearchResult(long total, Collection<T> results) {
        this.total = total;
        this.results.addAll(results);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Collection<T> getResults() {
        return results;
    }

    public void addResult(T result) {
        this.results.add(result);
    }

    public void addResults(Collection<T> results) {
        this.results.addAll(results);
    }

    public boolean isEmpty() {
        return this.results.isEmpty();
    }

    public void clearResults() {
        this.results.clear();
    }
}
