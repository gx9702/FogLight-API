package com.ociweb.oe.floglight.api.behaviors;

import com.ociweb.gl.api.PubSubMethodListener;
import com.ociweb.gl.api.WaitFor;
import com.ociweb.gl.api.Writable;
import com.ociweb.iot.maker.FogCommandChannel;
import com.ociweb.iot.maker.FogRuntime;
import com.ociweb.pronghorn.pipe.ChannelReader;

public class IngressBehavior implements PubSubMethodListener {
	private final FogCommandChannel cmd;
	private final String publishTopic;

	public IngressBehavior(FogRuntime runtime, String publishTopic) {
		cmd = runtime.newCommandChannel(DYNAMIC_MESSAGING);
		this.publishTopic = publishTopic;
	}

	public boolean receiveMqttMessage(CharSequence topic,  ChannelReader payload) {
		// this received when mosquitto_pub is invoked - see MQTTClient
		System.out.print("\ningress body: ");

		// Read the message payload and output it to System.out
		payload.readUTFOfLength(payload.available(), System.out);
		System.out.println();

		// Create the on-demand mqtt payload writer
		Writable mqttPayload = writer -> writer.writeUTF("\nsecond step test message");

		// On the 'localtest' topic publish the mqtt payload
		cmd.publishTopic(publishTopic, mqttPayload, WaitFor.None);

		// We consumed the message
		return true;
	}
}
