package ua.kpi.tzyapz.staynow.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** WebDriver-сценарії контактної форми (TC-024, TC-025, TC-027, TC-028 зі спринта 3). */
@Tag("ui")
@DisplayName("UI: контактна форма")
class ContactFormUiTest extends UiTestBase {

    private static final By NAME = By.id("name");
    private static final By EMAIL = By.id("email");
    private static final By PHONE = By.id("phone");
    private static final By SUBJECT = By.id("subject");
    private static final By MESSAGE = By.id("description");
    private static final By SUBMIT = By.xpath("//button[normalize-space()='Submit']");
    private static final By ERROR_MARKERS = By.cssSelector(
            ".alert, .is-invalid, [role='alert'], [class*='error'], [class*='invalid']");
    private static final String THANKS = "Thanks for getting in touch";
    private static final By THANKS_HEADING = By.xpath(
            "//h3[starts-with(normalize-space(.), 'Thanks for getting in touch')]");

    private void openContactForm() {
        open("/", NAME);
        click(By.xpath("//nav//a[normalize-space()='Contact']"));
    }

    private void fillAndSubmit(String name, String phone, String subject, String message) {
        type(NAME, name);
        type(EMAIL, "ostap.test@example.com");
        type(PHONE, phone);
        type(SUBJECT, subject);
        type(MESSAGE, message);
        click(SUBMIT);
    }

    @Test
    @DisplayName("TC-024 (F-70, GUI-85): коректні дані — форму замінено подякою з іменем і темою")
    void validFormShowsThanksWithNameAndSubject() {
        openContactForm();
        fillAndSubmit("Ostap Test", "01234567890", "BR question 01",
                "Question about booking: do you have a room for three nights in December?");

        wait.until(ExpectedConditions.visibilityOfElementLocated(THANKS_HEADING));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(NAME));

        String text = bodyText();
        assertAll("Подяка",
                () -> assertTrue(text.contains("Ostap Test"), "У подяці немає імені"),
                () -> assertTrue(text.contains("BR question 01"), "У подяці немає теми"),
                () -> assertTrue(driver.findElements(ERROR_MARKERS).stream().noneMatch(e -> e.isDisplayed()),
                        "Є повідомлення про помилку"));
    }

    @Test
    @DisplayName("TC-025 (F-70): мінімально допустимі довжини (Subject 5, Message 20, Phone 11) — повідомлення надіслано")
    void minimumLengthsAreAccepted() {
        openContactForm();
        fillAndSubmit("Ostap Test", "01234567890", "Abcde", "x".repeat(20));

        wait.until(ExpectedConditions.visibilityOfElementLocated(THANKS_HEADING));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(NAME));

        assertTrue(bodyText().contains("Ostap Test"), "У подяці немає імені");
    }

    @Test
    @DisplayName("TC-027 (F-70, GUI-60): порожня форма не надсилається, показано помилки")
    void emptyFormIsNotSubmitted() {
        openContactForm();
        click(SUBMIT);

        wait.until(d -> d.findElements(ERROR_MARKERS).stream().anyMatch(e -> e.isDisplayed()));

        assertAll("Порожня форма",
                () -> assertTrue(driver.findElement(NAME).isDisplayed(), "Форму замінено — повідомлення надіслано"),
                () -> assertFalse(bodyText().contains(THANKS), "Показано подяку"),
                () -> assertFalse(driver.findElements(ERROR_MARKERS).isEmpty(), "Помилок валідації не показано"));
    }

    @Test
    @DisplayName("TC-028 (F-70, GUI-60): Subject із 4 символів (межа мінус 1) не надсилається")
    void subjectOfFourCharactersIsRejected() {
        openContactForm();
        fillAndSubmit("Ostap Test", "01234567890", "Abcd", "x".repeat(20));

        wait.until(d -> d.findElements(ERROR_MARKERS).stream().anyMatch(e -> e.isDisplayed()));

        assertAll("Subject 4 символи",
                () -> assertTrue(driver.findElement(SUBJECT).isDisplayed(), "Форму замінено — повідомлення надіслано"),
                () -> assertFalse(bodyText().contains(THANKS), "Показано подяку"),
                () -> assertEquals("Abcd", driver.findElement(SUBJECT).getAttribute("value")));
    }
}
