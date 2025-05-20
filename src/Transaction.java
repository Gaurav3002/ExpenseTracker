import java.time.LocalDate;

public class Transaction {
    private final boolean income;
    private final String category;
    private final double amount;
    private final LocalDate date;

    public Transaction(boolean income, String category, double amount, LocalDate date) {
        this.income = income;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public boolean isIncome() { return income; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return (income ? "Income" : "Expense") + " - " + category + ": $" + amount + " on " + date;
    }
}
