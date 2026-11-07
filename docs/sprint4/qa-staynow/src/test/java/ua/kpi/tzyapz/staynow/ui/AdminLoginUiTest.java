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

/** WebDriver-сценарії входу адміністратора (TC-034, TC-035, TC-036 зі спринта 3). */
@Tag("ui")
@DisplayName("UI: вхід до адмінпанелі")
class AdminLoginUiTest extends UiTestBase {

    static final By LOGIN_HEADING = By.xpath("//*[self::h1 or self::h2 or self::h3][normalize-space()='Login']");
    private static final By USERNAME = By.cssSelector("input[placeholder='Enter username']");
    private static final By PASSWORD = By.cssSelector("input[type='password']");
    private static final By LOGIN_BUTTON = By.xpath("//button[normalize-space()='Login']");
    private static final By LOGOUT = By.xpath("//*[self::a or self::button][normalize-space()='Logout']");
    private static final By FRONT_PAGE = By.xpath("//a[normalize-space()='Front Page']");
    private static final By ERROR_MARKERS = By.cssSelector(
            ".alert, .is-invalid, [role='alert'], [class*='error'], [class*='invalid']");

    private void login(String user, String password) {
        open("/admin", LOGIN_HEADING);
        type(USERNAME, user);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    @Test
    @DisplayName("TC-034 (F-80, S-10): вхід admin/password відкриває керування номерами; облікові дані не в URL")
    void validLoginOpensRoomManagement() {
        login("admin", "password");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOGIN_HEADING));

        assertAll("Після входу",
                () -> assertTrue(driver.findElements(LOGIN_HEADING).isEmpty(), "Форма входу лишилась на екрані"),
                () -> assertTrue(driver.findElement(LOGOUT).isDisplayed(), "Кнопки «Logout» немає"),
                () -> assertFalse(driver.getCurrentUrl().contains("password"), "Пароль потрапив в адресу"),
                () -> assertFalse(driver.getCurrentUrl().contains("?"), "Параметри в адресі: " + driver.getCurrentUrl()));
    }

    @Test
    @DisplayName("TC-035 (F-80, GUI-70): неправильний пароль — доступу немає, сторінка входу лишається")
    void wrongPasswordKeepsLoginPage() {
        login("admin", "wrongpass");

        // Чекаємо фактичної видимої відповіді сервера; її відсутність завершує тест тайм-аутом.
        wait.until(d -> d.findElements(ERROR_MARKERS).stream().anyMatch(e -> e.isDisplayed()));

        assertAll("Після невдалого входу",
                () -> assertTrue(driver.findElement(LOGIN_HEADING).isDisplayed(), "Сторінку входу не показано"),
                () -> assertFalse(bodyText().contains("Room #"), "Показано керування номерами — доступ надано"),
                () -> assertEquals(1, driver.findElements(LOGIN_BUTTON).size(), "Кнопки «Login» немає"));
    }

    @Test
    @DisplayName("TC-036 (GUI-70): сторінка входу має заголовок, підписи, підказки, замасковане поле пароля і шапку")
    void loginPageLayout() {
        open("/admin", LOGIN_HEADING);
        type(PASSWORD, "p1");

        assertAll("Сторінка входу",
                () -> assertTrue(driver.findElement(LOGIN_HEADING).isDisplayed()),
                () -> assertEquals("Enter username", driver.findElement(USERNAME).getAttribute("placeholder")),
                () -> assertEquals("Password", driver.findElement(PASSWORD).getAttribute("placeholder")),
                () -> assertEquals("password", driver.findElement(PASSWORD).getAttribute("type")),
                () -> assertTrue(driver.findElement(LOGIN_BUTTON).isDisplayed(), "Кнопки «Login» немає"),
                () -> assertTrue(driver.findElement(FRONT_PAGE).isDisplayed(), "Посилання «Front Page» немає"),
                () -> assertTrue(driver.findElement(LOGOUT).isDisplayed(), "Кнопки «Logout» немає"));
    }
}
