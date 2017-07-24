import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.intel.bluetooth.BlueCoveConfigProperties;


public class frmMessengerBT extends JFrame implements ActionListener{
	private JPanel jpnBotoes, jpnDepto;
	private JScrollPane jspLogList;
	private JButton btbIniciar, btbParar, btbSair;
	private JTable logList;
	private RemoteDeviceDiscovery rdd;
	private LogListModel model;

	public frmMessengerBT(String fileImage1, String fileImage2, String macs) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("Messenger Bluetooth");
		setSize(400,400);
		rdd = new RemoteDeviceDiscovery(fileImage1,fileImage2,macs);
		rdd.loadData();
		createLog();
		createBotoes();
		createJpnDepto();
		setVisible(true);
		Timer t = new Timer();
		t.schedule(new Tarefa(), 1000,1000);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				rdd.saveData();
				JOptionPane.showMessageDialog(null, "Utilize o botão SAIR para fechar a aplicação...","Bluetooth Messenger",JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	private void createJpnDepto(){
		jpnDepto = new JPanel(new GridLayout(5,1));
		add("South",jpnDepto);
		Font f = new Font("Arial",Font.BOLD,15);
		JLabel jlbAutor = new JLabel("Desenvolvido por");
		JLabel jlbUniville = new JLabel("UNIVILLE - Universidade da Região de Joinville",JLabel.CENTER);
		jlbUniville.setFont(f);
		JLabel jlbDepto = new JLabel("Departamento de Informática",JLabel.CENTER);
		jlbDepto.setFont(f);
		JLabel jlbProf = new JLabel("Prof. Walter Silvestre Coan",JLabel.CENTER);
		JLabel jlbEmail = new JLabel("walter.s@univille.net",JLabel.CENTER);
		jpnDepto.add(jlbAutor);
		jpnDepto.add(jlbUniville);
		jpnDepto.add(jlbDepto);
		jpnDepto.add(jlbProf);
		jpnDepto.add(jlbEmail);
	}
	private void createBotoes(){
		jpnBotoes = new JPanel(new GridLayout(1,3));
		add("North",jpnBotoes);
		Font f = new Font("Verdana",Font.BOLD,18);
		btbIniciar = new JButton("Iniciar");
		btbIniciar.setName("btbIniciar");
		btbIniciar.setFont(f);
		btbIniciar.addActionListener(this);
		btbParar  = new JButton("Parar");
		btbParar.setFont(f);
		btbParar.addActionListener(this);
		btbParar.setName("btbParar");
		btbParar.setEnabled(false);
		btbSair = new JButton("Sair");
		btbSair.setFont(f);
		btbSair.setName("btbSair");
		btbSair.addActionListener(this);
		jpnBotoes.add(btbIniciar);
		jpnBotoes.add(btbParar);
		jpnBotoes.add(btbSair);
	}
	private void createLog(){
		model = new LogListModel(rdd.getLogList());
		logList = new JTable(model);
		jspLogList = new JScrollPane(logList);
		add("Center",jspLogList);
	}
	public static void main(String[] args) {
		System.setProperty("bluecove.obex.timeout", "10000");
		new frmMessengerBT(args[0],args[1],args[2]);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Component comp = (Component) e.getSource();
		if(comp.getName().equals("btbIniciar")){
			Thread t = new Thread(rdd);
			t.start();
			btbSair.setEnabled(false);
			btbIniciar.setEnabled(false);
			btbParar.setEnabled(true);
		}else{
			if(comp.getName().equals("btbParar")){
				rdd.interrupt();
				btbSair.setEnabled(true);
				btbIniciar.setEnabled(true);
				btbParar.setEnabled(false);
				
			}else{
				if(comp.getName().equals("btbSair")){
					rdd.saveData();
					System.exit(0);
				}
			}
		}
	}
	class Tarefa extends TimerTask{
		@Override
		public void run() {
			model.fireTableDataChanged();
		}
	}
}
