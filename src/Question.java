
import java.util.*;
import java.io.*;

/**
 * A representation of a basic question, consisting of a question, answer, and the option of a hint.
 */
public class Question {
    private String question;
    private String answer;
    private String hint;
    private boolean hasHint;

    /**
     * Create a question from a question and answer pair.
     * @param question the question.
     * @param answer the answer to the question.
     */
    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
        hint = null;
        hasHint = false;
    }

    /**
     * Creates a question from a question, answer, and hint tuple.
     * @param question the question.
     * @param answer the answer to the question.
     * @param hint a hint for the question.
     */
    public Question(String question, String answer, String hint) {
        this.question = question;
        this.answer = answer;
        this.hint = hint;
        hasHint = true;
    }

    /**
     * Get the question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Get the answer.
     * @return
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Get the hint.
     * @return
     */
    public String getHint() {
        return hint;
    }

    /**
     * See if the question has a hint.
     * @return whether or not the question has a hint.
     */
    public boolean hasHint() {
        return hasHint;
    }

    /**
     * Parse a file for question and answer pairs.
     * @param file the file to parse.
     * @return an ArrayList of the questions parsed from the file.
     * @throws FileNotFoundException if the file does not exist in the current directory.
     */
    public static ArrayList<Question> parse(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        ArrayList<Question> questionArray = new ArrayList<Question>();
        while(true) {
            if(!scanner.hasNextLine()) break;
            String inputQuestion = scanner.nextLine();
            if(!Question.isValidInput(inputQuestion)) continue;
            if(!scanner.hasNextLine()) break;
            String inputAnswer = scanner.nextLine();
            if(!Question.isValidInput(inputAnswer)) continue;
            questionArray.add(new Question(inputQuestion, inputAnswer));
        }
        return questionArray;
    }

    /**
     * Check for the validity of the input based on whether it is as question or answer.
     * @param input the input to check for validity.
     * @return the validity of the input.
     */
    public static boolean isValidInput(String input) {
        if(input.equals("")) return false; // If the input is blank.
        else if(input.matches("^\\s+$")) return false; // If the input consists of all spaces.
        else return true;
    }
}   

