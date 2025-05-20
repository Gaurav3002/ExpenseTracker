import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileTransactionRepository implements TransactionRepository {
    private final String dataFile;
    private final List<Transaction> transactions = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public FileTransactionRepository(String dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return transactions;
    }

    @Override
    public Map<String, List<Transaction>> groupByMonth() {
        Map<String, List<Transaction>> grouped = new HashMap<>();
        for (Transaction transaction : transactions) {
            String month = transaction.getDate().getYear() + "-" + String.format("%02d", transaction.getDate().getMonthValue());
            if (!grouped.containsKey(month)) {
                grouped.put(month, new ArrayList<>());
            }
            grouped.get(month).add(transaction);
        }
        return grouped;
    }


    @Override
    public void importFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    boolean isIncome = Boolean.parseBoolean(parts[0]);
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3]);
                    transactions.add(new Transaction(isIncome, category, amount, date));
                    count++;
                }
            }
            System.out.println("Imported " + count + " transactions.");
        }
    }

    // method is going to save the transaction itn to the file name 'transaction.txt'
    @Override
    public void saveToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
            for (Transaction t : transactions) {
                writer.printf("%b,%s,%.2f,%s%n",
                        t.isIncome(),
                        t.getCategory(),
                        t.getAmount(),
                        t.getDate());
            }
        }
    }
// method is going to load the existing data from the file
    @Override
    public void loadFromFile() throws IOException {
        File file = new File(dataFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 4) {
                    transactions.add(new Transaction(
                            Boolean.parseBoolean(parts[0]),
                            parts[1],
                            Double.parseDouble(parts[2]),
                            LocalDate.parse(parts[3])
                    ));
                }
            }
        }
    }
}
