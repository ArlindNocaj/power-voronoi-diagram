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
package kn.uni.voronoitreemap.datastructure;


import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;


/**
 * Direct accessable array list for faster iteration.
 * @author Arlind Nocaj	
 *
 * @param <E> type of Elements
 */
public class OpenList implements Iterable<Site>{

	public Site[] array;
	public int size=0;
	
	private static Random rand=new Random(1985);
	public OpenList(){
		this(10);
	}
	@SuppressWarnings("unchecked")
	public OpenList(int capacity){
		
		array=new Site[capacity];

	}
	
	private void increaseCapacity(){
			int newCapacity = (array.length * 3)/2 + 1;
			array = Arrays.copyOf(array, newCapacity);
	}
	
	public boolean add(Site e) {
		if (size>(array.length-1)){
			increaseCapacity();
		}
		array[size]=e;
		size++;
		return true;
	}


//	public void add(int index, E element) {
//		// TODO Auto-generated method stub
//		
//	}


	public void clear() {
		size=0;		
	}



	public Site get(int index) {
		if (index<size){
			return array[index];
		}
		return null;
	}

	public boolean isEmpty() {
		return size==0;
	}

	public Site set(int index, Site element) {
		if (index<size){
		array[index]=element;
		}
		return element;
	}

	
	/**
	 * Clones the list of sites but with zero weights
	 */
	public OpenList cloneWithZeroWeights()  {
		OpenList neu=new OpenList(size+1);
		neu.size=size;
		for (int i=0;i<size;i++){
			Site s=array[i];
			neu.array[i]=s.cloneZeroWeight();
		}
		return neu;
		
	}
	
	public OpenList clone()  {
		OpenList neu=new OpenList(size+1);
		neu.size=size;
		for (int i=0;i<size;i++){
			Site s=array[i];
			neu.array[i]=s.clone();
		}
		return neu;
		
	}
	
	public void permutate(){
		
		for(int i = 0; i < size; ++i){
			int ra = rand.nextInt(size);
			Site temp = array[ra];
			array[ra]=array[i];
			array[i]=temp;
		}
	}
	
	
	@Override
	public Iterator<Site> iterator() {
		return new Iterator<Site>() {
			
			int i=0;
			@Override
			public boolean hasNext() {
			return i<size;			
			}

			@Override
			public Site next() {				
				return array[i++];				
			}

			@Override
			public void remove() {
				
			}
		};
	}
	
	public PolygonSimple getBoundsPolygon(double offset){
		if(offset<0) return null;
		
		Rectangle2D rect = getBounds();
		
		double x=rect.getMinX();
		double y=rect.getMinY();
		double w=rect.getWidth();
		double h=rect.getHeight();
		x-=offset;
		y-=offset;
		w+=offset;
		h+=offset;
		
		PolygonSimple poly=new PolygonSimple(4);
		poly.add(x,y);
		poly.add(x+w,y);
		poly.add(x+w,y+h);
		poly.add(x,y+h);
			return poly;
	}
	
	public Rectangle2D getBounds(){
		PolygonSimple simple=new PolygonSimple(this.size);
		for(Site s:this)
			simple.add(s.x,s.y);
		
		return simple.getBounds2D();		
	}

}
