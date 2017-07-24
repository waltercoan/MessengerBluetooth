import images.SendFileTask;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class RemoteDeviceDiscovery implements Runnable{

	private HashMap<String,Phone> devicesDiscovered = new HashMap<String,Phone>();
	private boolean interrupt = false;
	private ArrayList<String> logList = new ArrayList<String>();
	private String dataFile;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:SS");
	private String fileImage1, fileImage2, fileMac;
	
	public RemoteDeviceDiscovery(String fileImage1, String fileImage2, String macs) {
		dataFile = System.getenv("temp") + "\\UNIVILLEBluetooth.xml";
		this.fileImage1 = fileImage1;
		this.fileImage2 = fileImage2;
		this.fileMac = macs;
	}
	public void saveData(){
		try {
			XMLEncoder xmlenc = new XMLEncoder(new FileOutputStream(dataFile));
			xmlenc.writeObject(devicesDiscovered);
			xmlenc.flush();
			xmlenc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void loadData(){
		File arquivo = new File(dataFile);
		if(arquivo.exists()){
			try {
				XMLDecoder xmldec = new XMLDecoder(new FileInputStream(dataFile));
				devicesDiscovered = (HashMap<String,Phone>) xmldec.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public ArrayList<String> getLogList(){
		return logList;
	}
	public void interrupt(){
		interrupt = true;
	}
	@Override
	public void run() {
		logList.add("Iniciado: " + sdf.format(new Date()));
		interrupt = false;
		while(!interrupt){
			try {
				main();
			} catch (IOException e) {
				logList.add("Erro: " + e.getMessage());
				interrupt = true;
			} catch (InterruptedException e) {
				logList.add("Erro: " + e.getMessage());
				interrupt = true;
			}
		}
		logList.add("Parou: " + sdf.format(new Date()));
	}
	public  long calcDiferHoras(Date hIni, Date hFim){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		long resp = 0;								
		resp = (hFim.getTime() - hIni.getTime()) / (60*1000);
		return resp;
	}
	public void main() throws IOException, InterruptedException {
		final Object inquiryCompletedEvent = new Object();
		DiscoveryListener listener = new DiscoveryListener() {
			public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
				logList.add("Device " + btDevice.getBluetoothAddress() + " found");
				Phone p = devicesDiscovered.get(btDevice.getBluetoothAddress());
				if (p == null)
					devicesDiscovered.put(btDevice.getBluetoothAddress(),new Phone(btDevice.getBluetoothAddress()));
				try {
					String friendlyName = btDevice.getFriendlyName(false);
					p.setName(friendlyName);
					logList.add("     name " + friendlyName);
				} catch (IOException cantGetDeviceName) {
				}
			}

			public void inquiryCompleted(int discType) {
				logList.add("Fim da Busca por dispositivos Bluetooth...");
				synchronized(inquiryCompletedEvent){
					inquiryCompletedEvent.notifyAll();
				}
			}

			public void serviceSearchCompleted(int transID, int respCode) {
			}

			public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
				for(ServiceRecord sr:servRecord){
					DataElement d = sr.getAttributeValue(DataElement.DATALT);
				}
			}
		};

		synchronized(inquiryCompletedEvent) {
			boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
			PhoneVendorDiscover.getInstance(fileMac);
			if (started) {
				logList.add("Iniciando Busca por dispositivos...");
				inquiryCompletedEvent.wait();
				logList.add("Conectando em " + devicesDiscovered.size() +  " Bluetooth");
				for(Phone p : devicesDiscovered.values()){
					try {
						logList.add(p.getAddress() + " numero visitas: " + p.getNumberVisit());
						boolean waitTime = false;
						long diferenca = 0;
						if(p.getNumberVisit() > 1){
							diferenca = calcDiferHoras(p.getLastVisit(),new Date());
							waitTime = diferenca < 2;
						}
						if(p.getNumberVisit() > 5){
							logList.add(p.getAddress() + " estourou limite de 5 tentativas");
							p.setIgnore(true);
							continue;
						}
						if(p.isIgnore()){
							logList.add(p.getAddress() + " lista de ignorados");
							continue;
						}
						if(waitTime){
							logList.add("Faltam " + (4-diferenca) + "h para reconectar a" +  p.getAddress());
							continue;
						}
						// Build URL for the bluetooth device, note the port 9
						String port = null;
						port = PhoneVendorDiscover.getInstance().getPort(p.getAddress().trim());
						if(port == null){
							for(Phone p2 : devicesDiscovered.values()){
								String addStart = p2.getAddress().substring(0, 6);
								if(addStart.equals(p.getAddress().substring(0, 6)) && 
										!p2.getAddress().equals(p.getAddress())){
									port = String.valueOf(p.getPort());
								}
							}
						}
						if(port == null && p.getNumberVisit() == 0){
							p.setPort(p.getPort()+1);
							if(p.getPort() >= 12){
								p.setIgnore(true);
							}
							port = String.valueOf(p.getPort());
						}
						logList.add("Tentativa: " + p.getAddress() + " numero visitas: " + p.getNumberVisit() + " porta: " + port);
						System.out.println("Endereco: " + p.getAddress() + " porta: " + port);
						String url = "btgoep://" + p.getAddress().trim()
						+ ":" + port.trim() + ";authenticate=false;encrypt=false;master=false";
						//http://coffer.com/mac_find/
						//sonyEriccson 6
						//nokia porta 9
						//sansung 5
						//powerPack 4
						//Motorola 8
						File f = null;
						String imageFileOption = fileImage1;
						if(p.getNumberVisit() % 2 == 1){
							imageFileOption = fileImage1;
						}else{
							if(fileImage2 != null)
								imageFileOption = fileImage2;
							else
								imageFileOption = fileImage1;
						}
						f = new File(imageFileOption);
						FileInputStream stream = new FileInputStream(f);
						int size = (int) f.length();
						byte[] file = new byte[size];
						stream.read(file);

						// Filename

						// Trigger the task in a different thread so it won't block the UI
						SendFileTask task = new SendFileTask(url, file);
						p.setPort(Integer.parseInt(port));

						if(task.getRunOK()){
							p.addNumberVisit();
							if(p.getNumberVisit() >= 10)
								p.setIgnore(true);
						}
					}catch(BluetoothConnectionException ex) {
						logList.add("Erro: " + ex.getMessage());
					} catch(NullPointerException e){
						logList.add("Erro: Porta nula " + e.getMessage());
					}
				}
			}
		}
	}
}
