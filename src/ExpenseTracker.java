import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ExpenseTracker {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TransactionRepository repo = new FileTransactionRepository("transactions.txt");
    private static final TransactionService service = new TransactionService(repo);

    public static void main(String[] args) {
        service.load();

        while (true) {
            System.out.println("\n1. Add income");
            System.out.println("2. Add expense");
            System.out.println("3. View monthly summary");
            System.out.println("4. Import from file");
            System.out.println("5. Exit");
            System.out.print("Select Service! : ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTransaction(true);
                case "2" -> addTransaction(false);
                case "3" -> service.showMonthlySummary();
                case "4" -> {
                    System.out.print("Please Enter file path: ");
                    String path = scanner.nextLine();
                    service.importFromFile(path);
                }
                case "5" -> {
                    service.save();
                    System.out.println("Visit again!");
                    return;
                }
                default -> System.out.println("Invalid selection!");
            }
        }
    }

    private static void addTransaction(boolean isIncome) {
        List<String> categories = isIncome ?
                Arrays.asList("Salary", "Business") :
                Arrays.asList("Food", "Rent", "Travel");

        System.out.println("Select categories: " + String.join(", ", categories));
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }

        System.out.print("Enter date (YYYY-MM) or enter for current month: ");
        String dateInput = scanner.nextLine();
        LocalDate date;

        try {
            if (dateInput.isEmpty()) {
                date = LocalDate.now();
            } else {

                date = LocalDate.parse(dateInput + "-01", DateTimeFormatter.ISO_DATE);
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. It for current months.");
            date = LocalDate.now();
        }


        service.addTransaction(new Transaction(isIncome, category, amount, date));
    }
}