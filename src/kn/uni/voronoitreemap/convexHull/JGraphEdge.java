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
 * Edge for JConflictList
 * @author Hildenbrand, Nocaj
 */
public class JGraphEdge {
	protected JFace face;
	protected JVertex vert;
	
	protected JGraphEdge nextf;
	protected JGraphEdge prevf;
	protected JGraphEdge nextv;
	protected JGraphEdge prevv;
	
	public JGraphEdge(JFace face, JVertex vert){
		this.face = face;
		this.vert = vert;
	}
}
