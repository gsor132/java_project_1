import java.util.Scanner;

public class Main {
    static ComplexMatrix previousResult = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = readInt(scanner, "> ");
            
            if (choice == 0) break;
            if (choice < 1 || choice > 5) {
                System.out.println("нет такого пункта");
                continue;
            }

            try {
                processAction(scanner, choice);
            } catch (Exception e) {
                System.out.println("ошибка: " + e.getMessage());
                previousResult = null;
            }
        }
        scanner.close();
    }

    static void printMenu() {
        System.out.println("1 - сложение");
        System.out.println("2 - умножение");
        System.out.println("3 - деление");
        System.out.println("4 - транспонирование");
        System.out.println("5 - определитель");
        System.out.println("0 - выход");
    }

    static void processAction(Scanner sc, int action) {
        if (action == 4 || action == 5) {
            ComplexMatrix mat = inputMatrix(sc, "A");
            if (mat == null) return;

            if (action == 4) {
                previousResult = mat.transpose();
                System.out.println("получилось:\n" + previousResult);
            } else {
                System.out.println("определитель = " + mat.determinant());
                previousResult = null;
            }
        } else {
            ComplexMatrix m1 = inputMatrix(sc, "A");
            if (m1 == null) return;
            
            ComplexMatrix m2 = inputMatrix(sc, "B");
            if (m2 == null) return;

            if (action == 1) previousResult = m1.add(m2);
            else if (action == 2) previousResult = m1.multiply(m2);
            else previousResult = m1.divide(m2);

            System.out.println("получилось:\n" + previousResult);
        }
    }

    static ComplexMatrix inputMatrix(Scanner sc, String name) {
        if (name.equals("A") && previousResult != null) {
            System.out.println("--- матрица A ---");
            System.out.println("продолжить работу с текущими значениями (1 - да, 0 - нет)");
            int usePrev = readInt(sc, "> ");
            
            if (usePrev == 1) {
                ComplexMatrix temp = previousResult;
                previousResult = null;
                System.out.println("беру:\n" + temp);
                return temp;
            }
            previousResult = null;
        }

        System.out.println("--- матрица " + name + " ---");
        int r = readPositiveInt(sc, "строк: ");
        if (r <= 0) return null;
        
        int c = readPositiveInt(sc, "столбцов: ");
        if (c <= 0) return null;

        ComplexMatrix matrix = new ComplexMatrix(r, c);
        System.out.println("введи " + (r * c) + " элементов, вводи re im через пробел:");
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                double re = readDouble(sc);
                double im = readDouble(sc);
                matrix.set(i, j, new Complex(re, im));
            }
        }
        return matrix;
    }


    static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.println("не число");
            sc.nextLine();
            System.out.print(prompt);
        }
        return sc.nextInt();
    }

    static int readPositiveInt(Scanner sc, String prompt) {
        int val = readInt(sc, prompt);
        if (val <= 0) {
            System.out.println("меньше нуля");
            return -1; 
        }
        return val;
    }

    static double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.println("не число");
            sc.nextLine();
        }
        return sc.nextDouble();
    }
}