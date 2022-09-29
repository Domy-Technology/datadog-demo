package br.com.domy.datadogdemo.api;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
// import org.springframework.metrics.annotation.Timed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/demonstration")
// @Timed(value = "demo")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class DemoController {

	@GetMapping("/logging/ok")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void okLogging() {
		List<Runnable> logs = List.of(
				() -> log.info("Gerando Métricas Sobre a Venda"),
				() -> log.info("Persistindo Dados no Banco"),
				() -> log.info("Enviando Notificação para a Área de Negócio"),
				() -> log.info("Emitindo Nota Fiscal"),
				() -> log.info("Processo de Venda Finalizado")
		);

		logs.forEach(Runnable::run);
	}

	@GetMapping("/logging/failure")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void failureLogging() {
		List<Runnable> logs = List.of(
				() -> log.info("Gerando Métricas Sobre a Venda"),
				() -> log.info("Persistindo Dados no Banco"),
				() -> log.info("Enviando Notificação para a Área de Negócio"),
				() -> {
					throw new NullPointerException();
				},
				() -> log.info("Processo de Venda Finalizado")
		);

		logs.forEach(Runnable::run);
	}

	@GetMapping("block-thread")
	@SneakyThrows
	public void blockThread() {
		try {
			var time = Math.random() * 2000;
			log.info("Gerando Sleep Thread por {} ms", time);
			Thread.sleep((long) time);
			log.info("Sleep Thread Concluído");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("http-client-block")
	@SneakyThrows
	public String clientBlock() {
		var rest = new RestTemplate();
		log.info("Requisição GET para {}", "https://catfact.ninja/fact");
		var response = rest.getForEntity("https://catfact.ninja/fact", String.class);
		var responseData = response.getBody();
		log.info("Resposta HTTP {}", responseData);
		return responseData;
	}
}
