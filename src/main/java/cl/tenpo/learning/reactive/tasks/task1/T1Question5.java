package cl.tenpo.learning.reactive.tasks.task1;

import cl.tenpo.learning.reactive.utils.service.CalculatorService;
import cl.tenpo.learning.reactive.utils.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class T1Question5 {

    private final CalculatorService calculatorService;
    private final UserService userService;

    public Mono<String> question5A() {
        return Flux.range(100, 901)
                .map(BigDecimal::valueOf)
                .concatMap(calculatorService::calculate)
                .then(Mono.defer(userService::findFirstName))
                .switchIfEmpty(Mono.defer(() -> Mono.just("Chuck Norris")))
                .doOnError(error -> log.error("[question5A] :: Error finding user", error))
                .onErrorReturn("Chuck Norris")
                .doOnSuccess(result -> log.info("[question5A] :: Process completed"));

    }

    public Flux<String> question5B() {
        return Flux.range(100, 901)
                .map(BigDecimal::valueOf)
                .flatMap(calculatorService::calculate)
                .thenMany(Flux.defer(userService::findAllNames)
                        .take(3))
                .doOnComplete(() -> log.info("[question5B] :: Process completed"))
                .doOnError(error -> log.error("[question5B] :: Error calculating", error))
                .onErrorResume(e -> Flux.empty());
    }

}
