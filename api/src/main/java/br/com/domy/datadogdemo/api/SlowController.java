package br.com.domy.datadogdemo.api;

import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/slow")
// @Timed(value = "slow")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class SlowController {

    @GetMapping("ok")
    @SneakyThrows
	public void okMethod() {
   

        // insert up to 2 second delay for a wider range of response times
        Thread.sleep((long) (Math.random() * 2000));

        
		log.info("test 200");
	}

	@GetMapping("failure")
    @SneakyThrows
	public void failureMethod() {


        // insert up to 2 second delay for a wider range of response times
        Thread.sleep((long) (Math.random() * 5000));

		log.info("test 500");
		throw new RuntimeException();
	}
}
