package application.reposervice;

import application.model.SearchCriteria;
import application.model.SearchResult;
import application.utils.ReflectionUtil;
import application.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lancec on 14-3-10.
 */
@Transactional
public class BaseService<T extends Object> {

    @PersistenceContext
    private EntityManager entityManager;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public SearchResult<T> likeSearch(SearchCriteria criteria, String keywords, Class<T> resultClass) {

        StringBuilder querySql = new StringBuilder();
        StringBuilder countSql = new StringBuilder();
        StringBuilder clauseSql = new StringBuilder();
        StringBuilder orderBySql = new StringBuilder();
        String val = SearchCriteria.ENTITY_NAME;
        String entityName = resultClass.getSimpleName();
        querySql.append("SELECT ").append(val).append(" FROM ").append(entityName).append(" AS ").append(val);
        if (criteria.getAdditionalQuery() != null) {
            querySql.append(" ").append(criteria.getAdditionalQuery());
        }
        countSql.append("SELECT COUNT(").append(val).append(")").append(" FROM ").append(entityName).append(" AS ").append(val);
        if (criteria.getAdditionalQuery() != null) {
            countSql.append(" ").append(criteria.getAdditionalQuery());
        }
        clauseSql.append(" WHERE 1 = 1");
        if (!criteria.getSorters().isEmpty()) {
            orderBySql.append(" ORDER BY ").append(criteria.getSorterString(val));
        }

        Map<String, Object> parameters = new HashMap<>();
        Field[] fieldList = resultClass.getDeclaredFields();
        boolean initFlag = false;
        for (Field field : fieldList) {
            if ("java.lang.String".equals(field.getType().getName())) {
                String fieldName = field.getName();
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                if (initFlag == false) {
                    clauseSql.append(" AND ( ");
                    initFlag = true;
                } else {
                    clauseSql.append(" OR ");
                }
                clauseSql.append(val).append(".").append(fieldName).append(" LIKE :").append(fieldName);
                parameters.put(fieldName, "%" + keywords + "%");
            }
        }
        clauseSql.append(")");

        Query resultQuery = entityManager.createQuery(querySql.toString() + clauseSql.toString() + orderBySql.toString());
        Query countQuery = entityManager.createQuery(countSql.toString() + clauseSql.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            resultQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        if (!criteria.isQueryAll()) {
            resultQuery.setFirstResult(criteria.getBegin()).setMaxResults(criteria.getSize());
        }

        Long total = (Long) countQuery.getSingleResult();
        List<T> resultList = resultQuery.getResultList();


        SearchResult<T> searchResult = new SearchResult<>(total, resultList);
        return searchResult;
    }

    public List<T> list(Class<T> resultClass) {
        StringBuilder querySql = new StringBuilder();
        String entityName = resultClass.getSimpleName();
        String val = SearchCriteria.ENTITY_NAME;
        querySql.append("SELECT ").append(val).append(" FROM ").append(entityName).append(" AS ").append(val);
        querySql.append(" WHERE 1=1");
        Query resultQuery = entityManager.createQuery(querySql.toString());
        List<T> resultList = resultQuery.getResultList();
        return resultList;
    }

    public SearchResult<T> search(SearchCriteria criteria, Class<T> resultClass) {
        StringBuilder querySql = new StringBuilder();
        StringBuilder countSql = new StringBuilder();
        StringBuilder clauseSql = new StringBuilder();
        StringBuilder orderBySql = new StringBuilder();
        String val = SearchCriteria.ENTITY_NAME;
        String entityName = resultClass.getSimpleName();
        querySql.append("SELECT ").append(val).append(" FROM ").append(entityName).append(" AS ").append(val);
        if (criteria.getAdditionalQuery() != null) {
            querySql.append(" ").append(criteria.getAdditionalQuery());
        }
        countSql.append("SELECT COUNT(").append(val).append(")").append(" FROM ").append(entityName).append(" AS ").append(val);
        if (criteria.getAdditionalQuery() != null) {
            countSql.append(" ").append(criteria.getAdditionalQuery());
        }
        clauseSql.append(" WHERE 1 = 1");
        if (!criteria.getSorters().isEmpty()) {
            orderBySql.append(" ORDER BY ").append(criteria.getSorterString(val));
        }

        Map<String, Object> parameters = new HashMap<>();
        if (criteria.getParameters() != null) {
            for (Map.Entry<String, String> parameter : criteria.getParameters().entrySet()) {
                if (StringUtil.isEmpty(parameter.getValue())) {
                    continue;
                }
                Field field = ReflectionUtil.getField(parameter.getKey(), resultClass);
                if (field == null) {
                    continue;
                }
                Object setValue = ReflectionUtil.getBasicFieldValue(parameter.getValue(), field.getType(), null);
                if (setValue == null) {
                    continue;
                }
                clauseSql.append(" AND ").append(val).append(".").append(parameter.getKey()).append(" = :").append(parameter.getKey());
                parameters.put(parameter.getKey(), setValue);
            }
        }
        if (criteria.getAdditionalClause() != null) {
            clauseSql.append(" ").append(criteria.getAdditionalClause());
        }
        if (criteria.getAdditionalParameters() != null) {
            for (Map.Entry<String, Object> entry : criteria.getAdditionalParameters().entrySet()) {
                parameters.put(entry.getKey(), entry.getValue());
            }
        }

        Query resultQuery = entityManager.createQuery(querySql.toString() + clauseSql.toString() + orderBySql.toString());
        Query countQuery = entityManager.createQuery(countSql.toString() + clauseSql.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            resultQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        if (!criteria.isQueryAll()) {
            resultQuery.setFirstResult(criteria.getBegin()).setMaxResults(criteria.getSize());
        }

        Long total = (Long) countQuery.getSingleResult();
        List<T> resultList = resultQuery.getResultList();


        SearchResult<T> searchResult = new SearchResult<>(total, resultList);
        return searchResult;
    }

    public T get(Integer id, Class<T> tClass) {
        T singleResult = null;
        try {
            String entityName = tClass.getSimpleName();
            String querySql = "SELECT s FROM " + entityName + " AS s  WHERE s.id = :id";
            EntityManager entityManager = getEntityManager();
            Query resultQuery = entityManager.createQuery(querySql);
            resultQuery.setParameter("id", id);
            singleResult = (T) resultQuery.getSingleResult();
        } catch (Exception e) {
            logger.error("error", e);
        }
        return singleResult;
    }

    public <T> boolean delete(Integer id, Class<T> tClass) {
        String entityName = tClass.getSimpleName();
        String hql = "DELETE FROM " + entityName + " WHERE id = :id";
        this.getEntityManager()
                .createQuery(hql)
                .setParameter("id", id)
                .executeUpdate();
        return true;
    }

    public boolean create(T object) {
        try {
            EntityManager entityManager = getEntityManager();
            entityManager.persist(object);
            return true;
        } catch (Exception e) {
            logger.error("save error", e);
        }
        return false;
    }


    public boolean createList(List<T> objList) {
        EntityManager entityManager = getEntityManager();
        try {
            for (T object : objList) {
                entityManager.persist(object);
            }
            return true;
        } catch (Exception e) {
            logger.error("exception:", e);
        }
        return false;
    }

    public boolean update(T object) {
        try {
            EntityManager entityManager = getEntityManager();
            entityManager.merge(object);
            return true;
        } catch (Exception e) {
            logger.error("save error", e);
        }
        return false;
    }


}
