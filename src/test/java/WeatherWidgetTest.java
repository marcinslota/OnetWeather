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
        //WebDriverWait wait = new WebDriverWait(getWebDriver(),3);
        $(By.name("locationSearch")).waitUntil(visible,3000);
        settingLocationToKatowice();
    }

    @Test
    public void shouldCheckIfLocationIsKatowice() {
        $(".mainName ").shouldHave(text("Pogoda Katowice"));
    }

    @Test
    public void shouldCheckIfLocationIsChanged() {
        $(By.name("locationSearch")).setValue("Tychy");
        WebElement dynamicElement = (new WebDriverWait(getWebDriver(),10)).until(ExpectedConditions.elementToBeClickable($("div.autocomplete-suggestions").$("div.autocomplete-suggestion")));
        dynamicElement.click();
        $(".mainName ").shouldHave(text("Pogoda Tychy"));
    }

    @Test
    public void shouldCheckTemperatureInWidgetAndOnTodayDiv() {
        int temp = Integer.parseInt($(".temp").getText().substring(0,2));
        int temp2 = Integer.parseInt($(By.className("swiper-slide-active")).$("div.temp").getText().substring(0,2));
        Assert.assertEquals("Temperature " + temp + " in main widget should be equal to temperature " + temp2 + " in actual time.", temp, temp2);
    }

    @Test
    public void shouldCheckAirQuality(){
        $(By.className("pollutionIconDesc")).shouldHave(text("DOBRA"));
    }

    @Test
    public void shouldCheckIfImageLoaded(){
        $(By.className("mood_1")).shouldBe(visible);
    }

    @Test
    public void shouldCheckIfLongterm(){
        $(By.className("longTermWeather")).waitUntil(visible,3000).click();
        $(By.className("boxTitle")).shouldHave(text("DŁUGOTERMINOWA"));
    }

    @After
    public void closingSite(){
        closeWebDriver();
    }
}
