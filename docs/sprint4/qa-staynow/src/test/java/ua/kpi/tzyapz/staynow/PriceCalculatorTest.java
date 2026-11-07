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

}
