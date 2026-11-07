package ua.kpi.tzyapz.staynow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Юніт-тести BookingValidator: вимоги BL-10 (параметри бронювання) і BL-20 (email, телефон). */
@DisplayName("BookingValidator: BL-10, BL-20")
class BookingValidatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 9, 8);
    private static final int CAPACITY = 4;
    private static final String EMAIL = "guest@example.com";
    private static final String PHONE = "+380501234567";

    private BookingValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BookingValidator();
    }

    private List<BookingError> check(LocalDate in, LocalDate out, int guests) {
        return validator.validate(in, out, guests, CAPACITY, TODAY);
    }
@Test
    @DisplayName("BL-10: заїзд сьогодні, 1 ніч, 2 гості — помилок немає")
    void validBookingHasNoErrors() {
        assertEquals(List.of(), check(TODAY, TODAY.plusDays(1), 2));
    }

    @DisplayName("BL-10: заїзд {0}, виїзд {1} — виїзд не пізніший за заїзд")
    @ParameterizedTest(name = "BL-10: заїзд {0}, виїзд {1} — виїзд не пізніший за заїзд")
    @CsvSource({
            "2026-09-15, 2026-09-15",   // виїзд = заїзд (0 ночей)
            "2026-09-15, 2026-09-14"    // виїзд раніший за заїзд
    })
    void checkOutNotAfterCheckIn(LocalDate in, LocalDate out) {
        assertEquals(List.of(BookingError.CHECK_OUT_NOT_AFTER_CHECK_IN), check(in, out, 2));
    }

    // Червоний на v1: BUG-019 (рядок 30 ночей) — звіт у GitHub Issues, мітка module
    @DisplayName("BL-10: {0} ночей — коректно")
    @ParameterizedTest(name = "BL-10: {0} ночей — коректно")
    @CsvSource({"1", "2", "15", "29", "30"})
    void allowedNightsAreAccepted(int nights) {
        LocalDate in = LocalDate.of(2026, 10, 1);
        assertEquals(List.of(), check(in, in.plusDays(nights), 2),
                "Кількість ночей " + nights + " входить у діапазон 1..30");
    }

    @Test
    @DisplayName("BL-10: 31 ніч — NIGHTS_OUT_OF_RANGE")
    void thirtyOneNightsRejected() {
        LocalDate in = LocalDate.of(2026, 10, 1);
        assertEquals(List.of(BookingError.NIGHTS_OUT_OF_RANGE), check(in, in.plusDays(31), 2));
    }

    @Test
    @DisplayName("BL-10: 100 ночей — NIGHTS_OUT_OF_RANGE")
    void farAboveMaxNightsRejected() {
        LocalDate in = LocalDate.of(2026, 10, 1);
        assertEquals(List.of(BookingError.NIGHTS_OUT_OF_RANGE), check(in, in.plusDays(100), 2));
    }

    @Test
    @DisplayName("BL-10: заїзд сьогодні — допустимо (межа)")
    void checkInToday() {
        assertEquals(List.of(), check(TODAY, TODAY.plusDays(2), 1));
    }

    // Червоний на v1: BUG-020 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-10: заїзд учора, виїзд завтра — CHECK_IN_IN_PAST")
    void checkInYesterdayCheckOutTomorrow() {
        assertEquals(List.of(BookingError.CHECK_IN_IN_PAST),
                check(TODAY.minusDays(1), TODAY.plusDays(1), 2));
    }

    @Test
    @DisplayName("BL-10: заїзд і виїзд у минулому — CHECK_IN_IN_PAST")
    void bothDatesInThePast() {
        assertEquals(List.of(BookingError.CHECK_IN_IN_PAST),
                check(TODAY.minusDays(10), TODAY.minusDays(8), 2));
    }

    @Test
    @DisplayName("BL-10: заїзд завтра — допустимо")
    void checkInTomorrow() {
        assertEquals(List.of(), check(TODAY.plusDays(1), TODAY.plusDays(3), 2));
    }
}
