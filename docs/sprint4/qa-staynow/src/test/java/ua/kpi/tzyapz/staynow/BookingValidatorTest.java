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

}
