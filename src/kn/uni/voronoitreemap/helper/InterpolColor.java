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
package kn.uni.voronoitreemap.helper;



import java.awt.Color;


/**
 * Color Interpolator, e.g. for Fading and Highlighting
 * @author Nocaj
 *
 */
public class InterpolColor {
	double minValue;
	double maxValue;
	double leftH;
	double leftS;
        double text;
	double leftV;
	double rightH;
	double rightS;
	double rightV;

	public InterpolColor(double minValue, double maxValue, double leftH,
			double leftS, double leftV, double rightH, double rightS,
			double rightV) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.leftH = leftH;
		this.leftS = leftS;
		this.leftV = leftV;
		this.rightH = rightH;
		this.rightS = rightS;
		this.rightV = rightV;
	}

	public InterpolColor() {
		this(0.0f, 100.0f, 0.0f, 1.0f, 0.001f, 0f, 1f, 1f);
	}

	public void leftHSV(double leftH, double leftS, double leftV) {
		this.leftH = leftH;
		this.leftS = leftS;
		this.leftV = leftV;
	}

	public void rightHSV(double rightH, double rightS, double rightV) {
		this.rightH = rightH;
		this.rightS = rightS;
		this.rightV = rightV;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public Color getColorLinear(double key) {
	final double anteil = (key - minValue) / (maxValue - minValue);

		double divH = rightH - leftH;
		double divS = rightS - leftS;
		double divV = rightV - leftV;
		double h = leftH + anteil * divH;
		double s = leftS + anteil * divS;
		double v = leftV + anteil * divV;

		return new Color(Color.HSBtoRGB((float) h, (float) s, (float) v));
	}
	
	/**
	 * key value and the alpha value for this color, which is from 0 to 255
	 * @param key
	 * @param alpha
	 * @return
	 */
	public Color getColorLinear(double key,int alpha) {
		double anteil = (key - minValue) / (maxValue - minValue);

		double divH = rightH - leftH;
		double divS = rightS - leftS;
		double divV = rightV - leftV;
		double h = leftH + anteil * divH;
		double s = leftS + anteil * divS;
		double v = leftV + anteil * divV;
	Color c = new Color(Color.HSBtoRGB((float) h, (float) s, (float) v));
	
		return new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
	}

	public Color getColorLog(double key) {

		double anteil = (double) ((Math.log(key - minValue)) / (Math
				.log(maxValue - minValue)));

		double divH = rightH - leftH;
		double divS = rightS - leftS;
		double divV = rightV - leftV;
		double h = leftH + anteil * divH;
		double s = leftS + anteil * divS;
		double v = leftV + anteil * divV;

		return new Color(Color.HSBtoRGB((float) h, (float) s, (float) v));
	}

}
