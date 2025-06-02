package cl.tenpo.learning.reactive.tasks.task1;

import cl.tenpo.learning.reactive.utils.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class T1Question2 {

    private final CountryService countryService;

    public Flux<String> question2A() {
        return countryService.findAllCountries()
                .distinct()
                .take(5)
                .doOnNext(country -> log.info("[question2A] :: Country found: {}", country))
                .doOnComplete(() -> log.info("[question2A] :: Process completed"))
                .doOnError(error -> log.error("[question2A] :: Error processing countries", error));
    }

    public Flux<String> question2B() {
        return countryService.findAllCountries()
                .takeUntil(country -> country.equals("Argentina"))
                .doOnNext(country -> log.info("[question2B] :: Country found: {}", country))
                .doOnComplete(() -> log.info("[question2B] :: Process completed"))
                .doOnError(error -> log.error("[question2B] :: Error processing countries", error));
    }

    public Flux<String> question2C() {
        return countryService.findAllCountries()
                .takeWhile(country -> !country.equalsIgnoreCase("France"))
                .doOnNext(country -> log.info("[question2C] :: Country found: {}", country))
                .doOnComplete(() -> log.info("[question2C] :: Process completed"))
                .doOnError(error -> log.error("[question2C] :: Error processing countries", error));
    }

}
