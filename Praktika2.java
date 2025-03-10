import java.io.*;

class ComputationData implements Serializable {
    private static final long serialVersionUID = 1L;

    private double input;
    private double result;
    private transient String tempData;

    public ComputationData(double input) {
        this.input = input;
        this.tempData = "Тимчасові дані";
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

    public String getTempData() {
        return tempData;
    }
}

class Solver {
    private ComputationData data;

    public Solver(ComputationData data) {
        this.data = data;
    }

    public void compute() {
        double result = Math.sqrt(data.getInput());
        data.setResult(result);
    }
}

class Demo {
    public static void serializeData(ComputationData data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            System.out.println("Дані збережені.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ComputationData deserializeData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (ComputationData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

class ComputationTest {
    public static void main(String[] args) {
        ComputationData data = new ComputationData(25);
        Solver solver = new Solver(data);
        solver.compute();

        System.out.println("Результат: " + data.getResult());

        String filename = "computation.ser";
        Demo.serializeData(data, filename);

        ComputationData restoredData = Demo.deserializeData(filename);
        if (restoredData != null) {
            System.out.println("Відновлений результат: " + restoredData.getResult());
            System.out.println("Тимчасові дані (transient): " + restoredData.getTempData());
        }

        double number = 12.75;
        System.out.println("Двійкове представлення числа " + number + ": " + toBinaryString(number));
    }

    public static String toBinaryString(double number) {
        long intPart = (long) number;
        double fracPart = number - intPart;

        String intBinary = Long.toBinaryString(intPart);
        StringBuilder fracBinary = new StringBuilder();

        while (fracPart > 0 && fracBinary.length() < 10) {
            fracPart *= 2;
            if (fracPart >= 1) {
                fracBinary.append("1");
                fracPart -= 1;
            } else {
                fracBinary.append("0");
            }
        }

        return intBinary + "." + fracBinary.toString();
    }
}