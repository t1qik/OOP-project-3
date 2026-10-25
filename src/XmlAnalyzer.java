import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlAnalyzer implements DataAnalyzer {

    public Map<Address, Integer> duplicatesMap = new HashMap<>();
    public Map<String, int[]> cityFloorsMap = new HashMap<>();

    @Override
    public void analyze(String filePath) throws IOException {
        long startTime = System.currentTimeMillis();

        // Настройка StAX
        XMLInputFactory factory = XMLInputFactory.newInstance();
        // Отключаем загрузку внешних DTD, чтобы избежать уязвимостей (XXE)
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        try (FileInputStream fis = new FileInputStream(filePath)) {
            XMLStreamReader reader = factory.createXMLStreamReader(fis);

            while (reader.hasNext()) {
                int event = reader.next();

                // Нас интересуют только открывающие теги <item>
                if (event == XMLStreamConstants.START_ELEMENT && "item".equals(reader.getLocalName())) {

                    // Читаем атрибуты
                    String city = reader.getAttributeValue(null, "city");
                    String street = reader.getAttributeValue(null, "street");

                    // Атрибуты могут отсутствовать, делаем проверку
                    if (city == null || street == null) continue;

                    int house = Integer.parseInt(reader.getAttributeValue(null, "house"));
                    int height = Integer.parseInt(reader.getAttributeValue(null, "floor"));

                    Address addr = new Address(city.trim(), street.trim(), house, height);

                    // 1. Считаем дубликаты
                    duplicatesMap.merge(addr, 1, Integer::sum);

                    // 2. Считаем этажность по городам
                    if (height >= 1 && height <= 5) {
                        int[] floors = cityFloorsMap.computeIfAbsent(city, k -> new int[5]);
                        floors[height - 1]++;
                    }
                }
            }
            reader.close();
        } catch (XMLStreamException e) {
            throw new IOException("Ошибка парсинга XML: " + e.getMessage(), e);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Время обработки XML: " + (endTime - startTime) + " мс\n");
    }
}
