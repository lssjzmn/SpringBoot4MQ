package application.model;

import java.util.Collection;

/**
 * Created by 212464288 on 2016/9/19.
 */
public class StatisticSearchResult<T> extends SearchResult<T>{

    private  Integer keyCount;

    public StatisticSearchResult(long total, Collection<T> results) {
        super(total, results);
    }

    public Integer getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(Integer keyCount) {
        this.keyCount = keyCount;
    }
}
