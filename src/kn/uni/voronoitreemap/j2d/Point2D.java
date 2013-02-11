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

/**
 * Two-dimensional point, which also has functions as a two-dimensional vectors
 * @author Arlind Nocaj
 *
 */
public class Point2D {
	public double x;
	
	public double y;

	public Point2D(){		
	}
	
	public Point2D(double x, double y){
		this.x=x;
		this.y=y;
		
	}
	/**
	 * Computes the euclidean distance between (x1,y1) and (x2,y2)
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return euclidean distance between (x1,y1) and (x2,y2)
	 */
	
	public static double distance(double x1, double y1,
			  double x2, double y2)
	{
		 x1 -= x2;
		 y1 -= y2;
		 return Math.sqrt(x1 * x1 + y1 * y1);
	}
	 /**
	  * Computes the euclidean distance bewteen (px,py) to the current point
	  * @param px
	  * @param py
	  * @return
	  */
	 public double distance(double px, double py) {
			px -= this.x;
			py -= this.y;
			return Math.sqrt(px * px + py * py);
		    }
	
	 public double distance(Point2D point){
		 return distance(point.x,point.y);
	 }
	
	 public double getX(){
		 return x;	
	 }
	 
	 public double getY(){
		 return y;
	 }
	 
	 public String toString(){
			return "(" + x + "," + y + ")";
	 }
	 
	 public final void scale(double d)
	 {
	     x *= d;
	     y *= d;
	 }
	 /**
	  * Computes the dot-product of the given vector with position vector of the current point
	  * @param vector2d
	  * @return
	  */
	 public final double dot(Point2D vector2d)
	 {
	     return x * vector2d.x + y * vector2d.y;
	 }

	 public final double length()
	 {
	     return Math.sqrt(x * x + y * y);
	 }

	 public final double lengthSquared()
	 {
	     return x * x + y * y;
	 }
	 /**
	  * Scales the coordinates with 1/(length of given vector)
	  * @param vector2d
	  */
	 public final void normalize(Point2D vector2d)
	 {
	     double d = 1.0D / Math.sqrt(vector2d.x * vector2d.x + vector2d.y * vector2d.y);
	     x = vector2d.x * d;
	     y = vector2d.y * d;
	 }

	 public final void normalize()
	 {
	     double d = 1.0D / Math.sqrt(x * x + y * y);
	     x *= d;
	     y *= d;
	 }
	 /**
	  * Computes the angle between the given vector and the position vector of the current point
	  * @param vector2d
	  * @return
	  */
	 public final double angle(Point2D vector2d)
	 {
	     double d = dot(vector2d) / (length() * vector2d.length());
	     if(d < -1D)
	         d = -1D;
	     if(d > 1.0D)
	         d = 1.0D;
	     return Math.acos(d);
	 }

	public void setLocation(double x, double y) {
		this.x=x;
		this.y=y;
	}
}
