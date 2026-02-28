import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

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
                //
                // Ask the user...
                // 1. Do you live in California
                // 2. When did you start living in California
                // If the currently lives in California and has been living in California for 2 years,
                // the user passes the residency portion
                //
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
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid format. Please use MM/YYYY (example: 02/2024).");
                    }
                }

                
                
            
        }
    }
    

    public static void main(String[] args) {
        aidApplication app = new aidApplication();
        app.ageCheck();
    }
}
