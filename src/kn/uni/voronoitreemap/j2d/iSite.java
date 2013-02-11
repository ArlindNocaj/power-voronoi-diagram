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

import java.util.ArrayList;

public interface iSite {

	public abstract PolygonSimple getPolygon();

	public abstract void setPolygon(PolygonSimple poly);

	public abstract void setNeighbours(ArrayList<Site> list);

	public abstract ArrayList<Site> getNeighbours();

	public abstract double getWeight();

	public abstract void setWeight(double weight);

	public abstract void setXY(double x, double y);

	public abstract void setXYW(double x, double y, double weight);

	public abstract void setX(double x);

	public abstract void setY(double y);

	public abstract void setPercentage(double percentage);

	public abstract double getPercentage();

	public abstract Point2D getPoint();

}
