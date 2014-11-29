
import java.util.*;
import java.io.*;

public class Questioner {
    private static final String PROMPT = "= ";

    private static final String GENERATE_COMMAND = "g";
    private static final String QUESTION_COMMAND = "q";
    private static final String ANSWER_COMMAND = "a";
    private static final String HINT_COMMAND = "h";
    private static final String LIST_COMMAND = "l";
    private static final String EXIT_COMMAND = "e";

    private static final String COMMAND_LIST
            = "Commands:\n"
            + "= " + GENERATE_COMMAND + " for generate\n"
            + "= " + QUESTION_COMMAND + " for question\n"
            + "= " + ANSWER_COMMAND   + " for answer\n"
            + "= " + HINT_COMMAND     + " for hint\n"
            + "= " + LIST_COMMAND     + " to list\n"
            + "= " + EXIT_COMMAND     + " to exit\n";

    private static ArrayList<Question> questions;

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("usage: " 
                    + Questioner.class.getSimpleName() 
                    + " <filename>");
            System.exit(1);
        }

        String filename = args[0];
        try {
            questions = Question.parse(new File(filename));
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("File not found: " + filename);
            System.exit(1);
        }

        loop();
    }

    private static void loop() {
        Scanner scanner = new Scanner(System.in);
        Random generator = new Random();
        int index = generator.nextInt(questions.size());
        Question currentQuestion = questions.get(index);
        System.out.println(currentQuestion.getQuestion());
        while(true) {
            System.out.print(PROMPT);
            String input = scanner.nextLine();
            Scanner commandScanner = new Scanner(input);
            if(commandScanner.hasNext()) {
                String command = commandScanner.next();
                if(command.equals(GENERATE_COMMAND) && !commandScanner.hasNext()) {
                    index = generator.nextInt(questions.size());
                    currentQuestion = questions.get(index);
                    System.out.println(currentQuestion.getQuestion());
                }
                else if(command.equals(QUESTION_COMMAND) && !commandScanner.hasNext()) {
                    System.out.println(currentQuestion.getQuestion()); 
                }
                else if(command.equals(ANSWER_COMMAND) && !commandScanner.hasNext()) {
                    System.out.println(currentQuestion.getAnswer()); 
                }
                else if(command.equals(HINT_COMMAND) && !commandScanner.hasNext()) {
                    if(currentQuestion.hasHint()) {
                        System.out.println(currentQuestion.getHint());
                    }
                    else {
                        System.out.println("No hint");
                    }
                }
                else if(command.equals(LIST_COMMAND) && !commandScanner.hasNext()) {
                    System.out.print(COMMAND_LIST);
                }
                else if(command.equals(EXIT_COMMAND) && !commandScanner.hasNext()) {
                    System.exit(0);
                }
                else {
                    System.out.println("Invalid command");
                    System.out.println("Type " + LIST_COMMAND + " to list commands");
                }
            }
        }
    }
}

