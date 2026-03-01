package aidapplication;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class aidApplicationTest {
    private Scanner scannerOf(String... lines) {
        return new Scanner(String.join(System.lineSeparator(), lines) + System.lineSeparator());
    }
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("M/uuuu");
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void ageCheck_returnsTrue_forLowerBoundary18() {
        aidApplication app = new aidApplication();
        try (java.util.Scanner scanner = new java.util.Scanner("18\n")) {
            assertTrue(app.ageCheck(scanner));
        }
    }

    @Test
    public void ageCheck_returnsFalse_forBelowLowerBoundry() {
        aidApplication app = new aidApplication();
        try(java.util.Scanner scanner = new java.util.Scanner("12\n")){
            assertFalse(app.ageCheck(scanner));
        }
    }

    @Test
    public void ageCheck_returnsTrue_forWithinBoundries() {
        aidApplication app = new aidApplication();
        try (java.util.Scanner scanner = new java.util.Scanner("20\n")) {
            assertTrue(app.ageCheck(scanner));
        }
    }

    @Test
    public void ageCheck_returnsTrue_forUpperBoundry24() {
        aidApplication app = new aidApplication();
        try (java.util.Scanner scanner = new java.util.Scanner("24\n")) {
            assertTrue(app.ageCheck(scanner));
        }
    }

    @Test
    public void ageCheck_returnsFalse_forAboveUpperBoundry() {
        aidApplication app = new aidApplication();
        try (java.util.Scanner scanner = new java.util.Scanner("26\n")) {
            assertFalse(app.ageCheck(scanner));
        }
    }

    @Test
    void residency_passes_ifLivesInCA_moreThan2Years() {
        aidApplication app = new aidApplication();
        String moveIn = YearMonth.now().minusYears(3).format(FMT);

        try (Scanner sc = scannerOf("Y", moveIn)) {
            assertTrue(app.residency(sc));
        }
    }

     @Test
    void residency_fails_ifLivesInCA_exactly2Years_andAllOthersNo() {
        aidApplication app = new aidApplication();
        String moveIn = YearMonth.now().minusYears(2).format(FMT);

        try (Scanner sc = scannerOf("Y", moveIn, "N", "N", "N")) {
            assertFalse(app.residency(sc));
        }
    }

    @Test
    void residency_retries_firstSection_onInvalidDate_thenPasses() {
        aidApplication app = new aidApplication();
        String validOldDate = YearMonth.now().minusYears(4).format(FMT);

        // invalid date causes retry of section 1 question loop
        try (Scanner sc = scannerOf("Y", "13/2020", "Y", validOldDate)) {
            assertTrue(app.residency(sc));
        }
    }

    @Test
    void residency_passes_ifWorkedInCA_6MonthsOrMore() {
        aidApplication app = new aidApplication();
        String start = "1/2024";
        String end = "7/2024"; // 6 months difference

        try (Scanner sc = scannerOf("N", "Y", start, end)) {
            assertTrue(app.residency(sc));
        }
    }

    @Test
    void residency_fails_ifWorkedInCA_lessThan6Months_andOthersNo() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("N", "Y", "1/2024", "5/2024", "N", "N")) {
            assertFalse(app.residency(sc));
        }
    }

    @Test
    void residency_retries_workSection_onInvalidDate_thenPasses() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf(
                "N",
                "Y", "bad", "7/2024",   // invalid start date => retry section 2
                "Y", "1/2024", "8/2024" // valid second attempt
        )) {
            assertTrue(app.residency(sc));
        }
    }

    @Test
    void residency_passes_ifParentsInCA_moreThan1Year() {
        aidApplication app = new aidApplication();
        String start = YearMonth.now().minusYears(2).format(FMT);

        try (Scanner sc = scannerOf("N", "N", "Y", start)) {
            assertTrue(app.residency(sc));
        }
    }

    @Test
    void residency_fails_ifParentsInCA_exactly1Year_andVolunteerNo() {
        aidApplication app = new aidApplication();
        String start = YearMonth.now().minusYears(1).format(FMT); // exactly one year -> false (isBefore check)

        try (Scanner sc = scannerOf("N", "N", "Y", start, "N")) {
            assertFalse(app.residency(sc));
        }
    }

    @Test
    void residency_passes_ifVolunteerAndProofYes() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("N", "N", "N", "Y", "Y")) {
            assertTrue(app.residency(sc));
        }
    }

    @Test
    void residency_fails_ifVolunteerYes_butNoProof() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("N", "N", "N", "Y", "N")) {
            assertFalse(app.residency(sc));
        }
    }

    @Test
    void residency_fails_ifAllNo() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("N", "N", "N", "N")) {
            assertFalse(app.residency(sc));
        }
    }

    @Test
    void residency_retries_onInvalidYN_thenContinues() {
        aidApplication app = new aidApplication();
        String oldDate = YearMonth.now().minusYears(3).format(FMT);

        // first input invalid; should ask again and then pass
        try (Scanner sc = scannerOf("maybe", "Y", oldDate)) {
            assertTrue(app.residency(sc));
        }
    }

    // ---------- deansConsideration() tests ----------

    @Test
    void deansConsideration_passes_ifIncomeBelow5000() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("4999")) {
            assertTrue(app.deansConsideration(sc));
        }
    }

    @Test
    void deansConsideration_fails_ifIncomeExactly5000() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("5000")) {
            assertFalse(app.deansConsideration(sc));
        }
    }

    @Test
    void deansConsideration_fails_ifIncomeAbove5000() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("7500")) {
            assertFalse(app.deansConsideration(sc));
        }
    }

    @Test
    void deansConsideration_retries_onInvalidNumber_thenPasses() {
        aidApplication app = new aidApplication();

        try (Scanner sc = scannerOf("abc", "4200")) {
            assertTrue(app.deansConsideration(sc));
        }
    }

    @Test
    void deansConsideration_currentBehavior_negativeIncomePasses() {
        aidApplication app = new aidApplication();

        // documents current behavior in implementation
        try (Scanner sc = scannerOf("-1")) {
            assertTrue(app.deansConsideration(sc));
        }
    }
}
