
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class QuestionerUI extends JFrame implements KeyListener {

    private final String FRAME_TITLE = "Questioner";
    private final int FRAME_WIDTH = 450;
    private final int FRAME_HEIGHT = 250;

    private final Color SHOW_COLOUR = Color.WHITE;
    private final Color HIDE_COLOUR = Color.DARK_GRAY;
    private final Insets TEXT_INSETS = new Insets(5, 5, 5, 5);
    private final EmptyBorder EMPTY_BORDER = new EmptyBorder(TEXT_INSETS);

    private JFileChooser fileChooser;

    private JPanel mainPanel;
    private JSplitPane questionSplitPane;
    private JPanel buttonPanel;
    private JScrollPane buttonPane;

    private JTextArea questionText;
    private JTextArea answerText;
    //private JTextArea hintText;
    private JScrollPane questionScrollPane;
    private JScrollPane answerScrollPane;
    //private JScrollPane hintScrollPane;

    private JButton loadButton;
    private JButton randomButton;
    private JButton nextButton;
    private JButton previousButton;
    private JButton answerButton;
    private JButton hintButton;
    private JButton resetButton;

    private ArrayList<Question> questions;
    private Question currentQuestion;

    private Random generator;
    private int index = 0;

    public QuestionerUI() {
        setupMainPanel();
        add(mainPanel);
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setLocationByPlatform(true);
        setTitle(FRAME_TITLE);
        addKeyListener(this);
        setVisible(true);
    }

    private void setupMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        setupQuestionPane();
        mainPanel.add(questionSplitPane, BorderLayout.CENTER);

        setupButtonPanel();
        mainPanel.add(buttonPane, BorderLayout.EAST);
    }

    private void setupQuestionPane() {
        Dimension textDimension = new Dimension(100, 100);

        questionText = new JTextArea();
        questionText.setMinimumSize(textDimension);
        questionText.setBorder(EMPTY_BORDER);
        questionText.setEditable(false);
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.addKeyListener(this);
        questionScrollPane = new JScrollPane(questionText);
        questionScrollPane.setMinimumSize(textDimension);

        answerText = new JTextArea();
        answerText.setMinimumSize(textDimension);
        answerText .setBorder(EMPTY_BORDER);
        answerText.setEditable(false);
        answerText.setLineWrap(true);
        answerText.setWrapStyleWord(true);
        answerText.addKeyListener(this);
        answerScrollPane = new JScrollPane(answerText);
        answerScrollPane.setMinimumSize(textDimension);

        /*hintText = new JTextArea();
        hintText.setBorder(EMPTY_BORDER);
        hintText.setEditable(false);
        hintText.setLineWrap(true);
        hintText.setWrapStyleWord(true);
        hintText.addKeyListener(this);
        hintScrollPane = new JScrollPane(hintText);*/
        
        questionSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, questionScrollPane, answerScrollPane);
        questionSplitPane.setBorder(EMPTY_BORDER);
    }

    private void setupButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPane = new JScrollPane(buttonPanel);

        generator = new Random();

        fileChooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);

        loadButton = new JButton("Load");
        class loadButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                loadFile();
            }
        }
        loadButton.addActionListener(new loadButtonListener());
        loadButton.addKeyListener(this);
        buttonPanel.add(loadButton);

        randomButton = new JButton("Generate");
        class randomButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                setIndex(generate());    
            }
        }
        randomButton.addActionListener(new randomButtonListener());
        randomButton.addKeyListener(this);
        buttonPanel.add(randomButton);

        nextButton = new JButton("Next");
        class nextButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                setIndex(nextIndex());
            }
        }
        nextButton.addActionListener(new nextButtonListener());
        nextButton.addKeyListener(this);
        buttonPanel.add(nextButton);

        previousButton = new JButton("Previous");
        class previousButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                setIndex(previousIndex());
            }
        }
        previousButton.addActionListener(new previousButtonListener());
        previousButton.addKeyListener(this);
        buttonPanel.add(previousButton);

        answerButton = new JButton("Answer");
        class answerButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                showAnswer();    
            }
        }
        answerButton.addActionListener(new answerButtonListener());
        answerButton.addKeyListener(this);
        buttonPanel.add(answerButton);

        /*hintButton = new JButton("Hint");
        class hintButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
               showHint();
            }
        }
        hintButton.addActionListener(new hintButtonListener());
        hintButton.addKeyListener(this);
        buttonPanel.add(hintButton);*/

        resetButton = new JButton("Reset");
        class resetButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
               resetQuestion();
            }
        }
        resetButton.addActionListener(new resetButtonListener());
        resetButton.addKeyListener(this);
        buttonPanel.add(resetButton);

        enableButtons(false);
    }

    private void enableButtons(boolean enabled) {
       randomButton.setEnabled(enabled);
       nextButton.setEnabled(enabled);
       previousButton.setEnabled(enabled);
       answerButton.setEnabled(enabled);
       //hintButton.setEnabled(enabled);
       resetButton.setEnabled(enabled);
    }

    private void loadFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ArrayList<Question> importedQuestions = Question.parse(file);
                if(importedQuestions.size() > 0) {
                    questions = importedQuestions;
                    setTitle(FRAME_TITLE + " - " + file.getName());
                    enableButtons(true);
                    setIndex(0);
                }
                else {
                    JOptionPane.showMessageDialog(this, "File does not contain parseable questions!", "File unparseable!", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(FileNotFoundException fnfe) {
                fnfe.printStackTrace();
                JOptionPane.showMessageDialog(this, "File not found!", "File not found!", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else {
            //
        }
    }

    private int generate() {
        if(questions.size() > 1) {
            int last = index;
            int next = generator.nextInt(questions.size());
            while(next == last) {
                next = generator.nextInt(questions.size());
            }
            return next;
        }
        else {
            return generator.nextInt(questions.size());
        }
    }

    private boolean setQuestion(int index) {
        if(questions != null) {
            if(questions.size() == 0) return false;
            if(index < 0 || index >= questions.size()) return false;
            currentQuestion = questions.get(index);
            clearQuestion();
            questionText.setText((index + 1) + ". " + currentQuestion.getQuestion());
            return true;
        }
        else return false;
    }

    private void showAnswer() {
        answerText.setText(currentQuestion.getAnswer());
        answerText.setBackground(SHOW_COLOUR);
    }

    /*private void showHint() {
        if(currentQuestion.hasHint()) {
            hintText.setText(currentQuestion.getHint());
        }
        else {
            hintText.setText("No hint");
        }
        hintText.setBackground(SHOW_COLOUR);
    }*/

    private void clearQuestion() {
        resetQuestion();
        questionText.setText("");
    }

    private void resetQuestion() {
        answerText.setText("");
        //hintText.setText("");
        answerText.setBackground(HIDE_COLOUR);
        //hintText.setBackground(HIDE_COLOUR);
    }

    private void setIndex(int location) {
        index = location;
        setQuestion(index);
    }

    private int nextIndex() {
        int size = questions.size();
        return (index + 1) % size;
    }

    private int previousIndex() {
        int size = questions.size();
        if((index - 1) < 0) return size - 1;
        else return index - 1;
    }

    public void keyPressed(KeyEvent keyEvent) 
    {
        int keyCode = keyEvent.getKeyCode() ;

        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0) ;
        }
        else if(keyCode == KeyEvent.VK_L) {
            loadButton.doClick() ;
        }
        else if(keyCode == KeyEvent.VK_G) {
            randomButton.doClick() ;
        }
        else if(keyCode == KeyEvent.VK_N
                || keyCode == KeyEvent.VK_RIGHT
                || keyCode == KeyEvent.VK_KP_RIGHT
                || keyCode == KeyEvent.VK_DOWN
                || keyCode == KeyEvent.VK_KP_DOWN) {
            nextButton.doClick() ;
        }
        else if(keyCode == KeyEvent.VK_P
                || keyCode == KeyEvent.VK_LEFT
                || keyCode == KeyEvent.VK_KP_LEFT
                || keyCode == KeyEvent.VK_UP
                || keyCode == KeyEvent.VK_KP_UP) {
            previousButton.doClick() ;
        }
        else if(keyCode == KeyEvent.VK_A) {
            answerButton.doClick() ;
        }
        /*else if(keyCode == KeyEvent.VK_H) {
            hintButton.doClick() ;
        }*/
        else if(keyCode == KeyEvent.VK_R) {
            resetButton.doClick() ;
        }
    }

    public void keyReleased(KeyEvent keyEvent)
    {
        //
    }

    public void keyTyped(KeyEvent keyEvent)
    {
        //
    }
}
