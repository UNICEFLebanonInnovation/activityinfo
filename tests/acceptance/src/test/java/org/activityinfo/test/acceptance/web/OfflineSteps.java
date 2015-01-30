package org.activityinfo.test.acceptance.web;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.activityinfo.test.harness.ScreenShotLogger;
import org.activityinfo.test.pageobject.api.PageBinder;
import org.activityinfo.test.pageobject.web.ApplicationPage;
import org.activityinfo.test.pageobject.web.OfflineMode;
import org.activityinfo.test.webdriver.BrowserProfile;
import org.activityinfo.test.webdriver.BrowserVendor;
import org.activityinfo.test.webdriver.WebDriverSession;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class OfflineSteps {

    @Inject
    private WebDriverSession session;

    @Inject
    private PageBinder pages;


    @Inject
    private ScreenShotLogger logger;


    @Given("^my browser supports offline mode$")
    public void my_browser_supports_offline_mode() throws Throwable {
        session.start(BrowserVendor.CHROME);
    }

    @Given("^offline mode is not enabled$")
    public void offline_mode_is_not_enabled() throws Throwable {
        ApplicationPage page = pages.assertIsOpen(ApplicationPage.class);

        assertThat(page.getOfflineMode(), equalTo(OfflineMode.ONLINE));
    }


    @When("^I enable offline mode$")
    public void I_enable_offline_mode() throws Throwable {
        pages.assertIsOpen(ApplicationPage.class)
                .openSettingsMenu()
                .enableOfflineMode();

        logger.snapshot();
    }

    @Then("^I should be working offline$")
    public void I_should_be_working_offline() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^offline mode should be enabled$")
    public void offline_mode_should_be_enabled() throws Throwable {
        ApplicationPage page = pages.assertIsOpen(ApplicationPage.class);
        page.assertOfflineModeLoads();
    }
}
