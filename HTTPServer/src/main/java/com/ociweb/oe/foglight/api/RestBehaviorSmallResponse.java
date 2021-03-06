package com.ociweb.oe.foglight.api;

import com.ociweb.gl.api.HTTPFieldReader;
import com.ociweb.gl.api.HTTPRequestReader;
import com.ociweb.gl.api.NetResponseWriter;
import com.ociweb.gl.api.Writable;
import com.ociweb.gl.api.Payloadable;
import com.ociweb.gl.api.RestListener;
import com.ociweb.iot.maker.FogCommandChannel;
import com.ociweb.iot.maker.FogRuntime;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;
import com.ociweb.pronghorn.pipe.ChannelReader;
import com.ociweb.pronghorn.pipe.ChannelWriter;

public class RestBehaviorSmallResponse implements RestListener {

	private final FogCommandChannel cmd;
	
	public RestBehaviorSmallResponse(FogRuntime runtime) {	
		this.cmd = runtime.newCommandChannel(NET_RESPONDER);
	}
	
	Payloadable reader = new Payloadable() {
		
		@Override
		public void read(ChannelReader reader) {
			
			System.out.println("POST: "+reader.readUTFOfLength(reader.available()));
			
		}			
	};


	Writable writableA = new Writable() {
		
		@Override
		public void write(ChannelWriter writer) {
			writer.writeUTF8Text("beginning of text file\n");
		}
		
	};
	
	Writable writableB = new Writable() {
		
		@Override
		public void write(ChannelWriter writer) {
			writer.writeUTF8Text("this is some text\n");
		}
		
	};
	
	@Override
	public boolean restRequest(HTTPRequestReader request) {
		
		if (request.isVerbPost()) {
			request.openPayloadData(reader );
		}

		//if this can not be published then we will get the request again later to be reattempted.
		return cmd.publishHTTPResponse(request, 200, 
								false,
				                HTTPContentTypeDefaults.TXT,
				                writableA);

	}

}
