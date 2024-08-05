import javax.swing.*;
import java.awt.*;

class Panel extends JPanel {

    public Panel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        int count = 1;
        setLayout(gbl);

        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(1,1,1,1);
        for (int week = 0; week < 6; week++) {
            c.gridy = week;
            for (int day = 0; day < 7; day++) {
                c.gridx = day;
                JButton button = new JButton(String.valueOf(count++));
                add(button, c);
            }
        }
    }
}

public class Main {

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new Panel());
        // Lay out components and set visibility
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}