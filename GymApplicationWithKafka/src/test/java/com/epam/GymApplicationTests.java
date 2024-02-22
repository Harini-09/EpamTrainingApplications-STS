package com.epam;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class, DataSourceAutoConfiguration.class})
@SpringBootTest
class GymApplicationTests {

//	@Test
//	void contextLoads() {
//		assertTrue(true);
//	}

}
