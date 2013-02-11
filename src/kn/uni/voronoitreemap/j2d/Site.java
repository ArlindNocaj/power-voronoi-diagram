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
 * Extended Version of a site, which has relative Positions
 * @author Arlind Nocaj
 *
 */
public class Site extends ASite {
	double deltaX;
	double deltaY;
	double alpha;
	/**
	 * This attribute is used to mark the sites, which are only added to bound the Voronoi diagram.
	 */
	public boolean isDummy;
	private Object data;
	
	/**
	  * 
	  * @param x x-coordinate
	  * @param y y-coordinate
	  */
	 public Site(double x, double y){
		super(x,y);
	 }
	 
	 /**
	  * 
	  * @param x x-coordinate
	  * @param y y-coordinate
	  * @param weight the weight of the site, where weight=radius*radius which is used for weighting
	  */
	 public Site(double x, double y, double weight){
		super(x,y,weight);
	 }
	 
	 public Site(double x, double y, double weight, double percentage){
		 super(x,y,weight,percentage); 
	 }
	 
	 public Site cloneZeroWeight(){
			Site site=new Site(x, y, 0);
			return site;
		}
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public void setAsDummy() {
		this.isDummy=true;
	}

	public void setData(Object child) {
		this.data=child;
	}
}
