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


/**
 * 3-dimensional Vector with some helpfull functions, e.g., normalizing ...
 * @author Arlind Nocaj
 *
 */
public class JVector {
	public double x;
	public double y;
	public double z;
	public JVector() {
		x = y = z = 0.0F;
	}

	public JVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public JVector(JVector a) {
		this.x = a.x;
		this.y = a.y;
		this.z = a.z;
	}

	public JVector subtract(JVector b) {
		return new JVector(x - b.x, y - b.y, z - b.z);
	}

	public JVector cross(JVector b) {
		return new JVector(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y
				* b.x);
	}

	public double dot(JVector b) {
		return x * b.x + y * b.y + z * b.z;
	}

	public double dot(JVertex b) {
		return x * b.x + y * b.y + z * b.z;
	}

	public void normalize() {
		double len = length();
		if (len > 0.0D) {
			x /= len;
			y /= len;
			z /= len;
		}
	}

	public void negate() {
		x *= -1;
		y *= -1;
		z *= -1;
	}

	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public String toString(){
		return "X: "+x+"\t Y: "+y+"\t Z: "+z;
	}
}
