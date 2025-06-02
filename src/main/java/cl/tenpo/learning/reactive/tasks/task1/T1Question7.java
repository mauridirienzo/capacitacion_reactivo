package cl.tenpo.learning.reactive.tasks.task1;


import cl.tenpo.learning.reactive.utils.model.Account;
import cl.tenpo.learning.reactive.utils.model.User;
import cl.tenpo.learning.reactive.utils.model.UserAccount;
import cl.tenpo.learning.reactive.utils.service.AccountService;
import cl.tenpo.learning.reactive.utils.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class T1Question7 {

    private final UserService userService;
    private final AccountService accountService;

    public Mono<UserAccount> question7(String userId) {
        return Mono.zip(
                accountService.getAccountByUserId(userId)
                        .doOnSuccess(account -> log.info("[question7] :: Account found: {}", account))
                        .doOnError(error -> log.error("[question7] :: Error fetching account: {}", error.getMessage())),
                userService.getUserById(userId)
                        .doOnSuccess(user -> log.info("[question7] :: User found: {}", user))
                        .doOnError(error -> log.error("[question7] :: Error fetching user: {}", error.getMessage()))
        ).map(tuple -> new UserAccount(tuple.getT2(), tuple.getT1()))
            .doOnSuccess(userAccount -> log.info("[question7] :: UserAccount created: {}", userAccount))
            .doOnError(error -> log.error("[question7] :: Error creating UserAccount: {}", error.getMessage()));
    }
}
