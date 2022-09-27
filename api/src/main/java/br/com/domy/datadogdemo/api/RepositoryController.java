package br.com.domy.datadogdemo.api;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

@RestController
@Slf4j
@RequestMapping("/repo")
// @Timed(value = "demo")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class RepositoryController {
    
    private final DemoRepository repository;
    private final MeterRegistry meterRegistry;

    @GetMapping()
	public List<DemoEntity> getAll() {
		var list = repository.findAll();
        meterRegistry.gaugeCollectionSize("demo.api.entities", Collections.emptyList(), list);
        return list;
	}

    @GetMapping("/count")
	public Long count() {
		return repository.count();
	}

    @GetMapping("/description/{description}")
	public List<DemoEntity> findAllByDescription(@PathVariable(name = "description") String description) {
		var list = repository.findAllByDescriptionContainingIgnoreCase(description);
        meterRegistry.gaugeCollectionSize("demo.api.entities.".concat(description), Collections.emptyList(), list);
        return list;
	}

    @GetMapping("/{id}")
	public ResponseEntity<DemoEntity> getById(@PathVariable(name = "id") String id) {
		return repository
            .findById(id)
            .map(entity -> {
                log.info("[GET-BY-ID] - Founded: {}", entity);
                return ResponseEntity.ok().body(entity);
            })
            .orElseGet(() -> {
                log.warn("[GET-BY-ID] - Nenhum registro encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            });
	}

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
	public DemoEntity createOrUpdate(@Valid @RequestBody DemoEntity entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setCreatedAt(LocalDateTime.now());
            return repository.save(entity); 
        }

		return repository
            .findById(entity.getId())
            .map(databaseEntity -> {
                log.info("[UPSERT] - Founded: {}", databaseEntity);
                entity.setModifiedAt(LocalDateTime.now());
                return repository.save(entity);
            })
            .orElseThrow(() -> {
                log.warn("[GET-BY-ID] - Nenhum registro encontrado");
                throw new NotFoundException("Nenhum registro encontrado");
            });
	}

    @PostMapping("/random")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
	public void createOrUpdateRandom() {
        var fillSize = new Random().nextInt(1000);
        Thread newThread = new Thread(() -> {
            fillDataBase(fillSize);
        });
        newThread.start();        
	}

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(name = "id") String id) {
        repository
            .findById(id)
            .ifPresentOrElse(databaseEntity -> {
                log.info("[DELETE] - Founded: {}", databaseEntity);
                repository.deleteById(id);

            }, () -> {
                log.warn("[DELETE] - Nenhum registro encontrado");
                throw new NotFoundException("Nenhum registro encontrado");
            });
	}

    // @PostConstruct
    // public void fillDatabaseAsync() {
    //     Thread newThread = new Thread(() -> {
    //         fillDataBase(100000);
    //     });
    //     newThread.start();
    // }

    private void fillDataBase(int fillSize) {
        var count = repository.count();
        var finalSize = count + fillSize;
        for (long i = count; i < finalSize; i++) {
            var demoEntity = DemoEntity.builder()
                .active(indexIsActive(i))
                .createdAt(LocalDateTime.now())
                .description("""
                        Um dev Java pela %s vez
                        """.formatted(i)
                )
                .name("João Paulo %s".formatted(i))
                .build();
            repository.save(demoEntity);
            log.info("[FILL-DATA-BASE] - Entity {} saved", demoEntity);
        }
    }

    private boolean indexIsActive(long anyNumber) {
        return anyNumber % 2 == 0;
    }
}
