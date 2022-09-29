package br.com.domy.datadogdemo.api;

import java.util.Vector;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.domy.datadogdemo.api.model.BusyThread;
import br.com.domy.datadogdemo.api.model.CpuLoadModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin("*")
@RequestMapping("/stress-scenarios")
@Slf4j
@RequiredArgsConstructor
public class FailureScenarioController {

    @PostMapping("/memory/load")
    public Mono<Void> loadMemory() {
        return Mono.fromRunnable(() -> {
            Vector v = new Vector();
            while (true)
            {
                byte b[] = new byte[1048576];
                v.add(b);
                Runtime rt = Runtime.getRuntime();
                log.info("Memória Livre: " + rt.freeMemory() );
            }
        });
    }
    
    @PostMapping("/cpu/load/{numOfInteractions}")
    public Mono<String> cpuLoadByInteractions(@PathVariable(name = "numOfInteractions") int numOfTests) {
        return Mono.fromCallable(() -> {
            long start = System.nanoTime();
            for (int i = 0; i < numOfTests; i++) {
                spin(500);
            }
            var message = "Demorou " + (System.nanoTime()-start) / 1000000 +
                    "ms para processar (Esperado: " + (numOfTests * 500) + ")";
            log.info(message);
            return message;
        });
    }

    @PostMapping("/cpu/load")
    public Mono<Void> loadMyCpu(@Valid @RequestBody CpuLoadModel cpuLoadModel) {
        return Mono.fromRunnable(() -> {
            for (int thread = 0; thread < cpuLoadModel.getNumCore() * cpuLoadModel.getNumThreadsPerCore(); thread++) {
                new BusyThread("Thread" + thread, cpuLoadModel.getLoad(), cpuLoadModel.getDuration())
                        .start();
            }
        });
    }

    private static void spin(int milliseconds) {
        long sleepTime = milliseconds * 1000000L;
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {}
    }
}
