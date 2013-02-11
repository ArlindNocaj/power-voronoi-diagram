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


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for computing the convex hull of given vertices with a randomized incremental algorithm in O(n*log(n)) after book of Mark de Berg/Marc van Kreveld
 * /Mark Overmars/Otfried Schwarzkopf, Computational geometry:algorithms and applications
 *@author Nocaj, Hildenbrand
 */
public class JConvexHull {
	//List of all vertices
	private List<JVertex> points;
	//List of current facets of the hull
	private List<JFace> facets;
	//List of created facets in every step
	private List<JFace> created;
	//List of horizon edges in every step
	private List<HEdge> horizon;
	//List of visible facets of the current vertex
	private List<JFace> visible;
	//Current index
	private int current;
	
	private boolean permutate=false;
	
	protected final Random rand = new Random(1985);
	
	public JConvexHull() {
		points = new ArrayList<JVertex>();
		facets = new ArrayList<JFace>();
		created = new ArrayList<JFace>();
		horizon = new ArrayList<HEdge>();
		visible = new ArrayList<JFace>();
		current = 0;
	}
	public void addPoint(JVertex v){
	JVertex tempVertex = new JVertex(v.x, v.y, v.z);
	tempVertex.originalObject=v;
	tempVertex.setIndex(points.size());
		points.add(tempVertex);
	}
	public void addPoint(double x, double y, double z){
		JVertex v = new JVertex(x,y,z);
		v.setIndex(points.size());
		points.add(v);
	}
	/**
	 * Computation method for the convex hull, after the algorithm in the Book of  Mark de Berg and the others.
	 * @return returns computed Hull as list of facets
	 */
	public List<JFace> compute(){
		prep();
		
		while(current < points.size()){
			JVertex next = points.get(current);
			if(next.getList().empty()){ //No conflict, point in hull
				++current;
				continue;
			}
			created.clear();
			horizon.clear();
			visible.clear();
			//The visible faces are also marked
			next.getList().fill(visible);
			//Horizon edges are orderly added to the horizon list
			HEdge e;
			for(JFace jF : visible){
				e = jF.getHorizon();
				if(e != null){
					e.findHorizon(horizon);
					break;
				}
			}
			JFace last = null, first = null;
			//Iterate over horizon edges and create new faces oriented with the marked face 3rd unused point
			for(HEdge hE : horizon){
				JFace fn = new JFace(next,hE.getOrigin(),hE.getDest(),hE.getTwin().getNext().getDest());
				fn.setList(new JConflictList(true));
				
				//Add to facet list
				addFacet(fn);
				created.add(fn);
				
				//Add new conflicts
				addConflicts(hE.getiFace(),hE.getTwin().getiFace(),fn);
				
				//Link the new face with the horizon edge
				fn.link(hE);
				if(last != null)
					fn.link(last, next, hE.getOrigin());
				last = fn;
				if(first == null)
					first = fn;
			}
			//Links the first and the last created JFace
			if(first != null && last != null){
				last.link(first, next, horizon.get(0).getOrigin());
			}
			if(created.size() != 0){
				//update conflict graph
				for(JFace f : visible){
					removeConflict(f);
				}
				++current;
				created.clear();
			}
		}
		return facets;
	}

	
	
	
	/**
	 * Conflicts of the new JFace can be only the conflicts of the incident JFaces of the horizon edge
	 * @param old1 incident facet of the horizon edge
	 * @param old2 other incident facet of the horizon edge
	 * @param fn newly created facet, which conflicts are added
	 */
	private void addConflicts(JFace old1, JFace old2, JFace fn) {
		//Adding the vertices
		List<JVertex> l1 = new ArrayList<JVertex>();
        old1.getList().getVertices(l1);
		List<JVertex> l2 = new ArrayList<JVertex>();
        old2.getList().getVertices(l2);
		List<JVertex> nCL = new ArrayList<JVertex>();
		JVertex v1,v2;
		int i,l;
		i = l = 0;
		//Fill the possible new Conflict List
		while(i < l1.size() || l < l2.size()){
			if(i < l1.size() && l < l2.size()){
				v1 = l1.get(i);
				v2 = l2.get(l);
				//If the index is the same, its the same vertex and only 1 has to be added
				if(v1.getIndex() == v2.getIndex()){
					nCL.add(v1);
					++i;
					++l;
				}else if(v1.getIndex() > v2.getIndex()){
					nCL.add(v1);
					++i;
				}else{
					nCL.add(v2);
					++l;
				}
			}else if( i < l1.size()){
				nCL.add(l1.get(i++));
			}else{
				nCL.add(l2.get(l++));
			}
		}
		//Check if the possible conflicts are real conflicts
		for(i = nCL.size() -1; i >= 0; --i){
			v1 = nCL.get(i);
			if(fn.conflict(v1))
				addConflict(fn,v1);
		}
	}
	
	/**
	 * Removes conflicts and rearranges the indices of the updated facets list.
	 */
	private void removeConflict(JFace f) {
		f.removeConflict();
		int index = f.getIndex();
		f.setIndex(-1);
		if(index == facets.size() - 1){
			facets.remove(facets.size() - 1);
			return;
		}
		if(index >= facets.size()|| index < 0)
			return;
	    JFace last = facets.remove(facets.size() - 1);
	    last.setIndex(index);
	    facets.set(index, last);
	}

	
	/**
	 * Prepares the convex hull computation
	 * Builds the tetrahedron, fills the conflict graph and builds permutation for the points list
	 * 
	 */
	private void prep() {
		if(points.size() <= 3) { //A tetrahedron needs at least 4 points
			throw new NotEnoughPointsException();
		}
		
		//Randomize the vertices
		if (permutate){
		permutation();
		}
		//set the indices ones again:
		for (int i=0;i<points.size();i++){
			points.get(i).setIndex(i);
		}
		
		JVertex v0,v1,v2,v3;
		JFace f1,f2,f3,f0;
		v0 = points.get(0);
		v1 = points.get(1);
		v2 = v3 =  null;
		for(int i = 2; i < points.size(); ++i) {
			if(!(v0.linearDependent(points.get(i)) && v1.linearDependent(points.get(i)))) { //not linearly dependent to both Vectors
				v2 = points.get(i);
				v2.setIndex(2);
				points.get(2).setIndex(i);
				points.set(i, points.get(2));
				points.set(2, v2);
				break;
			}
		}
		if(v2 == null) {
			throw new NotEnoughPointsException("Not enough non-planar Points");
		}
		//Create first JFace
		f0 = new JFace(v0,v1,v2);
		for(int i = 3; i < points.size(); ++i) {
			if((f0.getNormal().dot(f0.getVertex(0))  != f0.getNormal().dot(points.get(i)))) {// Point is valid
				v3 = points.get(i);
				v3.setIndex(3);
				points.get(3).setIndex(i);
				points.set(i, points.get(3));
				points.set(3, v3);
				break;
			}
		}
		if(v3 == null) {
			throw new NotEnoughPointsException("Not enough non-planar Points");
		}
		f0.orient(v3);
		f1 = new JFace(v0,v2,v3,v1);
		f2 = new JFace(v0,v1,v3,v2);
		f3 = new JFace(v1,v2,v3,v0);
		
		addFacet(f0);
		addFacet(f1);
		addFacet(f2);
		addFacet(f3);
		//Connect facets
		f0.link(f1, v0, v2);
		f0.link(f2,v0,v1);
		f0.link(f3,v1,v2);
		f1.link(f2,v0,v3);
		f1.link(f3, v2, v3);
		f2.link(f3,v3,v1);
		
		this.current = 4;
		//Fill conflict graph
		JVertex v;
		for(int i = current; i < points.size(); ++i){
			v = points.get(i);
			if(f0.conflict(v)){
				addConflict(f0,v);
			}
			//!f1.behind(v)
			if(f1.conflict(v)){
				addConflict(f1,v);
			}
			if(f2.conflict(v)){
				addConflict(f2,v);
			}
			if(f3.conflict(v)){
				addConflict(f3,v);
			}
		}		
	}
	/**
	 * Creates the permutation of the vertices with the Fisher-Yates Shuffle.
	 */
	private void permutation() {
		int pointSize=points.size();
		for(int i = pointSize-1; i> 0; i--){
			int ra = rand.nextInt(i);
			JVertex temp = points.get(ra);
			temp.setIndex(i);
			JVertex currentItem = points.get(i);
			currentItem.setIndex(ra);
			points.set(ra, currentItem);
			points.set(i,temp);
		}
		
	}
	private void addFacet(JFace f0) {
		f0.setIndex(facets.size());
		facets.add(f0);	
	}
	
	private void addConflict(JFace f0, JVertex v) {
		JGraphEdge e = new JGraphEdge(f0,v);
		f0.getList().add(e);
		v.getList().add(e);		
	}
	
	public int getVertexCount() {
		return points.size();
	}
	public JVertex getVertex(int i) {
		return points.get(i);
	}
	public int getFacetCount() {
		return facets.size();
	}
	public JFace getFacet(int i) {
		return facets.get(i);
	}
	private boolean isPermutate() {
		return permutate;
	}
	private void setPermutate(boolean permutate) {
		this.permutate = permutate;
	}
}
