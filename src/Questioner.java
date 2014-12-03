
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.io.*;

/**
 * Command line version of the QuestionerUI for use in the terminal,
 *  or for quick testing before implementing the gui elements.
 */
public class Questioner {
    private static final String PROMPT = "= "; // The prompt for entering a command.

    private static final String GENERATE_COMMAND = "g"; // The command for generating a random question.
    private static final String NEXT_COMMAND = "n"; // The command for moving to the next question.
    private static final String PREVIOUS_COMMAND = "p"; // The command for moving to the previous question.
    private static final String QUESTION_COMMAND = "q"; // The command or reprinting the current question.
    private static final String ANSWER_COMMAND = "a"; // The command for displaying the answer to the current question.
    private static final String HINT_COMMAND = "h"; // The command for displaying the hint to the current question.
    private static final String LIST_COMMAND = "l"; // The command for listing the possible commands.
    private static final String EXIT_COMMAND = "e"; // The command to exit the program.

    private static final String COMMAND_LIST // A representation of the list of commands and their usages.
            = "Commands:\n"
            + "= " + GENERATE_COMMAND + " for generate\n"
            + "= " + NEXT_COMMAND     + " for next\n"
            + "= " + PREVIOUS_COMMAND + " for previous\n"
            + "= " + QUESTION_COMMAND + " for question\n"
            + "= " + ANSWER_COMMAND   + " for answer\n"
            + "= " + HINT_COMMAND     + " for hint\n"
            + "= " + LIST_COMMAND     + " to list\n"
            + "= " + EXIT_COMMAND     + " to exit\n";

    private static ArrayList<Question> questions; // The array of questions.

    /**
     * Start the program by giving an argument of the filename to read as a question database.
     * @param args the list of inputs, in this case a file name in the current directory.
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("usage: " 
                    + Questioner.class.getSimpleName() 
                    + " <filename>");
            System.exit(1);
        }

        String filename = args[0];
        try {
            questions = Question.parseXML(new File(filename));
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("File not found: " + filename);
            System.exit(1);
        }
        catch(IOException ioe) {
            System.out.println("File could not be read: " + filename);
            System.exit(1);
        }
        catch(ParserConfigurationException pce) {
            System.out.println("File could not be parsed: " + filename);
            System.exit(1);
        }
        catch(SAXException se) {
            System.out.println("File could not be parsed: " + filename);
            System.exit(1);
        }

        loop();
    }

    /**
     * The main loop with the navigation logic.
     */
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
                    index = generator.nextInt(questions.size()); // Generate a random question.
                    currentQuestion = questions.get(index);
                    System.out.println(currentQuestion.getQuestion());
                }
                else if(command.equals(NEXT_COMMAND) && !commandScanner.hasNext()) {
                    index = nextIndex(index, questions.size());
                    currentQuestion = questions.get(index);
                    System.out.println(currentQuestion.getQuestion());
                }
                else if(command.equals(PREVIOUS_COMMAND) && !commandScanner.hasNext()) {
                    index = previousIndex(index, questions.size());
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

    /**
     * Move to the next question.
     * @param index the index of the current question in the question array.
     * @param size the size of the question array.
     * @return the index of the next question in the question array.
     */
    private static int nextIndex(int index, int size) {
        return (index + 1) % size;
    }

    /**
     * Move to the previous question.
     * @param index the index of the current question in the question array.
     * @param size the size of the question array.
     * @return the index of the previous question in the question array.
     */
    private static int previousIndex(int index, int size) {
        if((index - 1) < 0) return size - 1;
        return index - 1;
    }
}

