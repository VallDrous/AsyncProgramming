import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        CompletableFuture<Void> startMessage = CompletableFuture.runAsync(() ->
                System.out.println("Початок асинхронних обчислень...")
        );

        CompletableFuture<List<Integer>> generateNumbers = CompletableFuture.supplyAsync(() -> {
            List<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                numbers.add(ThreadLocalRandom.current().nextInt(1, 50));
            }
            return numbers;
        });

        CompletableFuture<Integer> calculateMinSum = generateNumbers.thenApplyAsync(numbers -> {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < numbers.size() - 1; i++) {
                int sum = numbers.get(i) + numbers.get(i + 1);
                if (sum < min) {
                    min = sum;
                }
            }
            return min;
        });

        CompletableFuture<Void> printSequence = generateNumbers.thenAcceptAsync(numbers -> {
            System.out.println("Початкова послідовність чисел:");
            System.out.println(numbers);
        });

        CompletableFuture<Void> printResult = calculateMinSum.thenAcceptAsync(min ->
                System.out.println("Мінімальне значення (ai + ai+1): " + min)
        );

        CompletableFuture<Void> finish = CompletableFuture.allOf(startMessage, printSequence, printResult).thenRunAsync(() -> {
            long endTime = System.currentTimeMillis();
            System.out.println("Усі асинхронні операції завершені.");
            System.out.println("Час виконання: " + (endTime - startTime) + " мс");
        });

        finish.join();
    }
}