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
@DisplayName("BL-10: {0} гостей у номері на {1} — коректно")
    @ParameterizedTest(name = "BL-10: {0} гостей у номері на {1} — коректно")
    @CsvSource({"1, 4", "2, 4", "3, 4", "4, 4", "1, 1"})
    void guestsWithinCapacity(int guests, int capacity) {
        assertEquals(List.of(), validator.validate(TODAY, TODAY.plusDays(1), guests, capacity, TODAY));
    }

    @DisplayName("BL-10: {0} гостей у номері на {1} — GUESTS_OUT_OF_RANGE")
    @ParameterizedTest(name = "BL-10: {0} гостей у номері на {1} — GUESTS_OUT_OF_RANGE")
    @CsvSource({"0, 4", "-1, 4", "5, 4", "2, 1"})
    void guestsOutsideRange(int guests, int capacity) {
        assertEquals(List.of(BookingError.GUESTS_OUT_OF_RANGE),
                validator.validate(TODAY, TODAY.plusDays(1), guests, capacity, TODAY));
    }

    // Червоний на v1: BUG-020 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-10: кілька порушень одночасно — повертаються всі відповідні коди")
    void severalViolationsReportedTogether() {
        List<BookingError> errors = check(TODAY.minusDays(1), TODAY.plusDays(40), 0);
        assertEquals(3, errors.size(), "Очікувалось 3 коди, отримано: " + errors);
        assertTrue(errors.containsAll(List.of(BookingError.NIGHTS_OUT_OF_RANGE,
                BookingError.CHECK_IN_IN_PAST, BookingError.GUESTS_OUT_OF_RANGE)), "Отримано: " + errors);
    }

    @DisplayName("BL-20: email «{0}» — коректний")
    @ParameterizedTest(name = "BL-20: email «{0}» — коректний")
    @CsvSource({"guest@example.com", "a.b+c@mail.example.co.ua", "user_1@host-name.org"})
    void validEmails(String email) {
        assertTrue(validator.isValidEmail(email));
    }

    @DisplayName("BL-20: email «{0}» — некоректний")
    @ParameterizedTest(name = "BL-20: email «{0}» — некоректний")
    @CsvSource({"guest", "guest@", "@example.com", "guest@example", "guest@@example.com",
            "guest@exam ple.com", "guest example@mail.com", "guest@.com"})
    void invalidEmails(String email) {
        assertFalse(validator.isValidEmail(email));
    }

    @Test
    @DisplayName("BL-20: null і порожній email — некоректні")
    void nullAndEmptyEmail() {
        assertFalse(validator.isValidEmail(null));
        assertFalse(validator.isValidEmail(""));
    }

    @Test
    @DisplayName("BL-20: +380 і 9 цифр — коректний телефон")
    void validPhone() {
        assertTrue(validator.isValidPhone("+380501234567"));
        assertTrue(validator.isValidPhone("+380000000000"));
    }

    @DisplayName("BL-20: телефон «{0}» — некоректний")
    @ParameterizedTest(name = "BL-20: телефон «{0}» — некоректний")
    @CsvSource({"380501234567", "+38050123456", "+3805012345678", "+381501234567",
            "+38050123456a", "0501234567", "+380 50 123 4567", "+380-50-1234567"})
    void invalidPhones(String phone) {
        assertFalse(validator.isValidPhone(phone));
    }

    @Test
    @DisplayName("BL-20: null і порожній телефон — некоректні")
    void nullAndEmptyPhone() {
        assertFalse(validator.isValidPhone(null));
        assertFalse(validator.isValidPhone(""));
    }

    @Test
    @DisplayName("BL-20: коректні дати й контакти — помилок немає")
    void validateWithContactsOk() {
        assertEquals(List.of(), validator.validate(TODAY, TODAY.plusDays(2), 2, CAPACITY, TODAY, EMAIL, PHONE));
    }

    @Test
    @DisplayName("BL-20: некоректний email — лише INVALID_EMAIL")
    void validateWithBadEmail() {
        assertEquals(List.of(BookingError.INVALID_EMAIL),
                validator.validate(TODAY, TODAY.plusDays(2), 2, CAPACITY, TODAY, "guest@", PHONE));
    }

    @Test
    @DisplayName("BL-20: некоректний телефон — лише INVALID_PHONE")
    void validateWithBadPhone() {
        assertEquals(List.of(BookingError.INVALID_PHONE),
                validator.validate(TODAY, TODAY.plusDays(2), 2, CAPACITY, TODAY, EMAIL, "0501234567"));
    }
    @Test
    @DisplayName("BL-10 + BL-20: помилки дат і контактів повертаються разом")
    void validateWithDateAndContactErrors() {
        List<BookingError> errors = validator.validate(TODAY, TODAY, 2, CAPACITY, TODAY, "x", "y");
        assertEquals(3, errors.size(), "Отримано: " + errors);
        assertTrue(errors.containsAll(List.of(BookingError.CHECK_OUT_NOT_AFTER_CHECK_IN,
                BookingError.INVALID_EMAIL, BookingError.INVALID_PHONE)), "Отримано: " + errors);
    }
}
