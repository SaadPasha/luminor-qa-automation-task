package ui.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class FinancialReportsPage {

    private final SelenideElement pageHeading = $$(By.tagName("h2")).findBy(exactText("Financial reports"));
    private final SelenideElement section2026Title = $$(By.className("accordion__title")).findBy(exactText("2026"));
    private final SelenideElement section2026 = section2026Title.parent();
    private final SelenideElement reportLink = section2026.$(By.tagName("a"));

    public FinancialReportsPage verifyPageIsOpen() {
        pageHeading.shouldBe(visible);
        return this;
    }

    public FinancialReportsPage verify2026SectionIsOpen() {
        section2026Title.shouldBe(visible).shouldHave(attribute("aria-expanded", "true"));
        return this;
    }

    public FinancialReportsPage verifyReportLinkIsPresent() {
        reportLink.shouldBe(visible).shouldHave(attribute("href"));
        return new FinancialReportsPage();
    }
}