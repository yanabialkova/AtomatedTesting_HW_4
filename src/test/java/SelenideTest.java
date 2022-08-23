import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.holdBrowserOpen;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class SelenideTest {

    public static String getLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru")));
    }

    @BeforeAll
    static void setUpAll () {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        holdBrowserOpen = true;
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    void shouldTest () {
        String date = getLocalDate(2);
        $("[data-test-id ='city'] input").setValue("Москва");
        $("[data-test-id ='date'] input").setValue(date);
        $("[data-test-id ='name'] input").setValue("Яна Петрова");
        $("[data-test-id ='phone'] input").setValue("+79999999999");
        $("[data-test-id ='agreement']").click();
        $$(By.className("button__text")).last().click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(10));
        $("[data-test-id=notification]").$x(".//div[@class='notification__title']").should(text("Успешно!"));
        $("[data-test-id=notification]").$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на " + date));
    }
}
