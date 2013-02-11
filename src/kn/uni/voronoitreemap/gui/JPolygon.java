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


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import kn.uni.voronoitreemap.extension.VoroCellObject;
import kn.uni.voronoitreemap.j2d.Point2D;
import kn.uni.voronoitreemap.j2d.PolygonSimple;



/**
 * Polygon Class to show the polygon as a component.
 * @author Nocaj
 *
 */

public class JPolygon extends JComponent implements VoroCellObject {
	String text;
	private Font textFont;
	PolygonSimple polygon;
	private boolean mouseIn=false;
	private boolean isFinal;
	private Integer id;
	private boolean isLast=false;
	private Color fillColor=new Color(255,255,255,0);
	private int height=0;
	private boolean makeFontSmaller=false;
	
	
	public JPolygon(Integer id, String title){
		this(id);
		this.text=title;
	}
	public JPolygon(Integer id){
		this.id=id;
		polygon=null;
		this.setOpaque(false);
//		initListeners();
	}
	
	public JPolygon(PolygonSimple polygon, Integer id){
		this(id);
		setPolygon(polygon);
	}
	
public void calculateFittingFont(){
	if (polygon==null || text==null) return;
		textFont=null;
		new Thread(){
		public void run(){
			Font font = new Font("Serif", Font.BOLD, 5);
		int fontSize = 5;
		int w=0;
		int h=0;
		Point2D centroid =polygon.getCentroid();
		Point2D p1=centroid;
		Point2D p2=centroid;
		Point2D p3=centroid;
		Point2D p4=centroid;
       do{
    	   fontSize+=2;
    	   font = new Font("Serif", Font.PLAIN, fontSize);
        	FontMetrics fontMetrics = getFontMetrics(font);
            
        	w = fontMetrics.stringWidth(text);
            h = fontMetrics.getHeight();
          
            p1 = new Point2D(centroid.getX()-w/2,centroid.getY()-h/2);
            p2 = new Point2D(centroid.getX()-w/2,centroid.getY()+h/2);
            p3 = new Point2D(centroid.getX()+w/2,centroid.getY()-h/2);
            p4 = new Point2D(centroid.getX()+w/2,centroid.getY()+h/2);
             
        }while(polygon.contains(p1)&& polygon.contains(p2) && polygon.contains(p3) && polygon.contains(p4));
		if (fontSize-4>0){
			if (makeFontSmaller){
				textFont=new Font("Serif", Font.BOLD, (int)((fontSize)*0.6));
			}
			else {
				if (text.length()<=4 && height==3){
					//small words gett smaller so that they do not look too important
					textFont=new Font("Serif", Font.BOLD, (int)(fontSize*0.6));
				}else{
				textFont=new Font("Serif", Font.BOLD, fontSize-4);
				}
				}
			
			
		}
		}
		}.start();
	}
	

	public PolygonSimple getPolygon(){
		return polygon;
	}
	private void initListeners() {
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				doMouseReleased(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				doMousePressed(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				doMouseExited(e);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				doMouseEntered(e);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				doMouseClicked(e);
			}
		});
	}
	
	
	protected void doMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e);
	}
	protected void doMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	protected void doMouseClicked(MouseEvent e) {
		
	}

	protected void doMouseEntered(MouseEvent e) {
		System.out.println(e);
		mouseIn=true;
		repaint();
	}

	protected void doMouseExited(MouseEvent e) {
		// TODO Auto-generated method stub,
		mouseIn=false;
		repaint();
	}
	
	public void setPolygon(PolygonSimple p){
		this.polygon=p;
		if (polygon!=null){
		Rectangle bounds = p.getBounds();
		setLocation(bounds.x, bounds.y);
		this.setSize(bounds.width, bounds.height);
		this.setPreferredSize(new Dimension(bounds.width, bounds.height));
		this.repaint();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
//		if (polygon!=null){
//		Graphics2D g2 = (Graphics2D)g;
//		if (isFinal)
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		 Stroke stroke = new BasicStroke(1.4F);
//			    g2.setStroke(stroke);
//		
//		int length = polygon.getNumPoints();
//		double[] x = polygon.getXPoints();
//		double[] y = polygon.getYPoints();
//		int tx = this.getLocation().x;
//		int ty = this.getLocation().y;
//		
//		/**
//		 * Fill Polygon
//		 */
//		int[] x2=new int[length+1];
//		int[] y2=new int[length+1];
//		for (int i = 0; i <= length; i++){
//			x2[i]=(int) x[i]-tx;
//			y2[i]=(int) y[i]-ty;
//		}
//		g.setColor(new Color(fillColor.getRed(),fillColor.getGreen(),fillColor.getBlue(),80));
//		g.fillPolygon(x2, y2, length);
//		
//		/**
//		 * Draw Border
//		 */
//		if (mouseIn==true){
//			g.setColor(Color.blue);
//		}else{
////			g.setColor(Color.black);
//			g.setColor(fillColor.darker());
//		}
//		
//		for (int i = 0; i < (length-1); i++){
//			g.drawLine(x2[i], y2[i], x2[i + 1], y2[i + 1]);
//		}
//		g.drawLine(x2[length-1], y2[length-1], x2[0], y2[0]);
//
//		
////		if (mouseIn){
////			Vector<Double> circle = polygon.getInnerCircle();
////			g.setColor(Color.blue);
////			g.drawOval((int)(circle.get(0)-circle.get(2))-getLocation().x, (int)(circle.get(1)-circle.get(2))-getLocation().y, (int)(circle.get(2)*2),(int)(circle.get(2)*2));
////		}
//		if (isFinal){
//		if (textFont!=null){
//		FontMetrics fontMetrics = getFontMetrics(textFont);
//   	int w = fontMetrics.stringWidth(text);
//        int h = fontMetrics.getHeight();
//		//g.setColor(new Color(237, 240, 249));
//        if (mouseIn && isLast){
//        	g.setColor(new Color(175,175,175,200));
//        }else{
//        	if (height==2)
//        	g.setColor(new Color(175,175,175,60));
//        	else g.setColor(new Color(175,175,175,100));
//        }
//       
//		g.setFont(textFont);
//       
//		g.drawString(text, (int)polygon.getCentroid().getX()-tx-(int)(w/2), (int)polygon.getCentroid().getY()-ty+(int)(h/2));
//		}
//		}
//		}
	}
	@Override
	public boolean contains(int x, int y) {
//	return false;
		if (super.contains(x, y)){
			if (polygon==null) return true;
			return polygon.contains(x+this.getLocation().x,y+ getLocation().y);
		}else return false;
	}
	
	@Override
	public boolean contains(Point p) {
//		return false;
		if (super.contains(p)){
			if (polygon==null) return true;
			return polygon.contains(p.x+getLocation().x,p.y+getLocation().y);
		}else return false;
	}
	
	
	public static void main(String[] args){
//		JFrame frame=new JFrame();
//		frame.setBounds(50, 50, 600, 700);
//		JPolygon polygon=new JPolygon(1);
//		NPolygon2D p=new NPolygon2D();
//		p.addPoint2D(new NPoint(30,50));
//		p.addPoint2D(new NPoint(300, 30));
//		p.addPoint2D(new NPoint(150,100));
//		p.addPoint2D(new NPoint(30, 50));
//		polygon.setPolygon(p);
//		frame.add(polygon);
//		frame.setVisible(true);
	}

	@Override
	public void doFinalWork() {
		calculateFittingFont();
		isFinal=true;
//		repaint();
	}

	
	public void setVoroPolygon(PolygonSimple p, int height) {
		p=(PolygonSimple)p.clone();
		this.height=height;
		isFinal=false;
		Rectangle bounds = p.getBounds();
//		if (height==2)
//		p.shrinkForBorder(0.98);
//		else if (height==3)
//			p.shrinkForBorder(0.96);
//		else{
//			p.shrinkForBorder(0.95);
//		}
		this.polygon=p;
		setLocation(bounds.x, bounds.y);

		this.setSize(bounds.width, bounds.height);
		this.setPreferredSize(new Dimension(bounds.width, bounds.height));
//		repaint();
	}
	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}
	public boolean getisLast() {
		return isLast;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	
	
	public Color getFillColor() {
		return fillColor;
	}
	
	public void setMakeFontSmaller(boolean b) {
		makeFontSmaller=b;
	}
	@Override
	public void setVoroPolygon(PolygonSimple polygon) {
		this.polygon=polygon;
		
	}
}
