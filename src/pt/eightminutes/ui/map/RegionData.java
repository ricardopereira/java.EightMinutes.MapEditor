package pt.eightminutes.ui.map;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionData implements IRegion , Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;






	List<Point> lst=new ArrayList<Point>();
	
	

	
	
	
	List<Shape> areas=new ArrayList<Shape>();

	Map<Shape,String> names=new HashMap<Shape,String>();
	
	public String getAreaName(Shape p){
		return names.get(p);
	}
	
	@Override
	public void addNewPoint(Point p){
		lst.add(p);
	}

	@Override
	public List<Shape> getRegions(){
		return areas;
	}
	
	@Override
	public void defineNewShape(String name){
		
		if(lst.size()<2)
			return;
		Path2D.Double perimetro=new Path2D.Double();
		Point first=lst.get(0);
		perimetro.moveTo(first.getX(), first.getY());
		for(int i=1;i<lst.size();i++)
			perimetro.lineTo(lst.get(i).getX(), lst.get(i).getY());
		Area a=new Area(perimetro);
		//area is not serializable, so we'll get an equivalent Shape instead
		Shape s=AffineTransform.getTranslateInstance(0,0).createTransformedShape(a); 
		
		lst.clear();
		
		areas.add(s);
		names.put(s,name);
	}
	
	/* (non-Javadoc)
	 * @see godzilla.logic.IRegion#getRegion(java.awt.Point)
	 */
	@Override
	public String getRegion(Point p){
		for(Shape a: areas)
			if(a.contains(p))
				return names.get(a);
		return null;
	}

	@Override
	public List<Point> getPoints() {
		
		return lst;
	}

	@Override
	public Point getCenterPoint(String location) {
		Shape s=getShape(location);
		if(s==null)
			return null;
		Rectangle2D bound=s.getBounds2D();
		return new Point((int)(bound.getCenterX()),(int)(bound.getCenterY()));
	}

	@Override
	public Shape getShape(String name) {
		for(Shape s: names.keySet())
			if(names.get(s).equals(name))
				return s;
		return null;
	}
	


}
