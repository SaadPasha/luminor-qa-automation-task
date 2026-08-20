package ui.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ui.BaseUiTest;
import ui.pages.FinancialReportsPage;
import ui.pages.HomePage;

import static io.qameta.allure.Allure.step;

class FinancialReportsTest extends BaseUiTest {

    private final HomePage homePage = new HomePage();
//    private final FinancialReportsPage financialReportsPage = new FinancialReportsPage();

    @Test
    @Tag("regression")
    @Feature("Luminor website navigation")
    @Story("Financial reports on Internet Bank Content Site")
    @DisplayName("2026 financial report is available from the main menu")
    @Description("""
            Verifies that a customer can navigate through the main menu
            to the Financial Reports page and that the expanded 2026
            section contains a report link.
            """)
    void shouldDisplay2026FinancialReport() {
        step(
                "Open Luminor LV homepage and reject cookies",
                () -> homePage
                        .openPage()
                        .rejectCookies()
        );

        step(
                "Open side menu using the hamburger icon",
                homePage::openSideMenu
        );

        step(
                "Open About Us list",
                homePage::openAboutUsList
        );

        FinancialReportsPage financialReportsPage = step(
                "Open Financial Reports",
                homePage::openFinancialReports
        );

        step(
                "Verify that the Financial Reports page is open",
                financialReportsPage::verifyPageIsOpen
        );

        step(
                "Verify that the 2026 section is open",
                financialReportsPage::verify2026SectionIsOpen
        );

        step(
                "Verify that a report link is present in the 2026 section",
                financialReportsPage::verifyReportLinkIsPresent
        );
    }
}