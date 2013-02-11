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

public class TestClip{
	public void test6(){
		System.out.println("Neuer Test1");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(800.0,50.0));
		l1.InsertBeforeHead(new cVertex(800.0,800.0));
		l1.InsertBeforeHead(new cVertex(50.0,800.0));
		l1.InsertBeforeHead(new cVertex(50.0,50.0));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(425.0,-894.1825396825396));
		l2.InsertBeforeHead(new cVertex(-762.2642857142857,425.0));
		l2.InsertBeforeHead(new cVertex(355.15493430516926,1324.0729356478373));
		l2.InsertBeforeHead(new cVertex(558.2178966086625,-725.0169566874126));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
		System.out.println("Neuer Test2");
		l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(800.0,50.0));
		l1.InsertBeforeHead(new cVertex(800.0,800.0));
		l1.InsertBeforeHead(new cVertex(50.0,800.0));
		l1.InsertBeforeHead(new cVertex(50.0,50.0));
		l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(558.2178966086625,-725.0169566874126));
		l2.InsertBeforeHead(new cVertex(355.15493430516926,1324.0729356478373));
		l2.InsertBeforeHead(new cVertex(425.0,1393.3191738113796));
		l2.InsertBeforeHead(new cVertex(1695.3001022494886,425.0));
		test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
		System.out.println("Neuer Test3");
		l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(800.0,50.0));
		l1.InsertBeforeHead(new cVertex(800.0,800.0));
		l1.InsertBeforeHead(new cVertex(50.0,800.0));
		l1.InsertBeforeHead(new cVertex(50.0,50.0));
		l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(451.91545042778057,347.6713638651233));
		l2.InsertBeforeHead(new cVertex(1007.617161263345,949.2041436355796));
		l2.InsertBeforeHead(new cVertex(1695.3001022494886,425.0));
		l2.InsertBeforeHead(new cVertex(558.2178966086625,-725.0169566874126));
		test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}

//	public void test2(){
//		System.out.println("Testdatensatz2:");
//		cVertexList l1 = new cVertexList();
//		l1.InsertBeforeHead(new cVertex(800.0,50.0));
//		l1.InsertBeforeHead(new cVertex(800.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,50.0));
//		cVertexList l2 = new cVertexList();
//		l2.InsertBeforeHead(new cVertex(247.7850851410801,-823.8032890245753));
//		l2.InsertBeforeHead(new cVertex(425.0,-1056.142162818955));
//		l2.InsertBeforeHead(new cVertex(1465.9735269000853,425.0));
//		l2.InsertBeforeHead(new cVertex(425.0,1279.2256482130344));
//		l2.InsertBeforeHead(new cVertex(255.52974041223132,1151.0838051189892));
//		ConvexClip test = new ConvexClip();
//		test.Start(l1, l2);
//		System.out.println(test.inters.toString());
//	}
//	public void test3(){
//		System.out.println("Testdatensatz3:");
//		cVertexList l1 = new cVertexList();
//		l1.InsertBeforeHead(new cVertex(800.0,50.0));
//		l1.InsertBeforeHead(new cVertex(800.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,50.0));
//		cVertexList l2 = new cVertexList();
//		l2.InsertBeforeHead(new cVertex(247.7850851410801,-823.8032890245753));
//		l2.InsertBeforeHead(new cVertex(255.52974041223132,1151.0838051189892));
//		l2.InsertBeforeHead(new cVertex(-1001.0182038834952,425.0));
//		ConvexClip test = new ConvexClip();
//		test.Start(l1, l2);
//		System.out.println(test.inters.toString());
//	}
//	public void test4(){
//		System.out.println("Testdatensatz4:");
//		cVertexList l1 = new cVertexList();
//		l1.InsertBeforeHead(new cVertex(800.0,50.0));
//		l1.InsertBeforeHead(new cVertex(800.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,50.0));
//		cVertexList l2 = new cVertexList();
//		l2.InsertBeforeHead(new cVertex(251.79841203291232,199.5950683926419));
//		l2.InsertBeforeHead(new cVertex(247.7850851410801,-823.8032890245753));
//		l2.InsertBeforeHead(new cVertex(425.0,-1056.142162818955));
//		l2.InsertBeforeHead(new cVertex(1465.9735269000853,425.0));
//		l2.InsertBeforeHead(new cVertex(1119.9764064191006,708.9261584325391));
//		ConvexClip test = new ConvexClip();
//		test.Start(l1, l2);
//		System.out.println(test.inters.toString());
//	}
//	public void test5(){
//		System.out.println("Testdatensatz5:");
//		cVertexList l1 = new cVertexList();
//		l1.InsertBeforeHead(new cVertex(800.0,50.0));
//		l1.InsertBeforeHead(new cVertex(800.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,800.0));
//		l1.InsertBeforeHead(new cVertex(50.0,50.0));
//		cVertexList l2 = new cVertexList();
//		l2.InsertBeforeHead(new cVertex(251.79841203291232,199.5950683926419));
//		l2.InsertBeforeHead(new cVertex(1119.9764064191006,708.9261584325391));
//		l2.InsertBeforeHead(new cVertex(425.0,1462.2990848585691));
//		l2.InsertBeforeHead(new cVertex(-590.5947179518521,662.1591531610617));
//		ConvexClip test = new ConvexClip();
//		test.Start(l1, l2);
//		System.out.println(test.inters.toString());
//	}
	public void testClipping1(){
		System.out.println("Test: 1");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0.5,0.5));
		l2.InsertBeforeHead(new cVertex(1.0,0.5));
		l2.InsertBeforeHead(new cVertex(1.0,0.7));
		l2.InsertBeforeHead(new cVertex(0.5,0.7));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping2(){
		System.out.println("Test: 2");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0.5,0.5));
		l2.InsertBeforeHead(new cVertex(0.5,-0.5));
		l2.InsertBeforeHead(new cVertex(1,-0.5));
		l2.InsertBeforeHead(new cVertex(1,0.5));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping3(){
		System.out.println("Test: 3");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0.5,1));
		l2.InsertBeforeHead(new cVertex(1.5,1));
		l2.InsertBeforeHead(new cVertex(1.5,1.5));
		l2.InsertBeforeHead(new cVertex(0.5,1.5));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping4(){
		System.out.println("Test: 4");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0,0.5));
		l2.InsertBeforeHead(new cVertex(0.5,0));
		l2.InsertBeforeHead(new cVertex(1,0.5));
		l2.InsertBeforeHead(new cVertex(0.5,1));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping5(){
		System.out.println("Test: 5");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(1,0.5));
		l2.InsertBeforeHead(new cVertex(1.5,0));
		l2.InsertBeforeHead(new cVertex(1.5,0.7));
		l2.InsertBeforeHead(new cVertex(1,0.7));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping6(){
		System.out.println("Test: 6");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(1,0.5));
		l2.InsertBeforeHead(new cVertex(1.5,0));
		l2.InsertBeforeHead(new cVertex(1.5,0.7));
		l2.InsertBeforeHead(new cVertex(1,1));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping7(){
		System.out.println("Test: 7");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0.5,0.3));
		l2.InsertBeforeHead(new cVertex(1,0.5));
		l2.InsertBeforeHead(new cVertex(0.5,0.7));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping8(){
		System.out.println("Test: 8");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0.5,1.5));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0.5,1));
		l2.InsertBeforeHead(new cVertex(1.5,1));
		l2.InsertBeforeHead(new cVertex(1.5,1.5));
		l2.InsertBeforeHead(new cVertex(0.5,1.5));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping9(){
		System.out.println("Test: 9");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(-0.5,0.2));
		l2.InsertBeforeHead(new cVertex(0.5,-0.2));
		l2.InsertBeforeHead(new cVertex(0,1));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping10(){
		System.out.println("Test: 10");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1.5,0.2));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(-0.2,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(-0.1,0.5));
		l2.InsertBeforeHead(new cVertex(0.2,0));
		l2.InsertBeforeHead(new cVertex(1.0,0));
		l2.InsertBeforeHead(new cVertex(1.3125,0.5));
		l2.InsertBeforeHead(new cVertex(1,1));
		l2.InsertBeforeHead(new cVertex(0.5,1));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping11(){
		System.out.println("Test: 11");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0.5,1));
		l2.InsertBeforeHead(new cVertex(0.7,1));
		l2.InsertBeforeHead(new cVertex(0.7,1.5));
		l2.InsertBeforeHead(new cVertex(0.5,1.5));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping12(){
		System.out.println("Test: 12");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(1,0.5));
		l2.InsertBeforeHead(new cVertex(1.5,0.2));
		l2.InsertBeforeHead(new cVertex(1.5,0.7));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}
	public void testClipping13(){
		System.out.println("Test: 13");
		cVertexList l1 = new cVertexList();
		l1.InsertBeforeHead(new cVertex(0,0));
		l1.InsertBeforeHead(new cVertex(1,0));
		l1.InsertBeforeHead(new cVertex(1,1));
		l1.InsertBeforeHead(new cVertex(0,1));
		cVertexList l2 = new cVertexList();
		l2.InsertBeforeHead(new cVertex(0,0));
		l2.InsertBeforeHead(new cVertex(1,1));
		l2.InsertBeforeHead(new cVertex(0,1.5));
		ConvexClip test = new ConvexClip();
		test.Start(l1, l2);
		System.out.println(test.inters.toString());
	}

}
