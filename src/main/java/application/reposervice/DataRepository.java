package application.reposervice;

import application.pojobeans.DataEntity;
import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<DataEntity, Integer> {

}
