package br.com.bsfraga.fwBaseWebApi.core;

public class ProjectProperties {

	public static boolean CLOSE_BROWSER = true;
	
	public static Browsers browser = Browsers.CHROME;

	public enum Browsers {
		CHROME, FIREFOX, EDGE
	}
	
}
