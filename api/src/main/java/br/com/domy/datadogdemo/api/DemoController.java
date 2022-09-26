package br.com.domy.datadogdemo.api;

import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/demo")
// @Timed(value = "demo")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class DemoController {

    @GetMapping("ok")
	public void okMethod() {

		log.info("test 200");
	}

	@GetMapping("failure")
	public void failureMethod() {
		log.info("test 500");
		throw new RuntimeException();
	}
}
