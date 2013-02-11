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



import kn.uni.voronoitreemap.j2d.Point2D;
import kn.uni.voronoitreemap.j3d.Plane3D;
import kn.uni.voronoitreemap.j3d.Point3d;

/**
 * Facet class connected over double-connected edge list. Contains incident vertices and edges.
 * @author Nocaj, Hildenbrand
 */
public class JFace {
	private JConflictList list;
	private boolean marked;
	/**
	 * Usefull
	 */
	 private JVertex v[];     // List of incident vertices
	 private HEdge e[];       // Boundary edges of the facet
	 private JVector normal;
	 private static final double epsilon = 1e-10;
	 private int index;		  //Position in points list, important for fast update of conflicts.


	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	private Point2D dualPoint;  
	public JFace(JVertex a, JVertex b, JVertex c) {
		list = new JConflictList(true);
		v = new JVertex[3];
		e = new HEdge[3];
		v[0] = a; 
		v[1] = b;
		v[2] = c;
		setMarked(false);
		JVertex t = (v[0].subtract(v[1])).crossProduct((v[1].subtract(v[2])));
		this.normal = new JVector(-t.x, -t.y, -t.z);
	    this.normal.normalize();
	    createEdges();
	}
	/**
	 * Creates the Face with a,b,c and orients with the 4th point (which is behind the facet).
	 * 
	 */
	public JFace(JVertex a, JVertex b, JVertex c, JVertex orient){
		this(a,b,c);
		orient(orient);
	}
	public void orient(JVertex orient) {
		if(!behind(orient)){
			JVertex temp = v[1];
			v[1] = v[2];
			v[2] = temp;
			normal.negate();
			createEdges();
		}
		
	}
	private void createEdges() {
		e[0] = new HEdge(v[0],v[1], this);
		e[1] = new HEdge(v[1],v[2], this);
		e[2] = new HEdge(v[2],v[0], this);
		e[0].setNext(e[1]);
		e[0].setPrev(e[2]);
		e[1].setNext(e[2]);
		e[1].setPrev(e[0]);
		e[2].setNext(e[0]);
		e[2].setPrev(e[1]);
	}

	public JConflictList getList() {
		return list;
	}
	public void setList(JConflictList list) {
		this.list = list;
	}

	public String toString() {
//		String str = "[ " + v[0] + ", " + v[1] + ", " + v[2] + " ]";
//		return str;
		return super.toString();
	}

	public JVertex getVertex(int i) {
		return v[i];
	}

	public boolean isVisibleFromBelow() {
	return  (normal.z < -1.4259414393190911E-9);
		
	}

	public int getEdgeCount() {
		return e.length;
	}

	public HEdge getEdge(int index) {
		return this.e[index];
	}
	public JVector getNormal() {
		return normal;
	}
	public void setNormal(JVector normal) {
		this.normal = normal;
	}
	/**
	 * Links the given face f0 with the edge between v0 and v1 with the current face
	 * 
	 */
	public void link(JFace f0, JVertex v0, JVertex v1){
		HEdge twin = f0.getEdge(v0,v1);
		if(twin == null)
			throw new RuntimeException("Twin of Edge is NULL. Cannot Connect Edges!");
		HEdge edge = getEdge(v0,v1);
		twin.setTwin(edge);
		edge.setTwin(twin);
		
	}
	public void link(HEdge e){
		HEdge edge = getEdge(e.getOrigin(),e.getDest());
		e.setTwin(edge);
		edge.setTwin(e);
		
	}
	private HEdge getEdge(JVertex v0, JVertex v1) {
		for(int i = 0; i < 3; i++){
			if(e[i].isEqual(v0, v1))
				return e[i];
		}
		return null;
	}
	public Point2D getDualPoint(){
		 
		if (this.dualPoint==null){
			  Plane3D plane3d=new Plane3D(this);
			  dualPoint= plane3d.getDualPointMappedToPlane();
		   }
		return dualPoint;
	   }
	
	/**
	 * Is the given vertex in front of or behind this face?
	 */
	public boolean behind(JVertex test) {
		 return (normal.dot(test) < normal.dot(v[0])) ;
	}
	
	public boolean conflict(JVertex test){
		return (normal.dot(test) > normal.dot(v[0]) + epsilon);
	}
	
	public int getVertexCount() {
		return v.length;
	}
	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	public boolean isMarked() {
		return marked;
	}
	public void removeConflict() {
		list.removeAll();
		
	}
	public HEdge getHorizon() {
		for(int i = 0; i < 3; i++){
			if(e[i].getTwin() != null && e[i].getTwin().isHorizon())
				return e[i];
		}
		return null;
	}
	
}
