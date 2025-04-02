package dthunn.reactivetutorial;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class ReactiveTutorial {

    private Mono<String> testMethod() {
        return Mono.empty();
    }

    private Flux<String> testFlow() {
        return Flux.fromStream(List.of("a", "b", "c").stream());
    }

    private Flux<String> testMap() {
        Flux<String> flux = Flux.just("Rust", "Java", "Python");
        return flux.map(String::toUpperCase);
    }

    private Flux<String> testFLatMap() {
        Flux<String> flux = Flux.just("Rust", "Java", "Python");
        return flux.flatMap(s -> Mono.just(s.toUpperCase()));
    }

    private Flux<String> testBasicSkip() {
        Flux<String> flux = Flux.just("Java", "Cpp", "Rust", "Dart")
                .delayElements(Duration.ofSeconds(1));
        //flux.skip(2);
        //return flux.skip(Duration.ofMillis(2010));
        return flux.skipLast(2);
    }

    private Flux<Integer> testComplexSkip() {
        Flux<Integer> flux = Flux.range(1, 20);
        return flux.skipUntil(integer -> integer == 30);
    }

    public static void main(String[] args) {
        ReactiveTutorial reactiveTutorial = new ReactiveTutorial();
        reactiveTutorial.testMethod().subscribe(System.out::println);
        reactiveTutorial.testMap().subscribe(System.out::println);
    }
}
