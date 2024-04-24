package tests.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import managers.DriverManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.time.Duration;

import static java.lang.System.getProperty;

public class FruitSteps {

    DriverManager driverManager = DriverManager.getDriverManager();
    WebDriver driver = null;

    @Before
    public void before() {
        //driverManager.initRemoteDriver();
        driver = driverManager.getDriver();

        // driver.get("http://149.154.71.152:8080/food");

        WebElement tittleProductsList = driver.findElement(By.xpath("//h5"));
        Assertions.assertEquals("Список товаров", tittleProductsList.getText(), "Не перешли на страницу");
    }

    @И("выполнено нажатие на {string}")
    public void выполнено_нажатие_на(String element) {
        if (element.equals("Добавить")) {
            WebElement additionButton = driver.findElement(By.xpath("//button[@data-target = '#editModal']"));
            additionButton.click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            WebElement tittleAdditionForm = null;
            try {
                tittleAdditionForm = driver.findElement(By.xpath("//div[@class = 'modal-backdrop fade show']"));
            } catch (Exception e) {

            }

            if (tittleAdditionForm == null) {
                Assertions.assertFalse(true, "Форма добавления не открылась");
            }
        }
        if (element.equals("Сохранить")) {
            WebElement saveButton = driver.findElement(By.xpath("//button[@id = 'save']"));
            saveButton.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            WebElement bananaRecording = null;
            try {

                bananaRecording = driver.findElement(By.xpath("//tr[td= 'Банан' and td = 'Фрукт' and td ='true']"));
            } catch (Exception e) {

            }
            if (bananaRecording == null) {
                Assertions.assertFalse(true, "Запись не найдена");
            }
        }
        if (element.equals("Песочница")) {
            WebElement navBar = driver.findElement(By.xpath("//a[@id = 'navbarDropdown']"));
            navBar.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (element.equals("Сброс данных")) {
            WebElement resetButton = driver.findElement(By.xpath("//a[@id = 'reset']"));
            resetButton.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            WebElement bananaRecording = null;
            if (bananaRecording != null) {
                try {
                    Assertions.assertFalse(bananaRecording.isDisplayed(), "Запись не была удалена");
                } catch (Exception e) {

                }
            }
        }
    }

    @И("поле {string} заполняется значением {string}")
    public void поле_заполняется_значением(String string, String string2) {
        WebElement nameInputForm = driver.findElement(By.xpath("//input[@id = 'name']"));
        nameInputForm.click();
        nameInputForm.sendKeys("Банан");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("Банан", nameInputForm.getDomProperty("value"), "Форма наименования не заполнилась");
    }

    @И("поле {string} видимо")
    public void поле_видимо(String string) {
        if (string.equals("Наименование")) {
            driver.findElement(By.xpath("//input[@id = 'name']"));
        }
        if (string.equals("Тип")) {
            driver.findElement(By.xpath("//select[@id = 'type']"));
        }
    }

    @И("в поле {string} выбирается значение {string}")
    public void в_поле_выбирается_значение(String string, String string3) {
        WebElement typeInputForm = driver.findElement(By.xpath("//select[@id = 'type']"));
        typeInputForm.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement fruitOption = driver.findElement(By.xpath("//option[@value = 'FRUIT']"));
        fruitOption.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("FRUIT", typeInputForm.getDomProperty("value"), "Некорректный тип товара");
    }

    @И("чекбокс {string} выделен")
    public void чекбокс_выделен(String element) {
        WebElement checkboxExotic = driver.findElement(By.xpath("//input[@id = 'exotic']"));
        checkboxExotic.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("true", checkboxExotic.getDomProperty("checked"), "Чекбокс не выставлен");
    }

    @After
    public void after() {

        driver.close();
    }
}