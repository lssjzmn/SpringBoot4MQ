package application.reposervice;

import application.pojobeans.DataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DataEntityService {

    @Autowired
    private DataRepository dataRepository;

    public void save(DataEntity dataEntity) {
        dataRepository.save(dataEntity);
    }

    public void save(List<DataEntity> dataEntitys) {
        dataRepository.save(dataEntitys);
    }

    public void delete(Integer id) {
        dataRepository.delete(id);
    }

    public void delete(DataEntity dataEntity) {
        dataRepository.delete(dataEntity);
    }

    public DataEntity getOne(Integer id) {
        return dataRepository.findOne(id);
    }

    public Iterable<DataEntity> getAll() {
        return dataRepository.findAll();
    }

    public Iterable<DataEntity> getContentEquals(String content) {
        return dataRepository.findByContentEquals(content);
    }

    public Iterable<DataEntity> getContentContains(String content) {
        return dataRepository.findByContentContains(content);
    }

    public Iterable<DataEntity> getContentNot(String content) {
        return dataRepository.findByContentIsNot(content);
    }

    public DataEntity getTopContentEndsWith(String content) {
        return dataRepository.findTopByContentEndsWith(content);
    }

    public void exists(Integer id) {
        dataRepository.exists(id);
    }

    public long count() {
        return dataRepository.count();
    }

}
