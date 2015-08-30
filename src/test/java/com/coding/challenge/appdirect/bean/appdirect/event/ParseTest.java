package com.coding.challenge.appdirect.bean.appdirect.event;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Test;

public class ParseTest {
	
	@Test
	public void parseOrderEvent() throws Exception {
		
		
		JAXBContext context = JAXBContext.newInstance(Event.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Event result = (Event) unmarshaller.unmarshal(this.getClass().getClassLoader().getResourceAsStream("dummyOrder.xml"));
        
        System.out.println(new ReflectionToStringBuilder(result, new RecursiveToStringStyle()).toString());
	}

}
