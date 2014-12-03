
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.io.*;

/**
 * A representation of a basic question, consisting of a question, answer, and the option of a hint.
 */
public class Question {
    private String question; // The question.
    private String answer; // The answer.
    private String hint; // The hint.
    private boolean hasHint; // Whether or not the hint is set.

    /**
     * Default constructor.
     */
    public Question() {
        this.question = null;
        this.answer = null;
        this.hint = null;
        hasHint = false;
    }

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
     * Parse a file for question, answer, and possibly hint tuple.
     * @param file the XML file to parse.
     * @return an ArrayList of the questions parsed from the XML file.
     * @throws IOException if the file cannot be read correctly.
     * @throws ParserConfigurationException if there was a parser configuration error.
     * @throws SAXException if there was a SAX error.
     */
    public static ArrayList<Question> parseXML(File file) throws IOException, ParserConfigurationException, SAXException {
        ArrayList<Question> questionArray = new ArrayList<Question>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node instanceof Element) {
                Question question = new Question();
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childNode = childNodes.item(j);
                    if (childNode instanceof Element) {
                        String content = childNode.getLastChild().
                                getTextContent().trim();
                        String nodeName = childNode.getNodeName();
                        if (nodeName.equals("question")) {
                            question.question = content;
                        } else if (nodeName.equals("answer")) {
                            question.answer = content;
                        } else if (nodeName.equals("hint")) {
                            question.hint = content;
                            question.hasHint = true;
                        }
                    }
                }
                questionArray.add(question);
            }
        }
        /*for(Question question : questionArray) {
            System.out.println(question);
        }*/
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

    @Override
    public String toString() {
        String hintString = "";
        if(hasHint) hintString += "Hint: " + hint + "\n";
        return "Question: " + question + "\nAnswer" + answer + "\n" + hintString;
    }
}   

