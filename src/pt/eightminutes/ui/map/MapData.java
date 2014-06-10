package pt.eightminutes.ui.map;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapData extends RegionData implements IMapData{

	private static final long serialVersionUID = 1L;
	transient Image mapBackground;
	String mapBackgroundName;
	
	public void setMapBackground(File f){
		mapBackgroundName=f.getName();
		mapBackground=null;		
	}

	@Override
	public Image getMapBackground() {
		
		if(mapBackground==null && (mapBackgroundName!=null))
			try {
				System.out.println("Opening map "+mapBackgroundName);
				mapBackground = ImageIO.read(new File("src/pt/eightminutes/ui/graphical/resources/images/"+mapBackgroundName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		return mapBackground;
	}

}
