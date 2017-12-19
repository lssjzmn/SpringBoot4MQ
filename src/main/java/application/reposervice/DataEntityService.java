package application.reposervice;

import application.pojobeans.DataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "dataEntityRedisCache")
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

    @Cacheable
    public DataEntity getOne(Integer id) {
        return dataRepository.findOne(id);
    }

    /*getAll方法不加缓存900+ms，加了缓存20+ms*/
    @Cacheable
    public Iterable<DataEntity> getAll() {
        return dataRepository.findAll();
    }

    @Cacheable
    public Iterable<DataEntity> getContentEquals(String content) {
        return dataRepository.findByContentEquals(content);
    }

    /*getContentContains方法不加缓存110ms，加了缓存20ms*/
    @Cacheable
    public Iterable<DataEntity> getContentContains(String content) {
        return dataRepository.findByContentContains(content);
    }

    @Cacheable
    public Iterable<DataEntity> getContentNot(String content) {
        return dataRepository.findByContentIsNot(content);
    }

    @Cacheable
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
