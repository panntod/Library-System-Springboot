package panntod.core.library.library_system.utils;

import java.time.LocalDateTime;

public class PenaltyUtil {
    private PenaltyUtil() {
        // prevent instantiation
    }

    public static Double calculatePenalty(LocalDateTime dueDate, LocalDateTime returnDate) {
        long daysLate = java.time.Duration.between(dueDate, returnDate).toDays();
        if (daysLate <= 0) return 0.0;

        double penaltyPerDay = 5000.0;
        return daysLate * penaltyPerDay;
    }
}
