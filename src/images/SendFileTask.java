package images;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;

public class SendFileTask{

	private String btConnectionURL;
	private byte[] file;
	private boolean runOK = true;
	private Connection connection;
	private ClientSession cs;
	private Operation putOperation;
	private OutputStream outputStream;

	public boolean getRunOK(){
		return runOK;
	}
	public SendFileTask(String url, byte[] file) {
		this.btConnectionURL = url;
		this.file = file;
		
		try {
			connection = Connector.open(btConnectionURL,Connector.WRITE,true);
			
			cs = (ClientSession) connection;
			HeaderSet hs = cs.createHeaderSet();
			cs.connect(hs);
			hs.setHeader(HeaderSet.NAME, "univille.jpg");
			hs.setHeader(HeaderSet.TYPE, "jpg");
			hs.setHeader(HeaderSet.LENGTH, (long)file.length);
			putOperation = cs.put(hs);

			outputStream = putOperation.openOutputStream();
			outputStream.write(file);
			outputStream.close();
			putOperation.close();
			cs.disconnect(null);
			connection.close();
		}catch(InterruptedIOException e){
			e.printStackTrace();
			runOK = false;
			return;
		}catch (Exception e) {
			e.printStackTrace();
			runOK = false;
		}
	}
}
