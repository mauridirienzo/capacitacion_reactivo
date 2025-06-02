package cl.tenpo.learning.reactive.tasks.task1;

import cl.tenpo.learning.reactive.utils.service.UserService;
import cl.tenpo.learning.reactive.utils.exception.ResourceNotFoundException;
import cl.tenpo.learning.reactive.utils.exception.UserServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class T1Question1 {
    private final UserService userService;

    public Mono<Integer> question1A() {
        return userService.findFirstName()
                .doOnNext(user -> log.info("[question1A] :: Name found: {}. Passing to filtering ..", user))
                .filter(name -> name.startsWith("A"))
                .doOnNext(name -> log.info("[question1A] :: Name starts with A: {}", name))
                .map(String::length)
                .switchIfEmpty(Mono.just(-1))
                .doOnError(error -> log.error("[question1A] :: Error processing name", error))
                .doOnSuccess(result -> log.info("[question1A] :: Process completed"));
    }

    public Mono<String> question1B() {
        return userService.findFirstName()
                .doOnNext(user -> log.info("[question1B] :: Name found: {}. Checking existence ..", user))
                .flatMap(name -> userService.existByName(name)
                .filter(Boolean::booleanValue)
                .doOnNext(exists -> log.info("[question1B] :: Name {} exists in DB", name))
                .flatMap(exists -> userService.update(name))
                .switchIfEmpty(Mono.defer(() -> userService.insert(name))))
                .doOnError(error -> log.error("[question1B] :: Error processing name", error))
                .doOnSuccess(result -> log.info("[question1B] :: Process completed"));

    }

    public Mono<String> question1C(String name) {
        return userService.findFirstByName(name)
                .doOnError(error -> log.error("[question1C] :: Error finding name: {}", name, error))
                .onErrorResume(e -> Mono.error(new UserServiceException()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()));
    }
}
