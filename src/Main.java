import java.util.*;
import java.util.stream.Stream;

class Main {
    final static String CHARS = "RLRFR";
    final static int CHARS_QUANTITY = 100;
    final static int THREADS_QUANTITY = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new TreeMap<>();

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < THREADS_QUANTITY; i++) {
            new Thread(() -> {
                String generated = generateRoute(CHARS, CHARS_QUANTITY);
                int frequency = generated.replace("R", "").length();
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(frequency)) {
                        sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                    } else {
                        sizeToFreq.put(frequency, 1);
                    }
                }
            }).start();
        }

        Map.Entry<Integer, Integer> max = sizeToFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Самое частое количество повторений " +
                max.getKey() +
                " (встретилось " +
                max.getValue() +
                " раз)");
        System.out.println("Другие размеры: ");

        sizeToFreq.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.println(" - " +
                        e.getKey() +
                        " (" + e.getValue() +
                        " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}