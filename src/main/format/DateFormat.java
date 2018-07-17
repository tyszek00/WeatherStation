package main.format;

public class DateFormat {

    public static String getDay(String day) {
        switch(day) {
            case "Mon": return "Poniedziałek";
            case "Tue": return "Wtorek";
            case "Wed": return "Środa";
            case "Thu": return "Czwartek";
            case "Fri": return "Piątek";
            case "Sat": return "Sobota";
            case "Sun": return "Niedziela";
        }
        return null;
    }

    public static String getMonth(String month) {
        switch(month) {
            case "Jan": return "stycznia";
            case "Feb": return "lutego";
            case "Mar": return "marca";
            case "Apr": return "kwietnia";
            case "May": return "maja";
            case "Jun": return "czerwca";
            case "Jul": return "lipca";
            case "Aug": return "sierpnia";
            case "Sep": return "września";
            case "Oct": return "października";
            case "Nov": return "listopada";
            case "Dec": return "grudnia";
        }
        return null;
    }

    public static String getFullDate(String inputDate) {
        String dayName = getDay(inputDate.substring(0, 3));
        String day = inputDate.substring(8, 10);
        String month = getMonth(inputDate.substring(4, 7));
        String year = inputDate.substring(24);
        String fullDate = dayName + ", " + day + " " + month + " " + year;
        return fullDate;
    }
}
