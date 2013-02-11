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
 * Data structure to save the Conflict Graph of vertices and JFaces.
 * For every vertex is saved, which faces are in conflict  and for every JFace is saved, which vertices are in conflict.
 * The JConflictList supports constant-time add and remove methods.
 *
 *@author HildenBrand, Nocaj
 */
public class JConflictList {
	private boolean face;
	protected JGraphEdge head;
	/*
	 * Whether the ConflictList is for a JFace or a vertex
	 */
	public JConflictList(boolean face) {
		this.face = face;
	}

	public void add(JGraphEdge e) {
		if(head == null){
			head = e;
		}else{
			if(face){//Is FaceList
				head.prevv = e;
				e.nextv = head;
				head = e;
			}else{//Is VertexList
				head.prevf = e;
				e.nextf = head;
				head = e;
			}
		}
	}

	public boolean empty() {
		return head == null;
	}
	/*
	 * fills visible List with all visible JFaces of the vertex and also marks the JFaces
	 */
	public void fill(List<JFace> visible) {
		if(face){
			System.out.println("can't fill facet");
			return;
		}
		JGraphEdge curr = head;
		do{
			visible.add(curr.face);
			curr.face.setMarked(true);
			curr = curr.nextf;
		}while(curr != null);
		
	}
	/*
	 * Remove all vertices from the JFace or all JFaces from the vertex
	 */
	public void removeAll() {
		if(face){//Remove all vertices from JFace
			JGraphEdge curr = head;
			do{
				if(curr.prevf == null){//Node is head
					if(curr.nextf == null){
						curr.vert.getList().head = null;
					}else{
						curr.nextf.prevf = null;
						curr.vert.getList().head = curr.nextf;
					}
				}else{//Node is not head
					if(curr.nextf != null)
						curr.nextf.prevf = curr.prevf;
					curr.prevf.nextf = curr.nextf;
				}
				curr = curr.nextv;
				if(curr != null)
					curr.prevv = null;
			}while(curr != null);
		}else{//Remove all JFaces from vertex
			JGraphEdge curr = head;
			do{
				if(curr.prevv == null){ //Node is head
					if(curr.nextv == null){
						curr.face.getList().head = null;
					}else{
						curr.nextv.prevv = null;
						curr.face.getList().head = curr.nextv;
					}
				}else{//Node is not head
					if(curr.nextv != null){
						curr.nextv.prevv = curr.prevv;
					}
					curr.prevv.nextv = curr.nextv;
				}
				curr = curr.nextf;
				if(curr != null)
					curr.prevf = null;
			}while(curr != null);
		}
		
	}
	
	public List<JVertex> getVertices(List<JVertex> l1) {
		JGraphEdge curr = head;
		while(curr != null){
			l1.add(curr.vert);
			curr = curr.nextv;
		}
		return l1;
	}

}
