package com.epam;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

 

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/resources",glue ="com.epam" )
public class BatchRunner {
}