import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Scanner;

public class aidApplication {

    public aidApplication(){
    }

    public boolean ageCheck(Scanner scanner){
       
            while (true) {
                System.out.print("Enter your age: ");
                String input = scanner.nextLine().trim();

                try {
                    int age = Integer.parseInt(input);
                    if (age < 0) {
                        System.out.println("Age cannot be negative. Try again...");
                        continue;
                    }
                    return age >= 18 && age <= 24;
                    
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid whole number...");
                }
            }
        
    }
    public boolean ynCheck(String input){
        return !(input.toUpperCase().equals("Y") || input.toUpperCase().equals("N"));
    }

    public boolean residency(Scanner scanner){
        
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
            System.out.print("Do you live in California? (Y/N): ");
            String input = scanner.nextLine().trim();
            if(ynCheck(input)){
                System.out.println("Enter Y or N as your response...");
            }
            else if(input.toUpperCase().equals("Y")){
                System.out.print("When did you start living in California? (MM/YYYY): ");
                String start = scanner.nextLine().trim();

                try {
                    YearMonth moveIn = YearMonth.parse(start, fmt);
                    YearMonth twoYearsAgo = YearMonth.now().minusYears(2);
                    if(moveIn.isBefore(twoYearsAgo)){
                        return true;
                    }
                    else{
                        qindex = false;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid format. Please use MM/YYYY (example: 02/2024).");
                }
            }
            else{qindex = false;}
        }
        
        //
        // Ask the user...
        // 1. Have you ever worked in California? (Y/N)
        // 2. What was your start date?
        // If the user has worked in California for longer 6 months or longer,
        // the user passes the residency portion
        //
        qindex = true;
        while(qindex){
            System.out.print("Have you ever worked in California? (Y/N): ");
            String input = scanner.nextLine().trim();
            if(ynCheck(input)){
                System.out.println("Enter Y or N as your response...");
            }
            else if(input.toUpperCase().equals("Y")){
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
                    }
                    else{qindex = false;}
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid format. Please use MM/YYYY (example: 02/2024).");
                }
            }
            else{qindex = false;}
        }

        //
        // Ask the user...
        // 1. Do your parents live in California? (Y/N)
        // 2. When did they start living in California? (MM/YYYY)
        // If the user's parents have lived in california for over a year,
        // the user passes the residency portion
        //
        qindex = true;
        while(qindex){
            System.out.print("Do your parents live in California? (Y/N): ");
            String input = scanner.nextLine().trim();
            if(ynCheck(input)){
                System.out.println("Enter Y or N as your response");
            }
            else if(input.toUpperCase().equals("Y")){
                System.out.print("When did they start living in California? (MM/YYYY): ");
                String start = scanner.nextLine().trim();
                
                try {
                    YearMonth startDate = YearMonth.parse(start, fmt);
                    YearMonth oneYear = YearMonth.now().minusYears(1);
                    if(startDate.isBefore(oneYear)){
                        return true;
                    }else{qindex = false;}
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid format. Please use MM/YYYY (example: 02/2024).");
                }
            }else{qindex = false;}
        }

        //
        // Ask the user...
        // 1. Have you volunteered for a public cause in California? (Y/N)
        // 2. Can you provide proof of this public cause? (Y/N)
        // If the user has participated in public cause and has proof,
        // the user passes the residency portion
        //
        qindex = true;
        while(qindex){
            System.out.print("Have you volunteered for a public cause in California? (Y/N): ");
            String input = scanner.nextLine().trim();
            if(ynCheck(input)){
                System.out.println("Enter Y or N as your response");
            }
            else if(input.toUpperCase().equals("Y")){
                System.out.print("Can you provide proof of this public cause? (Y/N): ");
                input = scanner.nextLine().trim();
                if(ynCheck(input)){
                    System.out.println("Enter Y or N as your response");
                }
                else if(input.toUpperCase().equals("Y")){
                    return true;
                }else{qindex = false;}
            }
            else{qindex = false;};
        }
        return false;
    }
    
    
    public boolean deansConsideration(Scanner scanner){
        boolean loop = true;
        //
        // Ask the user...
        // What is your household income? (per month)
        // If the household income is less than $5000 per month,
        // the user passes the Deans Consideration
        //
        while(loop){
            System.out.print("What is your household income? (per month): ");
            String input = scanner.nextLine().trim();
            try {
                int inputnum = Integer.parseInt(input.trim());
                if(inputnum < 5000){
                    return true;
                }else{loop = false;}
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number");
            }
        }
        return false;
    }

    public static void main(String[] args) {
        aidApplication app = new aidApplication();
        try (Scanner scanner = new Scanner(System.in)) {
            if (app.ageCheck(scanner) && (app.residency(scanner) || app.deansConsideration(scanner))) {
                System.out.println("Pass");
            } else {
                System.out.println("Fail");
            }
        }
    }
}
