package ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseUiTest {

    @BeforeAll
    static void configureSelenide() {
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserSize = "1920x1080";
        Configuration.headless =
                Boolean.parseBoolean(
                        System.getProperty("headless", "false")
                );

        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 30_000;

        SelenideLogger.addListener(
                "AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    @AfterAll
    static void removeSelenideListener() {
        SelenideLogger.removeListener("AllureSelenide");
    }
}