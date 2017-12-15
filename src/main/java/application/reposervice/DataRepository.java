package application.reposervice;

import application.pojobeans.DataEntity;
import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<DataEntity, Integer> {

    Iterable<DataEntity> findByContentEquals(String content);

    Iterable<DataEntity> findByContentContains(String content);

    Iterable<DataEntity> findByContentIsNot(String content);

}
