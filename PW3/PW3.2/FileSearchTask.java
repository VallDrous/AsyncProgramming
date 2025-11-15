import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

// Клас, що реалізує задачу пошуку файлів у директорії з використанням Fork/Join
public class FileSearchTask extends RecursiveTask<Integer> {
    private final File dir;
    private final String extension;

    public FileSearchTask(File dir, String extension) {
        this.dir = dir;
        this.extension = extension;
    }

    @Override
    protected Integer compute() {
        int count = 0;
        List<FileSearchTask> subTasks = new ArrayList<>();

        File[] files = dir.listFiles();
        if (files == null) return 0;

        for (File file : files) {
            if (file.isDirectory()) {
                FileSearchTask subTask = new FileSearchTask(file, extension);
                subTask.fork(); // асинхронно запускаємо підзадачу
                subTasks.add(subTask);
            } else if (file.getName().endsWith(extension)) {
                count++;
            }
        }

        for (FileSearchTask task : subTasks) {
            count += task.join(); // чекаємо завершення підзадач
        }

        return count;
    }
}