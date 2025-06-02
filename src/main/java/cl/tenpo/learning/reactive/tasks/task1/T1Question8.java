package cl.tenpo.learning.reactive.tasks.task1;

import cl.tenpo.learning.reactive.utils.exception.AuthorizationTimeoutException;
import cl.tenpo.learning.reactive.utils.exception.PaymentProcessingException;
import cl.tenpo.learning.reactive.utils.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class T1Question8 {

    private final TransactionService transactionService;

    public Mono<String> question8(int transactionId) {
        return transactionService.authorizeTransaction(transactionId)
                .doOnNext(response -> log.info("[question8] :: Authorization response: {}", response))
                .timeout(Duration.ofSeconds(3), Mono.error(new AuthorizationTimeoutException("Authorization timed out for transaction ID: " + transactionId)))
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500))
                        .filter(e -> !(e instanceof AuthorizationTimeoutException)))
                .doOnError(e -> log.error("[question8] :: Error during transaction authorization", e))
                .onErrorMap(e -> !(e instanceof AuthorizationTimeoutException) ?
                        new PaymentProcessingException("Payment processing failed for transaction ID: " + transactionId, e) : e)
                .doOnSuccess(result -> log.info("[question8] :: Transaction authorized successfully"));
    }
}
