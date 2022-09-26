package br.com.domy.datadogdemo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DemoRepository extends MongoRepository<DemoEntity, String> {
    List<DemoEntity> findAllByDescriptionContainingIgnoreCase(String desc);
    
}
