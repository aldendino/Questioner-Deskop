import java.util.*;
import java.io.*;

public class Question {
    private String question;
    private String answer;
    private String hint;
    private boolean hasHint;

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
        hint = null;
        hasHint = false;
    }

    public Question(String question, String answer, String hint) {
        this.question = question;
        this.answer = answer;
        this.hint = hint;
        hasHint = true;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getHint() {
        return hint;
    }

    public boolean hasHint() {
        return hasHint();
    }

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

    public static boolean isValidInput(String input) {
        if(input.equals("")) return false;
        else if(input.matches("^\\s+$")) return false;
        else return true;
    }
}   
