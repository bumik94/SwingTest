package test.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

public class CalendarTable extends JPanel{
    private final int MAX_DAYS_OF_WEEK   = 7;
    private final int MAX_WEEKS_OF_MONTH = 6;
    DateFormat df;
    Calendar calendar;
    int currentYear;
    int currentMonth;
    Object[] daysOfWeek;    // JTable column names
    Object[][] daysOfMonth; // JTable row data

    public CalendarTable() {
        this.df = DateFormat.getDateInstance();
        this.calendar = Calendar.getInstance();
        this.calendar.setMinimalDaysInFirstWeek(1);
        this.currentYear = calendar.get(Calendar.YEAR);
        this.currentMonth = calendar.get(Calendar.MONTH);
        this.daysOfWeek = getDaysOfWeek();
        this.daysOfMonth = getDaysOfMonth(getDateList());

        JTable calendarTable = new JTable(daysOfMonth, daysOfWeek);
        JScrollPane tablePane = new JScrollPane(calendarTable);

        /* DefaultTableCellRenderer is a subclass of JLabel and thus inherits its methods
            - setHorizontalAlignment aligns the label content horizontally

         */
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        calendarTable.setDefaultRenderer(Object.class, centerRenderer);
        calendarTable.setFillsViewportHeight(true);


        this.add(tablePane);

        System.out.println(Arrays.deepToString(getDaysOfMonth(getDateList())));
    }

    /**
     * Parses ordered days of week in short form into an array according to current locale's first day.
     *
     * @return A String array of short form days of week in a default locale
     */
    private Object[] getDaysOfWeek() {
        Map<String, Integer> daysOfWeekMap = calendar.getDisplayNames(
                Calendar.DAY_OF_WEEK,
                Calendar.SHORT_STANDALONE,
                Locale.getDefault());
        Object[] array = new String[7];

        if (calendar.getFirstDayOfWeek() == Calendar.MONDAY) {
                    daysOfWeekMap.forEach((day, idx) -> {
                        // Capitalize first letter of a day
                        day = day.substring(0, 1).toUpperCase() + day.substring(1);
                        // Put Sunday at the end of the array
                        if (idx == Calendar.SUNDAY) {
                            array[array.length - 1] = day;
                        // Shift all other days to`  the start of the array
                        } else {
                            array[idx - 2] = day;
                        }
                    });
        }
        else { daysOfWeekMap.forEach((day, idx) -> array[idx - 1] = day); }

        return array;
    }

    private List<Integer> getDateList() {
        ArrayList<Integer> list = new ArrayList<>();
        calendar.set(Calendar.WEEK_OF_MONTH, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Condition checks if the calendar has reached next month at the beginning of a new week
        do {
            // Extract the date string for the current day and add it to the list
            list.add(calendar.get(Calendar.DAY_OF_MONTH));
            // Increment day of the calendar
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (! (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY &&
                calendar.get(Calendar.MONTH) != currentMonth));
        // Set calendar back to current month and year in case of overlap to the next month/year
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        return list;
    }

    private Object[][] getDaysOfMonth(List<Integer> dateList) {
        Object[][] array = new Object[MAX_WEEKS_OF_MONTH][MAX_DAYS_OF_WEEK];
        ListIterator<Integer> iterator = dateList.listIterator();

        for (int week = 0; week < MAX_WEEKS_OF_MONTH; week++) {
            for (int day = 0; day < MAX_DAYS_OF_WEEK; day++) {
                if (iterator.hasNext()){
                    array[week][day] = iterator.next();
                }
            }
        }
        return array;
    }
}


class Main {

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(1, 1, 1, 1);

        // Lay out components and set visibility
        frame.add(new CalendarTable(), c);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}