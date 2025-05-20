import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void addTransaction(Transaction transaction) {
        repository.save(transaction);
    }

    public void showMonthlySummary() {
        Map<String, List<Transaction>> grouped = repository.groupByMonth();
        if (grouped.isEmpty()) {
            System.out.println("No transaction available at this moment.");
            return;
        }

        for (String month : grouped.keySet()) {
            double totalIncome = 0;
            double totalExpense = 0;

            Map<String, Double> incomeBreakdown = new HashMap<>();
            Map<String, Double> expenseBreakdown = new HashMap<>();
            List<Transaction> monthlyTransactions = grouped.get(month);

            for (int i = 0; i < monthlyTransactions.size(); i++) {
                Transaction t = monthlyTransactions.get(i);
                if (t.isIncome()) {
                    totalIncome += t.getAmount();
                    // Income category breakdown
                    String category = t.getCategory();
                    if (incomeBreakdown.containsKey(category)) {
                        double current = incomeBreakdown.get(category);
                        incomeBreakdown.put(category, current + t.getAmount());
                    } else {
                        incomeBreakdown.put(category, t.getAmount());
                    }

                } else {
                    totalExpense += t.getAmount();

                    // Expense category break int to chunks
                    String category = t.getCategory();
                    if (expenseBreakdown.containsKey(category)) {
                        double current = expenseBreakdown.get(category);
                        expenseBreakdown.put(category, current + t.getAmount());
                    } else {
                        expenseBreakdown.put(category, t.getAmount());
                    }
                }
            }

            System.out.println("\nMonth: " + month);
            System.out.printf("Total Income: $%.2f\n", totalIncome);

            for (String cat : incomeBreakdown.keySet()) {
                System.out.printf("  %s: $%.2f\n", cat, incomeBreakdown.get(cat));
            }

            System.out.printf("Total Expenses: $%.2f\n", totalExpense);

            for (String cat : expenseBreakdown.keySet()) {
                System.out.printf("  %s: $%.2f\n", cat, expenseBreakdown.get(cat));
            }

            System.out.printf("Net Income: $%.2f\n", totalIncome - totalExpense);
        }
    }


    public void importFromFile(String path) {
        try {
            repository.importFromFile(path);
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }

    public void save() {
        try {
            repository.saveToFile();
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public void load() {
        try {
            repository.loadFromFile();
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }
}
