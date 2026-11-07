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
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Юніт-тести PriceCalculator: BL-30 (вихідні), BL-40 (сезони), BL-50 (вартість). */
@DisplayName("PriceCalculator: BL-30, BL-40, BL-50")
class PriceCalculatorTest {

    private PriceCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new PriceCalculator();
    }

    private static BigDecimal money(String s) {
        return new BigDecimal(s);
    }
@DisplayName("BL-40: {0} — коефіцієнт {1}")
    @ParameterizedTest(name = "BL-40: {0} — коефіцієнт {1}")
    @CsvSource({
            "2026-05-31, 1.00", "2026-06-01, 1.30", "2026-07-15, 1.30", "2026-08-31, 1.30",
            "2026-09-01, 1.00", "2026-10-31, 1.00",
            "2026-11-01, 0.80", "2026-11-30, 0.80",
            "2026-12-01, 1.00", "2026-12-19, 1.00", "2026-12-20, 1.30", "2026-12-31, 1.30",
            "2027-01-01, 1.30", "2027-01-10, 1.30", "2027-01-11, 1.00",
            "2027-01-31, 1.00", "2027-02-01, 0.80", "2028-02-29, 0.80",
            "2027-03-01, 0.80", "2027-03-31, 0.80", "2027-04-01, 1.00", "2027-05-15, 1.00",
            "2026-06-02, 1.30", "2026-08-30, 1.30", "2026-12-21, 1.30", "2027-01-09, 1.30",
            "2027-02-02, 0.80", "2027-03-30, 0.80", "2026-11-02, 0.80", "2026-11-29, 0.80"
    })
    void seasonFactor(LocalDate date, BigDecimal expected) {
        assertEquals(0, expected.compareTo(calc.seasonFactor(date)),
                "Коефіцієнт для " + date + ": очікувався " + expected + ", отримано " + calc.seasonFactor(date));
    }

    @DisplayName("BL-30: ніч з {0} ({1}) — вихідна: {2}")
    @ParameterizedTest(name = "BL-30: ніч з {0} ({1}) — вихідна: {2}")
    @CsvSource({
            "2026-09-07, понеділок, false",
            "2026-09-08, вівторок, false",
            "2026-09-09, середа, false",
            "2026-09-10, четвер, false",
            "2026-09-11, п’ятниця, true",
            "2026-09-12, субота, true",
            "2026-09-13, неділя, false"
    })
    // Червоний на v1: BUG-021 (п’ятниця, неділя) — звіт у GitHub Issues, мітка module
    void weekendNight(LocalDate night, String dayName, boolean expected) {
        assertEquals(expected, calc.isWeekendNight(night), "День тижня: " + dayName);
    }

    @DisplayName("BL-50: тариф {0}, ніч {1} — {2}")
    @ParameterizedTest(name = "BL-50: тариф {0}, ніч {1} — {2}")
    @CsvSource({
            "100, 2026-09-09, 100.00",   // середа, звичайний період
            "100, 2026-07-08, 130.00",   // середа, високий сезон
            "100, 2026-11-04, 80.00",    // середа, низький сезон
            "100, 2026-09-11, 115.00",   // п’ятниця, звичайний період
            "100, 2026-07-03, 149.50",   // п’ятниця, високий сезон (1.30 × 1.15)
            "100, 2026-02-06, 92.00",    // п’ятниця, низький сезон (0.80 × 1.15)
            "100, 2026-09-13, 100.00"    // неділя: надбавки немає
    })
    // Червоний на v1: BUG-021 (рядки для п’ятниці й неділі) — звіт у GitHub Issues, мітка module
    void nightPrice(BigDecimal base, LocalDate night, BigDecimal expected) {
        assertEquals(expected, calc.nightPrice(base, night));
    }

    // Червоний на v1: BUG-022 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-50: округлення HALF_UP — 99.99 × 1.30 = 129.987 → 129.99")
    void nightPriceRoundsHalfUpAboveHalf() {
        assertEquals(money("129.99"), calc.nightPrice(money("99.99"), LocalDate.of(2026, 7, 1)));
    }

    // Червоний на v1: BUG-022 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-50: округлення HALF_UP — 0.15 × 1.30 = 0.195 → 0.20")
    void nightPriceRoundsHalfUpAtExactHalf() {
        assertEquals(money("0.20"), calc.nightPrice(money("0.15"), LocalDate.of(2026, 7, 8)));
    }

    // Червоний на v1: BUG-022 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-50: округлення HALF_UP — 0.01 × 0.80 = 0.008 → 0.01")
    void nightPriceRoundsHalfUpSmallValue() {
        assertEquals(money("0.01"), calc.nightPrice(money("0.01"), LocalDate.of(2026, 11, 4)));
    }

    @DisplayName("BL-50: nightPrice із тарифом «{0}» — IllegalArgumentException")
    @ParameterizedTest(name = "BL-50: nightPrice із тарифом «{0}» — IllegalArgumentException")
    @CsvSource({"0", "-1", "-0.01"})
    void nightPriceRejectsNonPositiveRate(BigDecimal base) {
        assertThrows(IllegalArgumentException.class, () -> calc.nightPrice(base, LocalDate.of(2026, 9, 9)));
    }
@Test
    @DisplayName("BL-50: 2 будні ночі у звичайний період = 200.00")
    void totalTwoWeekdayNights() {
        assertEquals(money("200.00"),
                calc.total(money("100"), LocalDate.of(2026, 9, 15), LocalDate.of(2026, 9, 17)));
    }

    // Червоний на v1: BUG-021 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-50: одна ніч з п'ятниці на суботу = 115.00")
    void totalFridayNightOnly() {
        assertEquals(money("115.00"),
                calc.total(money("100"), LocalDate.of(2026, 9, 11), LocalDate.of(2026, 9, 12)));
    }

    // Червоний на v1: BUG-021 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-50: одна ніч з неділі на понеділок = 100.00 (без надбавки)")
    void totalSundayNightOnly() {
        assertEquals(money("100.00"),
                calc.total(money("100"), LocalDate.of(2026, 9, 13), LocalDate.of(2026, 9, 14)));
    }

    // Червоний на v1: BUG-021 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-50: пн–сб (6 ночей) без знижки = 630.00")
    void totalSixNightsNoDiscount() {
        assertEquals(money("630.00"),
                calc.total(money("100"), LocalDate.of(2026, 9, 14), LocalDate.of(2026, 9, 20)));
    }

    @DisplayName("BL-50: на порозі 7 ночей і поруч вище — знижка 10 %")
    @ParameterizedTest(name = "BL-50: {0} ночей зі знижкою — {1}")
    @CsvSource({"7, 657.00", "8, 747.00"})
    void totalAtAndAboveSevenNightsDiscount(int nights, BigDecimal expected) {
        LocalDate checkIn = LocalDate.of(2026, 9, 14);
        assertEquals(expected, calc.total(money("100"), checkIn, checkIn.plusDays(nights)));
    }

    @Test
    @DisplayName("BL-50: 30 ночей (вересень) зі знижкою = 2808.00")
    void totalThirtyNights() {
        assertEquals(money("2808.00"),
                calc.total(money("100"), LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 1)));
    }

    @Test
    @DisplayName("BL-50: знижка округлюється HALF_UP: 243.45 × 0.90 = 219.105 → 219.11")
    void totalDiscountRoundsHalfUp() {
        assertEquals(money("219.11"),
                calc.total(money("33.35"), LocalDate.of(2026, 9, 14), LocalDate.of(2026, 9, 21)));
    }

    // Червоний на v1: BUG-021 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-40 + BL-50: перехід межі сезону 31.05→01.06: 100.00 + 130.00 = 230.00")
    void totalAcrossSeasonBoundary() {
        assertEquals(money("230.00"),
                calc.total(money("100"), LocalDate.of(2026, 5, 31), LocalDate.of(2026, 6, 2)));
    }

    // Червоний на v1: BUG-021 — звіт у GitHub Issues, мітка module
    @Test
    @DisplayName("BL-30 + BL-40: 19–21 грудня (сб, нд): 115.00 + 130.00 = 245.00")
    void totalSaturdayAndSundayInHighSeasonStart() {
        assertEquals(money("245.00"),
                calc.total(money("100"), LocalDate.of(2026, 12, 19), LocalDate.of(2026, 12, 21)));
    }

    @Test
    @DisplayName("BL-50: ніч виїзду не оплачується: інтервал [заїзд, виїзд)")
    void checkOutNightIsNotCharged() {
        assertEquals(money("100.00"),
                calc.total(money("100"), LocalDate.of(2026, 9, 9), LocalDate.of(2026, 9, 10)));
    }

    @DisplayName("BL-50: total із тарифом «{0}» — IllegalArgumentException")
    @ParameterizedTest(name = "BL-50: total із тарифом «{0}» — IllegalArgumentException")
    @CsvSource({"0", "-100"})
    void totalRejectsNonPositiveRate(BigDecimal base) {
        assertThrows(IllegalArgumentException.class,
                () -> calc.total(base, LocalDate.of(2026, 9, 9), LocalDate.of(2026, 9, 10)));
    }

    @Test
    @DisplayName("BL-50: виїзд = заїзд — IllegalArgumentException")
    void totalSameDayRejected() {
        LocalDate d = LocalDate.of(2026, 9, 9);
        assertThrows(IllegalArgumentException.class, () -> calc.total(money("100"), d, d));
    }
    @Test
    @DisplayName("BL-50: виїзд раніший за заїзд — IllegalArgumentException")
    void totalReversedDatesRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.total(money("100"), LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 9)));
    }

    @Test
    @DisplayName("BL-50: результат має рівно 2 знаки після коми")
    void totalHasTwoDecimalPlaces() {
        BigDecimal t = calc.total(money("100"), LocalDate.of(2026, 9, 15), LocalDate.of(2026, 9, 16));
        assertAll("BL-50: значення суми та грошова шкала",
                () -> assertEquals(2, t.scale()),
                () -> assertEquals(0, money("100").compareTo(t)));
    }
}
