package ui.pages;

import com.codeborne.selenide.SelenideElement;
import config.TestConfig;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    private final SelenideElement cookieRejectButton = $(By.id("onetrust-reject-all-handler"));
    private final SelenideElement cookieAcceptButton = $(By.id("onetrust-accept-btn-handler"));
    private final SelenideElement sideMenuButton = $$(byAttribute("aria-label", "Site menu")).findBy(visible);
    private final SelenideElement subMenuOverlay = $(By.id("overlay"));
    private final SelenideElement aboutUsButton = $$(byAttribute("data-meta-sub-menu", "meta-sub-menu-353")).findBy(visible);
    private final SelenideElement aboutUsSubMenu = $(By.id("meta-sub-menu-353"));
    private final SelenideElement financialReportsLink = $(By.linkText("Financial Reports"));

    public HomePage openPage() {
        open(TestConfig.uiBaseUrl());
        return this;
    }

    public HomePage rejectCookies() {
        if (cookieRejectButton.is(visible, Duration.ofSeconds(5))) {
            cookieRejectButton.click();
        }
        return this;
    }

    // In case if we want to accept the cookies - otherwise better to reject
    public HomePage acceptCookies() {
        if (cookieAcceptButton.is(visible, Duration.ofSeconds(5))) {
            cookieAcceptButton.click();
        }
        return this;
    }

    public HomePage openSideMenu() {
        sideMenuButton.click();
        return this;
    }

    public HomePage openAboutUsList() {
        aboutUsButton.click();
        aboutUsSubMenu.shouldBe(visible);
        return this;
    }

    public FinancialReportsPage openFinancialReports() {
        financialReportsLink.shouldBe(visible).click();
        return new FinancialReportsPage();
    }

}