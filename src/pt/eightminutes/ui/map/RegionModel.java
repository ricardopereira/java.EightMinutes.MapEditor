package pt.eightminutes.ui.map;

import java.awt.Point;
import java.awt.Shape;
import java.util.List;
import java.util.Observable;



public class RegionModel  extends Observable implements IRegion{

	
	private static final long serialVersionUID = 1L;
	IRegion r;
	
	public RegionModel(IRegion r){this.r=r;}
	
	public IRegion getRegion(){return r;}
	
	public void setRegion(IRegion r){
		this.r=r;
		setChanged();
		notifyObservers();
	}

	
	@Override
	public void addNewPoint(Point p) {
		r.addNewPoint(p);
		setChanged();
		notifyObservers();
	}

	@Override
	public void defineNewShape(String name) {
		r.defineNewShape(name);
		setChanged();
		notifyObservers();
	}

	@Override
	public String getRegion(Point p) {
		return r.getRegion(p);
	}

	@Override
	public List<Point> getPoints() {
		return r.getPoints();
	}

	@Override
	public List<Shape> getRegions() {
		return r.getRegions();
	}

	@Override
	public String getAreaName(Shape p) {
		return r.getAreaName(p);
	}

	@Override
	public Point getCenterPoint(String location) {
		return r.getCenterPoint(location);
	}

	@Override
	public Shape getShape(String name) {
		
		return r.getShape(name);
	}


}
