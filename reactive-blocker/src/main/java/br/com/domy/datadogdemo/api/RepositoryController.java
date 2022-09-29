package br.com.domy.datadogdemo.api;

import java.time.LocalDateTime;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import br.com.domy.datadogdemo.repository.DemoEntity;
import br.com.domy.datadogdemo.repository.DemoRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/demonstrative/entity")
// @Timed(value = "demo")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class RepositoryController {
    
    private final DemoRepository repository;
    private final MeterRegistry meterRegistry;

    @GetMapping()
	public Flux<DemoEntity> getAll() {
        return repository.findAll();
	}

    @GetMapping("/count")
	public Mono<Long> count() {
		return repository.count();
	}

    @GetMapping("/description/{description}")
	public Flux<DemoEntity> findAllByDescription(@PathVariable(name = "description") String description) {
		return repository.findAllByDescriptionContainingIgnoreCase(description);
	}

    @GetMapping("/{id}")
	public Mono<ResponseEntity<DemoEntity>> getById(@PathVariable(name = "id") String id) {
		return repository
            .findById(id)
            .map(entity -> {
                log.info("[GET-BY-ID] - Founded: {}", entity);
                return ResponseEntity.ok().body(entity);
            })
            .switchIfEmpty(Mono.fromCallable(() -> {
                log.warn("[GET-BY-ID] - Nenhum registro encontrado");
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(DemoEntity.builder().build());
            }));
	}

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
	public Mono<DemoEntity> createOrUpdate(@Valid @RequestBody DemoEntity entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setCreatedAt(LocalDateTime.now());
            return repository.save(entity); 
        }

		return repository
            .findById(entity.getId())
            .flatMap(databaseEntity -> {
                log.info("[UPSERT] - Founded: {}", databaseEntity);
                entity.setModifiedAt(LocalDateTime.now());
                return repository.save(entity);
            })
            .switchIfEmpty(Mono.fromRunnable(() -> {
                log.warn("[GET-BY-ID] - Nenhum registro encontrado");
                throw new NoSuchElementException("Nenhum registro encontrado");
            }));
	}

    @PostMapping("/load")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
	public Mono<Void> createOrUpdateRandom() {
        var fillSize = new Random().nextInt(1000);
        return fillDataBase(fillSize);
	}

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public Mono<Void> delete(@PathVariable(name = "id") String id) {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.defer(() -> {
                log.warn("[DELETE] - Nenhum registro encontrado");
                throw new NotFoundException("Nenhum registro encontrado");
            }))
            .flatMap(demoEntity -> {
                log.info("[DELETE] - Founded: {}", demoEntity);
                return repository.deleteById(id);
            });
	}

    private Mono<Void> fillDataBase(int fillSize) {
        return repository.count()
                .flatMapMany(aLong -> {
                    var finalSize = aLong + fillSize;
                    List<DemoEntity> listMonos = new ArrayList<>();
                    for (long i = aLong; i < finalSize; i++) {
                        var demoEntity = DemoEntity.builder()
                                .active(indexIsActive(i))
                                .createdAt(LocalDateTime.now())
                                .description("""
                        Um dev Java pela %s vez
                        """.formatted(i)
                                )
                                .name("João Paulo %s".formatted(i))
                                .build();
                        listMonos.add(demoEntity);
                    }
                    return repository.saveAll(listMonos);
                })
                .doOnNext(demoEntity -> log.info("[FILL-DATA-BASE] - Entity {} saved", demoEntity))
                .then();
    }

    private boolean indexIsActive(long anyNumber) {
        return anyNumber % 2 == 0;
    }
}
