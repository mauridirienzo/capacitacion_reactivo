package cl.tenpo.learning.reactive.tasks.task1;

import cl.tenpo.learning.reactive.utils.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class T1Question4 {

    private final CountryService countryService;

    public Flux<String> question4A() {
        Flux<String> cached = countryService.findAllCountries()
                .take(200)
                .cache();

        return cached.groupBy(country -> country)
                .flatMap(group -> group.count().map(count -> Map.entry(group.key(), count)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .doOnNext(map -> log.info("[question4A] :: Repetitions by country: {}", map))
                .thenMany(cached.distinct().sort())
                .doOnComplete(() -> log.info("[question4A] :: Process completed"))
                .doOnError(error -> log.error("[question4A] :: Error processing countries", error));
    }
}
