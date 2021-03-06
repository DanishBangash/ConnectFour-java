/**
 * @authors:Danish Bangash
 * date:20.10.2011
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ConnectFourGUI {
   
    private JFrame frame;
    private JLabel[][] slots;
    private int currentPlayer;
    public ConnectFourGUI(){
        frame = new JFrame ("Connect Four");
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setLayout(new GridLayout(6,7));
        slots= new JLabel[7][6];
        
        for(int row=5; row>=0; row--){
            for(int column=0; column<7; column++){
                slots[column][row] = new JLabel();
               // slots[column][row].setFont(newFont("SanSerif", Font.BOLD, 18));
                slots[column][row].setHorizontalAlignment(SwingConstants.CENTER);
                slots[column][row].setBorder(new LineBorder(Color.black));
                panel.add(slots[column][row]);
            }
        }
        frame.setContentPane(panel);
        frame.setSize(500,400);
        frame.setVisible(true);

        currentPlayer = 1;
    }

    public void addListener(ConnectFourListener listener){
        for(int row=0; row<6; row++){
            for(int column=0; column<7; column++){
                slots[column][row].addMouseListener(listener);
            }
        }
    }

    public int getColumn(JLabel label){
        int returnColumn = -1;
        for(int row=0; row<6; row++){
            for(int column=0; column<7; column++){
                if(slots[column][row] == label) {
                    returnColumn = column;
                }
            }
        }
        return returnColumn;
    }
    public void set( int column, int row){
        slots[column][row].setText("*" + currentPlayer + "*");
        if(currentPlayer == 1){
            slots[column][row].setIcon(new ImageIcon("/Users/DanishKhan/wallpapers"));
        }
        else{
            slots[column][row].setIcon(new ImageIcon("+++++++++++++++"));
        }
        currentPlayer = (currentPlayer%2)+1;
    }
    public static void main(String[] args){
        ConnectFour game = new ConnectFour();
        ConnectFourGUI gui = new ConnectFourGUI();
        ConnectFourListener listener = new ConnectFourListener( game, gui);
    }
}
        



