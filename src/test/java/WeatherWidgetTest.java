import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class WeatherWidgetTest {

    public void AcceptCookies(){
        try {
            WebElement inputButton = $(By.className("cmp-button_button"));
            inputButton.click();
            inputButton.click();
        } catch  (NoSuchElementException e){
            e.printStackTrace();
        }
    }

    public void settingLocationToKatowice(){
        $(By.name("locationSearch")).setValue("Katowice");
        WebElement dynamicElement = (new WebDriverWait(getWebDriver(),10)).until(ExpectedConditions.elementToBeClickable($("div.autocomplete-suggestions").$("div.autocomplete-suggestion")));
        dynamicElement.click();
    }

    @Before
    public void prepareSite(){
        open("https://pogoda.onet.pl");
        AcceptCookies();
        $(By.name("locationSearch")).waitUntil(visible,3000);
        settingLocationToKatowice();
    }


    @Test
    public void shouldCheckIfLocationIsKatowice() {
        $(".mainName ").shouldHave(text("Pogoda Katowice"));
    }

    @Test
    public void shouldCheckIfLocationChangedToSiemianowice() {
        $(By.name("locationSearch")).waitUntil(visible,3000).setValue("Siemianowice");
        WebElement dynamicElement = (new WebDriverWait(getWebDriver(),10)).until(ExpectedConditions.elementToBeClickable($("div.autocomplete-suggestions").$("div.autocomplete-suggestion")));
        dynamicElement.click();
        $(".mainName ").shouldHave(text("Pogoda Siemianowice"));
    }

    @Test
    public void shouldCheckIfTempInWidgetIsEqualToTempOnActualTimeDiv() {
        int tempInWidget = Integer.parseInt($(".temp").getText().substring(0,2));
        int tempOnActualTimeDiv = Integer.parseInt($(By.className("swiper-slide-active")).$("div.temp").getText().substring(0,2));
        Assert.assertEquals(tempInWidget, tempOnActualTimeDiv);
    }

    @Test
    public void shouldCheckIfAirQualityIsGood(){
        $(By.className("pollutionIconDesc")).shouldHave(text("DOBRA"));
    }

    @Test
    public void shouldCheckIfAirQualityImageIsLoaded(){
        $(By.className("mood_1")).shouldBe(visible);
    }

    @Test
    public void shouldCheckIfLongtermTitleChanged(){
        $(By.className("longTermWeather")).click();
        $(By.className("seoTitle")).shouldHave(text("DŁUGOTERMINOWA"));
    }

    @After
    public void closingSite(){
        closeWebDriver();
    }
}
