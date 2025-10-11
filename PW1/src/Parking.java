import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

// Клас який моделює паркінг
class Parking {
    private Semaphore semaphore; // семафор для контролю кількості доступних місць

    // Конструктор який приймає кількість паркомісць
    public Parking(int places) {
        this.semaphore = new Semaphore(places, true);
    }

    // Метод повертає кількість вільних місць
    public int getAvailablePlaces() {
        return semaphore.availablePermits();
    }

    // Метод який моделює процес паркування автомобіля
    public void parkCar(String carName) {
        try {
            System.out.println(carName + " приїхала до паркінгу. Вільних місць: " + getAvailablePlaces());

            // Отримання дозволу на паркування
            semaphore.acquire();

            System.out.println(carName + " зайняла місце. Залишилось вільних: " + getAvailablePlaces());
            // Імітування часу перебування на паркінгу (2–6 секунд)
            TimeUnit.SECONDS.sleep((int) (Math.random() * 5 + 2));
            System.out.println(carName + " виїжджає з паркінгу.");
            // Звільнення місця
            semaphore.release();
            System.out.println("Після виїзду " + carName + " вільних місць: " + getAvailablePlaces());

        } catch (InterruptedException e) {
            System.out.println(carName + " не змогла припаркуватися: " + e.getMessage());
        }
    }
}
