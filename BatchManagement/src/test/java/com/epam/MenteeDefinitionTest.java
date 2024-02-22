package com.epam;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.epam.dtos.BatchDto;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenteeDefinitionTest {
    
    @LocalServerPort
    private int port;
    private ResponseEntity<BatchDto> responseEntity;
    int id;
    RestTemplate restTemplate = new RestTemplate();
    private String baseUrl = "http://localhost:";

    @Given("batch {int}")
    public void batchId(int id) {
        this.id = id;
        baseUrl = baseUrl.concat(port + "").concat("/rd/batch/");
    }

    @When("requested to find batch")
    public void requestedToFindBatch() {

        responseEntity = restTemplate.getForEntity(baseUrl+id, BatchDto.class);
    }

    @Then("the status code should be {int}")
    public void giveBatchInfo(int expectedStatus) {
        assertEquals(expectedStatus, responseEntity.getStatusCode().value());
    }



}