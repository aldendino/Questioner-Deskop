
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;

public class QuestionerUI extends JFrame implements KeyListener {

    private final String FRAME_TITLE = "Questioner";
    private final String EXTENSION = "qnr";

    private ImageIcon icon ;
    private final String iconPath = "/Media/Images/Questioner.png" ;

    private final int FRAME_WIDTH = 550;
    private final int FRAME_HEIGHT = 350;
    private final int MIN_FRAME_WIDTH = 450;
    private final int MIN_FRAME_HEIGHT = 250;

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

    private QuestionManager qm;
    private Question currentQuestion;


    public QuestionerUI() {
        setupMainPanel();
        add(mainPanel);
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setLocationByPlatform(true);
        setTitle(FRAME_TITLE);
		setIcon();
        addKeyListener(this);
        setVisible(true);
    }

    private void setIcon() {
        icon = new ImageIcon(getClass().getResource(iconPath)) ;
        setIconImage(icon.getImage()) ;
    }

    private void setupMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        setupQuestionPane();
        mainPanel.add(questionSplitPane, BorderLayout.CENTER);

        setupButtonPane();
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
    }

    private void setupButtonPane() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPane = new JScrollPane(buttonPanel);

        fileChooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Questioner files only" ,EXTENSION));

        loadButton = new JButton("Load");
        class loadButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                loadFile();
            }
        }
        loadButton.addActionListener(new loadButtonListener());
        loadButton.addKeyListener(this);

        randomButton = new JButton("Generate");
        class randomButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                qm.generate();
                setQuestion();
            }
        }
        randomButton.addActionListener(new randomButtonListener());
        randomButton.addKeyListener(this);

        nextButton = new JButton("Next");
        class nextButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                qm.nextIndex();
                setQuestion();
            }
        }
        nextButton.addActionListener(new nextButtonListener());
        nextButton.addKeyListener(this);

        previousButton = new JButton("Previous");
        class previousButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                qm.previousIndex();
                setQuestion();
            }
        }
        previousButton.addActionListener(new previousButtonListener());
        previousButton.addKeyListener(this);

        answerButton = new JButton("Answer");
        class answerButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                showAnswer();    
            }
        }
        answerButton.addActionListener(new answerButtonListener());
        answerButton.addKeyListener(this);

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
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        buttonPanel.add(loadButton, gbc);
        gbc.gridy++;
        buttonPanel.add(randomButton, gbc);
        gbc.gridy++;
        buttonPanel.add(nextButton, gbc);
        gbc.gridy++;
        buttonPanel.add(previousButton, gbc);
        gbc.gridy++;
        buttonPanel.add(answerButton, gbc);
        gbc.gridy++;
        gbc.weighty = 1;
        buttonPanel.add(resetButton, gbc);

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
                    qm = new QuestionManager(importedQuestions);
                    boolean success = setQuestion();
                    setTitle(FRAME_TITLE + " - " + file.getName());
                    if(success) enableButtons(true);
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

    private boolean setQuestion() {
        if(qm != null) {
            if(qm.isEmpty()) return false;
            clearQuestion();
            questionText.setText((qm.getIndex() + 1) + ". " + qm.getCurrentQuestion().getQuestion());
            return true;
        }
        else return false;
    }

    private void showAnswer() {
        answerText.setText(qm.getCurrentQuestion().getAnswer());
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
