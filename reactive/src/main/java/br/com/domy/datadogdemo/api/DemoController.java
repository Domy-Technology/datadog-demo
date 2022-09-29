package br.com.domy.datadogdemo.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/demonstration")
// @Timed(value = "demo")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class DemoController {

    @GetMapping("/logging/ok")
	public Mono<Void> okLogging() {
		List<Runnable> logs = List.of(
				() -> log.info("Gerando Métricas Sobre a Venda"),
				() -> log.info("Persistindo Dados no Banco"),
				() -> log.info("Enviando Notificação para a Área de Negócio"),
				() -> log.info("Emitindo Nota Fiscal"),
				() -> log.info("Processo de Venda Finalizado")
		);

		return Flux.fromIterable(logs)
				.doOnNext(Runnable::run)
				.then();
	}

	@GetMapping("/logging/failure")
	public Mono<Void> failureLogging() {
		List<Runnable> logs = List.of(
				() -> log.info("Gerando Métricas Sobre a Venda"),
				() -> log.info("Persistindo Dados no Banco"),
				() -> log.info("Enviando Notificação para a Área de Negócio"),
				() -> {
					throw new NullPointerException();
				},
				() -> log.info("Processo de Venda Finalizado")
		);

		return Flux.fromIterable(logs)
				.map(runnable -> {
					runnable.run();
					return true;
				})
				.then();
	}

	@GetMapping("block-thread")
	@SneakyThrows
	public Mono<Void> blockThread() {
		return Mono.fromRunnable(() -> {
					try {
						var time = Math.random() * 2000;
						log.info("Gerando Sleep Thread por {} ms", time);
						Thread.sleep((long) time);
						log.info("Sleep Thread Concluído");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log.info("test 200");
				})
				.delayElement(Duration.ofSeconds(10))
				.cache()
				.then()
				.subscribeOn(Schedulers.parallel());
	}

	@GetMapping("http-client-block")
	@SneakyThrows
	public Mono<String> clientBlock() {
		return Mono.fromCallable(() -> {
					var rest = new RestTemplate();
					log.info("Requisição GET para {}", "https://catfact.ninja/fact");
					var response = rest.getForEntity("https://catfact.ninja/fact", String.class);
					var responseData = response.getBody();
					log.info("Resposta HTTP {}", responseData);
					return responseData;
				}).delayElement(Duration.ofSeconds(10))
				.cache()
				.subscribeOn(Schedulers.parallel());
	}
}
