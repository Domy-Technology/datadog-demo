// package br.com.domy.datadogdemo.datadog;

// import java.util.EnumSet;
// import java.util.concurrent.TimeUnit;

// import org.coursera.metrics.datadog.DatadogReporter;
// import org.coursera.metrics.datadog.DatadogReporter.Expansion;
// import org.coursera.metrics.datadog.transport.UdpTransport;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.codahale.metrics.MetricRegistry;

// import lombok.Data;
// import lombok.extern.slf4j.Slf4j;

// // @Configuration
// // @ConfigurationProperties("datadog.metrics")
// @Data
// @Slf4j
// public class DatadogReporterConfig {
//   private String apiKey;
//   private long period;
//   private boolean enabled = false;

//   @Bean
//   @Autowired
//   public DatadogReporter datadogReporter(MetricRegistry registry) {

//       DatadogReporter reporter = null;
//       if(enabled) {
//           reporter = enableDatadogMetrics(registry);
//       } else {
//           if(log.isWarnEnabled()) {
//               log.warn("Datadog reporter is disabled. To turn on this feature just set 'rJavaServer.metrics.enabled:true' in your config file (property or YAML)");
//           }
//       }

//       return reporter;
//   }

//   private DatadogReporter enableDatadogMetrics(MetricRegistry registry) {

//       if(log.isInfoEnabled()) {
//         log.info("Initializing Datadog reporter using [ {} ]", this);
//       }

//       EnumSet<Expansion> expansions = DatadogReporter.Expansion.ALL;
//       var udpTransport = new UdpTransport
//                                 .Builder()
//                                 .build();

//       DatadogReporter reporter = DatadogReporter.forRegistry(registry)
//         .withTransport(udpTransport)
//         .withExpansions(expansions)
//         .build();

//       reporter.start(getPeriod(), TimeUnit.SECONDS);

//       if(log.isInfoEnabled()) {
//         log.info("Datadog reporter successfully initialized");
//       }

//       return reporter;
//   }
// }
