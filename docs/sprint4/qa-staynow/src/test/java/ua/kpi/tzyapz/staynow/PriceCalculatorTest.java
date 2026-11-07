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

}
