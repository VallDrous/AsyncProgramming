import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

// Головний клас програми
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введіть шлях до директорії: ");
        String path = sc.nextLine();

        System.out.print("Введіть розширення файлу (наприклад .pdf): ");
        String ext = sc.nextLine();

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Некоректна директорія!");
            return;
        }

        ForkJoinPool pool = new ForkJoinPool();
        FileSearchTask task = new FileSearchTask(dir, ext);

        int result = pool.invoke(task);

        System.out.println("Кількість знайдених файлів з розширенням " + ext + ": " + result);
        pool.shutdown();
    }
}