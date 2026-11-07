package ua.kpi.tzyapz.staynow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Юніт-тести Availability: BL-60 (скасування) і BL-70 (доступність номера). */
@DisplayName("Availability: BL-60, BL-70")
class AvailabilityTest {

    private Availability availability;

    @BeforeEach
    void setUp() {
        availability = new Availability();
    }

    private static LocalDate sep(int day) {
        return LocalDate.of(2026, 9, day);
    }

    private static BigDecimal money(String s) {
        return new BigDecimal(s);
    }

}
