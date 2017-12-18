package application.reposervice;

import application.pojobeans.DataEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataPageRepository extends PagingAndSortingRepository<DataEntity, Integer> {
}
