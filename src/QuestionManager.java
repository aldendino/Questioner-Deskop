
import java.util.*;

public class QuestionManager {
    
    private ArrayList<Question> questions;
    private int index;

    private Random generator;    

    public QuestionManager(ArrayList<Question> questions) {
        this.questions = questions;
        index = 0;
        generator = new Random();
    }

    public boolean isEmpty() {
        if(questions != null) {
            return questions.size() == 0;
        }
        else {
            return true;
        }
    }

    public int getIndex() {
        return index;
    }

    public Question getCurrentQuestion() {
        return questions.get(index);
    }

    public void nextIndex() {
        index = (index + 1) % questions.size();
    }

    public void previousIndex() {
        if((index - 1) < 0) index = questions.size() - 1;
        else index = index - 1;
    }

    public void generate() {
        if(questions.size() > 1) {
            int last = index;
            int next = generator.nextInt(questions.size());
            while(next == last) {
                next = generator.nextInt(questions.size());
            }
            index = next;
        }
        else {
            index = generator.nextInt(questions.size());
        }
    }

}
