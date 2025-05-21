package expenseTracker;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class ExpenseTracker {
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Load Transactions from File");
            System.out.println("4. Save Transactions to File");
            System.out.println("5. View Monthly Summary");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (option) {
                case 1 -> addTransaction(Transaction.Type.INCOME);
                case 2 -> addTransaction(Transaction.Type.EXPENSE);
                case 3 -> loadFromFile();
                case 4 -> saveToFile();
                case 5 -> showMonthlySummary();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void addTransaction(Transaction.Type type) {
        System.out.print("Enter category (e.g. Salary, Business, Food, Rent, Travel): ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        LocalDate date = LocalDate.now(); // for simplicity
        Transaction t = new Transaction(type, category, amount, date);
        transactions.add(t);
        System.out.println("Transaction added: " + t);
    }

    private static void loadFromFile() {
        //ystem.out.print("Enter filename to load: ");
        //String fileName = scanner.nextLine();
    	String fileName = "C:\\Users\\johnd\\eclipse\\ExpenseTracker\\src\\expenseTracker\\transactions.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                transactions.add(Transaction.fromString(line));
            }
            System.out.println("Transactions loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        System.out.print("Enter filename to save: ");
        String fileName = scanner.nextLine();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
            System.out.println("Transactions saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void showMonthlySummary() {
        System.out.print("Enter month and year (YYYY-MM): ");
        String input = scanner.nextLine();
        YearMonth ym = YearMonth.parse(input);
        double incomeTotal = 0;
        double expenseTotal = 0;

        for (Transaction t : transactions) {
            if (YearMonth.from(t.getDate()).equals(ym)) {
                if (t.getType() == Transaction.Type.INCOME) {
                    incomeTotal += t.getAmount();
                } else {
                    expenseTotal += t.getAmount();
                }
            }
        }

        System.out.println("Summary for " + ym + ":");
        System.out.printf("Total Income: %.2f\n", incomeTotal);
        System.out.printf("Total Expenses: %.2f\n", expenseTotal);
        System.out.printf("Net Balance: %.2f\n", (incomeTotal - expenseTotal));
    }
}

