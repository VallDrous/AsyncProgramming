import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            //Ввід параметрів
            System.out.print("Введіть кількість елементів масиву: ");
            int n = sc.nextInt();
            if (n < 2) throw new IllegalArgumentException("Масив повинен мати хоча б 2 елементи.");

            System.out.print("Введіть мінімальне значення: ");
            int min = sc.nextInt();

            System.out.print("Введіть максимальне значення: ");
            int max = sc.nextInt();
            if (min > max) {
                System.out.println("Помилка: мінімальне значення не може бути більшим за максимальне!");
                return;
            }
            //Генерація випадкового масиву
            int[] arr = new Random().ints(n, min, max + 1).toArray();
            System.out.println("\nЗгенерований масив: " + Arrays.toString(arr));
            //WORK STEALING
            ForkJoinPool poolStealing = new ForkJoinPool(); // створюємо ForkJoinPool

            long t1 = System.nanoTime();
            long resStealing = poolStealing.invoke(
                    new SumPairsTask(arr, 0, arr.length - 1, false) // запускаємо recursive task
            );
            long t2 = System.nanoTime();
            //WORK DEALING
            ExecutorService poolDealing =
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            long t3 = System.nanoTime();
            long resDealing = workDealingSum(arr, poolDealing, true);
            long t4 = System.nanoTime();

            poolDealing.shutdown(); // завершуємо пул
            //Результат
            System.out.println("\n===========================");
            System.out.println("Результат Work Stealing: " + resStealing +
                    " | Час: " + (t2 - t1) / 1_000_000 + " мс");
            System.out.println("Результат Work Dealing:  " + resDealing +
                    " | Час: " + (t4 - t3) / 1_000_000 + " мс");

        } catch (Exception e) {
            System.out.println("Помилка введення: " + e.getMessage());
        }
    }
    public static long workDealingSum(int[] arr, ExecutorService pool, boolean showSteps) throws InterruptedException, ExecutionException {

        int numTasks = Runtime.getRuntime().availableProcessors(); // кількість потоків
        int chunk = (arr.length - 1) / numTasks; // ділянка для кожного

        List<Future<Long>> futures = new ArrayList<>();

        //Роздаємо задачі потокам
        for (int i = 0; i < numTasks; i++) {
            // межі підзадачі
            int start = i * chunk;
            int end = (i == numTasks - 1) ? arr.length - 1 : (i + 1) * chunk;

            futures.add(pool.submit(() -> { // запускаємо підзадачу в окремому потоці
                long localSum = 0;
                StringBuilder sb = new StringBuilder();
                // Обчислюємо попарні суми
                for (int j = start; j < end; j++) {
                    long pair = arr[j] + arr[j + 1];
                    localSum += pair;
                    if (showSteps)
                        sb.append(arr[j]).append(" + ").append(arr[j + 1]).append(" = ").append(pair).append("\n");
                }
                // Вивід (якщо увімкнено)
                if (showSteps) {
                    sb.append("Сума підзадачі [").append(start).append("-").append(end)
                            .append("] = ").append(localSum).append("\n");
                    System.out.print(sb);
                }

                return localSum;
            }));
        }
        // --- Збираємо результати всіх потоків ---
        long total = 0;
        for (Future<Long> f : futures) total += f.get();

        if (showSteps) System.out.println("Загальна сума = " + total + "\n");

        return total;
    }
}