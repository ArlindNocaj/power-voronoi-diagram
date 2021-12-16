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


import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import kn.uni.voronoitreemap.convexClip.ConvexClip;
import kn.uni.voronoitreemap.convexClip.cVertex;
import kn.uni.voronoitreemap.convexClip.cVertexList;


/**
 * Implements a simple polygon with one continous region, by using two double arrays.
 * @author Arlind Nocaj
 *
 */
public class PolygonSimple implements Shape, Cloneable, Iterable<Point2D>{
	/**
	 * centroid of the polygon is stored for faster access, once it is computed
	 */
	protected Point2D centroid;
	protected double area = -1;
	protected Rectangle2D.Double bounds;

	/**
	 * Stores the orginal polygon result, without shrinking
	 */
	protected PolygonSimple oldPolygon;

	protected double[] x; /** x-coordinates */
	protected double[] y; /** y-coordinates */
	public int length = 0;
	
	
	
	public PolygonSimple() {
		x = new double[16];
		y = new double[16];
	}

	/**
	 * 
	 * @param numberPoints initial array size, default initial array size is 16.
	 */
	public PolygonSimple(int numberPoints) {
		if (numberPoints > 2) {
			x = new double[numberPoints];
			y = new double[numberPoints];
		} else {
			x = new double[16];
			y = new double[16];
		}
	}
	
	/**
	 * 
	 * @param xPoints x-coordinate of the polygon points
	 * @param yPoints y-coordinate of the polygon points
	 * @param length number of elements which should be considered from the given arrays
	 */
	public PolygonSimple(double[] xPoints, double[] yPoints, int length ){
		bounds = null;
		centroid = null;
		area = -1;
		this.x = Arrays.copyOf(xPoints, length);
		this.y = Arrays.copyOf(yPoints, length);
		this.length = length;
	}
	
	/**
	 * 
	 * @param xPoints x-coordinate of the polygon points
	 * @param yPoints y-coordinate of the polygon points
	 */
	public PolygonSimple(double[] xPoints, double[] yPoints){
		int length=xPoints.length;
		bounds = null;
		centroid = null;
		area = -1;
		this.x = Arrays.copyOf(xPoints, length);
		this.y = Arrays.copyOf(yPoints, length);
		this.length = length;
	}

	/**
	 * tests whether the given point is contained in the polygon (linear time).
	 */
	@Override
	public boolean contains(double inX, double inY) {
		boolean contains = false;
		if (bounds == null)
			getBounds();
		if (!bounds.contains(inX, inY)) {
			return false;
		}
		// Take a horizontal ray from (inX,inY) to the right.
		// If ray across the polygon edges an odd # of times, the point is
		// inside.
		for (int i = 0, j = length - 1; i < length; j = i++) {
			if ((((y[i] <= inY) && (inY < y[j]) || ((y[j] <= inY) && (inY < y[i]))) && (inX < (x[j] - x[i])
					* (inY - y[i]) / (y[j] - y[i]) + x[i])))
				contains = !contains;
		}
		return contains;
	}
/**
 * {@link #contains(double, double, double, double)}
 */
	@Override
	public boolean contains(Rectangle2D r) {
		return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	/**
	 * {@link #contains(double, double)}
	 */
	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}
	
	/**
	 * {@link #contains(double, double)}
	 */
	@Override
	public boolean contains(java.awt.geom.Point2D p) {
		return contains(p.getX(), p.getY());
	}
	
	/**
	 * tests whether each corner point of the given rectangle is contained in the polygon.
	 */
	@Override
	public boolean contains(double x, double y, double w, double h) {
		if (bounds == null)
			getBounds2D();
		if (bounds.contains(x, y, w, h)) {
			if (contains(x, y) && contains(x + w, y) && contains(x, y + h)
					&& contains(x + w, y + h))
				return true;
			return false;
		} else
			return false;
	}

	@Override
	public Rectangle2D getBounds2D() {
		bounds=null;
		if (bounds == null) {
			getBounds();
		}
		return bounds;
	}


	/**
	 * Returns the path iterator for the shape(rather slow compared to using the point arrays directly).
	 */
	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return new PolygonPathIterator(this, at);
	}

	/**
	 * {@link #getPathIterator(AffineTransform)}
	 */
	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return getPathIterator(at);
	}

	class PolygonPathIterator implements PathIterator {
		PolygonSimple poly;
		AffineTransform transform;
		int index;

		public PolygonPathIterator(PolygonSimple pg, AffineTransform at) {
			poly = pg;
			transform = at;
			if (pg.getNumPoints() == 0) {
				// Prevent a spurious SEG_CLOSE segment
				index = 1;
			}
		}

		/**
		 * Returns the winding rule for determining the interior of the path.
		 * 
		 * @return an integer representing the current winding rule.
		 * @see PathIterator#WIND_NON_ZERO
		 */
		public int getWindingRule() {
			return WIND_EVEN_ODD;
		}

		/**
		 * Tests if there are more points to read.
		 * 
		 * @return <code>true</code> if there are more points to read;
		 *         <code>false</code> otherwise.
		 */
		public boolean isDone() {
			return index > poly.getNumPoints();
		}

		/**
		 * Moves the iterator forwards, along the primary direction of
		 * traversal, to the next segment of the path when there are more points
		 * in that direction.
		 */
		public void next() {
			index++;
		}

		/**
		 * Returns the coordinates and type of the current path segment in the
		 * iteration. The return value is the path segment type: SEG_MOVETO,
		 * SEG_LINETO, or SEG_CLOSE. A <code>float</code> array of length 2 must
		 * be passed in and can be used to store the coordinates of the
		 * point(s). Each point is stored as a pair of <code>float</code>
		 * x,&nbsp;y coordinates. SEG_MOVETO and SEG_LINETO types return one
		 * point, and SEG_CLOSE does not return any points.
		 * 
		 * @param coords
		 *            a <code>float</code> array that specifies the coordinates
		 *            of the point(s)
		 * @return an integer representing the type and coordinates of the
		 *         current path segment.
		 * @see PathIterator#SEG_MOVETO
		 * @see PathIterator#SEG_LINETO
		 * @see PathIterator#SEG_CLOSE
		 */
		public int currentSegment(float[] coords) {
			if (index >= poly.getNumPoints()) {
				return SEG_CLOSE;
			}
			coords[0] = (float) poly.x[index];
			coords[1] = (float) poly.y[index];
			if (transform != null) {
				transform.transform(coords, 0, coords, 0, 1);
			}
			return (index == 0 ? SEG_MOVETO : SEG_LINETO);
		}

		/**
		 * Returns the coordinates and type of the current path segment in the
		 * iteration. The return value is the path segment type: SEG_MOVETO,
		 * SEG_LINETO, or SEG_CLOSE. A <code>double</code> array of length 2
		 * must be passed in and can be used to store the coordinates of the
		 * point(s). Each point is stored as a pair of <code>double</code>
		 * x,&nbsp;y coordinates. SEG_MOVETO and SEG_LINETO types return one
		 * point, and SEG_CLOSE does not return any points.
		 * 
		 * @param coords
		 *            a <code>double</code> array that specifies the coordinates
		 *            of the point(s)
		 * @return an integer representing the type and coordinates of the
		 *         current path segment.
		 * @see PathIterator#SEG_MOVETO
		 * @see PathIterator#SEG_LINETO
		 * @see PathIterator#SEG_CLOSE
		 */
		public int currentSegment(double[] coords) {
			if (index >= poly.length) {
				return SEG_CLOSE;
			}
			coords[0] = poly.x[index];
			coords[1] = poly.y[index];
			if (transform != null) {
				transform.transform(coords, 0, coords, 0, 1);
			}
			return (index == 0 ? SEG_MOVETO : SEG_LINETO);
		}
	}
	
	/**
	 * tests whether the given rectangle will intersect to the bounds of the polygon.
	 */
	@Override
	public boolean intersects(Rectangle2D r) {
		if (bounds==null){
		getBounds();
		}
		return bounds.intersects(r);
	}

	/**
	 * {@link #intersects(Rectangle2D)}
	 */
	@Override
	public boolean intersects(double x, double y, double w, double h) {
		if (bounds==null){
			getBounds();
		}
		return bounds.intersects(x, y, w, h);
	}

	/**
	 * Returns the bounding rectangle of this polygon.
	 */
	public Rectangle getBounds() {
		bounds=null;
		if (bounds == null) {
			double xmin = Double.MAX_VALUE;
			double ymin = Double.MAX_VALUE;
			double xmax = Double.MIN_VALUE;
			double ymax = Double.MIN_VALUE;

			for (int i = 0; i < length; i++) {
				double x = this.x[i];
				double y = this.y[i];
				if (x < xmin)
					xmin = x;
				if (x > xmax)
					xmax = x;
				if (y < ymin)
					ymin = y;
				if (y > ymax)
					ymax = y;
			}
			bounds = new Rectangle2D.Double(xmin, ymin, (xmax - xmin),
					(ymax - ymin));
		}
		return bounds.getBounds();
	}

	/**
	 * Return the number of points in this polygon.
	 * @return integer number of points
	 */
	public int getNumPoints(){
		return length;
	}
	
	/**
	 * Adds a point to the polygon. Extends the corresponding array if necessary.
	 */
	public void add(double x, double y) {
		if (this.x.length <= length) {
			double[] newX = new double[this.x.length * 2];
			double[] newY = new double[this.x.length * 2];
			System.arraycopy(this.x, 0, newX, 0, length);
			System.arraycopy(this.y, 0, newY, 0, length);
			this.x = newX;
			this.y = newY;

		}
		this.x[length] = x;
		this.y[length] = y;
		length++;

	}

	public void clearCacheOnly() {
		this.centroid=null;
		this.bounds=null;
		this.area=-1;
		if(this.oldPolygon!=null)
			oldPolygon.clearCacheOnly();
	}

	/**
	 * {@link #add(double, double)}
	 */
	public void add(Point2D p) {
		add(p.x, p.y);
	}

	
	/**
	 * Uses the linear time algorithm of O'Rourke to compute the intersection of
	 * two convex polygons.
	 * 
	 * @param poly
	 * @return
	 */
	public PolygonSimple convexClip(PolygonSimple poly) {
		//bounding box have to match for intersection
		if (!this.getBounds2D().intersects(poly.getBounds2D()))
			return null;
		//check if bounding box corners are in polygon: then poly is contained completely inside the outer polygon
		if (this.contains(poly.getBounds2D()))
			return poly;		

		//bounding boxes intersect 
	
		// to vertexList
		cVertexList list1 = this.getVertexList();
		cVertexList list2 = poly.getVertexList();
		ConvexClip clipper = new ConvexClip();
		clipper.Start(list1, list2);
		PolygonSimple res = new PolygonSimple();
		if (clipper.inters != null && clipper.inters.n > 0) {
			cVertex node = clipper.inters.head;
			double firstX = node.v.x;
			double firstY = node.v.y;
			res.add(node.v.x, node.v.y);
			double lastX = node.v.x;
			double lastY = node.v.y;
			for (int i = 1; i < clipper.inters.n; i++) {
				node = node.next;

				if (lastX != node.v.x || lastY != node.v.y) {// do not add point
																// if its the
																// same as
																// before
					if (i != (clipper.inters.n - 1) || (node.v.x != firstX)
							|| node.v.y != firstY) {// do not add if it is the
													// end point and the same as
													// the first point
						res.add(node.v.x, node.v.y);
					}
				}

			}
			return res;
		} 
		//no intersection between the two polygons, so check if one is inside the other
		if(contains(poly.x[0],poly.y[0]))
			return poly;
		
		// no intersection between the polygons at all
		return null;
	}

	private cVertexList getVertexList() {
		cVertexList list = new cVertexList();
		for (int i = length - 1; i >= 0; i--) {
			cVertex vertex = new cVertex(x[i], y[i]);
			list.InsertBeforeHead(vertex);
		}
		return list;

	}

	/**
	 * Returns the area of the polygon.
	 */
	public double getArea() {
		if (area > 0) {
			return area;
		}
		double area = 0;
		// we can implement it like this because the polygon is closed
		// (point2D.get(0) = point2D.get(length + 1)
		int size = length - 1;
		for (int i = 0; i < size; i++) {
			area += (x[i] * y[i + 1] - x[i + 1] * y[i]);
		}
		area += (x[size] * y[0] - x[0] * y[size]);
		this.area = Math.abs(area) * 0.5;
		return this.area;
	}

	/**
	 * Computes the centroid of a polygon.
	 * 
	 * @return centroid point
	 */
	public Point2D getCentroid() {
		if (centroid == null) {
			double xv = 0;
			double yv = 0;
			double areaQuotient = getArea() * 6;
			for (int i = 0; i < length; i++) {
				double temp = (x[i] * y[(i + 1) % length] - x[(i + 1) % length]
						* y[i]);
				xv += (x[i] + x[(i + 1) % length]) * temp;
				yv += (y[i] + y[(i + 1) % length]) * temp;
			}
			xv = xv / areaQuotient;
			yv = yv / areaQuotient;
			this.centroid = new Point2D(xv, yv);
		}

		return centroid;
	}

	@Override
	public PolygonSimple clone() {
		PolygonSimple p = new PolygonSimple(this.getXPoints(),this.getYPoints(),length);
		p.oldPolygon = this.oldPolygon;
		return p;
	}

	/**
	 * Array with x-values of the polygon points.
	 * @return 
	 */
	public double[] getXPoints() {
		return x;
	}

	/**
	 * Array with y-values of the polygon points.
	 * @return
	 */
	public double[] getYPoints() {
		return y;
	}

	@Override
	public Iterator<Point2D> iterator() {
		return new Iterator<Point2D>() {
			int i=0;
			@Override
			public boolean hasNext() {
			return i<length;
			}

			@Override
			public Point2D next() {
				Point2D p = new Point2D(x[i],y[i]);
				i++;				
				return p; 
			}

			@Override
			public void remove() {
				
			}
			
			
		};
	}
}
