/*******************************************************************************
 * Copyright (c) 2013 Arlind Nocaj, University of Konstanz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * For distributors of proprietary software, other licensing is possible on request: arlind.nocaj@gmail.com
 * 
 * This work is based on the publication below, please cite on usage, e.g.,  when publishing an article.
 * Arlind Nocaj, Ulrik Brandes, "Computing Voronoi Treemaps: Faster, Simpler, and Resolution-independent", Computer Graphics Forum, vol. 31, no. 3, June 2012, pp. 855-864
 ******************************************************************************/
package kn.uni.voronoitreemap.j2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import kn.uni.voronoitreemap.convexHull.JVertex;
import kn.uni.voronoitreemap.debuge.Colors;
import kn.uni.voronoitreemap.extension.VoroCellObject;


/**
 * A site is a 2 dimensional point which has a weight.
 * 
 * @author Arlind Nocaj
 *
 */
public class ASite extends JVertex implements Comparable<Site>, iSite {
 protected static final double nearlyZero = 1E-10;
protected double weight=nearlyZero;
 protected double percentage=nearlyZero;
 
 protected PolygonSimple polygon;
 public PolygonSimple nonClippedPolyon;
 protected ArrayList<Site> neighbours;
 
 public VoroCellObject cellObject;
 /**
  * Preflow Extrapolation
  */
 public Point2D preflowVector=new Point2D();
private ArrayList<Site> oldNeighbors;
 
 /**
  * 
  * @param x
  * @param y
  * @param weight
  * @return
  */
 
 /* 
  * return the dual point of the plane which is projected, see Aurenhammer,1987, Power Diagrams
  */
 private static double projectZ(double x,double y,double weight){
	 return x*x+y*y-weight;
 }
 
 
 /* (non-Javadoc)
 * @see j2d.iSite#getPolygon()
 */
public PolygonSimple getPolygon(){
	 return polygon;
 }
 
 public String toString(){
	 return "("+ getX()+"\t,"+getY()+","+weight+","+percentage+")+z: "+z;
 }
 
 /* (non-Javadoc)
 * @see j2d.iSite#setPolygon(j2d.NPoly)
 */
public void setPolygon(PolygonSimple poly){
	 polygon=poly;
 }
 
 /* (non-Javadoc)
 * @see j2d.iSite#setNeighbours(java.util.ArrayList)
 */
public void setNeighbours(ArrayList<Site> list){
	this.setOldNeighbors(neighbours);
	 neighbours=list;
 }
 
 /* (non-Javadoc)
 * @see j2d.iSite#getNeighbours()
 */
public ArrayList<Site> getNeighbours(){
	 return neighbours;
 }
 
 /**
  * 
  * @param x x-coordinate
  * @param y y-coordinate
  */
 public ASite(double x, double y){
	 super(x,y,projectZ(x, y, nearlyZero));
	 this.weight=nearlyZero;
 }
 
 /**
  * 
  * @param x x-coordinate
  * @param y y-coordinate
  * @param weight the weight of the site, where weight=radius*radius which is used for weighting
  */
 public ASite(double x, double y, double weight){
	 super(x,y,projectZ(x, y, weight));
	 this.weight=weight;
 }
 
 public ASite(double x, double y, double weight, double percentage){
	 super(x,y,projectZ(x, y, weight));
	 this.weight=weight;
	 this.percentage=percentage;
	 
 }
 
 /* (non-Javadoc)
 * @see j2d.iSite#getWeight()
 */
public double getWeight(){
	 return weight;
 }
 
 /* (non-Javadoc)
 * @see j2d.iSite#setWeight(double)
 */
public void setWeight(double weight){
	this.weight=weight;
	project();
 }
 
 /* (non-Javadoc)
 * @see j2d.iSite#setXY(double, double)
 */
public void setXY(double x, double y){
	 this.x=x;
	 this.y=y;
	 project();
 }
 
 /* (non-Javadoc)
 * @see j2d.iSite#setXYW(double, double, double)
 */
public void setXYW(double x,double y, double weight){
	 this.x=x;
	 this.y=y;
	 this.weight=weight;
	z=projectZ(x, y, weight);
 }
 private void project() {
	z=projectZ(x, y, weight);
}

/* (non-Javadoc)
 * @see j2d.iSite#setX(double)
 */
public void setX(double x) {
		this.x = x;
		project();
	}
	/* (non-Javadoc)
	 * @see j2d.iSite#setY(double)
	 */
	public void setY(double y) {
		this.y = y;
		project();
	}

/**
 * The sites are first ordered according to the x-axis and then according to the y axis
 */
@Override
public int compareTo(Site b) {
	if (this.x<b.x) return -1;
	if (this.x>b.x) return 1;
	if (this.x==b.x){
		if (this.y<b.y) return -1;
		if (this.y>b.y) return 1;
		if (this.y==b.y)return 0;			
	}
	return 0;
}

public void paint(Graphics2D g){
	
	int radius = (int)Math.sqrt(this.getWeight());
//	g.fillOval((int)this.getX()-radius, (int)this.getY()-radius, 2*radius, 2*radius);
	g.setColor(Colors.circleBorder);
	g.drawOval((int)this.getX()-radius, (int)this.getY()-radius, 2*radius, 2*radius);
	g.setColor(Color.yellow);
	int width=1;
	g.fillRect((int)this.getX()-width, (int)this.getY()-width, 2*width,2*width );
	
}



/* (non-Javadoc)
 * @see j2d.iSite#setPercentage(double)
 */
public void setPercentage(double percentage) {
	this.percentage = percentage;
}
/* (non-Javadoc)
 * @see j2d.iSite#getPercentage()
 */
public double getPercentage() {
	return percentage;
}

/* (non-Javadoc)
 * @see j2d.iSite#getPoint()
 */
public Point2D getPoint(){
	return new Point2D(getX(),getY());
}

/**
 * Euclid
 * @param point
 * @return
 */
public double distance(Site point) {
	double dx = x-point.getX();
	double dy = y-point.getY();
	return Math.sqrt(dx*dx+dy*dy);
}

public double distanceCircles(Site point){
	double dx = x-point.getX();
	double dy = y-point.getY();
	double radius1 = Math.sqrt(weight);
	double radius2=Math.sqrt(point.weight);
	return Math.sqrt(dx*dx+dy*dy)-radius1-radius2;
}


public ArrayList<Site> getOldNeighbors() {
	return oldNeighbors;
}


private void setOldNeighbors(ArrayList<Site> oldNeighbors) {
	this.oldNeighbors = oldNeighbors;
}



}
