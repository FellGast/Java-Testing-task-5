package tests.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class VegetableSteps {
    private WebDriver driver;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        String baseUrl = "http://localhost:8080/food";
        driver.get(baseUrl);

        WebElement tittleProductsList = driver.findElement(By.xpath("//h5"));
        Assertions.assertEquals("Список товаров", tittleProductsList.getText(), "Не перешли на страницу");


    }


    @И("выполнено нажатие {string}")
    public void выполнено_нажатие(String element) {
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

                bananaRecording = driver.findElement(By.xpath("//tr[td= 'Картофель' and td = 'Овощ' and td ='false']"));
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
            WebElement bananaRecording = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (bananaRecording != null) {
                try {
                    Assertions.assertFalse(bananaRecording.isDisplayed(), "Запись не была удалена");
                } catch (Exception e) {

                }
            }
        }
    }

    @И("поле {string} заполняется текстом {string}")
    public void поле_заполняется_текстом(String string, String string2) {
        WebElement nameInputForm = driver.findElement(By.xpath("//input[@id = 'name']"));
        nameInputForm.click();
        nameInputForm.sendKeys("Картофель");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("Картофель", nameInputForm.getDomProperty("value"), "Форма наименования не заполнилась");
    }

    @И("поле {string} видимое")
    public void поле_видимое(String string) {
        if (string.equals("Наименование")) {
            driver.findElement(By.xpath("//input[@id = 'name']"));
        }
        if (string.equals("Тип")) {
            driver.findElement(By.xpath("//select[@id = 'type']"));
        }
    }

    @И("в поле {string} выбирается {string}")
    public void в_поле_выбирается(String string, String string3) {
        WebElement typeInputForm = driver.findElement(By.xpath("//select[@id = 'type']"));
        typeInputForm.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement fruitOption = driver.findElement(By.xpath("//option[@value = 'VEGETABLE']"));
        fruitOption.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("VEGETABLE", typeInputForm.getDomProperty("value"), "Некорректный тип товара");
    }

    @After
    public void after() {
        driver.close();
    }

}
