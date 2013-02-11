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
package kn.uni.voronoitreemap.convexClip;

import kn.uni.voronoitreemap.j2d.Point2D;

/**
 * Container of the VertexList, with prev/next pointer and Point2D as information
 * @author Hildenbrand, Nocaj
 *
 */
public class cVertex {
		public Point2D v;
		public cVertex prev, next;
		public cVertex(double x, double y){
			v = new Point2D(x,y);
		}
		public cVertex() {
		}
		public cVertex(Point2D erg) {
			v = new Point2D(erg.x, erg.y);
		}
		public String toString(){
			if(v != null){
				return v.toString(); 
			}
			return "";
		}
}
