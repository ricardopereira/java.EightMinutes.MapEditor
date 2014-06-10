package pt.eightminutes.ui.map;

import java.awt.Point;
import java.awt.Shape;
import java.io.Serializable;
import java.util.List;

public interface IRegion extends Serializable{

	public abstract void addNewPoint(Point p);
	public abstract List<Point> getPoints();

	public abstract void defineNewShape(String name);

	public abstract String getRegion(Point p);
	public abstract List<Shape> getRegions();
	public abstract String getAreaName(Shape p);
	public Point getCenterPoint(String location);
	public Shape getShape(String name);
}