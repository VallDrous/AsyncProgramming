
// Клас який представляє автомобіль (окремий потік)
class Car implements Runnable {
    private String name;
    private Parking parking;

    public Car(String name, Parking parking) {
        this.name = name;
        this.parking = parking;
    }

    // Дія яку виконує потік (спроба припаркуватися)
    @Override
    public void run() {
        parking.parkCar(name);
    }
}
