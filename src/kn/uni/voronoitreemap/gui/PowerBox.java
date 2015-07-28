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
package kn.uni.voronoitreemap.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.Point2D;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;




/**
 * JFrame where you can place and move sites around. The weightings can be increased too.
 * @author Arlind Nocaj
 *
 */
public class PowerBox extends JFrame {
	ArrayList<JSite> points=new ArrayList<JSite>();
	OpenList sites=new OpenList();
	PolygonSimple clipPoly=new PolygonSimple();
	
	HashMap<Double, HashSet<Site>> vertices=new HashMap<Double, HashSet<Site>>();
	public static PowerBox powerBox=null;
	JPanel panel=new JPanel();
	public PowerBox() {
		super();
		clipPoly.add(50,50);
		clipPoly.add(50,800);
		clipPoly.add(800,800);
		clipPoly.add(800,50);
		this.powerBox=this;
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(Color.BLACK);
		getContentPane().add(panel);
		panel.setVisible(true);
		panel.setLayout(null);
		panel.addMouseListener(new MouseAdapter() {
	
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
			
			}
			@Override
			synchronized public void mouseClicked(MouseEvent e) {
				
				Point p = e.getPoint();
				double weight=30;
				Site site = new Site(p.getX(), p.getY(),weight);
				sites.add(site);
				JSite jsite=new JSite(site);
				
				panel.add(jsite);
				jsite.setVisible(true);
				points.add(jsite);
				if (points.size()>1){
//					computeDiagram();
//					vertices = diagram.getVertices();
					//lines = diagram.getLines();
				}	
			    PowerBox.powerBox.computeDiagram();
				setPreferredSize(getSize());
				repaint();
			}
		});
		setVisible(true);
		setSize(500, 400);
	}

	public void computeDiagram() {
		
		
		PowerDiagram diagram = new PowerDiagram(sites, clipPoly);
		diagram.computeDiagram();
		for(JSite component:points){
			
			component.setPolygon(component.getSite().getPolygon());
		}		
		setPreferredSize(getSize());
		pack();	
		repaint();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PowerBox();
	}
	
	@Override
	public void paint(Graphics g2) {
		
		// TODO Auto-generated method stub
		super.paint(g2);
//		paintComponents(g2);
		Graphics2D g=(Graphics2D)g2;
		g2.translate(2,22);
		g.setColor(Color.red);
		Site[] array = sites.array;
		int size=sites.size;
		g2.setColor(Color.green);
		for (int z=0;z<size;z++){
		Site s = array[z];	
		double posX=s.getX();
		double posY=s.getY();
			double radius = Math.sqrt(s.getWeight());
			g.drawOval((int)posX-(int)radius, (int)posY-(int)radius, (int)(2*radius), (int)(2*radius));
			int r2=5;
			Color normal = g.getColor();
			if(s.getPolygon()==null){
				g.setColor(Color.red);
			}
			g.drawRect((int)posX-r2, (int)posY-r2, 2*r2, 2*r2);
			g.setColor(normal);
		}
//		if (vertices!=null){
//		for (Point2D p:vertices.keySet()){
//			g.setColor(Color.green);
//			int radius=5;
//			g.drawRect((int)p.getX()-radius, (int)p.getY()-radius, 2*radius, 2*radius);
//			
//		}
//		}
//		if (lines!=null){
//		for (HLine line:lines){
//			line.paint((Graphics2D)g);
//		}
//		}
		
		
		int i=0;
		for (int z=0;z<size;z++){
			g.setColor(Color.GRAY.brighter());
		Site s = array[z];	
				if (i!=-2){
				PolygonSimple poly = s.getPolygon();
				if (poly!=null){
				g.draw(poly);
				}
				s.paint(g);
				}
			i++;
	
		}
		
		for (int z=0;z<size;z++){
			Site s = array[z];	
			double posX=s.getX();
			double posY=s.getY();
				double radius = Math.sqrt(s.getWeight());
				
				g.drawOval((int)posX-(int)radius, (int)posY-(int)radius, (int)(2*radius), (int)(2*radius));
				int r2=7;
				g.drawRect((int)posX-r2, (int)posY-r2, 2*r2, 2*r2);
				PolygonSimple poly = s.getPolygon();
				if (poly!=null){
				Point2D center = poly.getCentroid();
				r2=5;
				g.setColor(Color.blue);
				g.drawRect((int)center.getX()-r2, (int)center.getY()-r2, 2*r2, 2*r2);
				}
			}
		
		this.validate();
	}

}
