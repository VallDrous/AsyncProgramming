import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture<Route> train = CompletableFuture.supplyAsync(
                () -> new Route("Потяг", 1200, 6), executor
        );

        CompletableFuture<Route> bus = CompletableFuture.supplyAsync(
                () -> new Route("Автобус", 800, 10), executor
        );

        CompletableFuture<Route> plane = CompletableFuture.supplyAsync(
                () -> new Route("Літак", 2500, 2), executor
        );

        CompletableFuture<Route> fastest = train.thenCombine(plane,
                (r1, r2) -> r1.time < r2.time ? r1 : r2
        );

        CompletableFuture<Route> cheapest = bus.thenCompose(route ->
                CompletableFuture.completedFuture(route)
        );

        CompletableFuture<Void> allRoutes = CompletableFuture.allOf(train, bus, plane);

        allRoutes.thenRun(() -> {
            try {
                List<Route> routes = List.of(train.get(), bus.get(), plane.get());
                Route best = routes.stream()
                        .min(Comparator.comparingInt(r -> r.price + r.time * 100))
                        .get();
                System.out.println("Найкращий маршрут:");
                System.out.println(best);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        CompletableFuture.anyOf(train, bus, plane)
                .thenAccept(result -> {
                    System.out.println("Перший доступний варіант:");
                    System.out.println(result);
                });

        fastest.thenAccept(r ->
                System.out.println("Найшвидший маршрут: " + r)
        );

        cheapest.thenAccept(r ->
                System.out.println("Найдешевший маршрут: " + r)
        );

        allRoutes.join();
        executor.shutdown();
    }
}