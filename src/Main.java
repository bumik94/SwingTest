import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

class CalendarPanel extends JPanel implements ActionListener {
    // Constants used to offset a component
    private static final int X_OFFSET = 3;
    private static final int Y_OFFSET = 0;

    private final Calendar calendar;
    private int currentMonth;
    private int currentYear;
    private ArrayList<JButton> CalendarButtons = new ArrayList<>();

    // TODO: Create separate panel for header and calendar grid, manage all with GridBagLayout

    public CalendarPanel() {
        // Initialize and set Calendar
        this.calendar = Calendar.getInstance();
        this.calendar.setMinimalDaysInFirstWeek(1);
        // Get initial month and year
        this.currentMonth = calendar.get(Calendar.MONTH);
        this.currentYear = calendar.get(Calendar.YEAR);
        // Set layout manager
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create buttons field to populate with calendar days
//        c.fill = GridBagConstraints.BOTH; // no need to fill, labels are now centered
        c.insets = new Insets(1, 1, 1, 1);
        // Generate calendar grid and copy button objects to CalendarButtons list for future reference.
        for (int week = X_OFFSET; week < X_OFFSET + 6; week++) {
            c.gridy = week;
            for (int day = Y_OFFSET; day < Y_OFFSET + 7; day++) {
                c.gridx = day;
                // Assign button to the specified cell
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                add(button, c);
                CalendarButtons.add(button);
            }
        }
        /*
        Generate header for calendar control.
        Each respective button sets new calendar property and recalls the setButtonsText(getDaysOfMonthList()) method
        to redraw the calendar.

        TODO: implement actionListener method

         */
        // Previous year button
        c.gridx = 0;
        c.gridy = 0;
        JButton prevYear = new JButton("<");
        prevYear.setPreferredSize(new Dimension(50, 30));
        this.add(prevYear, c);

        // Year label
        c.gridx = 1;
        c.gridwidth = 5;
        c.gridy = 0;
        JLabel yearLabel = new JLabel(String.valueOf(calendar.get(Calendar.YEAR)));
        this.add(yearLabel, c);

        // Next year button
        c.gridx = 6;
        c.gridwidth = 1;
        c.gridy = 0;
        JButton nextYear = new JButton(">");
        nextYear.setPreferredSize(new Dimension(50, 30));
        this.add(nextYear, c);

        // Separator
        c.insets = new Insets(15, 1, 15, 1);
        c.gridx = 0;
        c.gridwidth = 7;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
//        separator.setPreferredSize(new Dimension(350, 1));
        this.add(separator, c);

        // Previous month button
        c.insets = new Insets(1, 1, 1, 1);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        JButton prevMonth = new JButton("<");
        prevMonth.setPreferredSize(new Dimension(50, 30));
        this.add(prevMonth, c);

        //Month label
        c.gridx = 1;
        c.gridwidth = 5;
        c.gridy = 2;
        JLabel monthLabel = new JLabel(
                calendar.getDisplayName(
                        calendar.MONTH,
                        Calendar.LONG_STANDALONE,
                        Locale.getDefault()));
        this.add(monthLabel, c);

        // Next month button
        c.gridx = 6;
        c.gridwidth = 1;
        c.gridy = 2;
        JButton nextMonth = new JButton(">");
        nextMonth.setPreferredSize(new Dimension(50, 30));
        this.add(nextMonth, c);

        // Fill buttons with corresponding days for the current month
        setButtonsText(getDaysOfMonthList());
    }

    /**
     * Sets the text of calendar buttons to each consecutive day from the daysOfMonth list.
     * When the list gets out of bounds, exception is caught and the respective button gets disabled and its text
     * field set to empty string.
     *
     * @param daysOfMonth a list containing days of the current month with overlaps to make up a full week
     */
    public void setButtonsText(ArrayList<Integer> daysOfMonth) {
        for (int i = 0; i < CalendarButtons.size(); i++) {
            JButton button = CalendarButtons.get(i);
            try {
                button.setText(String.valueOf(daysOfMonth.get(i)));
                button.setEnabled(true);
            }
            catch (Exception ArrayIndexOutOfBoundsException) {
                button.setText("");
                button.setEnabled(false);
            }
        }
        this.revalidate();
    }

    /**
     * Returns a list of days in the current month with overlaps to make full weeks.
     * Calendar is set to start on Monday of the first week of the month.
     *
     * @return  a list containing the days of the current month
     *          with overlaps to other months to make up for a full week
     */
    public ArrayList<Integer> getDaysOfMonthList() {
        ArrayList<Integer> list = new ArrayList<>();
        calendar.set(Calendar.WEEK_OF_MONTH, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
         // Condition checks if isFullWeek is divisible by 7 and if the Calendar month is different
        // from the currentMonth meaning that the end of month and week has been reached.
        int isFullWeek = 0;
        do {
            list.add(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (! (++isFullWeek % 7 == 0 && calendar.get(Calendar.MONTH) != currentMonth));

        return list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

public class Main {

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new CalendarPanel());
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