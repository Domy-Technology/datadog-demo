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

@RestController
@CrossOrigin("*")
@RequestMapping("/danger-zone")
@Slf4j
@RequiredArgsConstructor
public class FailureScenarioController {

    @PostMapping("/gc-destroy")
    public void destroyMyRam() {
        Vector v = new Vector();
        while (true)
        {
            byte b[] = new byte[1048576];
            v.add(b);
            Runtime rt = Runtime.getRuntime();
            log.info( "free memory: " + rt.freeMemory() );
        }
    }
    
    @PostMapping("/cpu-destroy/{numOfTests}")
    public String destroyMyCpu(@PathVariable(name = "numOfTests") int numOfTests) {
        long start = System.nanoTime();
        for (int i = 0; i < numOfTests; i++) {
            spin(500);
        }
        var message = "Took " + (System.nanoTime()-start) / 1000000 +
        "ms (expected " + (numOfTests * 500) + ")";
        log.info(message);
        return message;
    }

    @PostMapping("/cpu-load")
    public void loadMyCpu(@Valid @RequestBody CpuLoadModel cpuLoadModel) {
        for (int thread = 0; thread < cpuLoadModel.getNumCore() * cpuLoadModel.getNumThreadsPerCore(); thread++) {
            new BusyThread("Thread" + thread, cpuLoadModel.getLoad(), cpuLoadModel.getDuration())
                .start();
        }
    }

    private static void spin(int milliseconds) {
        long sleepTime = milliseconds * 1000000L;
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {}
    }
}
