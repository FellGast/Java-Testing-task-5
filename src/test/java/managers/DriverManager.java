package managers;

import org.apache.commons.exec.OS;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.PropConst;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import static java.lang.System.getProperty;

/**
 * @author Arkadiy_Alaverdyan
 * Класс для управления веб драйвером
 */
public class DriverManager {

    /**
     * Переменна для хранения объекта веб-драйвера
     *
     * @see WebDriver
     */
    private WebDriver driver;


    /**
     * Переменна для хранения объекта DriverManager
     */
    private static DriverManager INSTANCE = null;


    /**
     * Менеджер properties
     *
     * @see TestPropManager#getTestPropManager()
     */
    private final TestPropManager props = TestPropManager.getTestPropManager();


    /**
     * Конструктор специально был объявлен как private (singleton паттерн)
     *
     * @see DriverManager#getDriverManager()
     */
    private DriverManager() {
    }

    /**
     * Метод ленивой инициализации DriverManager
     *
     * @return DriverManager - возвращает DriverManager
     */
    public static DriverManager getDriverManager() {
        if (INSTANCE == null) {
            INSTANCE = new DriverManager();
        }
        return INSTANCE;
    }

    /**
     * Метод ленивой инициализации веб драйвера
     *
     * @return WebDriver - возвращает веб драйвер
     */
    public WebDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }


    /**
     * Метод для закрытия сессии драйвера и браузера
     *
     * @see WebDriver#quit()
     */
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }


    /**
     * Метод инициализирующий веб драйвер
     */
    private void initDriver() {
        if(props.getProperty("type.driver").equals("remote")) {
            initRemoteDriver();
        }
        else {
            if (OS.isFamilyWindows()) {
                initDriverWindowsOsFamily();
            } else if (OS.isFamilyMac()) {
                initDriverMacOsFamily();
            } else if (OS.isFamilyUnix()) {
                initDriverUnixOsFamily();
            }
        }
    }

    /**
     * Метод инициализирующий веб драйвер под ОС семейства Windows
     */
    private void initDriverWindowsOsFamily() {
        initDriverAnyOsFamily(PropConst.PATH_GECKO_DRIVER_WINDOWS, PropConst.PATH_CHROME_DRIVER_WINDOWS);
    }


    /**
     * Метод инициализирующий веб драйвер под ОС семейства Mac
     */
    private void initDriverMacOsFamily() {
       initDriverAnyOsFamily(PropConst.PATH_GECKO_DRIVER_MAC, PropConst.PATH_CHROME_DRIVER_MAC);
    }

    /**
     * Метод инициализирующий веб драйвер под ОС семейства Unix
     */
    private void initDriverUnixOsFamily() {
       initDriverAnyOsFamily(PropConst.PATH_GECKO_DRIVER_UNIX, PropConst.PATH_CHROME_DRIVER_UNIX);
    }


    /**
     * Метод инициализирующий веб драйвер под любую ОС
     *
     * @param gecko  - переменная firefox из файла application.properties в классе {@link PropConst}
     * @param chrome - переменная chrome из файла application.properties в классе {@link PropConst}
     */

    private void initDriverAnyOsFamily(String gecko, String chrome) {
        switch (props.getProperty(PropConst.TYPE_BROWSER)) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", props.getProperty(gecko));
                driver = new FirefoxDriver();
                driver.manage().window().maximize();
                break;
            case "chrome":
                System.setProperty("webdriver.chrome.driver", props.getProperty(chrome));
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                break;
            default:
                Assertions.fail("Типа браузера '" + props.getProperty(PropConst.TYPE_BROWSER) + "' не существует во фреймворке");
        }
    }

    public void initRemoteDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "109.0");

        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", false
        ));
        try {
            driver = new RemoteWebDriver(
                    URI.create("http://149.154.71.152:4444/wd/hub").toURL(), capabilities
            );
            driver.manage().window().maximize();
            driver.get("http://149.154.71.152:8080/food");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
