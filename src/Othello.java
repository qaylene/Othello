import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;

public class Othello extends JFrame 
{
    /** 
     * creates game window 
     */
    public Othello() 
    {
        setTitle("Othello");

        add(new Surface()); // creates the game field surface

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700); 
        setResizable(false); // prevents player from resizing window
        setLocationRelativeTo(null);           
    } // end WhackAMole

    /** 
     * runs the game
     */
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() 
                {
                    Othello ht = new Othello();
                    ht.setVisible(true);
                }
            });     
    } // end method main
} // end class WhackAMole
