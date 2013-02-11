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
package kn.uni.voronoitreemap.convexHull;
import kn.uni.voronoitreemap.j3d.Point3d;

/**
 * 3-dimensional Point with some extra fields for ConvexHull computation.
 * @author Arlind Nocaj
 *
 */

public class JVertex {
	public double x;
	public double y;
	public double z;
	private static final double epsilon = 1e-10;
	private JConflictList list;
	private int index;
	private boolean handled;
	
	public Object originalObject;
	public JVertex(double x, double y, double z) {
		list = new JConflictList(false);
		index = -1;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public JConflictList getList() {
		return list;
	}
	public void setList(JConflictList list) {
		this.list = list;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean equals(Object o) {
		if(!(o instanceof JVertex))
			return false;
		else {
			JVertex obj = (JVertex) o;
			if(obj.x == x && obj.y == y && obj.z == z)
				return true;
			else return false;
		}
	}
	/**
	 * 
	 * @param v Vertex compared to Object
	 * @return Whether given Vertex and the Object are linearly dependent
	 */
	public boolean linearDependent(JVertex v) {
		if(x == 0 && v.x == 0) {
			if(y == 0 && v.y == 0) {
				if(z == 0 && v.z == 0) {
					return true;
				}
				if(z == 0 || v.y == 0) {
					return false;
				}
				return true;
			}
			if(y == 0 || v.y == 0) {
				return false;
			}
			if(z/y >= v.z/v.y -epsilon  && z/y <= v.z/v.y +epsilon)
				return true;
			else 
				return false;
		}
		if(x == 0 || v.x == 0) {
			return false;
		}
		if(y/x <= v.y/v.x+epsilon && y/x >= v.y/v.x-epsilon &&  z/x >= v.y/v.x -epsilon  && z/x <= v.z/v.x +epsilon)
			return true;
		else 
			return false;
	}
	public void negate(){
		x *= -1;
		y *= -1;
		z *= -1;
	}
	public JVertex subtract(JVertex v) {
		return new JVertex(v.x-x, v.y-y, v.z -z);
	}
	public JVertex crossProduct(JVertex v) {
		return new JVertex(y*v.z-z*v.y, z*v.x- x*v.z, x*v.y - y*v.x);
	}
	public String toString() {
		return "" + x +"/" + y + "/" +z;
	}
	public void setHandled(boolean b) {
		this.handled=b;
	}
	public boolean isHandled() {
		return handled;
	}
	
	public Point3d toPoint3D(){
		return new Point3d(x,y,z);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void clear(){
		list = new JConflictList(false);
		index = -1;
		handled=false;
	}
}
