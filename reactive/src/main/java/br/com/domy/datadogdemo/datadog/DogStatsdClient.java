package br.com.domy.datadogdemo.datadog;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;

@Component
public class DogStatsdClient {
    
    @PostConstruct
    public StatsDClient buildClient() {
        return new NonBlockingStatsDClientBuilder()
            .prefix("statsd")
            .hostname("localhost")
            .port(8125)
            .build();
    } 
}
