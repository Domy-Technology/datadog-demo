package br.com.domy.datadogdemo.api;

import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/slow")
// @Timed(value = "slow")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class SlowController {

    @GetMapping("ok")
    @SneakyThrows
	public Mono<Void> okMethod() {
   		return Mono.fromRunnable(() -> {
			try {
				Thread.sleep((long) (Math.random() * 2000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info("test 200");
		});
	}

	@GetMapping("failure")
    @SneakyThrows
	public Mono<Void> failureMethod() {
		return Mono.fromRunnable(() -> {
			try {
				Thread.sleep((long) (Math.random() * 5000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			log.info("test 500");
			throw new RuntimeException();
		});
	}
}
