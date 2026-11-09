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
@DisplayName("BL-60: сума {0}, до заїзду {1} дн. — плата {2}")
    @ParameterizedTest(name = "BL-60: сума {0}, до заїзду {1} дн. — плата {2}")
    @CsvSource({
            "1000.00, 30, 0.00",
            "1000.00, 8, 0.00",
            "1000.00, 7, 0.00",       // межа: рівно 7 днів — безкоштовно
            "1000.00, 6, 500.00",     // межа: 6 днів — 50 %
            "1000.00, 4, 500.00",
            "1000.00, 3, 500.00",     // межа: рівно 3 дні — 50 %
            "1000.00, 2, 1000.00",    // межа: 2 дні — 100 %
            "1000.00, 1, 1000.00",
            "1000.00, 0, 1000.00",
            "1000.00, -1, 1000.00"    // заїзд уже минув
    })
    // Червоний на v1: BUG-023 (рядок 7 днів) — звіт у GitHub Issues, мітка module
    void cancellationFee(BigDecimal total, int days, BigDecimal expected) {
        assertEquals(expected, availability.cancellationFee(total, days));
    }

    @Test
    @DisplayName("BL-60: 50 % від 333.33 = 166.665 → 166.67 (HALF_UP)")
    void cancellationFeeRoundsHalfUp() {
        assertEquals(money("166.67"), availability.cancellationFee(money("333.33"), 5));
    }

    @Test
    @DisplayName("BL-60: від'ємна сума — IllegalArgumentException")
    void cancellationFeeNegativeTotal() {
        assertThrows(IllegalArgumentException.class, () -> availability.cancellationFee(money("-1"), 10));
    }

    @DisplayName("BL-60: сума {0}, до заїзду {1} дн. — повернення {2}")
    @ParameterizedTest(name = "BL-60: сума {0}, до заїзду {1} дн. — повернення {2}")
    @CsvSource({
            "1000.00, 7, 1000.00",
            "1000.00, 6, 500.00",
            "1000.00, 3, 500.00",
            "1000.00, 2, 0.00",
            "333.33, 5, 166.66"
    })
    // Червоний на v1: BUG-023 (рядок 7 днів) — звіт у GitHub Issues, мітка module
    void refund(BigDecimal total, int days, BigDecimal expected) {
        assertEquals(expected, availability.refund(total, days));
    }

    @Test
    @DisplayName("BL-70: у порожньому реєстрі номер вільний")
    void emptyRegistryIsAvailable() {
        assertTrue(availability.isAvailable("101", sep(10), sep(12)));
    }

    @DisplayName("BL-70: існує 10–15 вер.; новий {0}–{1} вер. — вільний: {2}")
    @ParameterizedTest(name = "BL-70: існує 10–15 вер.; новий {0}–{1} вер. — вільний: {2}")
    @CsvSource({
            "10, 15, false",   // той самий інтервал
            "12, 13, false",   // всередині
            "8, 12, false",    // перетин зліва
            "13, 18, false",   // перетин справа
            "8, 18, false",    // охоплює наявне
            "14, 16, false",   // перетин на одну ніч справа
            "9, 11, false",    // перетин на одну ніч зліва
            "15, 17, true",    // заїзд у день виїзду попереднього гостя
            "8, 10, true",     // виїзд у день заїзду наступного гостя
            "16, 18, true",    // зазор 1 день
            "5, 9, true"       // зазор 1 день зліва
    })
    // Червоний на v1: BUG-024 (рядки 15–17 і 8–10) — звіт у GitHub Issues, мітка module
    void intervalAgainstExistingStay(int in, int out, boolean expected) {
        availability.book("101", sep(10), sep(15));
        assertEquals(expected, availability.isAvailable("101", sep(in), sep(out)));
    }

    @Test
    @DisplayName("BL-70: бронювання іншого номера не впливає")
    void otherRoomIsIndependent() {
        availability.book("101", sep(10), sep(15));
        assertTrue(availability.isAvailable("102", sep(10), sep(15)));
    }
@Test
    @DisplayName("BL-70: book повертає бронювання і додає його до реєстру")
    void bookRegistersStay() {
        Availability.Stay stay = availability.book("101", sep(10), sep(12));
        assertAll("BL-70: результат book і стан реєстру",
                () -> assertEquals(new Availability.Stay("101", sep(10), sep(12)), stay),
                () -> assertEquals(1, availability.stays().size()));
    }
@Test
    @DisplayName("BL-70: book на зайняті дати — IllegalStateException, реєстр не змінюється")
    void bookBusyRoomThrows() {
        availability.book("101", sep(10), sep(15));
        assertThrows(IllegalStateException.class, () -> availability.book("101", sep(12), sep(14)));
        assertEquals(1, availability.stays().size());
    }

    // Червоний на v1: BUG-024 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-70: два послідовні бронювання «день у день» допустимі")
    void backToBackBookingsAllowed() {
        availability.book("101", sep(10), sep(12));
        availability.book("101", sep(12), sep(14));
        assertEquals(2, availability.stays().size());
    }

    @Test
    @DisplayName("BL-70: після скасованої спроби номер лишається вільним на вільні дати")
    void freeDatesStillBookableAfterFailedBooking() {
        availability.book("101", sep(10), sep(15));
        assertThrows(IllegalStateException.class, () -> availability.book("101", sep(14), sep(16)));
        assertTrue(availability.isAvailable("101", sep(20), sep(22)));
    }

    @Test
    @DisplayName("BL-70: виїзд = заїзд — IllegalArgumentException у isAvailable і book")
    void emptyIntervalRejected() {
        assertThrows(IllegalArgumentException.class, () -> availability.isAvailable("101", sep(10), sep(10)));
        assertThrows(IllegalArgumentException.class, () -> availability.book("101", sep(10), sep(10)));
    }

    @Test
    @DisplayName("BL-70: виїзд раніший за заїзд — IllegalArgumentException")
    void reversedIntervalRejected() {
        assertThrows(IllegalArgumentException.class, () -> availability.isAvailable("101", sep(12), sep(10)));
    }

    @Test
    @DisplayName("BL-70: stays() повертає незмінний список")
    void staysIsUnmodifiable() {
        availability.book("101", sep(10), sep(12));
        assertThrows(UnsupportedOperationException.class, () -> availability.stays().clear());
        assertFalse(availability.stays().isEmpty());
    }
}
