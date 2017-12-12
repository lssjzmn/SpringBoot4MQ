package application.model;


import application.utils.StringUtil;

import java.io.Serializable;
import java.util.*;

/**
 * Created by lancec on 14-3-11.
 */
public class SearchCriteria implements Serializable {


    private static final long serialVersionUID = -6242327859768494335L;
    public static final String ENTITY_NAME = "o";

    /**
     * Whether query all, if true, ignore begin and size.
     */
    private boolean queryAll;

    private static final int DEFAULT_BEGIN = 0;
    private static final int DEFAULT_SIZE = 10;
    /**
     * The pagination begin offset.
     */
    private int begin = DEFAULT_BEGIN;
    /**
     * The pagination page size.
     */
    private int size = DEFAULT_SIZE;

    private List<Sorter> sorters = new ArrayList<Sorter>();

    /**
     * The search parameters.
     */
    private Map<String, String> parameters = new HashMap<>();

    private String additionalQuery;

    private String additionalClause;

    private Map<String, Object> additionalParameters = new HashMap<>();

    protected SearchCriteria() {

    }


    private String keyFieldName;

    private  String keyValue;

//    private Integer parentId;
//
//    public Integer getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(Integer parentId) {
//        this.parentId = parentId;
//    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getKeyFieldName() {
        return keyFieldName;
    }

    public void setKeyFieldName(String keyFieldName) {
        this.keyFieldName = keyFieldName;
    }

    /**
     * Create search criteria instance.
     *
     * @return Return Search criteria
     */
    public static SearchCriteria createInstance() {
        return new SearchCriteria();
    }

    public boolean isQueryAll() {
        return queryAll;
    }

    public void setQueryAll(boolean queryAll) {
        this.queryAll = queryAll;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Sorter> getSorters() {
        return sorters;
    }

    public void addSorter(String fieldName) {
        this.sorters.add(new Sorter(fieldName));
    }

    public void addSorter(String fieldName, SortDirection direction) {
        this.sorters.add(new Sorter(fieldName, direction));
    }

    public void addSorter(Sorter sorter) {
        this.sorters.add(sorter);
    }

    public void addSorters(Collection<Sorter> sorters) {
        this.sorters.addAll(sorters);
    }

    public void clearSorters() {
        this.sorters.clear();
    }

    public String getSorterString(String topName) {
        StringBuilder sorterString = new StringBuilder();
        int index = 0;
        for (Sorter sorter : sorters) {
            if (sorter.getName() != null) {
                if (index != 0) {
                    sorterString.append(", ");
                }
                if (!StringUtil.isEmpty(topName)) {
                    sorterString.append(topName).append(".");
                }
                sorterString.append(sorter.getName()).append(" ").append(sorter.getDirection());
                index++;
            }
        }
        if (index == 0) {
            return null;
        }
        return sorterString.toString();
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public SearchCriteria addParameter(String paramName, String paramValue) {
        this.parameters.put(paramName, paramValue);
        return this;
    }

    public String getAdditionalQuery() {
        return additionalQuery;
    }

    public void setAdditionalQuery(String additionalQuery) {
        this.additionalQuery = additionalQuery;
    }

    public String getAdditionalClause() {
        return additionalClause;
    }

    public void setAdditionalClause(String additionalClause) {
        this.additionalClause = additionalClause;
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(Map<String, Object> additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    public SearchCriteria addAdditionalParameter(String paramName, Object paramValue) {
        this.additionalParameters.put(paramName, paramValue);
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SearchCriteria searchCriteria = (SearchCriteria) super.clone();
        searchCriteria.sorters = new ArrayList<Sorter>();
        for (Sorter sorter : this.getSorters()) {
            searchCriteria.addSorter((Sorter) sorter.clone());
        }
        return searchCriteria;
    }

    public enum SortDirection {
        ASC,
        DESC;
    }

    public class Sorter implements Serializable, Cloneable {


        private static final long serialVersionUID = 6913599964529701889L;
        private String name;
        private SortDirection direction = SortDirection.DESC;

        public Sorter(String name) {
            this.name = name;
        }

        public Sorter(String name, SortDirection direction) {
            this.name = name;
            this.direction = direction;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SortDirection getDirection() {
            return direction;
        }

        public void setDirection(SortDirection direction) {
            this.direction = direction;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Sorter)) return false;

            Sorter sorter = (Sorter) o;

            if (name != null ? !name.equals(sorter.name) : sorter.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

}
