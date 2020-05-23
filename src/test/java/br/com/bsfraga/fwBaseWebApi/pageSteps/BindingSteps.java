package br.com.bsfraga.fwBaseWebApi.pageSteps;


import br.com.bsfraga.fwBaseWebApi.core.ProjectProperties;
import br.com.bsfraga.fwBaseWebApi.utils.Report;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;

import static br.com.bsfraga.fwBaseWebApi.core.DriverFactory.getDriver;
import static br.com.bsfraga.fwBaseWebApi.core.DriverFactory.killDriver;
import static br.com.bsfraga.fwBaseWebApi.utils.FixReport.fixReport;

public class BindingSteps {

    private Scenario scenario;

    @Before
    public void before(Scenario scenario){
        this.scenario = scenario;
        Report.startTest(scenario.getName());
    }


    @Before(value = "@First")
    public void start(){
        getDriver();
    }

    @After(value = "@Last")
    public void finish(){
        if(ProjectProperties.CLOSE_BROWSER){
            killDriver();
        }
        Report.close();
    }

    @AfterStep
    public void afterStep(){
        fixReport();
    }


}
