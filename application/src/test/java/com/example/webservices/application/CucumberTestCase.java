package com.example.webservices.application;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources"},
        plugin = {"pretty"}
)
public class CucumberTestCase {
    // Required to run cucumber tests.
}
