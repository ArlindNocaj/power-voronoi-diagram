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
import kn.uni.voronoitreemap.helper.Geometry;


/**
 * Implements a simple polygon with one continous region, by using two double arrays.
 * @author Arlind Nocaj
 *
 */
public class PolygonSimple implements Shape, Cloneable, Iterable<Point2D>{

	/** 
	 * Used for generation of a random point in a polygon.
	 */
	protected Random seed = new Random(5);

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
	 * Replaces the pointers of the coordinate arrays to show to the given coordinate arrays.
	 */
	public void PolygonSimple(PolygonSimple simple) {
		bounds = null;
		centroid = null;
		area = -1;
		this.x = simple.x;
		this.y = simple.y;
		length = simple.length;
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

	/**
	 * Scales all points by multiplying with the scalingFactor
	 * 
	 * @param scalingFactor
	 */
	public void scale(double scalingFactor) {
		for (int i = 0; i < length; i++) {
			x[i] = x[i] * scalingFactor;
			y[i] = y[i] * scalingFactor;
		}
		clearCacheOnly();
	}

	/**
	 * Translates all points of the polygon by adding the values
	 * @param tx translation on x
	 * @param ty translation on y
	 */
	public void translate(double tx, double ty) {
		
		for (int i = 0; i < length; i++) {
			x[i] = x[i] + tx;
			y[i] = y[i] + ty;
		}
		clearCacheOnly();
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
//		list1.PrintVertices();
//		list2.PrintVertices();
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

	/**
	 * Debugging only
	 * @param poly
	 */
	private Point2D containsPoly(PolygonSimple poly) {
		boolean inside=true;
			for(Point2D p:poly){
				if(!this.contains(p)){
					System.out.println(p);
					inside=false;
					return p;
					
				}
			}
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
	 * For the given point, the minimal distance to the segments of the polygon
	 * is computed.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double getMinDistanceToBorder(double x, double y) {
		double result = Geometry.distancePointToSegment(this.x[length - 1],
				this.y[length - 1], this.x[0], this.y[0], x, y);
		for (int i = 0; i < (length - 1); i++) {
			double distance = Geometry.distancePointToSegment(this.x[i],
					this.y[i], this.x[i + 1], this.y[i + 1], x, y);
			if (distance < result) {
				result = distance;
			}
		}
		return result;

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
	 * Default percentage can be 0.96
	 * 
	 * @param percentage
	 */
	public void shrinkForBorder(double percentage) {
		oldPolygon = (PolygonSimple) this.clone();
		getCentroid();
		double cx = centroid.getX();
		double cy = centroid.getY();
		for (int i = 0; i < length; i++) {

			double deltaX = x[i] - cx;
			double deltaY = y[i] - cy;
			double xnew = cx + deltaX * percentage;
			double ynew = cy + deltaY * percentage;
			x[i] = xnew;
			y[i] = ynew;
		}

		// /**
		// * Method where you use the angle bisector of three points to shrink
		// it.
		// */
		// double[] xnew = new double[x.length];
		// double[] ynew=new double[y.length];
		//	
		// Point2D p0=null;
		// Point2D p1=new Point2D.Double(x[length-1], y[length-1]);
		// Point2D p2=new Point2D.Double(x[0],y[0]);
		// double borderWidth=10;
		// for (int i=1;i<=length;i++){
		//			
		// p0=p1;
		// p1=p2;
		// if (i==length){
		// p2=new Point2D.Double(x[0],y[0]);
		// }else{
		// p2=new Point2D.Double(x[i], y[i]);
		// }
		//			
		// double endPointX =(p0.getX()+p2.getX())/2;
		// double endPointY=(p0.getY()+p2.getY())/2;
		// double deltaX=endPointX-p1.getX();
		// double deltaY=endPointY-p1.getY();
		//			
		// double euclidLength = Math.sqrt(deltaX*deltaX+deltaY*deltaY);
		//			
		//			
		// deltaX=deltaX/euclidLength;
		// deltaY=deltaY/euclidLength;
		//			
		// deltaX=deltaX*borderWidth;
		// deltaY=deltaY*borderWidth;
		//			
		// xnew[i-1]=p1.getX()+deltaX;
		// ynew[i-1]=p1.getY()+deltaY;
		// // }
		//			
		//		
		// }
		// // xnew[length]=xnew[0];
		// // ynew[length]=ynew[0];
		//		
		// x=xnew;
		// y=ynew;

	}
	/**
	 * We get a vector which describes where the point should be relative to the
	 * center. We change the length of the vector so that the point fits in the
	 * polygon. (reimplementation needed here)
	 * 
	 * @return Point which is contained by this polygon and has same direction
	 *         as the given vector point
	 */
	public Point2D getRelativePosition(Point2D vector) {

		getCentroid();

		double endPointX = centroid.getX() + vector.getX();
		double endPointY = centroid.getY() + vector.getY();
		Point2D endPoint = new Point2D(endPointX, endPointY);
		if (contains(endPointX, endPointY)) {
			return new Point2D(endPointX, endPointY);
		} else {
			double endPointX2 = centroid.getX() + vector.getX() * 0.85;
			double endPointY2 = centroid.getY() + vector.getY() * 0.85;
			if (contains(endPointX2, endPointY2)) {
				return new Point2D(endPointX2, endPointY2);
			}
		}
		Point2D p1 = null;
		Point2D p2 = new Point2D(x[0], y[0]);
		Point2D result = null;
		for (int i = 1; i <= length; i++) {

			p1 = p2;
			//TODO Keine Ahnung ob richtig
			if(i == length){
				p2 = new Point2D(0,0);
			}else{
				p2 = new Point2D(x[i], y[i]);
			}
			Point2D intersection = getIntersection(p1, p2, centroid, endPoint);
			if (intersection != null) {

				double deltaX = intersection.getX() - centroid.getX();
				double deltaY = intersection.getY() - centroid.getY();
				double e = intersection.distance(centroid);
				double minimalDistanceToBorder = 10;
				double alpha = (e - minimalDistanceToBorder) / e;
				if (contains(centroid)) {
					// make vector smaller
					result = new Point2D(centroid.getX() + deltaX * 0.8,
							centroid.getY() + deltaY * 0.8);
				} else {
					// make vector longer
					result = new Point2D(centroid.getX() + deltaX * 1.1,
							centroid.getY() + deltaY * ((1 - alpha) + 1));
				}
				if (contains(result)) {
					return result;
				}

			}
		}
		if (result != null && contains(result))
			return result;
		else {
			// System.out.println("Innerpoint");

			return getInnerPoint();

		}
	}

	/**
	 * We get a vector which describes where the point should be relative to the
	 * center. We change the length of the vector so that the point fits in the
	 * polygon. alpha is the percentage of the point when considering the line
	 * to the border.
	 * 
	 * @return Point which is contained by this polygon and has same direction
	 *         as the given vector point
	 */
	final private Point2D getRelativePosition(Point2D vector, double alphaLine) {

		double dx = vector.getX();
		double dy = vector.getY();

		getCentroid();
		double centroidX = centroid.getX();
		double centroidY = centroid.getY();

		double endPointX = centroidX + dx;
		double endPointY = centroidY + dy;
		Point2D endPoint = new Point2D(endPointX, endPointY);

		Point2D p1 = null;
		Point2D p2 = new Point2D(x[0], y[0]);
		Point2D result = null;
		for (int i = 1; i <= length; i++) {

			p1 = p2;
			p2 = new Point2D(x[i], y[i]);
			Point2D intersection = getIntersectionOfSegmentAndLine(p1, p2,
					centroid, endPoint);
			if (intersection != null) {

				double deltaX = intersection.getX() - centroidX;
				double deltaY = intersection.getY() - centroidY;
				double e = intersection.distance(centroid);
				double nX = centroidX + deltaX * alphaLine;
				double nY = centroidY + deltaY * alphaLine;
				return new Point2D(nX, nY);
			}
		}
		System.out.println("Problem, relative Placement did not go right.");
		return null;

	}

	/**
	 * Returns a random point in the polygon.
	 * @return
	 */
	public Point2D getInnerPoint() {
		Rectangle b = getBounds();
		double x = -1;
		double y = -1;
		do {
			x = b.getMinX() + seed.nextDouble()*b.width;

			y = b.getMinY() + seed.nextDouble()*b.height;
		} while (!this.contains(x, y));

		return new Point2D(x, y);
	}

	/**
	 * Computes the intersection point iff (inx,iny) is within the polygon and (outx,outy) is outside of the polygon.
	 * Returns null if the is no intersection or the line crosses the complete polygon.
	 * @param inx x-coordinate of the presumably inner point
	 * @param iny y-coordiante of the presumably inner point
	 * @param outx x-coordinate of the presumably outer point
	 * @param outy y-coordinate of the presumably outer point
	 * @return Intersection Point
	 */
	public Point2D getIntersectionWithPolygon(Double inx, Double iny, Double outx, Double outy){
		double t,s,denum;
		Point2D[] intersections = new Point2D[4];
		int k = 0;
		for(int i = 0, j = length - 1; i < length; j = i++){
			denum = ((x[i] - x[j])*(iny - outy) - (inx - outx) *(y[i] - y[j]));
			if(denum == 0)
				continue;
			t = (outx*(y[i] - y[j]) - outy * (x[i] - x[j]) + y[j] * (x[i] - x[j]) - x[j]* (y[i] - y[j]))/denum;
			s = (outy* (inx - outx) + x[j] *(iny - outy)  - outx * (iny - outy) - y[j] *(inx - outx))/-denum;
			if(t > 0 && t <= 1 && s > 0 && s <= 1)
				intersections[k++] = new Point2D(outx + t*(inx - outx), outy + t*(iny - outy));
		}
		if(k > 1)
			return null;
		else
			return intersections[0];
		
	}
	
	/**
	 * intersection of two lines formed by the given points:
	 * http://paulbourke.net/geometry/lineline2d/
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return
	 */
	private Point2D getIntersection(Point2D p1, Point2D p2, Point2D p3,
			Point2D p4) {
	
		// Bounding Box test
		double x1 = 0;
		double x2 = 0;
		double y1 = 0;
		double y2 = 0;

		double x3 = 0;
		double x4 = 0;
		double y3 = 0;
		double y4 = 0;

		if (p1.getX() < p2.getX()) {
			x1 = p1.getX();
			x2 = p2.getX();
		} else {
			x1 = p2.getX();
			x2 = p1.getX();
		}
		if (p1.getY() < p2.getY()) {
			y1 = p1.getY();
			y2 = p2.getY();
		} else {
			y1 = p2.getY();
			y2 = p1.getY();
		}

		if (p3.getX() < p4.getX()) {
			x3 = p3.getX();
			x4 = p4.getX();
		} else {
			x3 = p4.getX();
			x4 = p3.getX();
		}
		if (p3.getY() < p4.getY()) {
			y3 = p3.getY();
			y4 = p4.getY();
		} else {
			y3 = p4.getY();
			y4 = p3.getY();
		}

		//FIXME bounding box intersection needs to be corrected
		if (!(x2 >= x2 && x4 >= x1 && y2 >= y3 && y4 >= y1)) {
			return null;
		}

		Point2D n1 = new Point2D(p3.getX() - p1.getX(), p3.getY()
				- p1.getY());
		Point2D n2 = new Point2D(p2.getX() - p1.getX(), p2.getY()
				- p1.getY());
		Point2D n3 = new Point2D(p4.getX() - p1.getX(), p4.getY()
				- p1.getY());
		Point2D n4 = new Point2D(p2.getX() - p1.getX(), p2.getY()
				- p1.getY());

		if (Geometry.crossProduct(n1, n2) * Geometry.crossProduct(n3, n4) >= 0) {
			return null;
		}

		double denominator = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());
		if (denominator == 0) {
			// return null;
			throw new RuntimeException("Lines are parallel");
		}
		double ua = (p4.getX() - p3.getX()) * (p1.getY() - p3.getY())
				- (p4.getY() - p3.getY()) * (p1.getX() - p3.getX());
		double ub = (p2.getX() - p1.getX()) * (p1.getY() - p3.getY())
				- (p2.getY() - p1.getY()) * (p1.getX() - p3.getX());
		ua = ua / denominator;
		ub = ub / denominator;

		if ((ua >= 0 && ua <= 1) && (ub >= 0 && ub <= 1)) {
			return new Point2D(p1.getX() + ua * (p2.getX() - p1.getX()),
					p1.getY() + ua * (p2.getY() - p1.getY()));
		} else {
			// no intersection of the two segments
			return null;
		}

	}

	/**
	 * Return the intersection of the segment given bei p1 and p2 and the line
	 * given by p3 and p4. intersection:
	 * http://paulbourke.net/geometry/lineline2d/
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return
	 */
	private static Point2D getIntersectionOfSegmentAndLine(Point2D p1,
			Point2D p2, Point2D p3, Point2D p4) {
		
		double denominator = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX())
				- (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());
		if (denominator == 0) {
			// return null;
			throw new RuntimeException("Lines are parallel");
		}
		double ua = (p4.getX() - p3.getX()) * (p1.getY() - p3.getY())
				- (p4.getY() - p3.getY()) * (p1.getX() - p3.getX());
		double ub = (p2.getX() - p1.getX()) * (p1.getY() - p3.getY())
				- (p2.getY() - p1.getY()) * (p1.getX() - p3.getX());
		ua = ua / denominator;
		ub = ub / denominator;

		if ((ua >= 0 && ua <= 1) && ub >= 1) {
			return new Point2D(p1.getX() + ua * (p2.getX() - p1.getX()),
					p1.getY() + ua * (p2.getY() - p1.getY()));
		} else {
			// no intersection of the two segments
			return null;
		}

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
	
	/**
	 * If the polygon is modified by e.g. shrinking, this method returns the original polygon. If the polyogn was not modified, it can return null.
	 * @return
	 */
	public PolygonSimple getOriginalPolygon() {
		return oldPolygon;
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
	
	

	public int[] getXpointsClosed(){
		return getPointsClosed(x);
	}
	
	public int[] getYpointsClosed(){
		return getPointsClosed(y);
	}
	
	private int[] getPointsClosed(double[] values){		
		int[] x=new int[length+1];
		for (int i = 0; i < length; i++) 
			x[i]=(int)values[i];		
		x[length]=x[0];
		return x;
	}

	
}
