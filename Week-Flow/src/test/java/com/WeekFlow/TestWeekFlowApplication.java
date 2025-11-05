package com.WeekFlow;

import org.springframework.boot.SpringApplication;

public class TestWeekFlowApplication {

	public static void main(String[] args) {
		SpringApplication.from(WeekFlowApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
