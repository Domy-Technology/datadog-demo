package br.com.domy.datadogdemo.metrics;

// import org.springframework.metrics.instrument.Counter;
// import org.springframework.metrics.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemoMetricReaderWriter {

    // private final MeterRegistry registry;
    // private Counter counter;

    // public DemoMetricReaderWriter(MeterRegistry _registry) {
    //     this.registry = _registry;
    //     this.counter = registry.counter("demo.counter");
    // }
    
    public void updateMetrics(long gauge) {
        // log.info("Updating foo-count and bar-gauge of {} for web call", gauge);
        // registry.gauge("demo.gauge", gauge);
        // counter.increment();        
    }
    
}
