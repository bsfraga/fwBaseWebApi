package br.com.bsfraga.fwBaseWebApi.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class is reponsable to manage how junit should work with cucumber.
 * Here it's possivel to set path to reports, pattern for auto-generated codes from junit,
 * console behavior and more.
 *
 * @author bsfraga
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/features",
        glue = {"pageSteps"},
        plugin = {"json:reports/cucumber-reports/cucumber.json",
                "html:reports/cucumber-reports/cucumber.html",
                "rerun:target/rerun.txt"},
        tags = {},
        monochrome = true,
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        strict = true
)
public class TestRunner {
}

