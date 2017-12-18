package application.reposervice;

import application.pojobeans.DataEntity;
import com.sun.istack.internal.Nullable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends CrudRepository<DataEntity, Integer> {

    Iterable<DataEntity> findByContentEquals(String content);

    Iterable<DataEntity> findByContentContains(String content);

    Iterable<DataEntity> findByContentIsNot(String content);

    @Nullable
    DataEntity findTopByContentEndsWith(String content);

    Integer countByContent(String content);
}
