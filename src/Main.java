import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    static Map<String, String> cards = new LinkedHashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String action = "";
        while (!action.equals("exit")) {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            action = scanner.nextLine().toLowerCase();

            switch (action) {
                case "add" :
                    addCard(scanner);
                    break;
                case "remove" :
                    removeCard(scanner);
                    break;
                case "import" :
                    importCard(scanner);
                    break;
                case "export" :
                    exportCard(scanner);
                    break;
                case "ask" :
                    askQuestions(scanner);
                    break;
                case "exit" :
                    System.out.println("Bye bye!");
                    break;
                default:
                    System.out.println("There is no such action available");
                    break;
            }

        }
    }

    public static String getKey(Map<String, String> map, String value) {
        return map.keySet().stream()
                .filter(key -> map.get(key).equals(value))
                .findFirst()
                .get();
    }

    public static void addCard(Scanner scanner) {
        System.out.println("The card:");
        String term = scanner.nextLine();
        if (cards.containsKey(term)) {
                System.out.printf("The card \"%s\" already exists.\n\n", term);
                return;
        }
        System.out.println("The definition of the card:");
        String definition = scanner.nextLine();
        if (cards.containsValue(definition)) {
            System.out.printf("The definition \"%s\" already exists.\n\n", definition);
            return;
        }
        cards.put(term, definition);
        System.out.printf("The pair (\"%s\":\"%s\") has been added.\n\n",term,definition);
    }

    public static void removeCard(Scanner scanner) {
        System.out.println("The card:");
        String cardName = scanner.nextLine();

        if (cards.containsKey(cardName)) {
            cards.remove(cardName);
            System.out.println("The card has been removed.\n");
        } else {
            System.out.printf("Can't remove \"%s\": there is no such card.\n\n",cardName);
        }
    }


    public static void importCard(Scanner scanner) {
        System.out.println("File name:");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        if (file.exists()) {
            int cardsImported = 0;
            try {
                Scanner scanner1 = new Scanner(file);
                while (scanner1.hasNext()) {
                    String term = scanner1.nextLine();
                    String definition = scanner1.nextLine();

                    if (!cards.containsKey(term) || !cards.containsValue(definition)) {
                        cards.put(term, definition);
                    } else {
                        cards.replace(term,definition);
                    }
                    ++cardsImported;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(cardsImported+" cards have been loaded");
        } else {
            System.out.println("File not found.");
        }
    }

    public static void exportCard(Scanner scanner) {
        System.out.println("File name:");
        String fileName = scanner.nextLine();

        try (PrintWriter pw = new PrintWriter(new File(fileName))){
            cards.forEach((x,y)-> pw.println(x+"\n"+y));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(cards.size()+" cards have been saved.");
    }

    public static void askQuestions(Scanner scanner) {
        System.out.println("How many time to ask?");
        int numQuestions = scanner.nextInt();
        scanner.nextLine();
        String answer;
        int cardNumber;
        Random random = new Random();
        String[] keys = cards.keySet().toArray(new String[cards.size()]);
        for (int i = 0; i<numQuestions; i++) {
            cardNumber = random.nextInt(cards.size());
            System.out.println("Print the definition of \"" + keys[cardNumber] + "\":");
            answer = scanner.nextLine();
            if (answer.toLowerCase().equals(cards.get(keys[cardNumber]).toLowerCase())) {
                System.out.println("Correct answer.");
            } else if (cards.containsValue(answer)) {
                System.out.printf("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".\n"
                        , cards.get(keys[cardNumber])
                        , getKey(cards, answer));
            } else {
                System.out.printf("Wrong answer. The correct one is \"%s\"\n",cards.get(keys[cardNumber]));
            }
        }
    }
}
