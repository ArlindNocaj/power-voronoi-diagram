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
package kn.uni.voronoitreemap.diagram;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kn.uni.voronoitreemap.convexHull.HEdge;
import kn.uni.voronoitreemap.convexHull.JConvexHull;
import kn.uni.voronoitreemap.convexHull.JFace;
import kn.uni.voronoitreemap.convexHull.JVertex;
import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.debuge.ImageFrame;
import kn.uni.voronoitreemap.j2d.Point2D;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;


/**
 * Computes the PowerDiagram by using the convex hull of the transformed half
 * planes of the sites.
 * 
 * @author Arlind Nocaj
 * 
 */
public class PowerDiagram {

	public static Random rand = new Random(99);
	public static final int halfLineScalingFactor = 10000;
	private static final double numericError = 1E-10;
	public static boolean debug = true;
	public static ImageFrame frame;
	public static Graphics2D graphics;

	protected JConvexHull hull = null;
	protected OpenList sites;
	protected PolygonSimple clipPoly;
	private int amountPolygons;
	private Rectangle2D bb;
	protected List<JFace> facets = null;

	// set of sites which forms a rectangle that is big enough to bound a
	// diagram with creating a bisector in the clipping polygon
	Site s1;
	Site s2;
	Site s3;
	Site s4;

	public PowerDiagram() {
		sites = null;
		clipPoly = null;
	}

	public PowerDiagram(OpenList sites, PolygonSimple clipPoly) {
		setSites(sites);
		setClipPoly(clipPoly);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see diagram.iPowerDiagram#setSites(datastructure.OpenList)
	 */
	public void setSites(OpenList sites) {
		this.sites = sites;
		hull = null;
	}

	public void setClipPoly(PolygonSimple polygon) {
		clipPoly = polygon;
		bb = polygon.getBounds2D();
		// create sites on a rectangle which is big enough to not create
		// bisectors which intersect the clippingPolygon
		double minX = bb.getMinX();
		double minY = bb.getMinY();

		double width = bb.getWidth();
		double height = bb.getHeight();

		s1 = new Site(minX - width, minY - height);
		s2 = new Site(minX + 2 * width, minY - height);
		s3 = new Site(minX + 2 * width, minY + 2 * height);
		s4 = new Site(minX - width, minY + 2 * height);

		s1.setAsDummy();
		s2.setAsDummy();
		s3.setAsDummy();
		s4.setAsDummy();

	}

	public PolygonSimple getClipPoly() {
		return clipPoly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see diagram.iPowerDiagram#computeDiagram()
	 */
	public void computeDiagram() {

		if (sites.size > 0) {
			sites.permutate();

			hull = new JConvexHull();
			Site[] array = sites.array;
			int size = sites.size;
			for (int z = 0; z < size; z++) {
				Site s = array[z];
				if (Double.isNaN(s.getWeight())){
			
//					s.setWeight(0.001);
					throw new RuntimeException(
							"Weight of a Site may not be NaN.");
				}
				hull.addPoint(s);
			}

			// reset the border sites, otherwise they could have old data
			// cached.
			s1.clear();
			s2.clear();
			s3.clear();
			s4.clear();

			hull.addPoint(s1);
			hull.addPoint(s2);
			hull.addPoint(s3);
			hull.addPoint(s4);

			// long start = System.currentTimeMillis();
			facets = hull.compute();

			// long end = System.currentTimeMillis();
			// double seconds = end - start;
			// seconds = seconds/1000.0;
			// System.out.println("Hull needed seconds: " + seconds);

			computeData();
		}
	}

	public void writeHullTestCodeOut(Site s) {
		System.out.println("hull.addPoint(" + s.x + "," + s.y + "," + s.z
				+ ");");
	}


	/**
	 * For each site the corresponding polygon and the corresponding neighbours
	 * are computed and stored in the site.
	 */
	private void computeData() {

		// make all vertices visible. When we finished working on one we make
		// invisible to not do it several times
		int vertexCount = hull.getVertexCount();
		boolean[] verticesVisited = new boolean[vertexCount];
		// for (int i = 0; i < vertexCount; i++) {
		//
		// JVertex v = hull.getVertex(i);
		//
		// Site site = (Site) v;
		// site.setPolygon(null);
		// site.setNeighbours(null);
		//
		// v.setHandled(true);
		// }

		int facetCount = facets.size();
		for (int i = 0; i < facetCount; i++) {
			JFace facet = facets.get(i);

			if (facet.isVisibleFromBelow()) {

				for (int e = 0; e < 3; e++) {
					// got through the edges and start to build the polygon by
					// going through the double connected edge list
					HEdge edge = facet.getEdge(e);
					JVertex destVertex = edge.getDest();
					Site site = (Site) destVertex.originalObject;

					if (!verticesVisited[destVertex.getIndex()]) {

						verticesVisited[destVertex.getIndex()] = true;
						if (site.isDummy) {
							continue;
						}

						// faces around the vertices which correspond to the
						// polygon corner points
						ArrayList<JFace> faces = getFacesOfDestVertex(edge);
						PolygonSimple poly = new PolygonSimple();
						double lastX = Double.NaN;
						double lastY = Double.NaN;
						double dx = 1;
						double dy = 1;
						for (JFace face : faces) {
							Point2D point = face
									.getDualPoint();
							double x1 = point.getX();
							double y1 = point.getY();
							if (!Double.isNaN(lastX)) {

								dx = lastX - x1;
								dy = lastY - y1;
								if (dx < 0) {
									dx = -dx;
								}
								if (dy < 0) {
									dy = -dy;
								}
							}
							if (dx > numericError || dy > numericError) {

								poly.add(x1, y1);
								lastX = x1;
								lastY = y1;
							}

						}
						site.nonClippedPolyon = poly;

						if (!site.isDummy) {
//							try {
								site.setPolygon(clipPoly.convexClip(poly));

//							} catch (Exception ex) {
//
//								ex.printStackTrace();
//								
//								// TODO fallback for nonconvex clipping
//							}
						}

					}
				}
			}

		}
	}

	/**
	 * Return the faces which are visible from below
	 * 
	 * @param edge
	 * @return
	 */
	private ArrayList<JFace> getFacesOfDestVertex(HEdge edge) {
		ArrayList<JFace> faces = new ArrayList<JFace>();
		HEdge previous = edge;
		JVertex first = edge.getDest();

		Site site = (Site) first.originalObject;
		ArrayList<Site> neighbours = new ArrayList<Site>();
		do {
			previous = previous.getTwin().getPrev();

			// add neighbour to the neighbourlist
			Site siteOrigin = (Site) previous.getOrigin().originalObject;
			if (!siteOrigin.isDummy) {
				neighbours.add(siteOrigin);
			}
			JFace iFace = previous.getiFace();

			if (iFace.isVisibleFromBelow()) {

				faces.add(iFace);
			}
		} while (previous != edge);
		site.setNeighbours(neighbours);
		return faces;
	}

	
	public void setAmountPolygons(int amountPolygons) {
		this.amountPolygons = amountPolygons;
	}

	public int getAmountPolygons() {
		return amountPolygons;
	}

	public static void initDebug() {
//		if (graphics == null) {
			BufferedImage image = new BufferedImage(2000, 2000,
					BufferedImage.TYPE_INT_RGB);

			frame = new ImageFrame(image);
			frame.setVisible(true);
			frame.setBounds(20, 20, 1600, 800);
			graphics = image.createGraphics();
			graphics.translate(200, 200);
//		}
	}

	public static void main(String[] args) {
		
		PowerDiagram diagram = new PowerDiagram();

		//normal list based on an array
		OpenList sites = new OpenList();

		Random rand = new Random(100);
			//  create a root polygon which limits the voronoi diagram.
		//  here it is just a rectangle.
		 
		PolygonSimple rootPolygon = new PolygonSimple();
		int width = 1000;
		int height = 1000;
		rootPolygon.add(0, 0);
		rootPolygon.add(width, 0);
		rootPolygon.add(width, height);
		rootPolygon.add(0, height);
		
		
		// create 100 points (sites) and set random positions in the rectangle defined above.
		for (int i = 0; i < 100; i++) {
			Site site = new Site(rand.nextInt(width), rand.nextInt(width));
			// we could also set a different weighting to some sites
			// site.setWeight(30)
			sites.add(site);
		}
		
		// set the list of points (sites), necessary for the power diagram
		diagram.setSites(sites);
		// set the clipping polygon, which limits the power voronoi diagram
		diagram.setClipPoly(rootPolygon);
		
		// do the computation
		diagram.computeDiagram();
		
		// for each site we can no get the resulting polygon of its cell. 
		// note that the cell can also be empty, in this case there is no polygon for the corresponding site.
		for (int i=0;i<sites.size;i++){
			Site site=sites.array[i];
			PolygonSimple polygon=site.getPolygon();
		}
	}

	public void showDiagram() {
		initDebug();

		graphics.clearRect(0, 0, 1600, 800);
		graphics.setColor(Color.blue);
		// graphics.scale(1/10.0, 1/10.0);

		Site[] array = sites.array;
		int size = sites.size;
		for (int z = 0; z < size; z++) {
			Site s = array[z];
			s.paint(graphics);

			PolygonSimple poly = s.getPolygon();
			if (poly != null) {
				graphics.draw(poly);
			} else {
				System.out.println("Poly null of:" + s);
			}
		}
		frame.repaint();
		// draw(s1);
		// draw(s2);
		// draw(s3);
		// draw(s4);

		// ArrayList<HLine> lines = this.getLines();
		// for (HLine line : lines) {
		// line.paint(graphics);
		// }
		// HashMap<Point2D.Double, HashSet<Site>> vertices = getVertices();
		// for (Point2D.Double p:vertices.keySet()){
		// System.out.println("Vertex:"+p);
		// graphics.setColor(Color.pink);
		// int radius = (int)Math.sqrt(3);
		// graphics.drawOval((int)p.getX()-20, (int)p.getY()-20, 2*20, 2*20);
		//
		// }

	}

	public void draw(Site s) {
		s.paint(graphics);

		PolygonSimple poly = s.getPolygon();
		if (poly != null) {
			graphics.draw(poly);
		} else {
			System.out.println("Poly null of:" + s);
		}
	}
}
