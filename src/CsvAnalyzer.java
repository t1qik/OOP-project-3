import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CsvAnalyzer implements  DataAnalyzer{

    public Map<Address, Integer> duplicatesMap = new HashMap<>();
    public Map<String, int[]> cityFloorsMap = new HashMap<>();

    @Override
    public void analyze(String filePath) throws IOException{
        long startTime = System.currentTimeMillis();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Пропускаем заголовок
                }

                int pos1 = line.indexOf(';');
                int pos2 = line.indexOf(';', pos1 + 1);
                int pos3 = line.indexOf(';', pos2 + 1);

                if (pos1 == -1 || pos2 == -1 || pos3 == -1) continue;

                String city = clean(line.substring(0, pos1));
                String street = clean(line.substring(pos1 + 1, pos2));
                int house = Integer.parseInt(clean(line.substring(pos2 + 1, pos3)));
                int height = Integer.parseInt(clean(line.substring(pos3 + 1)));

                Address addr = new Address(city, street, house, height);

                duplicatesMap.merge(addr, 1, Integer::sum);

                if (height >= 1 && height <= 5) {
                    int[] floors = cityFloorsMap.computeIfAbsent(city, k -> new int[5]);
                    floors[height - 1]++;
                }
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Время обработки: " + (endTime - startTime) + " мс\n");

    }
    private String clean(String s) {
        return s.trim().replace("\"", "");
    }

}
