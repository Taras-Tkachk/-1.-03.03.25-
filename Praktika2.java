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

abstract class DisplayFactory {
    public abstract Displayable createDisplay();
}

class TextDisplayFactory extends DisplayFactory {
    @Override
    public Displayable createDisplay() {
        return new TextDisplay();
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
        Solver solver = new Solver();
        solver.compute(25);
        solver.compute(16);
        solver.compute(9);

        DisplayFactory factory = new TextDisplayFactory();
        Displayable display = factory.createDisplay();
        
        System.out.println("Результати обчислень:");
        display.display(solver.getComputations());

        String filename = "computations.ser";
        Demo.serializeData(solver.getComputations(), filename);

        List<ComputationData> restoredComputations = Demo.deserializeData(filename);
        System.out.println("\nВідновлені результати:");
        display.display(restoredComputations);
    }
}