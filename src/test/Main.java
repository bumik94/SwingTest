package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

class CalendarPanel extends JPanel implements ActionListener {
    private static final String LEFT_ARROW  = "<";
    private static final String RIGHT_ARROW = ">";

    private final Calendar calendar;
    private final ArrayList<JButton> CalendarButtons = new ArrayList<>();

    private JLabel yearLabel;
    private JLabel monthLabel;
    private int currentMonth;
    private int currentYear;

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
        c.insets = new Insets(1, 1, 1, 1);

        // Calendar header
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(calendarHeader(), c);

        // Calendar grid cells
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        this.add(calendarGrid(), c);

        // Fill buttons with corresponding days for the current month
        setComponentsText(getDaysOfMonthList());
    }

    /**
     * Creates a header with controls to operate the calendar. Each button
     * increments/decrements year/month respectively via action listener.
     * Pressing each button initializes revalidation of the class panel.
     *
     * @return JPanel containing buttons and labels to control and display
     *         current year and month.
     */
    private JPanel calendarHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        this.yearLabel = new JLabel();
//        this.yearLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.monthLabel = new JLabel();
//        this.monthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton prevYear = new JButton(LEFT_ARROW);
        prevYear.setPreferredSize(new Dimension(50, 25));
        prevYear.addActionListener(this);
        prevYear.setActionCommand("prevYear");

        JButton prevMonth = new JButton(LEFT_ARROW);
        prevMonth.setPreferredSize(new Dimension(50, 25));
        prevMonth.addActionListener(this);
        prevMonth.setActionCommand("prevMonth");

        JButton nextYear = new JButton(RIGHT_ARROW);
        nextYear.setPreferredSize(new Dimension(50, 25));
        nextYear.addActionListener(this);
        nextYear.setActionCommand("nextYear");

        JButton nextMonth = new JButton(RIGHT_ARROW);
        nextMonth.setPreferredSize(new Dimension(50, 25));
        nextMonth.addActionListener(this);
        nextMonth.setActionCommand("nextMonth");

        // Previous buttons column
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        header.add(prevYear, c);
        c.gridy = 2;
        header.add(prevMonth, c);

        // Next buttons column
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 0;
        header.add(nextYear, c);
        c.gridy = 2;
        header.add(nextMonth, c);

        // Labels column
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        header.add(this.yearLabel, c);
        c.gridy = 2;
        header.add(this.monthLabel, c);

        // Separator
        c.insets = new Insets(1, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        header.add(separator, c);

        return header;
    }

    /**
     * Creates calendar cells for each possible day in a month in self-contained JPanel
     * and adds each new button to a list for reference by 'setComponentsText' method.
     *
     * @return JPanel containing buttons organized in a grid of (maximum) 6 weeks by 7 days
     */
    private JPanel calendarGrid() {
        JPanel grid = new JPanel(new GridLayout(6, 7));
        for (int i = 0; i < (6 * 7); i++) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(50, 50));
            grid.add(button);
            CalendarButtons.add(button);
        }
        return grid;
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

        // Set calendar back to current month and year in case of overlap to next month/year
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        return list;
    }

    /**
     * Sets the text of calendar buttons to each consecutive day from the daysOfMonth list.
     * When the list gets out of bounds, exception is caught and the respective button gets disabled and its text
     * field set to empty string.
     *
     * @param daysOfMonth a list containing days of the current month with overlaps to make up a full week
     */
    public void setComponentsText(ArrayList<Integer> daysOfMonth) {
        String yearName  = String.valueOf(calendar.get(Calendar.YEAR));
        String monthName = calendar.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG_STANDALONE,
                Locale.getDefault());
        // Set year label text to current year
        yearLabel.setText(yearName);
        // set month label text to current month
        monthLabel.setText(monthName.substring(0, 1).toUpperCase() + monthName.substring(1));
        // Set calendar grid text to current month days
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
     * Action listener method to iterate through the calendar by year or month
     * forwards or backwards by the respective button
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "prevYear"     -> { calendar.add(Calendar.YEAR,    -1); }
            case "nextYear"     -> { calendar.add(Calendar.YEAR,    1); }
            case "prevMonth"    -> { calendar.add(Calendar.MONTH,   -1); }
            case "nextMonth"    -> { calendar.add(Calendar.MONTH,   1); }
        }
        currentYear  =  calendar.get(Calendar.YEAR);
        currentMonth =  calendar.get(Calendar.MONTH);
        setComponentsText(getDaysOfMonthList());
    }
}

public class Main {

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
//        frame.
        frame.add(new CalendarPanel(), c);
        // Lay out components and set visibility
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}