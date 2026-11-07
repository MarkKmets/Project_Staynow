package ua.kpi.tzyapz.staynow.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Спільна основа UI-тестів: кожен тест сам відкриває й закриває браузер (незалежний життєвий цикл),
 * усі очікування — явні (WebDriverWait), без Thread.sleep.
 * Браузер видно, якщо запустити з -Dheadless=false (для демо).
 */
abstract class UiTestBase {

    static final String BASE_URL = "https://automationintesting.online";
    static final Duration TIMEOUT = Duration.ofSeconds(15);

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (!"false".equals(System.getProperty("headless"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1366,900", "--lang=en-US");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Відкриває сторінку й чекає, поки з'явиться вказаний елемент. */
    protected void open(String path, By readyLocator) {
        driver.get(BASE_URL + path);
        wait.until(ExpectedConditions.visibilityOfElementLocated(readyLocator));
    }

    /** Прокручує елемент у центр вікна (щоб липка шапка не перекривала) і натискає. */
    protected void click(By locator) {
        wait.until(d -> {
            try {
                WebElement el = d.findElement(locator);
                ((JavascriptExecutor) d).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', behavior: 'instant'});", el);
                if (!el.isDisplayed() || !el.isEnabled()) {
                    return false;
                }
                el.click();
                return true;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                return false;
            }
        });
    }

    protected void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    protected String bodyText() {
        return driver.findElement(By.tagName("body")).getText();
    }
}
