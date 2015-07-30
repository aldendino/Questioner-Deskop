
import org.xml.sax.SAXException;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.xml.parsers.ParserConfigurationException;

/**
 * The user interface to the Question Manager.
 */
public class QuestionerUI extends JFrame implements KeyListener {

    private final String FRAME_TITLE = "Questioner"; // The title of the application
    private final String EXTENSION = "xml"; // The file extension for Questioner database files.

    private ImageIcon icon ; // The icon for the application.
    private final String iconPath = "/Media/Images/Icons/Questioner.png" ; // The path to the icon.

    private final int FRAME_WIDTH = 550; // The default width of the application.
    private final int FRAME_HEIGHT = 350; // The default height of the application.
    private final int MIN_FRAME_WIDTH = 450; // The minimum width of the application.
    private final int MIN_FRAME_HEIGHT = 250; // The minimum height of the application.

    private final Color SHOW_COLOUR = Color.WHITE; // The background colour of an element that is being shown.
    private final Color HIDE_COLOUR = Color.DARK_GRAY; // The background colour of an element that is being hidden.
    private final Insets TEXT_INSETS = new Insets(5, 5, 5, 5); // The inset for text.
    private final EmptyBorder EMPTY_BORDER = new EmptyBorder(TEXT_INSETS); // The EmptyBorder for text.

    private JFileChooser fileChooser; // The file chooser.

    private JPanel mainPanel; // The main panel.
    private JSplitPane questionSplitPane; // The pane that holds the question and answer texts.
    private JPanel buttonPanel; // The button panel.
    private JScrollPane buttonPane; // The pane for button panel scrolling.

    private JTextArea questionText; // The question text.
    private JTextArea answerText; // The answer text.
    //private JTextArea hintText; // The hint text.
    private JScrollPane questionScrollPane; // The pane for question text scrolling.
    private JScrollPane answerScrollPane; // The pane for answer text scrolling.
    //private JScrollPane hintScrollPane; // The pane for hint text scrolling.

    private JButton loadButton; // The load button.
    private JButton randomButton; // The random button.
    private JButton nextButton; // The next button.
    private JButton previousButton; // The previous button.
    private JButton answerButton; // The answer button.
    private JButton hintButton; // The hint button.
    private JButton resetButton; // The reset button.

    private QuestionManager qm; // The question manager.

    /**
     * Set up the user interface.
     */
    public QuestionerUI() {
        setupMainPanel();
        add(mainPanel);
        init();
    }

    /**
     * Initialize the frame and its subcomponents.
     */
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

    /**
     * Set the icon.
     */
    private void setIcon() {
        try {
            icon = new ImageIcon(getClass().getResource(iconPath));
            setIconImage(icon.getImage());
        }
        catch(NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Setup the main panel.
     */
    private void setupMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        setupQuestionPane();
        mainPanel.add(questionSplitPane, BorderLayout.CENTER);

        setupButtonPane();
        mainPanel.add(buttonPane, BorderLayout.EAST);
    }

    /**
     * Setup the question panel.
     */
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

    /**
     * Setup the button panel.
     */
    private void setupButtonPane() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPane = new JScrollPane(buttonPanel);

        fileChooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Questioner XML files only", EXTENSION));

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
                qm.generateIndex(); // Generate a random index.
                setQuestion(); // Apply it.
            }
        }
        randomButton.addActionListener(new randomButtonListener());
        randomButton.addKeyListener(this);

        nextButton = new JButton("Next");
        class nextButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                qm.nextIndex(); // Go to the next index.
                setQuestion(); // Apply it.
            }
        }
        nextButton.addActionListener(new nextButtonListener());
        nextButton.addKeyListener(this);

        previousButton = new JButton("Previous");
        class previousButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                qm.previousIndex(); // Go to the previous index.
                setQuestion(); // Apply it.
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

        enableButtons(false); // Buttons should be disabled initially.
    }

    /**
     * Enable or disable all the buttons except the load button.
     * @param enabled whether the buttons should be enabled or not.
     */
    private void enableButtons(boolean enabled) {
       randomButton.setEnabled(enabled);
       nextButton.setEnabled(enabled);
       previousButton.setEnabled(enabled);
       answerButton.setEnabled(enabled);
       //hintButton.setEnabled(enabled);
       resetButton.setEnabled(enabled);
    }

    /**
     * Load a file based on user input.
     */
    private void loadFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                //ArrayList<Question> importedQuestions = Question.parse(file);
                ArrayList<Question> importedQuestions = Question.parseXML(file);
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
            catch(IOException ioe) {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(this, "File parsing error.", "IOException", JOptionPane.ERROR_MESSAGE);
            }
            catch(ParserConfigurationException pce) {
                pce.printStackTrace();
                JOptionPane.showMessageDialog(this, "File parsing error.", "ParserConfigurationException", JOptionPane.ERROR_MESSAGE);
            }
            catch(SAXException se) {
                se.printStackTrace();
                JOptionPane.showMessageDialog(this, "File parsing error.", "SAXException", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else {
            // Do something if the user did not choose the approve option.
        }
    }

    /**
     * Set the question to the current question in the question manager.
     * @return whether or not the question was set successfully.
     */
    private boolean setQuestion() {
        if(qm != null) {
            if(qm.isEmpty()) return false;
            clearQuestion();
            questionText.setText((qm.getIndex() + 1) + ". " + qm.getCurrentQuestion().getQuestion());
            return true;
        }
        else return false;
    }

    /**
     * Show the answer.
     */
    private void showAnswer() {
        answerText.setText(qm.getCurrentQuestion().getAnswer());
        answerText.setBackground(SHOW_COLOUR);
    }

    /**
     * Show the hint.
     */
    /*private void showHint() {
        if(currentQuestion.hasHint()) {
            hintText.setText(currentQuestion.getHint());
        }
        else {
            hintText.setText("No hint");
        }
        hintText.setBackground(SHOW_COLOUR);
    }*/

    /**
     * Clear the question.
     */
    private void clearQuestion() {
        resetQuestion();
        questionText.setText("");
    }

    /**
     * Reset the current question so that the answer is hidden.
     */
    private void resetQuestion() {
        answerText.setText("");
        //hintText.setText("");
        answerText.setBackground(HIDE_COLOUR);
        //hintText.setBackground(HIDE_COLOUR);
    }

    /**
     * Implement the KeyListener interface for keyboard shortcuts for the buttons.
     * @param keyEvent the key that was pressed.
     */
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
                || keyCode == KeyEvent.VK_KP_RIGHT) {
            nextButton.doClick() ;
        }
        else if(keyCode == KeyEvent.VK_P
                || keyCode == KeyEvent.VK_LEFT
                || keyCode == KeyEvent.VK_KP_LEFT) {
            previousButton.doClick() ;
        }
        else if(keyCode == KeyEvent.VK_A
                || keyCode == KeyEvent.VK_UP
                || keyCode == KeyEvent.VK_KP_UP) {
            answerButton.doClick() ;
        }
        /*else if(keyCode == KeyEvent.VK_H) {
            hintButton.doClick() ;
        }*/
        else if(keyCode == KeyEvent.VK_R
                || keyCode == KeyEvent.VK_DOWN
                || keyCode == KeyEvent.VK_KP_DOWN) {
            resetButton.doClick() ;
        }
    }

    /**
     * Not used.
     * @param keyEvent keyEvent the key that was released.
     */
    public void keyReleased(KeyEvent keyEvent)
    {
        // Do something after a key is released.
    }

    /**
     * Not used.
     * @param keyEvent keyEvent the key that was typed.
     */
    public void keyTyped(KeyEvent keyEvent)
    {
        // Do something after a key has been typed.
    }
}
