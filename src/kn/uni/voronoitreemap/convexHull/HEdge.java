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

import java.util.List;
/**
 * Class for representing an Edge in the JConvexHull.
 * Contains the origin and destination vertices, the twin edge, the incident JFace and the next and previous edge in the incident JFace
 *
 * @author Nocaj, Hildenbrand
 */



public class HEdge {
	private JVertex origin;
	private JVertex dest;
	private HEdge twin;
	private JFace iFace;
	private HEdge next;
	private HEdge prev;
	
	
	public HEdge(JVertex origin, JVertex dest, JFace iFace) {
		this.origin = origin;
		this.dest = dest;
		this.iFace = iFace;
	}
	public void setDest(JVertex dest) {
		this.dest = dest;
	}
	public boolean isEqual(JVertex origin, JVertex dest){
		return (this.origin.equals(origin) && this.dest.equals(dest)) || (this.origin.equals(dest) && this.dest.equals(origin));
	}
	public JVertex getOrigin() {
		return origin;
	}
	public void setOrigin(JVertex origin) {
		this.origin = origin;
	}
	public HEdge getTwin() {
		return twin;
	}
	public void setTwin(HEdge twin) {
		this.twin = twin;
	}
	public JFace getiFace() {
		return iFace;
	}
	public void setiFace(JFace iFace) {
		this.iFace = iFace;
	}
	public HEdge getNext() {
		return next;
	}
	public void setNext(HEdge next) {
		this.next = next;
	}
	public HEdge getPrev() {
		return prev;
	}
	public void setPrev(HEdge prev) {
		this.prev = prev;
	}
	public String toString() {
		return origin.toString() + "->" + dest.toString();
	}
	public JVertex getDest() {
		return dest;
	}
	public boolean isHorizon() {
		return twin != null && twin.getiFace().isMarked() && !iFace.isMarked();
	}
	
	public void findHorizon(List<HEdge> horizon) {
	      if (isHorizon()) {
	          if (horizon.size() > 0 && this == horizon.get(0)) {
	             return;
	          } else {
	             horizon.add(this);
	             next.findHorizon(horizon);
	          }
	       } else {
	          if (twin != null) {
	             twin.getNext().findHorizon(horizon);
	          }
	       }
	}
	
}
