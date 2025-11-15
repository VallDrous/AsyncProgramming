import java.util.concurrent.RecursiveTask;

public class SumPairsTask extends RecursiveTask<Long> {
    private final int[] arr;
    private final int start, end;
    private final boolean showSteps;
    private static final int THRESHOLD = 500;

    SumPairsTask(int[] arr, int start, int end, boolean showSteps) {
        this.arr = arr;
        this.start = start;
        this.end = end;
        this.showSteps = showSteps;
    }

    @Override
    protected Long compute() {
        // Якщо ділянка маленька, обчислюємо послідовно
        if (end - start <= THRESHOLD) {
            long sum = 0;
            StringBuilder sb = new StringBuilder();
            // Обчислюємо попарні суми
            for (int i = start; i < end; i++) {
                long pair = arr[i] + arr[i + 1];
                sum += pair;
                if (showSteps)
                    sb.append(arr[i]).append(" + ").append(arr[i + 1]).append(" = ").append(pair).append("\n");
            }
            // Вивід проміжної суми для ділянки
            if (showSteps) {
                sb.append("Сума на ділянці [").append(start).append("-").append(end).append("] = ").append(sum).append("\n");
                System.out.print(sb);
            }
            return sum; // повертаємо суму ділянки
        }
        // Якщо ділянка велика, ділимо задачу на дві підзадачі
        int mid = (start + end) / 2;
        SumPairsTask left = new SumPairsTask(arr, start, mid, showSteps);
        SumPairsTask right = new SumPairsTask(arr, mid + 1, end, showSteps);

        left.fork();              // асинхронно запускаємо ліву підзадачу
        return right.compute() + left.join(); // праву виконуємо у поточному потоці, додаємо результат лівої
    }
}
