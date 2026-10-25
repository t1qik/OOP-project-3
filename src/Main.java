import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

void main() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
        System.out.println("--------------------------------------------------");
        System.out.println("Введите путь к файлу-справочнику (.csv или .xml).");
        System.out.println("Для выхода из программы введите 0 и нажмите Enter.");
        System.out.print("> ");

        String input = scanner.nextLine().trim();

        if (input.equals("0")) {
            System.out.println("Завершение работы программы. До свидания!");
            break; // Выходим из цикла и завершаем программу
        }

        if (input.isEmpty()) {
            System.out.println("Путь не может быть пустым. Попробуйте снова.");
            continue;
        }

        DataAnalyzer analyzer;
        if (input.toLowerCase().endsWith(".csv")) {
            analyzer = new CsvAnalyzer();
        } else if (input.toLowerCase().endsWith(".xml")) {
            analyzer = new XmlAnalyzer();
        } else {
            System.out.println("Ошибка: Поддерживаются только файлы с расширением .csv или .xml");
            continue;
        }

        try {
            analyzer.analyze(input);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            continue;
        }

        Map<Address, Integer> duplicates;
        Map<String, int[]> cityFloors;

        if (analyzer instanceof CsvAnalyzer) {
            duplicates = ((CsvAnalyzer) analyzer).duplicatesMap;
            cityFloors = ((CsvAnalyzer) analyzer).cityFloorsMap;
        } else {
            duplicates = ((XmlAnalyzer) analyzer).duplicatesMap;
            cityFloors = ((XmlAnalyzer) analyzer).cityFloorsMap;
        }

        System.out.println("\n=== 1. Дублирующиеся записи ===");
        boolean hasDuplicates = false;
        for (Map.Entry<Address, Integer> entry : duplicates.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println("[" + entry.getValue() + " раз] " + entry.getKey());
                hasDuplicates = true;
            }
        }
        if (!hasDuplicates) System.out.println("Дубликатов не найдено.");

        System.out.println("\n=== 2. Количество зданий по этажам (по городам) ===");
        for (Map.Entry<String, int[]> entry : cityFloors.entrySet()) {
            String city = entry.getKey();
            int[] floors = entry.getValue();

            System.out.print(city + ": ");
            boolean hasData = false;
            for (int i = 0; i < 5; i++) {
                if (floors[i] > 0) {
                    System.out.print((i + 1) + "-эт: " + floors[i] + " | ");
                    hasData = true;
                }
            }
            if (hasData) System.out.println();
        }
        System.out.println();
    }

    scanner.close();
}
