import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findAll();
    Map<String, List<Transaction>> groupByMonth();
    void importFromFile(String filePath) throws IOException;
    void saveToFile() throws IOException;
    void loadFromFile() throws IOException;
}
