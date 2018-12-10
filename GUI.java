package mastermind.gui;

import mastermind.MasterMind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class ColorButton extends JButton{
    private int index;
    ColorButton(int index) {
        super();
        this.index = index;
    }
    int getIndex()
    {
        return this.index;
    }

}
public class GUI {
    private MasterMind masterMind;
    private JFrame frame;
    private JPanel mainPanel, topPanel, centerPanel, holderPanel, leftPanel,rightPanel ;
    private static ArrayList<ColorButton> colorButtons;
    private JPanel[][] userInput, results;
    private ArrayList<Integer> choices;
    private static ActionListener listener, giveUp;
    private int numGuesses = 0,rowIter = 0,columnIter=0, randSeed = 0;
    private GUI()
    {
        masterMind = new MasterMind();
        frame = new JFrame("MasterMind");
        mainPanel = new JPanel();
        topPanel = new JPanel();
        centerPanel = new JPanel();
        holderPanel = new JPanel();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        colorButtons = new ArrayList<>();
        userInput = new JPanel[20][6];
        results = new JPanel[20][6];
        choices = new ArrayList<>();
        randSeed = (int) System.currentTimeMillis();
        setUpFrame();
        setPanelColors();
        setUpPanels();

        addGridToPanel(leftPanel, 20,6, userInput);
        addGridToPanel(rightPanel, 20,6, results);

        masterMind.setSelectedColorIndices(masterMind.createRandomColorIndicies(10,6, randSeed));
        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ColorButton tempBtn = (ColorButton) e.getSource();
                GameLoop(tempBtn);
            }
        };
        giveUp = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAnswer();
            }
        };
        addColorButtonsToPanel(topPanel,10);
        setButtonColors(colorButtons);
        frame.setVisible(true);
    }
    private void GameLoop(ColorButton tempBtn)
    {
        numGuesses++;
        choices.add(tempBtn.getIndex());
        userInput[rowIter][columnIter].setBackground(tempBtn.getBackground());
        columnIter++;

        if (numGuesses == 6)
        {
            Map<MasterMind.Result, Integer> progressMap = masterMind.guess(choices);

            int correctPos = progressMap.get(MasterMind.Result.INPOSITION);
            int matchPos = progressMap.get(MasterMind.Result.MATCH);
            if (masterMind.getGameStatus() == MasterMind.GameStatus.WON) {
                JOptionPane.showMessageDialog(frame, "YOU WIN!");
                Restart();
            }
            else if(masterMind.getGameStatus()== MasterMind.GameStatus.LOST)
            {
                JOptionPane.showMessageDialog(frame, "YOU LOSE!");
                showAnswer();
            }
            else{
                int index = 0;
                while(correctPos + matchPos > 0)
                {
                    if(correctPos > 0){
                        results[rowIter][index].setBackground(Color.DARK_GRAY);
                        index++;
                        correctPos--;
                    }
                    else if(matchPos > 0){
                        results[rowIter][index].setBackground(Color.LIGHT_GRAY);
                        index++;
                        matchPos--;
                    }
                }
                rowIter++;
                columnIter=0;
                choices.clear();
            }
            numGuesses = 0;
        }
    }
    private void showAnswer()
    {
        JFrame newFrame = new JFrame("ANSWERS");
        newFrame.setSize(500,100);
        newFrame.setResizable(false);
        newFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Restart();
            }
        });
        Integer [] answers = new Integer[6];
        answers = masterMind.createRandomColorIndicies(10,6,randSeed).toArray(answers);
        JPanel[] answerPanels = new JPanel[6];
        JPanel newMain = new JPanel();
        int i=0;
        while(i < 6)
        {
            answerPanels[i] = new JPanel();
            answerPanels[i].setBackground(colorButtons.get(answers[i]).getBackground());
            newMain.add(answerPanels[i]);
            i++;
        }
        newFrame.add(newMain);
        newFrame.setVisible(true);

    }
    private void Restart()
    {
        masterMind = new MasterMind();
        removeColorfromPanels(20,6,results);
        removeColorfromPanels(20,6,userInput);
        randSeed = (int) System.currentTimeMillis();
        masterMind.setSelectedColorIndices(masterMind.createRandomColorIndicies(10,6, randSeed));
        setButtonColors(colorButtons);
        numGuesses = 0;
        rowIter = 0;
        columnIter=0;
        choices.clear();
    }
    private void setUpFrame()
    {
        frame.setLayout( new BorderLayout());
        frame.add(mainPanel);
        frame.setSize(500,500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    private void setUpPanels()
    {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel,BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new BorderLayout());

        holderPanel.add(leftPanel);
        holderPanel.add(rightPanel);
        centerPanel.add(holderPanel,BorderLayout.CENTER);

        holderPanel.setLayout(new GridLayout(1,2));
        leftPanel.setLayout(new GridLayout(20,6,1,1));
        rightPanel.setLayout(new GridLayout(20,6,1,1));

    }
    private void setPanelColors()
    {
        topPanel.setBackground(Color.white);
        centerPanel.setBackground(Color.GREEN);
        holderPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setBackground(Color.black);
        rightPanel.setBackground(Color.black);
    }
    private void removeColorfromPanels(int rows,int columns, JPanel[][] panels)
    {
        for(int i = 0; i < rows;i++ )
        {
            for(int j = 0; j < columns; j++)
            {
                panels[i][j].setBackground(Color.white);
            }
        }
    }
    private void addGridToPanel(JPanel panel, int rows, int columns, JPanel[][] panels)
    {
        for(int i = 0; i < rows;i++ )
        {
            for(int j = 0; j < columns; j++)
            {
                panels[i][j] = (new JPanel());
                panels[i][j].setPreferredSize(new Dimension(10,10));
                panels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panel.add(panels[i][j]);
            }
        }
    }
    private void addColorButtonsToPanel(JPanel panel, int num)
    {
        JButton giveUpButton = new JButton("Give Up?");
        panel.add(giveUpButton);
        giveUpButton.addActionListener(giveUp);

        for(int i =0; i < num; i++)
        {
            colorButtons.add(new ColorButton(i));
            colorButtons.get(i).setPreferredSize(new Dimension(25,25));
            colorButtons.get(i).addActionListener(listener);
            panel.add(colorButtons.get(i));
        }
    }
    private void setButtonColors(ArrayList<ColorButton> colors) {
        ArrayList<Color> colorList = new ArrayList<>();
        colorList.addAll(Arrays.asList(Color.RED,Color.BLUE,Color.CYAN,Color.GREEN,Color.YELLOW,Color.MAGENTA, Color.PINK,Color.GRAY,Color.LIGHT_GRAY,Color.ORANGE));
        Random rand = new Random();
        int i =0;
        while(!colorList.isEmpty()&&i<10)
        {
            int n = rand.nextInt(colorList.size());
            colors.get(i).setBackground(colorList.get(n));
            colorList.remove(n);
            i++;
        }
    }
    public static void main(String[] args) {
        new GUI();
    }
}