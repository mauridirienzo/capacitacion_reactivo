package cl.tenpo.learning.reactive.tasks.task1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class T1Question6 {

    public ConnectableFlux<Double> question6() {
        return Flux.interval(Duration.ofMillis(500))
                .doOnNext(tick -> log.info("[question6] :: Getting prices"))
                .map(tick -> ThreadLocalRandom.current().nextDouble(1, 501))
                .doOnNext(price -> log.info("[question6] :: Price generated: {}", price))
                .doOnError(error -> log.error("[question6] :: Error generating price", error))
                .publish();
    }
}