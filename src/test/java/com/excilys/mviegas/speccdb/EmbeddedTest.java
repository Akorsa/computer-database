package com.excilys.mviegas.speccdb;

import static org.junit.Assert.*;

import org.junit.Test;

import com.excilys.mviegas.speccdb.selenium.BaseSeleniumTest;

/**
 * Sert juste à vérifier que le Embedded marche bien
 * 
 * @author excilys
 *
 */
public class EmbeddedTest extends BaseSeleniumTest {

	//===========================================================
	// Callback
	//===========================================================
	@Override
	public void setUp() throws Exception {
		
	}

	@Override
	public void tearDown() throws Exception {
		
	}
	
	//===========================================================
	// Tests
	//===========================================================
	@Test
	public void testInit() throws Exception {
		this.initServer();
	}
	
	@Test
	public void testInitDeploy() throws Exception {
		try { 
			this.initServer();
		} catch (Throwable e) {
			fail();
		}
		
		this.deploy();
	}
	
	@Test
	public void testInitStartTart() throws Exception {
		try { 
			this.initServer();
			this.startServer();
		} catch (Throwable e) {
			e.printStackTrace();
			fail();
		}
		
		this.deploy();
		
	}
	
	@Test
	public void testInitDeployStart() throws Exception {
		try { 
			this.initServer();
			this.deploy();
		} catch (Throwable e) {
			fail();
		}
		
		this.startServer();
	}
	
	@Test
	public void testStop() throws Exception {
		try { 
			this.initServer();
			this.startServer();
			this.deploy();
		} catch (Throwable e) {
			fail();
		}
		
		this.stopServer();		
	}
	
	
	
	

}
