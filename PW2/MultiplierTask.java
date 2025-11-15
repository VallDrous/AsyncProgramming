import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MultiplierTask implements Callable<List<Integer>> {
    private final List<Integer> part;
    private final int factor;

    public MultiplierTask(List<Integer> part, int factor) {
        this.part = part;
        this.factor = factor;
    }

    @Override
    public List<Integer> call() {
        List<Integer> result = new ArrayList<>();
        for (int num : part) {

            result.add(num * factor);
        }
        return result;
    }
}
