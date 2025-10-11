import java.util.concurrent.TimeUnit;
import java.time.LocalTime;

// Основний клас програми
public class Main {
    public static void main(String[] args) {
        try {
            LocalTime now = LocalTime.now(); // поточний час
            int hour = now.getHour();
            int parkingPlaces;

            // Визначення кількості місць залежно від часу доби
            if (hour >= 6 && hour <= 20) {
                parkingPlaces = 5;
                System.out.println("Зараз день (" + hour + ":00). Доступно місць: 5");
            } else {
                parkingPlaces = 8;
                System.out.println("Зараз ніч (" + hour + ":00). Доступно місць: 8");
            }

            // Створення об’єкт паркінгу
            Parking parking = new Parking(parkingPlaces);

            // Створення автомобілів які намагаються припаркуватися
            for (int i = 1; i <= 12; i++) {
                Thread carThread = new Thread(new Car("Авто №" + i, parking));
                carThread.start();

                // Затримка між прибуттям автомобілів
                TimeUnit.SECONDS.sleep(1);
            }

        } catch (Exception e) {
            System.out.println("Помилка у програмі: " + e.getMessage());
        }
    }
}