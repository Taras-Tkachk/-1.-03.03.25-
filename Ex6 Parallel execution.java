import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

class ComputationManager {
    private List<Double> data = Collections.synchronizedList(new ArrayList<>());

    public void addData(double value) {
        data.add(value);
    }

    public List<Double> getData() {
        return data;
    }

    public double findMin() {
        return data.parallelStream().min(Double::compareTo).orElse(Double.NaN);
    }

    public double findMax() {
        return data.parallelStream().max(Double::compareTo).orElse(Double.NaN);
    }

    public double computeAverage() {
        return data.parallelStream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    public List<Double> filterByCriterion(double threshold) {
        return data.parallelStream().filter(value -> value > threshold).collect(Collectors.toList());
    }
}

interface Task {
    void execute();
}

class StatsTask implements Task {
    private ComputationManager manager;

    public StatsTask(ComputationManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println("Статистична обробка даних...");
        System.out.println("Мінімум: " + manager.findMin());
        System.out.println("Максимум: " + manager.findMax());
        System.out.println("Середнє значення: " + manager.computeAverage());
    }
}

class TaskQueue {
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private final ExecutorService workerPool = Executors.newFixedThreadPool(2); // Два робочих потоки

    public TaskQueue() {
        for (int i = 0; i < 2; i++) {
            workerPool.execute(this::processTasks);
        }
    }

    public void addTask(Task task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processTasks() {
        while (true) {
            try {
                Task task = taskQueue.take();
                task.execute();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void shutdown() {
        workerPool.shutdown();
    }
}

public class ParallelProcessingTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ComputationManager manager = new ComputationManager();
        TaskQueue taskQueue = new TaskQueue();

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1 - Додати число");
            System.out.println("2 - Запустити статистичну обробку");
            System.out.println("3 - Відфільтрувати за критерієм (>10)");
            System.out.println("4 - Вийти");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Введіть число:");
                    double value = scanner.nextDouble();
                    manager.addData(value);
                    break;

                case 2:
                    taskQueue.addTask(new StatsTask(manager));
                    break;

                case 3:
                    System.out.println("Результати фільтрації (>10): " + manager.filterByCriterion(10));
                    break;

                case 4:
                    taskQueue.shutdown();
                    System.out.println("Програма завершена.");
                    return;

                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }
}