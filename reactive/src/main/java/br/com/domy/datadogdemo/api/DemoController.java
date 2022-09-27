package br.com.domy.datadogdemo.api;

import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/demo")
// @Timed(value = "demo")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class DemoController {

    @GetMapping("ok")
	public Mono<Void> okMethod() {
		return Mono.fromRunnable(() -> log.info("test 200"))
				.then();
	}

	@GetMapping("failure")
	public Mono<Void> failureMethod() {
		return Mono.fromRunnable(() -> {
			log.info("test 500");
			throw new RuntimeException();
		}).then();
	}
}
