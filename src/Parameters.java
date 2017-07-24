import java.util.ArrayList;


public class Parameters {
	private ArrayList<String> messages = new ArrayList<String>();
	private ArrayList<Phone> colPhones = new ArrayList<Phone>();
	
	public String getMessage(int pos){
		return messages.get(pos);
	}
	public void addMessage(String message){
		messages.add(message);
	}
	public void removeMessage(String message){
		
	}
	
	public ArrayList<String> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}
	public ArrayList<Phone> getColPhones() {
		return colPhones;
	}
	public void setColPhones(ArrayList<Phone> colPhones) {
		this.colPhones = colPhones;
	}
	
	
}
