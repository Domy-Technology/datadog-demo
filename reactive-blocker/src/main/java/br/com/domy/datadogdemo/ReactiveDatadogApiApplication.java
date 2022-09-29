package br.com.domy.datadogdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ReactiveDatadogApiApplication {

	public static void main(String[] args) {
		BlockHound.install(
				builder -> {
					builder.allowBlockingCallsInside("io.netty.util.concurrent.FastThreadLocalRunnable", "run");
				}
		);
		SpringApplication.run(ReactiveDatadogApiApplication.class, args);
	}

}
