import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.math.BigInteger;

public class Main {

    public static BigInteger factorialBig(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<int[]> arrayFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();

            int[] arr = new Random().ints(10, 1, 10).toArray();

            long end = System.currentTimeMillis();
            System.out.println("Генерація масиву: " + Arrays.toString(arr));
            System.out.println("Час виконання (генерація): " + (end - start) + " ms\n");

            return arr;
        });

        CompletableFuture<int[]> increasedArrayFuture = arrayFuture.thenApplyAsync(initialArr -> {
            long start = System.currentTimeMillis();

            int[] arr2 = Arrays.stream(initialArr).map(x -> x + 5).toArray();

            long end = System.currentTimeMillis();
            System.out.println("Масив після збільшення на 5: " + Arrays.toString(arr2));
            System.out.println("Час виконання (збільшення): " + (end - start) + " ms\n");

            return arr2;
        });

        CompletableFuture<BigInteger> factorialResult = increasedArrayFuture.thenApplyAsync(arr2 -> {
            long start = System.currentTimeMillis();

            try {
                int[] initialArr = arrayFuture.get();

                int sum1 = Arrays.stream(initialArr).sum();
                int sum2 = Arrays.stream(arr2).sum();
                int total = sum1 + sum2;

                BigInteger fact = factorialBig(total);

                long end = System.currentTimeMillis();
                System.out.println("Сума початкового масиву = " + sum1);
                System.out.println("Сума другого масиву = " + sum2);
                System.out.println("Загальна сума = " + total);
                System.out.println("Факторіал = " + fact);
                System.out.println("Час виконання (факторіал): " + (end - start) + " ms\n");

                return fact;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        factorialResult.thenRunAsync(() -> {
            System.out.println("Усі асинхронні обчислення завершено!");
        });

        factorialResult.get();
    }
}