package com.example.reactive.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

public class RxJavaTests {

    @Test
    void helloWorld() {
        Flowable.just("Hello world").subscribe(System.out::println);
    }

    @Test
    void range() {
        Flowable.range(1, 5)
            .map(v -> v * v)
            .filter(v -> v % 3 == 0)
            .subscribe(System.out::println);
    }

    @Test
    void runtime() {
        Observable.create(emitter -> {
                while (!emitter.isDisposed()) {
                    long time = System.currentTimeMillis();
                    emitter.onNext(time);
                    if (time % 2 != 0) {
                        emitter.onError(new IllegalStateException("Odd millisecond!"));
                        break;
                    }
                }
            })
            .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    void simpleBackgroundComputation() throws InterruptedException {
        Flowable.fromCallable(() -> {
                Thread.sleep(1000); //  imitate expensive computation
                return "Done";
            })
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe(x -> {
                System.out.println("Background thread name: " + Thread.currentThread().getName());
                System.out.println(x);
            }, Throwable::printStackTrace);

        System.out.println("Main thread name: " + Thread.currentThread().getName());
        Thread.sleep(2000); // <--- wait for the flow to finish
    }

    @Test
    void concurrencyWithinAFlow() {
        Flowable.range(1, 10)
            .observeOn(Schedulers.computation())
            .map(v -> v * v)
            .blockingSubscribe(System.out::println);
    }

    @Test
    void parallelProcessing() throws InterruptedException {
        Flowable.range(1, 10)
            .flatMap(v ->
                Flowable.just(v)
                    .subscribeOn(Schedulers.computation())
                    .map(w -> w * w)
            )
            .blockingSubscribe(System.out::println);

        Thread.sleep(2000);
        System.out.println("------------------");

        Flowable.range(1, 10)
            .parallel()
            .runOn(Schedulers.computation())
            .map(v -> v * v)
            .sequential()
            .blockingSubscribe(System.out::println);
    }

    @Test
    void dependentSubFlows() {

        class Inventory {
            private final Integer value;

            public Inventory(Integer value) {
                this.value = value;
            }

            public Integer getValue() {
                return value;
            }
        }

        class Demand {
            private final String demandValue;

            public Demand(String demandValue) {
                this.demandValue = demandValue;
            }

            public String getDemandValue() {
                return demandValue;
            }
        }

        Function<Inventory, Flowable<Demand>> getDemandAsync =
            i -> Flowable.just(new Demand(i.getValue().toString()));

        Flowable<Inventory> inventorySource = Flowable.just(new Inventory(1), new Inventory(2));

        inventorySource
            .flatMap(inventoryItem -> getDemandAsync.apply(inventoryItem)
                .map(demand -> "Item " + inventoryItem.getValue() + " has demand " + demand.getDemandValue()))
            .subscribe(System.out::println);
    }
}
