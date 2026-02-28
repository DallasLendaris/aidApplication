import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class aidApplication {
    private boolean appStatus;

    public aidApplication(){
        this.appStatus = false;
    }

    public boolean ageCheck(){
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter your age: ");
                String input = scanner.nextLine().trim();

                try {
                    int age = Integer.parseInt(input);
                    if (age < 0) {
                        System.out.println("Age cannot be negative. Try again.");
                        continue;
                    }
                    return age >= 18 && age <= 24;
                    
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid whole number.");
                }
            }
        }
    }

    public boolean residency(){
        try (Scanner scanner = new Scanner(System.in)) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("M/uuuu", Locale.US);
                boolean qindex = true;
                //
                // Ask the user...
                // 1. Do you live in California
                // 2. When did you start living in California
                // If the currently lives in California and has been living in California for 2 years,
                // the user passes the residency portion
                //
                while(qindex){
                    System.out.print("Do you live in California? Y/N");
                    String input = scanner.nextLine().trim();
                    if(input.toUpperCase().equals("Y")){
                        System.out.print("When did you start living in California? (MM/YYYY): ");
                        String start = scanner.nextLine().trim();

                        try {
                            YearMonth moveIn = YearMonth.parse(start, fmt);
                            YearMonth twoYearsAgo = YearMonth.now().minusYears(2);
                            if(moveIn.isAfter(twoYearsAgo)){
                                return true;
                            }
                            else{
                                qindex = false;
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid format. Please use MM/YYYY (example: 02/2024).");
                        }
                    }
                }
             
                //
                // Ask the user...
                // 1. Have you ever worked in California
                // 2. What was your start date?
                // If the user has worked in California for longer 6 months or longer,
                // the user passes the residency portion
                qindex = true;
                while(qindex){
                    System.out.print("Have you ever worked in California? Y/N");
                    String input = scanner.nextLine().trim();
                    if(input.toUpperCase().equals("Y")){
                        System.out.print("What was your start date? (MM/YYYY)");
                        String start = scanner.nextLine().trim();
                        System.out.print("What was your end date? (MM/YYYY)");
                        String end = scanner.nextLine().trim();

                        try {
                            YearMonth startDate = YearMonth.parse(start, fmt);
                            YearMonth endDate = YearMonth.parse(end, fmt);
                            long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
                            if(monthsBetween >= 6){
                                return true;
                            }else{
                                qindex = false;
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid format. Please use MM/YYYY (example: 02/2024).");
                        }
                    }
                }

                
                
            
        }
    }
    

    public static void main(String[] args) {
        aidApplication app = new aidApplication();
        app.ageCheck();
    }
}
