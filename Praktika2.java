import java.io.*;
import java.util.*;

class ComputationData implements Serializable {
    private static final long serialVersionUID = 1L;
    private double input;
    private double result;

    public ComputationData(double input) {
        this.input = input;
    }

    public double getInput() {
        return input;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}

class Solver {
    private List<ComputationData> computations = new ArrayList<>();

    public void compute(double value) {
        ComputationData data = new ComputationData(value);
        data.setResult(Math.sqrt(value));
        computations.add(data);
    }

    public List<ComputationData> getComputations() {
        return computations;
    }
}

interface Displayable {
    void display(List<ComputationData> computations);
}

class TextDisplay implements Displayable {
    @Override
    public void display(List<ComputationData> computations) {
        for (ComputationData data : computations) {
            System.out.println("Вхідне значення: " + data.getInput() + ", Результат: " + data.getResult());
        }
    }
}

class TableDisplay extends TextDisplay {
    private int columnWidth;

    public TableDisplay(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    @Override
    public void display(List<ComputationData> computations) {
        String format = "| %-" + columnWidth + "s | %-" + columnWidth + "s |\n";
        System.out.printf(format, "Вхідне", "Результат");
        System.out.println("-".repeat(columnWidth * 2 + 5));
        for (ComputationData data : computations) {
            System.out.printf(format, data.getInput(), data.getResult());
        }
    }

    public void display(List<ComputationData> computations, String title) {
        System.out.println("\n" + title);
        display(computations);
    }
}

abstract class DisplayFactory {
    public abstract Displayable createDisplay();
}

class TextDisplayFactory extends DisplayFactory {
    @Override
    public Displayable createDisplay() {
        return new TextDisplay();
    }
}

class TableDisplayFactory extends DisplayFactory {
    private int columnWidth;

    public TableDisplayFactory(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    @Override
    public Displayable createDisplay() {
        return new TableDisplay(columnWidth);
    }
}

class Demo {
    public static void serializeData(List<ComputationData> computations, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(computations);
            System.out.println("Дані збережені.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ComputationData> deserializeData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<ComputationData>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

public class ComputationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Solver solver = new Solver();

        System.out.println("Введіть кількість обчислень:");
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Введіть число для обчислення:");
            double value = scanner.nextDouble();
            solver.compute(value);
        }

        System.out.println("Виберіть спосіб виводу результатів:");
        System.out.println("1 - Текстовий");
        System.out.println("2 - Табличний");
        int choice = scanner.nextInt();

        DisplayFactory factory;
        if (choice == 2) {
            System.out.println("Введіть ширину колонки таблиці:");
            int width = scanner.nextInt();
            factory = new TableDisplayFactory(width);
        } else {
            factory = new TextDisplayFactory();
        }

        Displayable display = factory.createDisplay();

        display.display(solver.getComputations());

        String filename = "computations.ser";
        Demo.serializeData(solver.getComputations(), filename);

        List<ComputationData> restoredComputations = Demo.deserializeData(filename);
        System.out.println("\nВідновлені результати:");
        display.display(restoredComputations);
    }
}