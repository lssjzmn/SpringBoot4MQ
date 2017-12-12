package application.utils;


import application.model.SearchCriteria;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author lancec
 * Date: 14-1-27
 */
public class CollectionUtil {
    /**
     * Sort the list data by sorters.
     *
     * @param data    The list data
     * @param sorters The sorters
     * @param <T>     The type of data
     */
    public static <T> void sort(List<T> data, final List<SearchCriteria.Sorter> sorters) {

        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                //todo

                int result = 0;
                if (sorters == null) {
                    return result;
                }
                for (SearchCriteria.Sorter sorter : sorters) {
                    String elName = "${" + sorter.getName() + "}";
                    Object value1 = ExpressionUtil.eval(o1, elName);
                    Object value2 = ExpressionUtil.eval(o2, elName);
                    if (!(value1 instanceof Comparable) || !(value2 instanceof Comparable)) {
                        continue;
                    }
                    if (value1 == null) {
                        if (value2 == null) {
                            result = 0;
                        } else {
                            result = -1;
                        }
                    } else if (value2 == null) {
                        result = 1;
                    } else {
                        result = ((Comparable) value1).compareTo(value2);
                    }

                    if (SearchCriteria.SortDirection.DESC.equals(sorter.getDirection())) {
                        result = -result;
                    }
                    if (result != 0) {
                        break;
                    }
                }
                return result;
            }
        });
    }

}
