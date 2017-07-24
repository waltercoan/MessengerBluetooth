import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.JFrame;


public class ArduinoLED{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		boolean ligado = true;
		//001050200006
		//001102290224
		//001102290007
		//001102290302
		
		ChartFrame cf = new ChartFrame();
		Thread t = new Thread(cf);
		t.start();
		
		StreamConnection c = (StreamConnection)Connector.open("btspp://001050200006:1;master=false");

		OutputStream os = c.openOutputStream();
		OutputStreamWriter ow = new OutputStreamWriter(os);
		InputStream is = c.openInputStream();
		InputStreamReader ir = new InputStreamReader(is);
		while(true){
			StringBuilder sb = new StringBuilder();
			int numChars=0;
			int data = ir.read();
			while(data != -1){
			    char theChar = (char) data;
			    sb.append(theChar);
			    numChars++;
			    if(numChars ==5){
			    	numChars = 0;
			    	System.out.println(sb.toString());
			    	cf.setData(Float.parseFloat(sb.toString()));
			    	sb = new StringBuilder();
			    }
			    data = ir.read();
			    
			}
			
			//ow.write(((ligado)?'O':'N'));
			//ow.flush();
			//ligado = !ligado;
			try {
				Thread.currentThread().sleep(350);
			} catch (InterruptedException e) {
			}
		}
	}
}
