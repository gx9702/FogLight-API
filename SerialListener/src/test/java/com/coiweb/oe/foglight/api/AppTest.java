package com.coiweb.oe.foglight.api;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ociweb.iot.hardware.impl.test.TestHardware;
import com.ociweb.iot.maker.FogRuntime;
import com.ociweb.pronghorn.stage.scheduling.NonThreadScheduler;

/**
 * Unit test for simple App.
 */
public class AppTest { 

	
	 @Test
	    public void testApp()
	    {
		    StringBuilder builder = new StringBuilder();
		 
		    FogRuntime runtime = FogRuntime.test(new SerialListener(builder));	    	
	    	NonThreadScheduler scheduler = (NonThreadScheduler)runtime.getScheduler();    	
	    	TestHardware hardware = (TestHardware)runtime.getHardware();
	    
	    	//TODO: set the TestSerial impl to check the values sent that match [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] 
	    	
	    	scheduler.startup();
	    	
	    	final long testStop = System.currentTimeMillis()+4000;
	    	
			while (System.currentTimeMillis()<testStop) {				    		
					scheduler.run();
			}
			
			scheduler.shutdown();
			
			assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9][10, 11, 12, 13, 14, 15, 16, 17, 18, 19]",builder.toString());
			
			
			
	    }
}
