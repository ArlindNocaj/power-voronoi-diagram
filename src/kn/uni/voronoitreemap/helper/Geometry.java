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
package kn.uni.voronoitreemap.helper;

import kn.uni.voronoitreemap.j2d.Point2D;


/**
 * Helper class for sum small computations.
 * @author Nocaj
 *
 */
public class Geometry {
public static double crossProduct(final Point2D p1,final Point2D p2){ 
		return p1.getX()*p2.getY()-p1.getY()*p2.getX();
	}

/**
 * * Shortest Distance from a Point p3 to a line segment p1-p2
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/pointline/
 
 * @param x1 x value of first segment point
 * @param y1 y value of first segment point
 * @param x2 x value of second segment point
 * @param y2 y value of second segment point
 * @param x3 x value of point we are interested in
 * @param y3 y value of point we are interested in
 * @return
 */
public static double distancePointToSegment(final double x1,final double y1,final double x2,final double y2,final double x3, final double y3) { // NOPMD by nocaj on 8/13/12 2:51 PM
	final double px = x2-x1;
	final double py = y2-y1;
	final double d= Math.sqrt(px * px + py * py);
	
	final double u = ((x3-x1)*(x2-x1)+(y3-y1)*(y2-y1))/(d*d);
	final double kx = x1+u*(x2-x1);
	final double ky=y1+u*(y2-y1);
	
	final double dkx = x3-kx;
	final double dky = y3-ky;
	return Math.sqrt(dkx*dkx+dky*dky);
}

}
