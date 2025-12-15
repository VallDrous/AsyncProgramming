public class Route {
    String transport;
    int price;
    int time;

    Route(String transport, int price, int time) {
        this.transport = transport;
        this.price = price;
        this.time = time;
    }

    @Override
    public String toString() {
        return transport + " | Ціна: " + price + " грн | Час: " + time + " год";
    }
}
