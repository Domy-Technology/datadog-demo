package br.com.domy.datadogdemo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DemoRepository extends ReactiveMongoRepository<DemoEntity, String> {
    Flux<DemoEntity> findAllByDescriptionContainingIgnoreCase(String desc);
    
}
