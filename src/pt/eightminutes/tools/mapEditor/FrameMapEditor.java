package pt.eightminutes.tools.mapEditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pt.eightminutes.ui.map.IMapData;
import pt.eightminutes.ui.map.MapData;
import pt.eightminutes.ui.map.MapDataModel;
import pt.eightminutes.ui.map.RegionData;
import pt.eightminutes.ui.map.RegionModel;

class MapBackground extends JPanel implements Observer {
	
	MapDataModel model;
	String overLocation=null;
	
	Shape highlight=null;
	
	MapBackground(MapDataModel themodel) {
		themodel.addObserver(this);
		this.model = themodel;
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent ev){
				model.addNewPoint(getMousePosition());
			}
		});
		
		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				//no need, just for illustration purposes...
			}
			});
		
		addMouseMotionListener(new MouseMotionAdapter(){
			
			@Override
			public void mouseMoved(MouseEvent ev){
				String s=model.getRegion(ev.getPoint());
				if(s!=overLocation){
					overLocation=s;
					repaint();
				}
			}
			
		});
	}
	
	@Override
	public void update(Observable o, Object arg) {
	
		repaint();	
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int x=0,y=0;

		Image img = model.getMapBackground();
	
		if (img != null)
			g.drawImage(img, x, y,  null);
		else
			g.drawString("Abra uma imagem antes de continuar", x+10, y+10);

		
		if(overLocation!=null){
			g.drawString(overLocation, x+10, y+10);
		}

		List<Point> lst=model.getPoints();
	
		if(lst.size()>=1){
			Point p1=lst.get(0);
			int diameter=10;
			int x1=(int)(p1.getX());
			int y1=(int)(p1.getY());
			g.drawOval(x1-diameter/2, y1-diameter/2, diameter, diameter);
		}
		
		for(int i=0;i<lst.size()-1;i++)
		{
			Point p1=lst.get(i);
			Point p2=lst.get(i+1);
			
			int x1=(int)(p1.getX());
			int x2=(int)(p2.getX());
			int y1=(int)(p1.getY());
			int y2=(int)(p2.getY());
			
			g.drawLine(x1, y1, x2, y2);
			int diameter=10;
			g.drawOval(x2-diameter/2, y2-diameter/2, diameter, diameter);	
		}
		

		for(Shape a:model.getRegions())
		{
			Graphics2D g2d=(Graphics2D)g;
			g2d.draw(a);
		}
		if(!model.getRegions().isEmpty())
			highlight=model.getRegions().get(model.getRegions().size()-1);
		
		if(highlight!=null)
		{
			Graphics2D g2d=(Graphics2D)g;
			g2d.fill(highlight);
		}
	}

}

public class FrameMapEditor extends JFrame implements Observer {

	MapDataModel model=new MapDataModel(new MapData());
	
	
	class NewMapListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc=new JFileChooser();
			int retval=jfc.showOpenDialog(FrameMapEditor.this);
			if(retval == JFileChooser.APPROVE_OPTION){
				model.setMapData(new MapData()); //create new empty map model
				loadBackground(jfc.getSelectedFile());
			}
		}
	}
	
	private ActionListener saveListener=new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc=new JFileChooser();
			int retval=jfc.showSaveDialog(FrameMapEditor.this);
			if(retval == JFileChooser.APPROVE_OPTION){
				saveMap(jfc.getSelectedFile());
			}
		}		
	};
	

	private ActionListener loadListener=new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc=new JFileChooser();
			int retval=jfc.showOpenDialog(FrameMapEditor.this);
			if(retval == JFileChooser.APPROVE_OPTION){
				loadMap(jfc.getSelectedFile());
			}
		}		
	};
	

	private void saveMap(File f){
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(model.getMapData());
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(File f){
		try {
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
			IMapData mr=(IMapData)(ois.readObject());
			model.setMapData(mr);
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void loadBackground(File f){
		
			model.setMapBackground(f);
		
	}
	
	FrameMapEditor(String name, int h, int w){
		super(name);
		setSize(h,w);
		buildLayout();
		registerListeners();
		buildMenus();
		setVisible(true);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		model.addObserver(this);
		
		File f = new File("map.gif");
		model.setMapBackground(f);
		System.out.println(f.getName());
	}
	
	JButton newButton=new JButton("Novo");
	
	class AddButton extends JButton implements Observer{
		MapDataModel model;
		
		public AddButton(String n, MapDataModel model){
		super(n);
		model.addObserver(this);
		this.model=model;
		setEnabled(model.getPoints().size()>=2);
		}

		@Override
		public void update(Observable o, Object arg) {
			setEnabled(model.getPoints().size()>=2);
		}
	}
	
	JButton addRegionButton=new AddButton("Acrescentar Regiao", model);


	private MapBackground mapPanel;
	
	private final void buildLayout(){
		Container c=getContentPane();
		c.setLayout(new BorderLayout());
		mapPanel=new MapBackground(model);
		c.add(mapPanel, BorderLayout.CENTER);
		JPanel southPanel=new JPanel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(newButton);
		southPanel.add(addRegionButton);
		c.add(southPanel, BorderLayout.SOUTH);
		
	}
	
	private final void buildMenus(){
		JMenuBar menu=new JMenuBar();
		JMenu file=new JMenu("File");
		JMenu about=new JMenu("About");
		menu.add(file);
		menu.add(about);
		JMenuItem newMapMenu=new JMenuItem("New map");
		JMenuItem open=new JMenuItem("Open...");
		JMenuItem save=new JMenuItem("Save...");
		JMenuItem exitMenu=new JMenuItem("Exit");
		file.add(newMapMenu);
		file.addSeparator();
		file.add(open);
		file.add(save);
		file.addSeparator();
		file.add(exitMenu);
		setJMenuBar(menu);
		
		exitMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			
		});
		
		newMapMenu.addActionListener(new NewMapListener());
		save.addActionListener(saveListener);
		open.addActionListener(loadListener);
	}
	
	private final void registerListeners(){
		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e){
			
			}
			
		});
		

		
		
		newButton.addActionListener(new NewMapListener());
		addRegionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				String s = (String)JOptionPane.showInputDialog(
	                    FrameMapEditor.this,
	                    "Please input name of region",
	                    "New Region",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    "");

				if ((s != null) && (s.length() > 0)) {
					model.defineNewShape(s);
					}
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
	//	setResizable(model.getPoints().isEmpty()&&model.getRegions().isEmpty());
		//mapPanel.resized();
	}
	
	
}
