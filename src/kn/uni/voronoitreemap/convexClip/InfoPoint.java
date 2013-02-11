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
 * Result wrapper for intersection with information (= code) of intersection type
 * @author Hildenbrand, Nocaj
 *
 */
public class InfoPoint {
	Point2D erg;
	Point2D snd;
	char code;
	public InfoPoint(Point2D erg, char code){
		this.erg = erg;
		this.code = code;
	}
	public InfoPoint(Point2D erg, Point2D snd, char code){
		this.erg = erg;
		this.code = code;
		this.snd = snd;
	}
	public String toString(){
		String ergs, snds = "";
		if(erg == null){
			ergs = snds = "";
		}else if( snd == null){
			ergs = erg.toString();
		}else{
			ergs = erg.toString();
			snds = snd.toString();
		}
		return "c: " + code + " " + ergs + " " + snds; 
	}
}
