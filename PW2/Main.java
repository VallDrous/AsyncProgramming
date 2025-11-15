import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        //Введення значень користувачем
        System.out.print("Введіть мінімальне значення діапазону: ");
        int min = sc.nextInt();
        System.out.print("Введіть максимальне значення діапазону: ");
        int max = sc.nextInt();
        System.out.print("Введіть множник: ");
        int factor = sc.nextInt();
        //Перевірка коректності введених даних
        if (min < -100 || max > 100) {
            System.out.println("Помилка: діапазон повинен бути в межах [-100; 100].");
            return;
        }
        if (min > max) {
            System.out.println("Помилка: мінімальне значення не може бути більшим за максимальне!");
            return;
        }
        //Створення списку та заповнення значеннями в діапазоні
        int size = 50; // можна випадково від 40 до 60
        List<Integer> array = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < size; i++) array.add(rand.nextInt(max - min + 1) + min);

        // Створення потоків
        int chunkSize = 10;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<List<Integer>>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        //Розбиття масиву та додавання об'єкту класу для обчислення даних у окремому потоці
        for (int i = 0; i < array.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, array.size());
            List<Integer> part = new ArrayList<>(array.subList(i, end));
            futures.add(executor.submit(new MultiplierTask(part, factor)));
        }
        //Дістаємо результат
        CopyOnWriteArrayList<Integer> result = new CopyOnWriteArrayList<>();
        for (Future<List<Integer>> f : futures) {
            while (!f.isDone()) { /* чекаємо завершення */ }
            if (!f.isCancelled()) {
                result.addAll(f.get());
            }
        }

        executor.shutdown();

        System.out.println("Результат: " + result);
        System.out.println("Час виконання: " + (System.currentTimeMillis() - startTime) + " мс");
    }
}