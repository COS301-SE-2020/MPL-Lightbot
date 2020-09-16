package diplomski;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import diplomski.enums.VrstaTrake;


public class Main {//dolje, desno, gore, lijevo
	public static int semafor[] = {5, 5, 5, 5};
	public static int semaforLijevo[] = {0, 0, 0, 0};
	public static int strelicaDesno[] = {0, 0, 0, 0};
	public static int vjerojatnost[] = {1, 1, 1, 1};
	public static int gustoca1[] = {12, 12, 12, 12};
	public static int gustoca2[] = {18, 18, 18, 18};
	public static int brojIzlaznihTraka[] = {2, 2, 2, 2};
	public static int Choices[] = {2};
	public static int vrstaIzlaznihTraka[] = {
		VrstaTrake.LIJEVO_GOREDESNO.getVrstaTrake(), VrstaTrake.LIJEVO_GOREDESNO.getVrstaTrake(), 
		VrstaTrake.LIJEVO_GOREDESNO.getVrstaTrake(), VrstaTrake.LIJEVO_GOREDESNO.getVrstaTrake()};
	public static int brojUlaznihTraka[] = {2, 2, 2, 2};
	public static long cekanje[] = {0, 0, 0, 0};//ms
	public static long brojAuta[] = {0, 0, 0, 0};
	public static JLabel brojSekundi;
	public static JSlider simulationSpeed;
	public final static String DELIMITER = ";";
	private static Charts charts;

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void main(String[] args) {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
		int velicinaX = (int)screenSize.getWidth();
		//int velicinaY = (int)screenSize.getHeight();
		int velicinaGraphics = (int)(0.45 * velicinaX);
		int velicinaCharts = (int)(0.2 * velicinaX);
		brojSekundi = new JLabel("0");
		
		JPanel mainWindow = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel graphicsPanel = new JPanel();
		graphicsPanel.setLayout(new BoxLayout(graphicsPanel, BoxLayout.PAGE_AXIS));

		GraphicsSimulator graphicsSimulator = new GraphicsSimulator(velicinaGraphics);
		graphicsPanel.add(graphicsSimulator);
		
		simulationSpeed = kreirajSimulationSpeed(velicinaGraphics);
		simulationSpeed.addChangeListener(e -> {
			JSlider source = (JSlider)e.getSource();
			graphicsSimulator.setSimulationSpeed((int)source.getValue());
		});
		graphicsPanel.add(simulationSpeed);
		
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = velicinaGraphics;
		c.ipady = velicinaGraphics;
		mainWindow.add(graphicsPanel, c);
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
		optionsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel parametresPanel = new JPanel(new GridLayout(0, 5));
		parametresPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		parametresPanel.setMaximumSize(new Dimension((int)(0.5 * velicinaX), (int)(0.5 * velicinaX)));
		
//		MY CODE
		JPanel choicePanel = new JPanel();
		choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.X_AXIS));
//		choicePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		choicePanel.setMaximumSize(new Dimension((int)(0.5 * velicinaX), (int)(0.5 * velicinaX)));
//		choiceHeader(choicePanel);
		String activeControllers[] = {"Fuzzy Logic Controller", "Fixed Time-Based Controller"};
		JComboBox<ComboItem> controllerDrop[] = choicePanel1(choicePanel, "Traffic Light Controller: ", 
				"Currently active traffic light controller", Choices, 2, activeControllers);
		optionsPanel.add(choicePanel);
		
//		MY CODE		

			
		dodajHeadere(parametresPanel);
		JTextField semaforTF[] = dodajUParametresPanel(parametresPanel, "Traffic light: ",
				"The length of green light on a traffic light in seconds.", semafor);
		JTextField semaforLijevoTF[] = dodajUParametresPanel(parametresPanel, "Left sign: ",
				"The length of green light on a traffic light for turning left in seconds (only if there are 2 output tracks and the type of it is type 1).", semaforLijevo);
		JTextField strelicaDesnoTF[] = dodajUParametresPanel(parametresPanel, "Additional arrow: ",
				"The length of additional arrow for turing right (last n seconds of red light).", strelicaDesno);
		JTextField vjerojatnostTF[] = dodajUParametresPanel(parametresPanel, "Direction probability: ",
				"Probability for going in other directions.", vjerojatnost);
		JTextField gustocaTFs[][] = dodajUParametresPanel(parametresPanel, "Density (per min): ",
				"Density of the cars per minute.", gustoca1, gustoca2);
		JTextField gustoca1TF[] = gustocaTFs[0];
		JTextField gustoca2TF[] = gustocaTFs[1];
		String labeleZaBrojTraki[] = {"1 track", "2 tracks"};
		String labeleZaVrstuTraka[] = {"Left & up/right", "Left/up & right", "Left/up & up/right"};
		JComboBox<ComboItem> brojIzlaznihTrakaC[] = dodajUParametresPanel(parametresPanel, "Output tracks: ", 
				"Number of output tracks in some direction.", brojIzlaznihTraka, 2, labeleZaBrojTraki);
		JComboBox<ComboItem> vrstaIzlaznihTrakaC[] = dodajUParametresPanel(parametresPanel, "Track type: ", 
				"Type of output tracks (only where there are 2 tracks).", vrstaIzlaznihTraka, 3, labeleZaVrstuTraka);
		JComboBox<ComboItem> brojUlaznihTrakaC[] = dodajUParametresPanel(parametresPanel, "Input tracks: ", 
				"Number of input tracks in some direction.", brojUlaznihTraka, 2, labeleZaBrojTraki);
		optionsPanel.add(parametresPanel);
	

		iskljuciLijeveSemaforeAkoJePotrebno(semaforLijevoTF);
		iskljuciVrsteAkoJePotrebno(vrstaIzlaznihTrakaC);
		iskljuciUlazneTrakeAkoJePotrebno(brojUlaznihTrakaC);
	
		
		charts = new Charts(velicinaCharts);
		optionsPanel.add(charts);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		
		JLabel proteklo = new JLabel("Passed (s): ");
		bottomPanel.add(proteklo);
		bottomPanel.add(brojSekundi);
		JLabel spaceLabel = new JLabel(" ");
		spaceLabel.setBorder(new EmptyBorder(0, 0, 0, 10) );
		bottomPanel.add(spaceLabel);
		
		JButton setParametres = new JButton("Reset");
		setParametres.addActionListener(e -> {
			int[] defaultValuesForTextFields[] = {semafor, semaforLijevo, vjerojatnost, gustoca1, gustoca2};
			JTextField[] textFields[] = {semaforTF, semaforLijevoTF, vjerojatnostTF, gustoca1TF, gustoca2TF};
			for (int i = 0; i < textFields.length; i ++) {
				pokusajPostavitiParametre(defaultValuesForTextFields[i], textFields[i], false);
			}
			pokusajPostavitiParametre(strelicaDesno, strelicaDesnoTF, true);
			
			int[] defaultValuesForChoices[] = {brojIzlaznihTraka, vrstaIzlaznihTraka, brojUlaznihTraka};
			JComboBox<ComboItem>[] comboBoxes[] = new JComboBox[3][];
			comboBoxes[0] = brojIzlaznihTrakaC;
			comboBoxes[1] = vrstaIzlaznihTrakaC;
			comboBoxes[2] = brojUlaznihTrakaC;
			for (int i = 0; i < comboBoxes.length; i ++) {
				pokusajPostavitiParametre(defaultValuesForChoices[i], comboBoxes[i]);
			}

			iskljuciLijeveSemaforeAkoJePotrebno(semaforLijevoTF);
			iskljuciVrsteAkoJePotrebno(vrstaIzlaznihTrakaC);
			iskljuciUlazneTrakeAkoJePotrebno(brojUlaznihTrakaC);
			
			if(controllerAction(controllerDrop) == false){
				parametresPanel.setVisible(false);
			}
			else
				parametresPanel.setVisible(true);
			;
			
			graphicsSimulator.resetiraj();
		});
		bottomPanel.add(setParametres);
	
		
		JButton exportButton = new JButton("Export parameters");
		exportButton.addActionListener(e -> {
			simulationSpeed.setValue(0);
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setSelectedFile(new File("Parameters.txt"));
	        int returnVal = fileChooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file.exists()) {
                	int result = JOptionPane.showConfirmDialog((Component) null, 
                			"Are you sure you want to overwrite that file?", 
                			"File with that name already exists", 
                			JOptionPane.YES_NO_CANCEL_OPTION);
                	if (result != 0) {
                		return;
                	}
                }
                
                setParametres.doClick();

    			try {
    				List<String> postavke = new ArrayList<String>();
					
					for (int i = 0; i < 4; i ++) {
						postavke.add(semafor[i] + DELIMITER);
						postavke.add(semaforLijevo[i] + DELIMITER);
						postavke.add(strelicaDesno[i] + DELIMITER);
						postavke.add(vjerojatnost[i] + DELIMITER);
						postavke.add(gustoca1[i] + DELIMITER);
						postavke.add(gustoca2[i] + DELIMITER);
						postavke.add(brojIzlaznihTraka[i] + DELIMITER);
						postavke.add(vrstaIzlaznihTraka[i] + DELIMITER);
						postavke.add(brojUlaznihTraka[i] + (i < 3 ? DELIMITER : ""));
					}
					
					try (PrintWriter pw = new PrintWriter(file.getAbsolutePath(), "UTF-8")) {
						postavke.stream()
					          .forEachOrdered(pw::print);
					}
			        
					JOptionPane.showMessageDialog((Component) null, "Simulation parameters successfully saved.");
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
            }
		});
		bottomPanel.add(exportButton);
		
		JButton importButton = new JButton("Import parameters");
		importButton.addActionListener(e -> {
			simulationSpeed.setValue(0);
	        JFileChooser fileChooser = new JFileChooser();
	        int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.exists()) {
                	JOptionPane.showMessageDialog((Component) null, "There is no such file.");
                	return;
                }

    			try {
			        
			        String[] vrijednosti = Files.lines(new File(file.getAbsolutePath()).toPath())
			        	.map(sadrzaj -> sadrzaj.replace("\0", ""))
			        	.map(sadrzaj -> sadrzaj.split(";"))
			        	.collect(Collectors.toList())
			        	.get(0);
			        
			        for (int i = 0; i < 4; i ++) {
			        	semaforTF[i].setText(vrijednosti[i * 9]);
			        	semaforLijevoTF[i].setText(vrijednosti[i * 9 + 1]);
			        	strelicaDesnoTF[i].setText(vrijednosti[i * 9 + 2]);
			        	vjerojatnostTF[i].setText(vrijednosti[i * 9 + 3]);
			        	gustocaTFs[0][i].setText(vrijednosti[i * 9 + 4]);
			        	gustocaTFs[1][i].setText(vrijednosti[i * 9 + 5]);
			        	brojIzlaznihTrakaC[i].setSelectedIndex(Integer.parseInt(vrijednosti[i * 9 + 6]) - 1);
			        	vrstaIzlaznihTrakaC[i].setSelectedIndex(Integer.parseInt(vrijednosti[i * 9 + 7]) - 1);
			        	brojUlaznihTrakaC[i].setSelectedIndex(Integer.parseInt(vrijednosti[i * 9 + 8]) - 1);
					}

			        setParametres.doClick();
					JOptionPane.showMessageDialog((Component) null, "Simulation parameters successfully imported.");
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
            }
		});
		bottomPanel.add(importButton);
		
		JButton exportGrafikonButton = new JButton("Export graph");
		exportGrafikonButton.addActionListener(e -> {
			simulationSpeed.setValue(0);
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setSelectedFile(new File("Simulation_results.zip"));
	        int returnVal = fileChooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file.exists()) {
                	int result = JOptionPane.showConfirmDialog((Component) null, 
                			"Are you sure you want to overwrite that file?", 
                			"File with that name already exists", 
                			JOptionPane.YES_NO_CANCEL_OPTION);
                	if (result != 0) {
                		return;
                	}
                }
    			
                //kreiranje slike
                File imageFile = new File("Graph.png");
    			BufferedImage bImg = new BufferedImage(charts.getWidth(), charts.getHeight(), BufferedImage.TYPE_INT_RGB);
    		    Graphics2D cg = bImg.createGraphics();
    		    charts.paintAll(cg);
    		    try {
    		    	ImageIO.write(bImg, "png", imageFile);
    		    } catch (IOException e1) {}
    		    
    		    //kreiranje txt file-a
    		    File txtFile = new File("Simulation_results.txt");
    		    try {
    				List<String> rezultati = new ArrayList<String>();
    				List<Double> postotci = charts.dajPostotke();
    				String zaokruzeno;
    				String[] naziviSmjerova = {"Down: ", "Right: ", "Up: ", "Left: "};
    				
    				rezultati.add("--- Simulation results ---");
    				rezultati.add("");
    				rezultati.add("Passed: " + brojSekundi.getText() + " s");
    				rezultati.add("");
    				rezultati.add("Average time waiting per direction:");
    				
    				for (int i = 0; i < 4; i ++) {
        				zaokruzeno = String.format("%.2f", postotci.get(i) / 1000);
        				rezultati.add(naziviSmjerova[i] + zaokruzeno + " s");
    				}
					
					try (PrintWriter pw = new PrintWriter(txtFile.getAbsolutePath(), "UTF-8")) {
						rezultati.stream()
					          .forEachOrdered(pw::println);
					}
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
    		    
    		    //zipping
    		    byte[] buffer = new byte[1024];
    	    	try{
    	    		FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
    	    		ZipOutputStream zos = new ZipOutputStream(fos);
    	    		
    	    		ZipEntry ze= new ZipEntry("Graph.png");
    	    		zos.putNextEntry(ze);
    	    		FileInputStream in = new FileInputStream(imageFile.getAbsolutePath());
    	    		int len;
    	    		while ((len = in.read(buffer)) > 0) {
    	    			zos.write(buffer, 0, len);
    	    		}
    	    		in.close();
    	    		zos.closeEntry();
    	    		
    	    		ze= new ZipEntry("Simulation_results.txt");
    	    		zos.putNextEntry(ze);
    	    		in = new FileInputStream(txtFile.getAbsolutePath());
    	    		while ((len = in.read(buffer)) > 0) {
    	    			zos.write(buffer, 0, len);
    	    		}
    	    		in.close();
    	    		zos.closeEntry();
    	    		
    	    		zos.close();
    	    	} catch(IOException ex){
    	    	   ex.printStackTrace();
    	    	}
    	    	
    	    	imageFile.delete();
    	    	txtFile.delete();
    		    
    		    JOptionPane.showMessageDialog((Component) null, "Graph with simulation results successfully saved.");
            }
		});
		bottomPanel.add(exportGrafikonButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(e -> System.exit(0));
		bottomPanel.add(exitButton);
		
		optionsPanel.add(bottomPanel);
		
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 0;
		c.ipady = velicinaCharts;
		mainWindow.add(optionsPanel, c);
		
		JFrame window = new JFrame("Intersection simulator");
		window.setContentPane(mainWindow);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		
	}
	
	
	private static JSlider kreirajSimulationSpeed(int velicina) {
		JSlider simulationSpeed = new JSlider(JSlider.HORIZONTAL, 0, 16, 1);
		simulationSpeed.setMaximumSize(new Dimension(velicina, 50));
		simulationSpeed.setMajorTickSpacing(4);
		simulationSpeed.setMinorTickSpacing(1);
		simulationSpeed.setPaintTicks(true);
		simulationSpeed.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(new Integer(0), new JLabel("Stop"));
		labelTable.put(new Integer(2), new JLabel("2x"));
		labelTable.put(new Integer(4), new JLabel("4x"));
		labelTable.put(new Integer(8), new JLabel("8x"));
		labelTable.put(new Integer(12), new JLabel("12x"));
		labelTable.put(new Integer(16), new JLabel("16x"));
		simulationSpeed.setLabelTable(labelTable);
		return simulationSpeed;
	}
	
	private static void pokusajPostavitiParametre(int[] vrijednosti, JTextField[] textFieldovi, boolean provjeriZaDopunsko) {
		int maxGoreDolje, maxLijevoDesno, maxGoreDoljeLijevo, maxLijevoDesnoLijevo, ciklus = 0;
		if (provjeriZaDopunsko) {
			maxGoreDolje = Integer.max(semafor[0], semafor[2]);
			maxLijevoDesno = Integer.max(semafor[1], semafor[3]);
			maxGoreDoljeLijevo = Integer.max(semaforLijevo[0], semaforLijevo[2]);
			maxGoreDoljeLijevo = maxGoreDoljeLijevo > 0 ? maxGoreDoljeLijevo + 6 : 0;
			maxLijevoDesnoLijevo = Integer.max(semaforLijevo[1], semaforLijevo[3]);
			maxLijevoDesnoLijevo = maxLijevoDesnoLijevo > 0 ? maxLijevoDesnoLijevo + 6 : 0;
			ciklus = maxGoreDolje + maxLijevoDesno + maxGoreDoljeLijevo + maxLijevoDesnoLijevo + 12;
		}
		
		for (int i = 0; i < 4; i ++) {
			try {
				int pom = Integer.parseInt(textFieldovi[i].getText());
				if (pom < 0) {
					throw new Exception();
				}
				if (provjeriZaDopunsko) {
					if (pom > (ciklus - semafor[i] - 4)) {
						vrijednosti[i] = ciklus - semafor[i] - 4;
						textFieldovi[i].setText(ciklus - semafor[i] - 4 + "");
					}
					else {
						vrijednosti[i] = pom;
					}
				}
				else {
					vrijednosti[i] = pom;
				}
			}
			catch (Exception ex) {
				textFieldovi[i].setText(vrijednosti[i] + "");
			}
		}
	}
	
	private static void pokusajPostavitiParametre(int[] vrijednosti, JComboBox<ComboItem>[] comboBoxevi) {
		for (int i = 0; i < 4; i ++) {
			Object item = comboBoxevi[i].getSelectedItem();
			String value = ((ComboItem)item).getValue();
			vrijednosti[i] = Integer.parseInt(value);
		}
	}
	
	//MY CODE
	private static boolean controllerAction(JComboBox<ComboItem>[] comboBoxevi) {
		boolean choice = true;
		for (int i = 0; i < 1; i ++) {
//			Object item = comboBoxevi[0].getSelectedItem();
//			String value = ((ComboItem)item).getValue();
			String x = comboBoxevi[i].getSelectedItem().toString();
			if (x != "Fixed Time-Based Controller")
			{
				choice = false;
			}
		}
		return choice;
		
	}
	
	private static void iskljuciLijeveSemaforeAkoJePotrebno(JTextField[] semaforLijevoTF) {
		for (int i = 0; i < 4; i ++) {
			if (brojIzlaznihTraka[i] == 2 && vrstaIzlaznihTraka[i] == VrstaTrake.LIJEVO_GOREDESNO.getVrstaTrake()) {
				semaforLijevoTF[i].setEditable(true);
			}
			else {
				semaforLijevoTF[i].setEditable(false);
				semaforLijevo[i] = 0;
				semaforLijevoTF[i].setText(semaforLijevo[i] + "");
			}
		}
	}
	
	private static void iskljuciVrsteAkoJePotrebno(JComboBox<ComboItem>[] comboBoxovi) {
		for (int i = 0; i < 4; i ++) {
			comboBoxovi[i].setEnabled(brojIzlaznihTraka[i] == 1 ? false : true);
		}
	}
	
	private static void iskljuciUlazneTrakeAkoJePotrebno(JComboBox<ComboItem>[] comboBoxovi) {
		for (int i = 0; i < 4; i ++) {
			if (vrstaIzlaznihTraka[4 % (i < 3 ? i + 2 : i - 2)] == VrstaTrake.LIJEVOGORE_GOREDESNO.getVrstaTrake()) {
				brojUlaznihTraka[i] = 2;
				comboBoxovi[i].setSelectedIndex(brojUlaznihTraka[i] - 1);
			}
		}
	}
	
	
//	PARAMETER PANEL HERE
	private static void dodajHeadere(JPanel parametresPanel) {
		List<String> labele = Arrays.asList("", "Down", "Right", "Up", "Left");
		labele.stream()
			.forEach(labela -> {
				JLabel label = new JLabel(labela, SwingConstants.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				parametresPanel.add(label);
			});
	}
	
	private static JTextField[] dodajUParametresPanel(JPanel parametresPanel, String label, String tooltip, int[] defaultValues) {
		JLabel labela = new JLabel(label, SwingConstants.CENTER);
		labela.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		labela.setToolTipText(tooltip);
		parametresPanel.add(labela);
		JTextField[] textField = new JTextField[4];
		
		for (int i = 0; i < 4; i ++) {
			textField[i] = new JTextField("" + defaultValues[i]);
			textField[i].setHorizontalAlignment(JTextField.CENTER);
			textField[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			parametresPanel.add(textField[i]);
		}
		
		return textField;
	}
	
//	MY CODE
	private static JComboBox<ComboItem>[] choicePanel1(JPanel parametresPanel, String label, String tooltip, int[] defaultValues, 
			int limit, String[] labele) {
		JLabel labela = new JLabel(label, SwingConstants.CENTER);
		labela.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		labela.setToolTipText(tooltip);
		parametresPanel.add(labela);
		@SuppressWarnings("unchecked")
		JComboBox<ComboItem> comboBox[] = new JComboBox[2];
		
		for (int i = 0; i < 1; i ++) {
			comboBox[i] = new JComboBox<ComboItem>();
			dodajComboVrijednosti(comboBox[i], limit, defaultValues[i], labele);
			comboBox[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			parametresPanel.add(comboBox[i]);
		}

		return comboBox;
	}
	
	private static JTextField[][] dodajUParametresPanel(JPanel parametresPanel, String label, String tooltip, int[] defaultValues1, int[] defaultValues2) {
		JLabel labela = new JLabel(label, SwingConstants.CENTER);
		labela.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		labela.setToolTipText(tooltip);
		parametresPanel.add(labela);
		JTextField gustoca1TF[] = new JTextField[4];
		JTextField gustoca2TF[] = new JTextField[4];
		
		for (int i = 0; i < 4; i ++) {
			JPanel panel = new JPanel();
			gustoca1TF[i] = new JTextField("" + defaultValues1[i], 3);
			gustoca1TF[i].setHorizontalAlignment(JTextField.CENTER);
			panel.add(gustoca1TF[i]);
			Label crtica = new Label("-");
			panel.add(crtica);
			gustoca2TF[i] = new JTextField("" + defaultValues2[i], 3);
			gustoca2TF[i].setHorizontalAlignment(JTextField.CENTER);
			panel.add(gustoca2TF[i]);
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			parametresPanel.add(panel);
		}
		
		JTextField zaVratiti[][] = {gustoca1TF, gustoca2TF};
		return zaVratiti;
	}
	
	@SuppressWarnings("unchecked")
	private static JComboBox<ComboItem>[] dodajUParametresPanel(JPanel parametresPanel, String label, String tooltip, int[] defaultValues, 
			int limit, String[] labele) {
		JLabel labela = new JLabel(label, SwingConstants.CENTER);
		labela.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		labela.setToolTipText(tooltip);
		parametresPanel.add(labela);
		JComboBox<ComboItem> comboBox[] = new JComboBox[4];
		
		for (int i = 0; i < 4; i ++) {
			comboBox[i] = new JComboBox<ComboItem>();
			dodajComboVrijednosti(comboBox[i], limit, defaultValues[i], labele);
			comboBox[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			parametresPanel.add(comboBox[i]);
		}

		return comboBox;
	}
	
	private static void dodajComboVrijednosti (JComboBox<ComboItem> comboBox, int max, int defaultValue, String[] labele) {
		for (int i = 1; i <= max; i++) {
			comboBox.addItem(new ComboItem(labele[i - 1], i + ""));
		}
		comboBox.setSelectedIndex(defaultValue - 1);
	}
}
