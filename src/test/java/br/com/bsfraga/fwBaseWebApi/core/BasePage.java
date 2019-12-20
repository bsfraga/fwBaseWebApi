package br.com.bsfraga.fwBaseWebApi.core;

import java.util.concurrent.TimeUnit;

import br.com.bsfraga.fwBaseWebApi.utils.Utils;

public class BasePage {

	protected Utils utils;
	
	public BasePage() {
		utils = new Utils();
		DriverFactory.getDriver().manage().window().maximize();
		DriverFactory.getDriver().manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
	}
}
