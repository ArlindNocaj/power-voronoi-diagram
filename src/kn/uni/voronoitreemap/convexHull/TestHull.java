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
import java.util.Random;




public class TestHull{

//	public void testExample3(){
//		JConvexHull hull=new JConvexHull();
//		hull.addPoint(273.0,237.0,130668.0);
//		hull.addPoint(302.0,507.0,348223.0);
//		hull.addPoint(296.0,394.0,242822.0);
//		hull.addPoint(-700.0,-700.0,979999.9999999999);
//		hull.addPoint(1550.0,-700.0,2892500.0);
//		hull.addPoint(1550.0,1550.0,4805000.0);
//		hull.addPoint(-700.0,1550.0,2892500.0);
//		List<JFace> res = hull.computeHull();
//	}
//	public void testExample1(){
//		JConvexHull hull=new JConvexHull();
//		
//		JVertex vertexMiddle = new JVertex(0, 0, 0);
//		JVertex v1 = new JVertex(-1, 0, 1);
//		JVertex v2 = new JVertex(0,-1,1);
//		JVertex v3 =new JVertex(1,0,1);
//		JVertex v4 =new JVertex(0,1,1);
//		hull.addPoint(vertexMiddle);
//		hull.addPoint(v1);
//		hull.addPoint(v2);
//		hull.addPoint(v3);
//		hull.addPoint(v4);
//		
//		hull.computeHull();
//		List<JFace> list = hull.getHull();
//		for (JFace face:list){
//			System.out.println(face);
//		}
////		assertTrue(existsFacet(list,v1,vertexMiddle, v2));
////		assertTrue(existsFacet(list, vertexMiddle, v2, v3));
////		assertTrue(existsFacet(list, vertexMiddle, v3, v4));
////		assertTrue(existsFacet(list, vertexMiddle, v4, v1));
////		assertTrue(existsFacet(list, vertexMiddle, v1, v2));
////		
////		assertTrue(existsFacet(list, v1, v2, v3));
////		assertTrue(existsFacet(list, v1, v3, v4));
////		assertTrue((existsFacet(list, v1, v2, v3) && existsFacet(list, v1, v3, v4))||(existsFacet(list, v2, v3, v4) && existsFacet(list, v2, v4, v3)));
//	
//	}
//	
//	
//	public void testExample2(){
//		JConvexHull hull=new JConvexHull();
//		
//		JVertex vertexMiddle = new JVertex(0, 0, 1);
//		JVertex v1 = new JVertex(-1, 0, 1);
//		JVertex v2 = new JVertex(0,-1,1);
//		JVertex v3 =new JVertex(1,0,1);
//		JVertex v4 =new JVertex(0,1,1);
//		
//		JVertex v5=new JVertex(1, 1, 2);
//		hull.addPoint(vertexMiddle);
//		hull.addPoint(v1);
//		hull.addPoint(v2);
//		hull.addPoint(v3);
//		hull.addPoint(v4);
//		hull.addPoint(v5);
//		hull.computeHull();
//		List<JFace> list = hull.getHull();
//		for (JFace face:list){
//			System.out.println(face);
//		}
////		assertTrue(existsFacet(list,v1,vertexMiddle, v2));
////		assertTrue(existsFacet(list, vertexMiddle, v2, v3));
////		assertTrue(existsFacet(list, vertexMiddle, v3, v4));
////		assertTrue(existsFacet(list, vertexMiddle, v4, v1));
////		assertTrue(existsFacet(list, vertexMiddle, v1, v2));
////		
////		assertTrue(existsFacet(list, v1, v2, v3));
////		assertTrue(existsFacet(list, v1, v3, v4));
////		assertTrue((existsFacet(list, v1, v2, v3) && existsFacet(list, v1, v3, v4))||(existsFacet(list, v2, v3, v4) && existsFacet(list, v2, v4, v3)));
//	
//	}
//	
	
	public void testHull(){
		
		JConvexHull hull;
		for(int k = 0;  k < 1000; ++k){
		hull =new JConvexHull();
			
		long startTime=System.currentTimeMillis();
		Random rand=new Random();
		for (int i=0;i<10000;i++){
			double x=rand.nextDouble()*5;
			double y=rand.nextDouble()*5;
			double weight=rand.nextDouble()*5;
			weight=weight*weight;
			double z=x*x+y*y-weight; //harder test
//			z=rand.nextInt(500); //easy test
			hull.addPoint(new JVertex(x, y, z));
		}
		
			
		List<JFace> erg = hull.compute();
		long endTime=System.currentTimeMillis();
		
		long delta = endTime-startTime;
		double time=delta;
		System.out.println(time);
		}
//		convexHull.JConvexHull hull2 = new convexHull.JConvexHull();
//		
//		startTime=System.currentTimeMillis();
//		rand=new Random(0);
//		for (int i=0;i<100;i++){
//			double x=rand.nextDouble()*500;
//			double y=rand.nextDouble()*500;
//			double weight=rand.nextDouble()*500;
//			weight=weight*weight;
//			double z=x*x+y*y-weight; //harder test
////			z=rand.nextInt(500); //easy test
//			hull2.addPoint(new convexHull.JVertex(x, y, z));
//			}
//		
//			
//			List<convexHull.JFace> erg2 = hull2.computeHull();
//		endTime=System.currentTimeMillis();
//		
//		delta = endTime-startTime;
//		time=delta/1000;
//		System.out.println(time);
//		System.out.println(erg2);
//		hull.ConvexHull hull3 = new hull.ConvexHull();
//		
//		startTime=System.currentTimeMillis();
//		rand=new Random(0);
//		for (int i=0;i<1000000;i++){
//			double x=rand.nextDouble()*500;
//			double y=rand.nextDouble()*500;
//			double weight=rand.nextDouble()*500;
//			weight=weight*weight;
//			double z=x*x+y*y-weight; //harder test
////			z=rand.nextInt(500); //easy test
//			hull3.addPoint(new j3d.Vertex(x, y, z));
//			}
//		
//			
//			hull3.compute();
//		endTime=System.currentTimeMillis();
//		
//		delta = endTime-startTime;
//		time=delta;
//		System.out.println(time);
//		JConvexHull hull = new JConvexHull();
//		hull.addPoint(0, 0, 0);
//		hull.addPoint(0, 10, 0);
//		hull.addPoint(10,10,0);
//		hull.addPoint(0, 0, 10);
//		hull.addPoint(0, 10, 10);
//		hull.addPoint(10, 10, 10);
//		hull.addPoint(10, 0, 10);
//		hull.addPoint(10, 0, 0);
//		List<JFace> test = hull.compute();
//		System.out.println(test);
//		}
//	public void testHull2(){
//		hull.ConvexHull hull3 = new hull.ConvexHull();
//		
//		long startTime=System.currentTimeMillis();
//		Random rand=new Random(0);
//		for (int i=0;i<1000000;i++){
//			double x=rand.nextDouble()*500;
//			double y=rand.nextDouble()*500;
//			double weight=rand.nextDouble()*500;
//			weight=weight*weight;
//			double z=x*x+y*y-weight; //harder test
////			z=rand.nextInt(500); //easy test
//			hull3.addPoint(new j3d.Vertex(x, y, z));
//			}
//		
//			
//			hull3.compute();
//		long endTime=System.currentTimeMillis();
//		
//		long delta = endTime-startTime;
//		double time=delta;
//		System.out.println(time);
//		JConvexHull hull=new JConvexHull();
//			
//		startTime=System.currentTimeMillis();
//		 rand=new Random(0);
//		for (int i=0;i<1000000;i++){
//			double x=rand.nextDouble()*500;
//			double y=rand.nextDouble()*500;
//			double weight=rand.nextDouble()*500;
//			weight=weight*weight;
//			double z=x*x+y*y-weight; //harder test
////			z=rand.nextInt(500); //easy test
//			hull.addPoint(new JVertex(x, y, z));
//			}
//		
//			
//			hull.compute();
//		 endTime=System.currentTimeMillis();
//		
//		delta = endTime-startTime;
//		time=delta;
//		System.out.println(time);
//		
//		JConvexHull hull2 = new JConvexHull();
//		
//		startTime=System.currentTimeMillis();
//		rand=new Random(0);
//		for (int i=0;i<1000000;i++){
//			double x=rand.nextDouble()*500;
//			double y=rand.nextDouble()*500;
//			double weight=rand.nextDouble()*500;
//			weight=weight*weight;
//			double z=x*x+y*y-weight; //harder test
////			z=rand.nextInt(500); //easy test
//			hull2.addPoint(new JVertex(x, y, z));
//			}
//		
//			
//			hull2.compute();
//		endTime=System.currentTimeMillis();
//		
//		delta = endTime-startTime;
//		time=delta;
//		System.out.println(time);
//		

////		JConvexHull hull = new JConvexHull();
////		hull.addPoint(0, 0, 0);
////		hull.addPoint(0, 10, 0);
////		hull.addPoint(10,10,0);
////		hull.addPoint(0, 10, 10);
////		hull.addPoint(-2, 12, 0);
////		List<JFace> test = hull.compute();
////		System.out.println(test);
		}
}
