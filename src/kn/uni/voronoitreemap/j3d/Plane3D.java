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
package kn.uni.voronoitreemap.j3d;

import kn.uni.voronoitreemap.convexHull.JFace;
import kn.uni.voronoitreemap.j2d.Point2D;


public class Plane3D {
	/**
	 * Storing a plane in 3 dimensions in the form of: ax + bx +cx +d=0
	 */
final public double a;
final public double b;
final public double c;
final public double d;


/**
 * Computation of the plane from three points, 
 * see http://paulbourke.net/geometry/planeeq/
 * 
 * @param p1
 * @param p2
 * @param p3
 */
public Plane3D(Point3d p1, Point3d p2, Point3d p3){
	a=p1.y*(p2.z-p3.z)+p2.y*(p3.z-p1.z)+p3.y*(p1.z-p2.z);
	b=p1.z*(p2.x-p3.x)+p2.z*(p3.x-p1.x)+p3.z*(p1.x-p2.x);
	c=p1.x*(p2.y-p3.y)+p2.x*(p3.y-p1.y)+p3.x*(p1.y-p2.y);
	d=-1*(p1.x*(p2.y*p3.z-p3.y*p2.z)+p2.x*(p3.y*p1.z-p1.y*p3.z)+p3.x*(p1.y*p2.z-p2.y*p1.z));	
}

public Plane3D(JFace jFace){
	this(jFace.getVertex(0).toPoint3D(),jFace.getVertex(1).toPoint3D(),jFace.getVertex(2).toPoint3D());
}

/**
 * Return z- normalized plane, which looks as follow: h: z=ax + by + c
 * a=array[0]
 * b=array[1]
 * c=array[2]
 * @return
 */
protected double[] getNormZPlane(){
	double[] res=new double[3];
	res[0]=-1*(a/c);
	res[1]=-1*(b/c);
	res[2]=-1*(d/c);
	return res;
}

/**
 * This is a bijectiv function between planes (non vertical) and points,
 * see delta(h) function in [Aurenhammer,1987], Power Diagrams: Properties, Algorithms and Applications
 * @return
 */
public Point2D getDualPointMappedToPlane(){
	double[] nPlane = getNormZPlane();
	Point2D dualPoint=new Point2D(nPlane[0]/2, nPlane[1]/2);
	return dualPoint;
}

}
