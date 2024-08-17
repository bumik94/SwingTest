package test;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

class CalendarPanel extends JPanel implements ActionListener {
    private static final String LEFT_ARROW  = "<";
    private static final String RIGHT_ARROW = ">";

    private final DateFormat df;
    private final Calendar calendar;
    private final Calendar today;
    private final ArrayList<JButton> CalendarButtons = new ArrayList<>();

    private JLabel yearLabel;
    private JLabel monthLabel;
    private int currentYear;
    private int currentMonth;

    public CalendarPanel(Calendar today) {
        // Initialize date format to the default locale
        df = DateFormat.getDateInstance();  // DateFormat.SHORT
        // Initialize and set Calendar
        this.calendar = Calendar.getInstance();
        this.calendar.setMinimalDaysInFirstWeek(1);
        // Get initial year and month
        this.currentYear = calendar.get(Calendar.YEAR);
        this.currentMonth = calendar.get(Calendar.MONTH);
        // Get Calendar instance to keep track of today
        this.today = today;
        // Set layout manager
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        // Calendar header
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(calendarHeader(), c);

        // Calendar grid cells
        c.gridx = 0;
        c.gridy = 1;
        this.add(calendarGrid(), c);

        // Fill buttons with corresponding days for the current month
        setComponentsText(getDateList());
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
        header.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        GridBagConstraints c = new GridBagConstraints();
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        this.yearLabel = new JLabel();
        this.monthLabel = new JLabel();

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

        c.insets = new Insets(1, 1, 1, 1);

        // Previous buttons column
        c.weightx = 0.0;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0; header.add(prevYear, c);
        c.gridy = 2; header.add(prevMonth, c);

        // Next buttons column
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 0; header.add(nextYear, c);
        c.gridy = 2; header.add(nextMonth, c);

        // Labels column
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0; header.add(this.yearLabel, c);
        c.gridy = 2; header.add(this.monthLabel, c);

        // Separator

        c.insets = new Insets(1, 0, 1, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1; header.add(separator, c);

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
//        grid.setBorder(BorderFactory.createLoweredBevelBorder());
        for (int i = 0; i < (6 * 7); i++) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(50, 50));
            grid.add(button);
            CalendarButtons.add(button);
        }
        return grid;
    }

    /**
     * Creates a list of date strings in the current month with overlaps to make up full weeks.
     * Calendar is set to start on Monday of the first week of the month.
     * This method is used as an argument for the 'setComponentsText' method.
     *
     * @return  a list containing formatted date strings
     * <p>
     * TODO: Create an array of Date objects that can be used to parse data from, like day for the calendar cell.
     *       Save this array to the class instance and refer to it in conjunction with
     *       respective button to call other methods in the future.
     */
    private List<String> getDateList() {
        ArrayList<String> list = new ArrayList<>();
        calendar.set(Calendar.WEEK_OF_MONTH, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Condition checks if the calendar has reached next month at the beginning of a new week
        do {
            // Extract the date string for the current day and add it to the list
            list.add(df.format(calendar.getTime()));
            // Increment day of the calendar
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (! (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY &&
                    calendar.get(Calendar.MONTH) != currentMonth));
        // Set calendar back to current month and year in case of overlap to the next month/year
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        return List.copyOf(list);
    }

    /**
     * Sets the text of calendar buttons to each consecutive day from the 'dates' list.
     * When the list gets out of bounds, exception is caught and the respective button gets disabled and its text
     * field set to empty string.
     *
     * @param dates a list containing strings of date returned by the 'getDateList' method
     * @throws ArrayIndexOutOfBoundsException raised when 'dates' list gets out of bounds
     * <p>
     * TODO: Use List of Date objects to parse days from and set each button to respective date.
     *       The date will determine the cell color:
     *                    - DARK_GREY   for different months from the current
     *                    - LIGHT_GREY  for the current month
     *                    - WHITE       for "today"
     */
    private void setComponentsText(List<String> dates) throws ArrayIndexOutOfBoundsException{
        // Creates a Calendar instance to parse dates from daysOfMonth list
        Calendar c = Calendar.getInstance();
        String yearName  = String.valueOf(calendar.get(Calendar.YEAR));
        //noinspection MagicConstant
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
                String date = dates.get(i);
                // set the local calendar with the date string
                c.setTime(df.parse(date));
                button.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
                button.setEnabled(true);
                button.addActionListener(this);
                button.setActionCommand(date);
                if (today.get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
                    today.get(Calendar.MONTH) == c.get(Calendar.MONTH )&&
                    today.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
                    button.setBackground(Color.WHITE);
                }
                else if (currentMonth != c.get(Calendar.MONTH)) {
                    button.setBackground(Color.GRAY);
                }
                else {
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }
            catch (Exception ArrayIndexOutOfBoundsException) {
                button.setText("");
                button.setEnabled(false);
                button.setBackground(null);
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
            case "prevYear"     -> calendar.add(Calendar.YEAR,    -1);
            case "nextYear"     -> calendar.add(Calendar.YEAR,    1);
            case "prevMonth"    -> calendar.add(Calendar.MONTH,   -1);
            case "nextMonth"    -> calendar.add(Calendar.MONTH,   1);
            default -> System.out.println(e.getActionCommand());
        }
        currentYear  =  calendar.get(Calendar.YEAR);
        currentMonth =  calendar.get(Calendar.MONTH);
        setComponentsText(getDateList());
    }
}

public class Main {

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(1, 1, 1, 1);

        // Lay out components and set visibility
        frame.add(new CalendarPanel(Calendar.getInstance()), c);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}
