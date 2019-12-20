package br.com.bsfraga.fwBaseWebApi.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

	private static WebDriver driver;
	public DriverFactory() {}
	
	public static WebDriver getDriver() {
		
		if(driver == null) {
			switch (ProjectProperties.browser) {
			case CHROME:
				System.setProperty("webdriver.chrome.driver", "E:\\codes\\SandBox-workspace\\fwBaseWebApi\\src\\test\\resources\\drivers\\chromedriver.exe");
				driver = new ChromeDriver();
				break;
			case FIREFOX:
				System.setProperty("webdriver.gecko.driver", "E:\\codes\\SandBox-workspace\\fwBaseWebApi\\src\\test\\resources\\drivers\\geckodriver.exe");
				driver = new FirefoxDriver();
				break;
			case EDGE:
				driver = new EdgeDriver();
				break;
			}
		}
		return driver;
	}
	
	public static void killDriver() {
		if(driver != null) {
			driver.quit();
			driver = null;
		}
	}
	
}
