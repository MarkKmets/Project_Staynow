package ua.kpi.tzyapz.staynow.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** WebDriver-сценарії головної сторінки (TC-001, TC-004 зі спринта 3). */
@Tag("ui")
@DisplayName("UI: головна сторінка")
class HomePageUiTest extends UiTestBase {

    private static final By HEADING = By.tagName("h1");
    private static final By BOOK_NOW = By.xpath("//a[normalize-space()='Book Now']");
    private static final By MENU_ITEMS = By.cssSelector("nav a.nav-link");

    @Test
    @DisplayName("TC-001 (F-10): назва, кнопка «Book Now» і меню з шести пунктів")
    void homePageShowsTitleBookNowAndSixMenuItems() {
        open("/", HEADING);

        List<String> menu = driver.findElements(MENU_ITEMS).stream().map(WebElement::getText).toList();

        assertAll("Головна сторінка",
                () -> assertFalse(driver.findElement(HEADING).getText().isBlank(), "Назва готелю не показана"),
                () -> assertTrue(driver.findElement(BOOK_NOW).isDisplayed(), "Кнопки «Book Now» немає"),
                () -> assertEquals(List.of("Rooms", "Booking", "Amenities", "Location", "Contact", "Admin"), menu));
    }

    @Test
    @DisplayName("TC-004 (F-10): пункт меню «Admin» відкриває сторінку входу за адресою /admin")
    void adminMenuOpensLoginPage() {
        open("/", HEADING);

        click(By.xpath("//nav//a[normalize-space()='Admin']"));
        wait.until(ExpectedConditions.urlToBe(BASE_URL + "/admin"));

        assertAll("Сторінка входу",
                () -> assertEquals(BASE_URL + "/admin", driver.getCurrentUrl()),
                () -> assertTrue(driver.findElement(AdminLoginUiTest.LOGIN_HEADING).isDisplayed(),
                        "Заголовок «Login» не показано"));
    }
}
