import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    static CompletableFuture<String> getDataFromSource1() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(2);
            return "Дані з джерела 1";
        });
    }

    static CompletableFuture<String> getDataFromSource2() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(3);
            return "Дані з джерела 2";
        });
    }

    static CompletableFuture<String> processData(String data) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return data + " (оброблено)";
        });
    }

    public static void main(String[] args) {

        CompletableFuture<String> composedFuture = getDataFromSource1().thenCompose(Main::processData);

        CompletableFuture<String> combinedFuture = getDataFromSource1().thenCombine(getDataFromSource2(), (data1, data2) -> data1 + " + " + data2);

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(getDataFromSource1(), getDataFromSource2(), composedFuture);

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(getDataFromSource1(), getDataFromSource2());

        composedFuture.thenAccept(result -> System.out.println("thenCompose результат: " + result));

        combinedFuture.thenAccept(result -> System.out.println("thenCombine результат: " + result));

        allOfFuture.thenRun(() -> System.out.println("allOf: всі задачі завершені"));

        anyOfFuture.thenAccept(result -> System.out.println("anyOf: перший результат -> " + result));

        allOfFuture.join();
    }

    static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}