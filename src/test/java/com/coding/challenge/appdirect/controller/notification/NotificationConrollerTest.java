package com.coding.challenge.appdirect.controller.notification;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.coding.challenge.appdirect.bean.Account;
import com.coding.challenge.appdirect.bean.User;
import com.coding.challenge.appdirect.bean.appdirect.event.Event;
import com.coding.challenge.appdirect.bean.appdirect.result.ErrorCode;
import com.coding.challenge.appdirect.bean.appdirect.result.Result;
import com.coding.challenge.appdirect.repositories.AccountRepository;
import com.coding.challenge.appdirect.repositories.UserRepository;

public class NotificationConrollerTest {

	
	@Mock
	UserRepository userRepository;
	@Mock
	AccountRepository accountRepository;
	
	NotificationController controller;
    Result result;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		controller = new NotificationController();
		controller.setAccountRepository(accountRepository);
		controller.setUserRepository(userRepository);
		
		result = new Result();
	}
	
	@Test
	public void processOrderOK() throws Exception {
		Event event = getEvent("dummyOrder.xml");
		
        result = controller.processEvent(event);
        
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("d15bb36e-5fb5-11e0-8c3c-00262d2cda03", result.getAccountIdentifier());
	}
	
	@Test
	public void processOrderAccountExists() throws Exception {
		Event event = getEvent("dummyOrder.xml");
		
        Mockito.when(accountRepository.findOne("d15bb36e-5fb5-11e0-8c3c-00262d2cda03")).thenReturn(new Account());
        result = controller.processEvent(event);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ErrorCode.USER_ALREADY_EXISTS, result.getErrorCode());
	}
	
	@Test
	public void processOrderStateless() throws Exception {
		Event event = getEvent("dummyOrder-stateless.xml");

        result = controller.processEvent(event);
        
        Assert.assertTrue(result.isSuccess());
        Assert.assertNull(result.getAccountIdentifier());
	}
	
	@Test
	public void processCancelOK() throws Exception {
		Event event = getEvent("dummyCancel.xml");
        
        Mockito.when(accountRepository.findOne("dummy-account")).thenReturn(new Account());
        
        result = controller.processEvent(event);
        
        Assert.assertTrue(result.isSuccess());
	}
	
	@Test
	public void processCancelAccountNotExist() throws Exception {
		Event event = getEvent("dummyCancel.xml");
        
        result = controller.processEvent(event);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
	}
	
	@Test
	public void processChangeOK() throws Exception {
		Event event = getEvent("dummyChange.xml");
        
        Mockito.when(accountRepository.findOne("dummy-account")).thenReturn(new Account());
        
        result = controller.processEvent(event);
        
        Assert.assertTrue(result.isSuccess());
	}
	
	@Test
	public void processChangeAccountNotExist() throws Exception {
		Event event = getEvent("dummyChange.xml");
        
        result = controller.processEvent(event);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
	}
	
	@Test
	public void processNoticeOK() throws Exception {
		Event event = getEvent("dummyNotice.xml");
        
        Mockito.when(accountRepository.findOne("dummy-account")).thenReturn(new Account());
        
        result = controller.processEvent(event);
        
        Assert.assertTrue(result.isSuccess());
	}
	
	@Test
	public void processNoticeAccountNotExist() throws Exception {
		Event event = getEvent("dummyNotice.xml");
        
        result = controller.processEvent(event);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
	}
	
	@Test
	public void processAssignOK() throws Exception {
		Event event = getEvent("dummyAssign.xml");
        
        Mockito.when(accountRepository.findOne("dummy-account")).thenReturn(new Account());
        
        result = controller.processEvent(event);
        
        Assert.assertTrue(result.isSuccess());
	}
	
	@Test
	public void processAssignAccountNotExist() throws Exception {
		Event event = getEvent("dummyAssign.xml");
        
        result = controller.processEvent(event);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
	}
	
	@Test
	public void processUnassignOK() throws Exception {
		Event event = getEvent("dummyUnassign.xml");
        
        Mockito.when(accountRepository.findOne("dummy-account")).thenReturn(new Account());
        Mockito.when(userRepository.findByOpenID("https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2")).thenReturn(new User());
        
        result = controller.processEvent(event);
        
        Assert.assertTrue(result.isSuccess());
	}
	
	@Test
	public void processUnassignAccountNotExist() throws Exception {
		Event event = getEvent("dummyUnassign.xml");
        
        result = controller.processEvent(event);
        
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, result.getErrorCode());
	}
	
	@Test
	public void processUnassignUserNotExist() throws Exception {
		Event event = getEvent("dummyUnassign.xml");
        
        Mockito.when(accountRepository.findOne("dummy-account")).thenReturn(new Account());
        
        result = controller.processEvent(event);
        
        Assert.assertFalse(result.isSuccess());
	}
	
	private Event getEvent(String file) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Event.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Event event = (Event) unmarshaller.unmarshal(this.getClass().getClassLoader().getResourceAsStream(file));
		return event;
	}
}
