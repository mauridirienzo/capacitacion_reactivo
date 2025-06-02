package cl.tenpo.learning.reactive.tasks.task1;

import cl.tenpo.learning.reactive.utils.model.Page;
import cl.tenpo.learning.reactive.utils.service.CountryService;
import cl.tenpo.learning.reactive.utils.service.TranslatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class T1Question3 {

    private final CountryService countryService;
    private final TranslatorService translatorService;

    public Flux<String> question3A(Page<String> page) {
        return Flux.fromIterable(Optional.ofNullable(page)
                        .map(Page::items)
                        .orElse(Collections.emptyList()))
                .doOnNext(item -> log.info("[question3A] :: Element found: {}", item))
                .doOnComplete(() -> log.info("[question3A] :: Process completed"))
                .doOnError(error -> log.error("[question3A] :: Error processing elements", error));
    }

    public Flux<String> question3B(String country) {
        return countryService.findCurrenciesByCountry(country)
                .doOnNext(c -> log.info("[question3B] :: Currency found: {}", c))
                .doOnComplete(() -> log.info("[question3B] :: Process completed"))
                .doOnError(error -> log.error("[question3B] :: Error processing currencies", error));
    }

    public Flux<String> question3C() {

        return countryService.findAllCountries()
                .flatMap(country -> Optional.ofNullable(translatorService.translate(country))
                        .map(Flux::just)
                        .orElseGet(Flux::empty))
                .doOnNext(tr -> log.info("[question3C] :: Translated country: {}", tr))
                .doOnComplete(() -> log.info("[question3C] :: Process completed"))
                .doOnError(error -> log.error("[question3C] :: Error translating countries", error));
    }

}
