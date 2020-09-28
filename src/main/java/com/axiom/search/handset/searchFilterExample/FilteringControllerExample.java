package com.axiom.search.handset.searchFilterExample;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringControllerExample {
//returning a single bean as response
//values to send name and salary															
	@RequestMapping("/filtering")
	public MappingJacksonValue retrieveSomeBean() {
		SomeBean someBean = new SomeBean("Apple iPad Pro 12.9 (2018)", "Nano-SIM eSIM", "2048 x 2732 pixels");
//invoking static method filterOutAllExcept()
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("Apple iPad Pro 12.9 (2018)",
				"Nano-SIM eSIM");
//creating filter using FilterProvider class
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
//constructor of MappingJacksonValue class  that has bean as constructor argument
		MappingJacksonValue mapping = new MappingJacksonValue(someBean);
//configuring filters
		mapping.setFilters(filters);
		return mapping;
	}

//returning a list of SomeBeans as response
//values to send name and phone
	@RequestMapping("/filtering-list")
	public MappingJacksonValue retrieveListOfSomeBeans() {
		List<SomeBean> list = Arrays.asList(new SomeBean("Apple iPhone XS Max", "Single SIM", "1242 x 2688 pixels"),
				new SomeBean("Apple Watch Series 4", "eSIM", "448 x 368 pixels"));
//invoking static method filterOutAllExcept()
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("phone", "sim");
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
//constructor of MappingJacksonValue class that has list as constructor argument
		MappingJacksonValue mapping = new MappingJacksonValue(list);
//configuring filter
		mapping.setFilters(filters);
		return mapping;
	}
}
