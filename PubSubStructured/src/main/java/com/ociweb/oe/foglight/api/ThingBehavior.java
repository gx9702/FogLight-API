package com.ociweb.oe.foglight.api;

import com.ociweb.gl.api.PubSubListener;
import com.ociweb.gl.api.PubSubStructuredWritable;
import com.ociweb.iot.maker.FogCommandChannel;
import com.ociweb.iot.maker.FogRuntime;
import com.ociweb.pronghorn.pipe.ChannelReader;
import com.ociweb.pronghorn.util.field.IntegerFieldProcessor;
import com.ociweb.pronghorn.util.field.MessageConsumer;
import com.ociweb.pronghorn.util.field.StructuredBlobWriter;

public class ThingBehavior implements PubSubListener {

	private final FogCommandChannel cmd;
    private final MessageConsumer consumer;
    private int lastValue;
    private final CharSequence publishTopic;
    private final FogRuntime runtime;
		
    public ThingBehavior(FogRuntime runtime, CharSequence topic) {
    	this.cmd = runtime.newCommandChannel(DYNAMIC_MESSAGING);

		this.consumer = new MessageConsumer()
				            .integerProcessor(PubSubStructured.COUNT_DOWN_FIELD, processor);
		
		this.publishTopic = topic;
		this.runtime = runtime;
	}
    
    private final PubSubStructuredWritable writable = new PubSubStructuredWritable() {
    	@Override
    	public void write(StructuredBlobWriter writer) {
    		writer.writeLong(PubSubStructured.COUNT_DOWN_FIELD, lastValue-1);
    		writer.writeUTF8(PubSubStructured.SENDER_FIELD, "from thing one behavior");
    	}			
    };

    private final IntegerFieldProcessor processor = new IntegerFieldProcessor() {			
    	@Override
    	public boolean process(long value) {
    		lastValue = (int)value;
    		return true;
    	}
    };
    
	@Override
	public boolean message(CharSequence topic, ChannelReader payload) {
					
		//
		////NOTE: this one line will copy messages from payload if consumer returns true
		////      when the message is copied its topic is changed to the first argument string
		//
		//cmd.copyStructuredTopic("outgoing topic", payload, consumer);
		//
		
		if (consumer.process(payload)) {
			if (lastValue>0) {
				System.out.println(lastValue);
				return cmd.publishStructuredTopic(publishTopic, writable);
			} else {
				runtime.shutdownRuntime();
				return true;
			} 
		} else {
			return false;
		}
		
	}

}
