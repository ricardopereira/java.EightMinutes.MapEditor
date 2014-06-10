package pt.eightminutes.ui.map;

import java.awt.Image;
import java.io.File;




public interface IMapData extends IRegion {
	public void setMapBackground(File f);
	public Image getMapBackground();
}
