package br.com.bsfraga.fwBaseWebApi.utils;

import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static br.com.bsfraga.fwBaseWebApi.core.DriverFactory.getDriver;


/**
 * This class provides methods that interacts with web pages to executes tests.
 *
 * @author bsfraga
 */
public class Utils {

    private StringBuilder param;
    private Timestamp timestamp;
    private DateFormat dateFormat;
    private Calendar actualDate;
    private LocalDateTime localDateTime;
    private DateTimeFormatter dateTimeFormatter;
    private Random random;

    /*****************************************************/
    //Variaveis globais (temporarias)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /*****************************************************/

    /**
     * This method takes a screenshot whenever a method successfully interact with
     * a specific element.
     *
     * @param message
     */
    public void logPass(String message) {
        Report.logPass(message, ScreenshotManager.capture(getDriver()));
    }

    /**
     * This method takes a screenshot whenever a method fails to interact with a
     * specific element.
     *
     * @param message
     */
    public void logFail(String message) {
        Report.logFail(message, ScreenshotManager.capture(getDriver()));
    }

    /**
     * This method return a valid CNPJ.
     *
     * @return String CNPJ
     */
    public String getCNPJ() {
        return Cnpj.getOneCNPJ();
    }

    /**
     * This method perform a click on an Element using a WebElement.
     *
     * @param webElement
     */
    public void clickWebElement(WebElement webElement) {
        try {
            webElement.click();
        } catch (Exception e) {
            e.printStackTrace();
            logFail("Não foi possivel utilizar o WebElement \"" + webElement + "\" para efetuar o click.");
        }
    }

    public void clearInput(By locator) {
        try {
            getDriver().findElement(locator).clear();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel focar e limpar o campo informado no locator\"" + locator + "\".");
        }
    }

    /**
     * This method perform a click Action by using the Action library.
     * It simulates the mouse cursor movement to interact with elements.
     *
     * @param locator
     */
    public void clickAction(By locator) {
        try {
            Actions actions = new Actions(getDriver());
            WebElement element = getDriver().findElement(locator);
            actions.moveToElement(element).click(element).click().perform();
            if (!element.isDisplayed()) {
                waitElementIsEnable(locator, 30);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logFail("Não foi possivel clicar no elemento utilizando Action");
        }
    }

    /**
     * This method get an element by it's content (String) and return a WebElement.
     *
     * @param text
     * @return WebElement element
     */
    public WebElement getElementByText(String text) {
        try {
            WebElement element = getDriver().findElement(By.xpath("//*[text()='" + text + "']"));
            return element;
        } catch (Exception e) {
            e.printStackTrace();
            logFail("Não foi possivel recuperar o elemento pelo texto. Texto: \"" + text + "\".");
            return null;
        }
    }

    /**
     * This method returns a random value inside a table select
     * (a table that looks like a select). It uses the McGyver Wisdom to retrieve the
     * data inside the table and insert into an Array.
     *
     * @param locator By
     * @return The WebElement from the random Element inside the array.
     */
    public WebElement selectRandomValueInsideSelect(By locator) {
        try {
            random = new Random();
            ArrayList<WebElement> elements = getElementsWithMcGyver(locator);
            ArrayList<WebElement> auxList = new ArrayList<>();
            for (WebElement element : elements) {
                auxList.add(element);
            }
            WebElement element = auxList.get(random.nextInt(auxList.size()));
            Report.log("Sucesso ao retornar valor selecionado dentro de um Select.");
            return element;
        } catch (Exception e) {
            e.printStackTrace();
            logFail("Não foi possivel retornar o valor selecionado dentro de um Select.");
            return null;
        }
    }

    /**
     * This method get all the Elements inside an specific locator using the McGyver wisdom.
     * To use this, you need to pass the Locator that contains
     * the WebElements that you want to recover.
     *
     * @param locator
     * @return List of WebElement
     */
    public ArrayList<WebElement> getElementsWithMcGyver(By locator) {
        try {
            return (ArrayList<WebElement>) (ArrayList) getDriver().findElements(locator);
        } catch (Exception e) {
            e.printStackTrace();
            logFail("Não foi possivel recuperar os elementos contidos no locator informado. Locator: \"" + locator + "\".");
            return null;
        }
    }

    /**
     * This methods uses the McGyver wisdom to get the text inside a bad element.
     *
     * @param locator
     * @return List of Strings with the names of
     */
    public List<String> getValueWithMcGyver(By locator) {
        try {
            List<WebElement> elements = getDriver().findElements(locator);
            List<String> mcGyverJob = null;
            for (int i = 0; i < elements.size(); i++) {
                mcGyverJob.add(getText(elements.get(i)));
            }
            return mcGyverJob;
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel obter o texto dos elementos contido no locator \"" + locator + "\".");
            return null;
        }
    }

    /**
     * This method colects the current URL whithin the driver instance.
     *
     * @return String currentURL
     */
    public String getURL() {
        String url = getDriver().getCurrentUrl();
        Report.log("URL atual obtida. Url: " + url);
        return url;
    }

    /**
     * This method return a named cookie within the driver instance.
     *
     * @param namedCookie
     * @return Cookie object.
     */
    public Cookie getCookieByName(String namedCookie) {
        Cookie cookie = getDriver().manage().getCookieNamed(namedCookie);
        Report.log("Cookie especifico obtido. Cookie: " + namedCookie);
        return cookie;
    }

    /**
     * This method returns all cookies within the driver instance.
     *
     * @return Collection (Set) with Cookies
     */
    public Set<Cookie> getAllCookies() {
        Set<Cookie> cookies = getDriver().manage().getCookies();
        Report.log("Cookies da página atual obtidos.");
        return cookies;
    }


    /**
     * This method makes the driver to move and focus to a specific element.
     *
     * @param locator
     */
    public void moveToElement(By locator) {
        try {
            Actions action = new Actions(getDriver());
            WebElement element = getDriver().findElement(locator);
            action.moveToElement(element).perform();
            Report.log("Ação de mover para um elemento efetuado. Locator: " + locator);
        } catch (Exception e) {
            e.printStackTrace();
            logFail("Não foi possivel mover mouse até o elemento.");
        }
    }

    /**
     * This method perform the mouse double click action.
     *
     * @param locator
     */
    public void performDoubleClick(By locator) {
        Actions action = new Actions(getDriver());
        WebElement element = getDriver().findElement(locator);
        action.doubleClick(element).perform();
        Report.log("Ação de double click efetuado. Locator: " + locator, ScreenshotManager.capture(getDriver()));
    }

    /**
     * This method generates a Random name using as tamplate String "Automação" + timestamp.
     *
     * @return String "Automação" + timestamp.
     */
    public String generateName() {
        try {
            param = new StringBuilder();
            timestamp = new Timestamp(System.currentTimeMillis());
            param.append("Automação ").append(timestamp);
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.log("Falha ao gerar um nome.");
            return null;
        }
    }

    /**
     * This method generates a Random name using as tamplate String "automacao" + timestamp + "@teste.com.br".
     *
     * @return String "automacao" + Calendar Time in Millis + @teste.com.br.
     */
    public String generateEmail() {
        try {
            param = new StringBuilder();
            param.append("automacao").append(Calendar.getInstance().getTimeInMillis()).append("@teste.com.br");
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar um e-mail.");
            return null;
        }
    }

    /**
     * This method generates a Random login using as tamplate String "testeAutomacao" + Calendar Time in Millis.
     *
     * @return String "testeAutomacao" + Calendar Time in Millis
     */
    public String generateLogin() {
        try {
            param = new StringBuilder();
            param.append("testeAutomacao").append(Calendar.getInstance().getTimeInMillis());
            Report.log("Texto inserido. Texto: " + param.toString().trim());
            return param.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar um login.");
            return null;
        }
    }

    /**
     * This method obtains the localDate and sum the informed value with the returned year value.
     *
     * @param value Integer
     * @return localDate (dd/MM/yyyy + value) as String.
     */
    public String localDatePlusYears(Integer value) {
        try {
            if (value == null) {
                System.err.println("Invoked localDatePlusYears but no value has been informed.");
                return null;
            }
            param = new StringBuilder();
            actualDate = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            actualDate.add(Calendar.YEAR, value);
            param.append(dateFormat.format(actualDate.getTime())).toString();
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar uma data \"dd/MM/yyyy +(" + value + ")\".");
            return null;
        }
    }

    /**
     * This method obtains the localDate and sum the informed value with the returned month value.
     *
     * @param value Integer
     * @return localDate (dd/MM + value/yyyy) as String.
     */
    public String localDatePlusMonths(Integer value) {
        try {
            if (value == null) {
                System.err.println("Invoked localDatePlusMonths but no value has been informed.");
                return null;
            }
            param = new StringBuilder();
            actualDate = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            actualDate.add(Calendar.MONTH, value);
            param.append((dateFormat.format(actualDate.getTime()))).toString();
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar uma data \"dd/MM+ (" + value + ")/yyyy\".");
            return null;
        }
    }

    /**
     * This method obtains the localDate and sum the informed value with the returned day value.
     *
     * @param value Integer
     * @return localDate (dd + value/MM/yyyy) as String.
     */
    public String localDatePlusDays(Integer value) {
        try {
            if (value == null) {
                System.err.println("Invoked localDatePlusDays but no value has been informed.");
                return null;
            }
            param = new StringBuilder();
            actualDate = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            actualDate.add(Calendar.DAY_OF_MONTH, value);
            param.append((dateFormat.format(actualDate.getTime()))).toString();
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar data \"dd +(" + value + ")/MM/yyyy\".");
            return null;
        }
    }

    /**
     * This method obtains the localDate and subtract the informed value with the returned day value.
     *
     * @param value Integer
     * @return localDate (dd/MM/yyyy - value) as String.
     */
    public String localDateMinusYears(Integer value) {
        try {
            if (value == null) {
                System.err.println("Invoked localDateMinusYears but no value has been informed.");
                return null;
            }
            param = new StringBuilder();
            localDateTime = LocalDateTime.now();
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            param.append(localDateTime.minusYears(value).format(dateTimeFormatter)).toString();
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar uma data \"dd/MM/yyyy -(" + value + ")\".");
            return null;
        }
    }

    /**
     * This method obtains the localDate and subtract the informed value with the returned day value.
     *
     * @param value Integer
     * @return localDate (dd/MM - value/yyyy) as String.
     */
    public String localDateMinusMonths(Integer value) {
        try {
            if (value == null) {
                System.err.println("Invoked localDateMinusMonths but no value has been informed.");
                return null;
            }
            param = new StringBuilder();
            localDateTime = LocalDateTime.now();
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            param.append(localDateTime.minusMonths(value).format(dateTimeFormatter)).toString();
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar uma data \"dd/MM -(" + value + ")/yyyy\".");
            return null;
        }
    }

    /**
     * This method obtains the localDate and subtract the informed value with the returned day value.
     *
     * @param value Integer
     * @return localDate (dd - value/MM/yyyy) as String.
     */
    public String localDateMinusDays(Integer value) {
        try {
            if (value == null) {
                System.err.println("Invoked localDateMinusDays but no value has been informed.");
                return null;
            }
            param = new StringBuilder();
            localDateTime = LocalDateTime.now();
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            param.append(localDateTime.minusDays(value).format(dateTimeFormatter)).toString();
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar uma data \"dd -(" + value + ")/MM/yyyy\".");
            return null;
        }
    }

    /**
     * This method obtains the localDate from the machine.
     *
     * @return localDate (dd/MM/yyyy) as String.
     */
    public String localDate() {
        try {
            param = new StringBuilder();
            localDateTime = LocalDateTime.now();
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            param.append(localDateTime.format(dateTimeFormatter)).toString();
            Report.log("Texto inserido. Texto: " + param.toString());
            return param.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel obter a data atual.");
            return null;
        }
    }

    /**
     * This method generates a ranodm number based on the informed range.
     *
     * @param range Integer
     * @return Random number based on informed range as String.
     */
    public String generateRandomNumber(Integer range) {
        try {
            if (range == null) {
                System.err.println("Invoked generateRandomNumber but no value has been informed.");
                return null;
            }
            random = new Random();
            int randomNumber = random.nextInt(range);
            return String.valueOf(randomNumber);
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar um numero randomico.");
            return null;
        }
    }

    /**
     * This method generates a ranodm number based on the informed range(value).
     *
     * @param value Integer
     * @return Random number based on informed range as String.
     */
    public String generateRandomNumberWithZero(Integer value) {
        try {
            param = new StringBuilder();
            if (value == null) {
                System.err.println("Invoked generateRandomNumberWithZero but no value has been informed.");
            }
            random = new Random();
            int number = random.nextInt(value);
            if (number > 10000) {
                return String.valueOf(number);
            } else if (number > 1000) {
                param.append("0").append(number);
                return param.toString();
            } else if (number > 100) {
                param.append("00").append(number);
                return param.toString();
            } else if (number > 10) {
                param.append("000").append(number);
                return param.toString();
            } else if (number > 1) {
                param.append("0000").append(number);
                return param.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel gerar números randomicos com 0.");
            return null;
        }
        return null;
    }


    /**
     * Get the current local machine date.
     *
     * @return <pre>
     * String
     * "yyyy-MM-dd'T'HH:mm:ss"
     *         </pre>
     */
    public String localTime() {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String localDateTime = ldt.format(formatter);
        return localDateTime;
    }

    /**
     * Get the current local machine date and add the number of years informed by
     * the user.
     *
     * @param years = numero de anos que deseja adicionar;
     * @return <pre>
     * String
     * "yyyy-MM-dd'T'HH:mm:ss-03:00"
     *         </pre>
     */
    public String localTimePlusYears(long years) {
        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.plusYears(years);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String localDateTime = ldt.format(formatter).concat("-03:00");
        Report.log("Texto inserido. Texto: " + localDateTime);
        return localDateTime;
    }

    /**
     * Get the current local date and time and add minutes informed by the user.
     *
     * @param minutes = numero de minutos que deseja adicionar ao horario atual da
     *                maquina que executa isto.
     * @return <pre>
     * String
     * "yyyy-MM-dd'T'HH:mm:ss-03:00"
     *         </pre>
     */
    public String localTimeModifiedInMinutes(int minutes) {
        LocalDateTime ldtReturn = LocalDateTime.now().plusMinutes(minutes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return ldtReturn.format(formatter).concat("-03:00");
    }

    /**
     * This method provides the action to navigate by URL.
     *
     * @param url The URL that you want to navigate.
     */
    public void goToUrl(String url) {
        try {
            getDriver().navigate().to(url);
            Report.log("URL acessada. URL: +" + url, ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Falhou ao acessar a URL " + url, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to go to the URL: " + url + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * Send keys (Keys) to an element by it's locator.
     *
     * @param locator Locator from the element that you want to interact.
     * @param key     For example: Keys.ENTER
     */
    public void sendKeys(By locator, Keys key) {
        try {
            getDriver().findElement(locator).sendKeys(key);
            Report.log("Tecla pressionada. Tecla: " + key.toString());
        } catch (Exception e) {
            Report.logFail("Falhou ao simular tecla: " + key.toString());
            e.printStackTrace();
            Assert.fail("It wasn't possible to send keys to element object: " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * Send Keys (text) to an element by it's locator.
     *
     * @param locator Locator from the element that you want to interact.
     * @param text    Text that you want to send.
     */
    public void sendKeys(By locator, String text) {
        try {
            getDriver().findElement(locator).clear();
            getDriver().findElement(locator).sendKeys(text);
            Report.log("Texto inserido. Texto: " + text, ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Falhou ao inserir texto: " + text, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to send keys to element object: " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method send keys (text only). If the element you want to interact is
     * being displayed, enabled and the text you sent contains only characters from
     * A to Z (lower or upper case). It uses Regex to validate the text that will be
     * sent.
     *
     * @param element The element that you want to interact with.
     * @param text    The text you want to send.
     */
    public void sendKeysOnlyText(WebElement element, String text) {
        try {
            boolean result = !Pattern.matches("^[a-zA-Z ]+$", element.getAttribute("value"));
            if (result && element.isDisplayed() && element.isEnabled()) {
                this.highlight(element, true);
                element.clear();
                element.sendKeys(text);
                Report.log("Texto inserido. Texto: " + text, ScreenshotManager.capture(getDriver()));
            }
        } catch (Exception e) {
            Report.logFail("Falhou ao inserir texto: " + text, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to clear and send keys to element object: \n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method send keys (text only). If the element you want to interact have a
     * null or empty value [.getAttribute("value")], this will fail.
     *
     * @param element The element that you want to interact with.
     * @param text    The text you want to send.
     */
    public void sendKeysInput(WebElement element, String text) {
        try {
            if (element.getAttribute("value") == null || element.getAttribute("value") == "") {
                this.highlight(element, true);
                element.sendKeys(text);
                Report.log("Inserido texto no elemento sem \"value\". Texto: " + text, ScreenshotManager.capture(getDriver()));
            }
        } catch (Exception e) {
            Report.logFail("Falhou ao inserir texto" + text, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to clear and send keys to element object: \n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method returns the field value from a Locator [.getAttribute("value")].
     *
     * @param locator Locator from the element that you want to interact.
     * @return The value from a field Locator.
     */
    public String getFieldValue(By locator) {
        try {
            String text = getDriver().findElement(locator).getAttribute("value");
            Report.log("Parâmetro \"value\" do elemento obtido com sucesso: Value: " + text);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Falhou ao receber o atributo \"value\":\nLocator" + locator);
            return null;
        }
    }

    /**
     * This method executes a click on a radio button by it's locator.
     *
     * @param locator Radio button locator.
     */
    public void clickRadio(By locator) {
        try {
            getDriver().findElement(locator).click();
            Report.log("Radio selecionado. Locator: " + locator, ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Falhou ao clicar no elemento Radio.\nLocator:" + locator, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to click on the Radio button: " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method check if a specific radio button is marked.
     *
     * @param locator Radio button locator.
     * @return True if it's marked, False if it's not.
     */
    public boolean isRadioMarked(By locator) {
        try {
            boolean is = getDriver().findElement(locator).isSelected();
            Report.log("Radio verificado. Valor: " + is, ScreenshotManager.capture(getDriver()));
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Falhou ao verificar se o Elemento Radio está marcado.\nLocator: " + locator, ScreenshotManager.capture(getDriver()));
            return false;
        }
    }

    /**
     * This method executes a click on a check box by it's locator
     *
     * @param locator Check Box locator.
     */
    public void clickCheckBox(By locator) {
        try {
            getDriver().findElement(locator).click();
            Report.log("CheckBox selecionada. Locator: " + locator, ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Falhou ao clicar no checkBox.\nLocator: " + locator, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to click on the CheckBox: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method check if a check box is marked.
     *
     * @param locator Check box locator.
     * @return True if it's marked, False if it's not.
     */
    public boolean isCheckBoxMarked(By locator) {
        try {
            boolean is = getDriver().findElement(locator).isSelected();
            Report.log("CheckBox verificada. Valor: " + is);
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Falhou ao verificar se checkBox está marcado. Locator: " + locator);
            return false;
        }
    }

    /**
     * This method selects a Combo by it's locator.
     *
     * @param locator Combo locator.
     * @param text    The text contained into the Combo.
     */
    public void selectCombo(By locator, String text) {
        try {
            WebElement element = getDriver().findElement(locator);
            Select combo = new Select(element);
            combo.selectByVisibleText(text);
            Report.log("Combo selecionado por Texto. Texto: " + text, ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Falhou ao selecionar uma Combo pelo texto: " + text + ". Locator: " + locator, ScreenshotManager.capture(getDriver()));
            Assert.fail("It wasn't possible to select the combo: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method deselect a Combo by it's locator.
     *
     * @param locator Combo locator.
     * @param text    The text contained into the Combo.
     */
    public void deselectCombo(By locator, String text) {
        try {
            WebElement element = getDriver().findElement(locator);
            Select combo = new Select(element);
            combo.deselectByVisibleText(text);
            Report.log("Combo desmarcado. Locator: " + locator, ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Falhou ao desmarcar uma Combo pelo texto: " + text + ". Locator: " + locator, ScreenshotManager.capture(getDriver()));
            Assert.fail("It wasn't possible to deselect the combo: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method obtains the content from a Combo.
     *
     * @param locator Combo locator.
     * @return The text contained into the Combo.
     */
    public String getComboValue(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            Select combo = new Select(element);
            String comboFirstValue = combo.getFirstSelectedOption().getText();
            Report.log("Texto do primeiro elemento contido no Combo obtido. Texto: " + comboFirstValue);
            return comboFirstValue;
        } catch (Exception e) {
            Report.logFail("Falhou ao obter o texto do elemento do Combo. Locator: " + locator);
            Assert.fail("It wasn't possible to get the combo value: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
        return null;
    }

    /**
     * This method get all the values contained into a Select.
     *
     * @param locator Select locator.
     * @return A list with all values inside the Select.
     */
    public List<String> getSelectValues(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            Select combo = new Select(element);
            List<WebElement> allSelectedOptions = combo.getAllSelectedOptions();
            List<String> values = new ArrayList<String>();
            for (WebElement option : allSelectedOptions) {
                values.add(option.getText());
            }
            Report.log("Texto das opções contidas no Select obtidas. Opções: " + values.toString());
            return values;
        } catch (Exception e) {
            Report.logFail("Falhou ao recuperar os valores do Select: Locator: " + locator);
            Assert.fail("It wasn't possible to get the Select values: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
        return null;
    }

    /**
     * This methos obtains the number of the elements inside a Combo.
     *
     * @param locator Combo locator.
     * @return The size (int) of the combo.
     */
    public int getComboOptionsNumber(By locator) {
        List<WebElement> options = null;
        try {
            int comboSize;
            WebElement element = getDriver().findElement(locator);
            Select combo = new Select(element);
            options = combo.getOptions();
            comboSize = options.size();
            Report.log("Número de opções contidas no Combo obtidas: Número de opções: " + comboSize);
            return comboSize;
        } catch (Exception e) {
            Report.logFail("Falhou ao obter o numero total de opções de uma Combo. Locator: " + locator);
            Assert.fail("It wasn't possible to click on the CheckBox: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
        return 0;
    }

    /**
     * This method verifies if the Combo contains an option with a specific text.
     *
     * @param locator Combo locator.
     * @param text    Text that you want to check if is contained into the combo.
     * @return
     */
    public boolean verifyComboOption(By locator, String text) {
        try {
            WebElement element = getDriver().findElement(locator);
            Select combo = new Select(element);
            List<WebElement> options = combo.getOptions();
            for (WebElement option : options) {
                if (option.getText().equals(text)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Report.log("Falha ao verificar o valor da opção de uma Combo. Locator: " + locator);
            Assert.fail("It wasn't possible to verify the combo option: " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
        return false;
    }

    /**
     * This method executes the action of a click on a button.
     *
     * @param locator Button locator.
     */
    public void clickButton(By locator) {
        try {
            getDriver().findElement(locator).click();
            Report.log("Click efetuado. Locator: " + locator, ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Não foi possivel clicar no botão. Locator: " + locator, ScreenshotManager.capture(getDriver()));
            Assert.fail("It wasn't possible to click the button: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    public String getText(WebElement element) {
        try {
            String text = element.getText();
            Report.log("Texto obtido do WebElement. Texto:" + text);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel obter o texto do WebElement. WebElement: " + element.toString());
            return null;
        }
    }

    /**
     * This method obtains the text inside an element.
     *
     * @param locator Element locator.
     * @return Text contained into the element.
     */
    public String getText(By locator) {
        try {
            String text = getDriver().findElement(locator).getText();
            Report.log("Texto obtido. Texto: " + text);
            return text;
        } catch (Exception e) {
            Report.logFail("Não foi possivel obter o texto do elemento. Locator: " + locator);
            e.printStackTrace();
            Assert.fail("It wasn't possible to get the text from: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
        return null;
    }

    /**
     * This method obtains the text inside an element. Generally used when Selenium
     * can't perform the action.'
     *
     * @param locator
     * @return String inside the element.
     */
    public String getTextJS(By locator) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) getDriver();
            WebElement element = getDriver().findElement(locator);
            String text = (String) jse.executeScript("return arguments[0].text", element);
            Report.log("Texto obtido por comando JavaScript. Texto: " + text);
            return text;
        } catch (Exception e) {
            Report.logFail("Não foi possivel obter o texto do elemento utilizando JavaScript. Locator: " + locator);
            e.printStackTrace();
            Assert.fail("It wasn't possible to get the text from: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
            return null;
        }
    }

    /**
     * This method obtains the text inside an AlertBox.
     *
     * @return The text contained into the AlertBox.
     */
    public String getTextFromAlert() {
        try {
            Alert alert = getDriver().switchTo().alert();
            String value = alert.getText();
            Report.logPass("Texto obtido do Alert. Texto: " + value);
            return value;
        } catch (Exception e) {
            Report.logFail("Não foi possivel obter o texto contido no Alert.");
            e.printStackTrace();
            Assert.fail("It wasn't possible to click the button: " + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
            return null;
        }
    }

    /**
     * This method get the text from a AlertBox and then click on the Accept/Ok
     * button.
     *
     * @return The text contained into the AlertBox.
     */
    public String getTextFromAlertAndAccept() {
        try {
            Alert alert = getDriver().switchTo().alert();
            String value = alert.getText();
            alert.accept();
            Report.log("Texto obtido do Alert e click Positivo efetuado. Texto: " + value);
            return value;
        } catch (Exception e) {
            Report.logFail("Não foi possivel obter o texto contido no Alert e clicar em Aceitar.");
            e.printStackTrace();
            Assert.fail("It wasn't possible to get the text from the Alert box and click Accept "
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
        return null;

    }

    /**
     * This method obtains the text inside a AlertBox and then click on Deny/Cancel
     * button.
     *
     * @return The text contained into the AlertBox.
     */
    public String getTextFromAlertAndDeny() {
        try {
            Alert alert = getDriver().switchTo().alert();
            String value = alert.getText();
            alert.dismiss();
            Report.log("Texto obtido do Alert e click Negativo efetuado. Texto: " + value);
            return value;
        } catch (Exception e) {
            Report.logFail("Não foi possivel obter o texto contido no Alert e clicar em Cancelar.");
            e.printStackTrace();
            Assert.fail("It wasn't possible to get the text from the Alert box and click Deny: "
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
        return null;

    }

    /**
     * This method send keys (text) to an AlertBox.
     *
     * @param text Text that will be sent to the AlertBox.
     */
    public void sendKeysOnAlert(String text) {
        try {
            Alert alert = getDriver().switchTo().alert();
            alert.sendKeys(text);
            Report.log("Texto inserido no Alert. Texto: " + text, ScreenshotManager.capture(getDriver()));
            alert.accept();
        } catch (Exception e) {
            Report.logFail("Não foi possivel enviar texto no Alert.", ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to send keys on Alert box: " + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method changes the driver veiw to a IFrame View.
     *
     * @param locator By
     */
    public void getIntoFrame(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            getDriver().switchTo().frame(element);
            Report.log("View trocada para o IFrame.", ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            e.printStackTrace();
            Report.logFail("Não foi possivel trocar view para IFrame.");
        }
    }

    /**
     * This method changes the driver focus to a Frame View.
     *
     * @param frame_id Frame id.
     */
    public void getIntoFrame(String frame_id) {
        try {
            getDriver().switchTo().frame(frame_id);
            Report.log("View trocada para o IFrame.", ScreenshotManager.capture(getDriver()));

        } catch (Exception e) {
            Report.log("Não foi possivel trocar a view para o IFrame.", ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to get into a the frame: " + frame_id + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method changes driver focus to out of the Frame View.
     */
    public void getOutFrame() {
        try {
            getDriver().switchTo().defaultContent();
            Report.log("Saiu da view do IFrame.", ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Não foi possivel sair da view do IFrame", ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to out of the frame:" + "\n-----==========-----\nMessage: " + e.getMessage()
                    + "\n-----==========-----");
        }
    }

    /**
     * This method change the driver focus to a specific window.
     *
     * @param window_id The if of the window.
     */
    public void switchWindow(String window_id) {
        try {
            getDriver().switchTo().window(window_id);
            Report.log("Troca de janela efetuada.", ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Não foi possivel trocar de Janela.", ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to switch to window: " + window_id + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method execute a JavaScript command.
     *
     * @param command The JavaScript command.
     * @param args    The arguments that the command will recieve.
     * @return
     */
    public Object executeJS(String command, Object... args) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            return executor.executeScript(command, args);
        } catch (Exception e) {
            Report.logFail("Não foi possivel executar o script JavaScript. Command: " + command + "Args: " + args, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to execute the JavaScript command: " + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
        return null;
    }

    /**
     * This method scroll the page to a specific locator.
     *
     * @param locator Element locator.
     */
    public void scrollToElement(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);

            if (element != null) {
                JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                executor.executeScript("arguments[0].scrollIntoView(true)", element);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("It wasn't possible to scroll to the element: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }

    }

    /**
     * This method scroll the page to a specific web element.
     *
     * @param element WebElement.
     */
    public void scrollToElement(WebElement element) {
        try {
            if (element != null) {
                JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                executor.executeScript("arguments[0].scrollIntoView(true)", element);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("It wasn't possible to scroll to the element locator element: "
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method highlights an element.
     *
     * @param locator Element locator.
     * @param arg     (boolean) If True highlight with green color, if False
     *                highlight with red color.
     */
    public void highlight(By locator, boolean arg) {
        try {
            WebElement element = getDriver().findElement(locator);
            String color = arg ? "outline: 4px solid #00FF00;" : "outline: 4px solid #ff0000;";
            this.scrollToElement(locator);
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].setAttribute('style, arguments[1]');", element, color);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("It wasn't possible to highlight the element: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method highlight an element.
     *
     * @param element WebElement.
     * @param arg     (boolean) If True highlight with green color, if False
     *                highlight with red color.
     */
    public void highlight(WebElement element, boolean arg) {
        try {
            String color = arg ? "outline: 4px solid #00FF00;" : "outline: 4px solid #ff0000;";
            this.scrollToElement(element);
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, color);
        } catch (Exception e) {
            Assert.fail("It wasn't possible to highlight the element locator element: "
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method focus on an element using JavaScript command.
     *
     * @param locator Element locator.
     */
    public void focusJS(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].focus();", element);
            Report.log("Elemento focado. Locator: " + locator);
        } catch (Exception e) {
            Report.logFail("Não foi possivel focar em um elemnto utilizando JavaScript. Locator: " + locator);
            e.printStackTrace();
            Assert.fail("It wasn't possible to focus on the element: " + locator + "\n-----==========-----\nMessage: "
                    + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method execute a click using javaScript command using an element Locator.
     *
     * @param locator Element locator.
     */
    public void clickJS(By locator) {
        try {
            waitElementIsVisible(locator, 10);
            WebElement element = getDriver().findElement(locator);
            focusJS(locator);
            this.highlight(element, true);
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].click();", element);
            Report.log("Click efetuado. Locator: " + locator, ScreenshotManager.capture(getDriver()));
            Thread.sleep(1000);
        } catch (Exception e) {
            Report.logFail("Não foi possivel executar a ação de click utilizando JavaScript.", ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to click using JavaScript on the element: " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method execute a click using JavaScript command using a WebElement.
     *
     * @param element WebElement
     */
    public void clickJS(WebElement element) {
        try {
            waitElementIsVisible(element, 10);
            this.highlight(element, true);
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].click();", element);
            Report.log("Click efetuado pelo WebElement." + ScreenshotManager.capture(getDriver()));
        } catch (Exception e) {
            Report.logFail("Não foi possivel executar a ação de click utilizando JavaScript.", ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to click using JavaScript on the element: "
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method executes a double click using JavaScript command.
     *
     * @param locator Element locator.
     */
    public void doubleClickJS(By locator) {
        try {
            waitElementIsVisible(locator, 10);
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].doubleclick();", element);
        } catch (Exception e) {
            Report.logFail("Não foi possivel efetuar o DoubleClick utilizando JavaScript. Locator: " + locator, ScreenshotManager.capture(getDriver()));
            e.printStackTrace();
            Assert.fail("It wasn't possible to double click using JavaScript on the element: " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method executes a click on a list.
     *
     * @param locator Element locator.
     * @param text    Text contained into the element on the list.
     */
    public void clickOnListJS(By locator, String text) {
        try {
            List<WebElement> elements = getDriver().findElements(locator);
            for (WebElement webElement : elements) {
                if (webElement.getText().contains(text)) {
                    JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                    executor.executeScript("arguments[0].click();", webElement);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("It wasn't possible to click on the list of elements using JavaScript " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * Send keys (text) to a specific element using JavaScript command.
     *
     * @param locator Element locator.
     * @param text    The text that will be sent.
     */
    public void sendKeysJS(By locator, String text) {
        try {
            waitElementIsVisible(locator, 10);
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].value = '" + text + "';", element);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("It wasn't possible to send keys on the element using JavaScript " + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method obtains the value of an element.
     *
     * @param locator Element locator.
     * @return The value of an element.
     */
    public String getElementValue(By locator) {
        return getDriver().findElement(locator).getAttribute("value");
    }

    /**
     * This method makes the driver sleep to wait an specific element to be visible.
     *
     * @param locator Element locator.
     * @param timeout Time (long). Recommended: 30.
     * @return True if the element is visible, False if it's not.
     * @throws Exception
     */
    public boolean waitElementIsVisible(By locator, int timeout) throws InterruptedException {
        try {
            int count = 0;
            do {
                WebElement element = getDriver().findElement(locator);
                if (element.isDisplayed()) {
                    return true;
                }
                Thread.sleep(250);
                count++;
            } while (count < timeout * 4);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Erro. O elemento informado não está visível ou demorou muito para ficar visível.");
        }
        return false;
    }

    /**
     * This method makes the driver sleep t wait an specific element to be visible.
     *
     * @param element WebElement
     * @param timeout Time (long). Recomended: 30.
     * @return True if the element if visible, False if it's not.
     * @throws InterruptedException
     */
    public boolean waitElementIsVisible(WebElement element, int timeout) throws InterruptedException {
        try {
            int count = 0;
            do {
                if (element.isDisplayed()) {
                    return true;
                }
                Thread.sleep(250);
                count++;
            } while (count < timeout * 4);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Erro. O elemento informado não está visível ou demorou muito para ficar visível.");
        }
        return false;
    }

    /**
     * This method makes the driver sleep to wait a specific element to be visible.
     *
     * @param locator Element locator.
     * @param timeout Time (long). Recommended: 30.
     * @return True if the element is NOT visible. False if it's.
     * @throws Exception
     */
    public boolean waitElementIsNotVisible(By locator, int timeout) throws InterruptedException {
        int count = 0;
        do {
            try {
                WebElement element = getDriver().findElement(locator);
                if (!element.isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {
                logFail("Não foi possivel aguardar pelo elemento desaparecer,");
                e.printStackTrace();
            }
            Thread.sleep(250);
            count++;
        } while (count < timeout * 4);
        return false;
    }

    /**
     * This method makes the driver wait until a specific element is located.
     *
     * @param locator Element locator.
     * @param timeout Time (long). Recommended: 30.
     */
    public void waitWebDriverElementExists(By locator, long timeout) {
        try {
            WebDriverWait driverWait = new WebDriverWait(getDriver(), timeout);
            driverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Não foi possivel verificar se o elemento existe.");
        }
    }

    /**
     * This method makes the driver wait until all elements from an element be
     * loaded.
     *
     * @param locator
     * @param timeout Time (long). Recommended: 30.
     */
    public void waitWebDriverElementListPresent(By locator, long timeout) {
        try {
            WebDriverWait driverWait = new WebDriverWait(getDriver(), timeout);
            driverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Nãi foi possivel verificar se o elemento está presente na lista.");
        }
    }

    /**
     * This method makes the driver sleep to wait a specific element to be enabled.
     *
     * @param locator Element locator.
     * @param timeout Time (long). Recommended: 30.
     * @return True if the element is Enable, False if it's not.
     * @throws Exception
     */
    public boolean waitElementIsEnable(By locator, long timeout) throws InterruptedException {

        try {
            int count = 0;
            do {
                WebElement element = getDriver().findElement(locator);
                if (element.isDisplayed() && element.isEnabled()) {
                    return true;
                }
                Thread.sleep(250);
                count++;
            } while (count < timeout * 4);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Não foi possível verificar se o elemento está habilitado.");
        }
        return false;
    }

    /**
     * This method verifies if the element contains a specific text.
     *
     * @param locator Element locator.
     * @param text    Text that will be verified.
     * @return True if the element contains the text, False if it's not.
     */
    public boolean elementContainsText(By locator, String text) {
        try {
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            return element.getText().contains(text);
        } catch (Exception e) {
            logFail("Não foi possivel validar se o elemento contem o texto \"" + text + "\".");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This methods verifies if the attribute of an element contains a specific text
     * by it's locator.
     *
     * @param locator Element locator.
     * @param att     Attribute name.
     * @param text    Text to be verified if it's contained into the attribute.
     * @return True if the text it's contained, False if it's not.
     */
    public boolean attributeContaisText(By locator, String att, String text) {
        try {
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            return element.getAttribute(att).contains(text);
        } catch (Exception e) {
            logFail("Não foi possivel verificar se o atributo \"" + att + "\" contem o texto\"" + text + "\".");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method obtains the value of an attribute by it's locator.
     *
     * @param locator Element locator.
     * @param att     Attribute to obtain it's value.
     * @return The attribute value.
     */
    public String getAttribute(By locator, String att) {

        try {
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            return element.getAttribute(att);
        } catch (Exception e) {
            logFail("Não foi possivel pegar o atribuito \"" + att + "\" do elemento informado");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method checks if an element is disabled by it's locator.
     *
     * @param locator Element locator.
     * @return True if the element is enabled, False if it's not.
     */
    public boolean isEnabled(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            return element.isEnabled();
        } catch (NoSuchElementException e) {
            Report.log(Status.INFO, "Não foi possivel verificar se o elemento está habilitado.");
            return false;
        }
    }

    /**
     * This method check if a element is disabled by it's locator.
     *
     * @param locator Element locator.
     * @return True if the element isn't enabled, False if it's.
     */
    public boolean isDisabled(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            return !element.isEnabled();
        } catch (NoSuchElementException e) {
            logFail("Não foi possivel verificar se o elemento está desabilitado.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method checks if an element is being displayed by it's locator.
     *
     * @param locator Element locator.
     * @return True if the element is being displayed, False if it's not.
     */
    public boolean isDisplayed(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            logFail("Não foi possivel verificar se o elemento está sendo exibido.");
            return false;
        }
    }

    /**
     * This method checks if an element is not being displayed by it's locator.
     *
     * @param locator Element locator.
     * @return True if the element is not being displayed, false if it is.
     */
    public boolean isNotDisplayed(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            this.highlight(element, true);
            return !element.isDisplayed();
        } catch (NoSuchElementException e) {
            logFail("Não foi possivel verificar se o elemento não está sendo exibidop");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method executes a refresh on the actual page.
     */
    public void refreshPage() {
        try {
            getDriver().navigate().refresh();
        } catch (Exception e) {
            logFail("Não foi possivel atualizar (F5) a página.");
            e.printStackTrace();
        }
    }

    /**
     * This method selects a specific element from a drop down list by it's index.
     *
     * @param locator Element locator.
     * @param index   Index of the element from the drop down list.
     */
    public void setSelect(By locator, int index) {
        try {
            WebElement element = getDriver().findElement(locator);
            Select selectElement = new Select(getDriver().findElement(locator));
            if (element.isDisplayed() && element.isEnabled()) {
                this.highlight(element, true);
                selectElement.selectByIndex(index);
            }
        } catch (Exception e) {
            logFail("Não foi possivel selecionar a opção \"+index+\" dentro do select.");
            e.printStackTrace();
            Assert.fail("It wasn't possible to select element locator index" + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

    /**
     * This method selects a specific element from a drop down list by it's text.
     *
     * @param locator Element locator.
     * @param text    Text of the element from the drop down list.
     */
    public void setSelect(By locator, String text) {
        try {
            WebElement element = getDriver().findElement(locator);
            Select selectElement = new Select(getDriver().findElement(locator));
            if (element.isDisplayed() && element.isEnabled()) {
                this.highlight(element, true);
                selectElement.selectByValue(text);
            }
        } catch (Exception e) {
            logFail("Não foi possivel selecionar a opção \"+index+\" dentro do select.");
            e.printStackTrace();
            Assert.fail("It wasn't possible to select element locator index" + locator
                    + "\n-----==========-----\nMessage: " + e.getMessage() + "\n-----==========-----");
        }
    }

}
