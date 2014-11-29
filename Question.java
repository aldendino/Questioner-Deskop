
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
}   
