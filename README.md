Java Power Voronoi Diagram
=====================

*Power Voronoi Diagram* is a fast standalone java (minimum 1.6) library which computes a weighted Voronoi Diagram, called Power diagram.

(see <http://en.wikipedia.org/wiki/Power_diagram>)

The main difference to the ordinary Voronoi diagram is the possibility to set a positive weighting to each site which influences the corresponding Voronoi cells. If these weights are set to zero, the result is the ordinary Voronoi diagram.

Implementation
---------------
The implementation is based on the computation of a 3-dimensional convex hull, for which a randomized incremental algorithm is used, which is expected to run in O(n log n) time and needs O(n) space.

The following article contains most important references related to this implementation.

* Arlind Nocaj, Ulrik Brandes, "Computing Voronoi Treemaps: Faster, Simpler, and Resolution-independent", Computer Graphics Forum, vol. 31, no. 3, June 2012, pp. 855-864


Usage
-------------

```java
PowerDiagram diagram = new PowerDiagram();

// normal list based on an array
OpenList sites = new OpenList();

Random rand = new Random(100);
// create a root polygon which limits the voronoi diagram.
// here it is just a rectangle.

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
```

maven (gradle sbt leiningen)
-------------

```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.ArlindNocaj</groupId>
    <artifactId>power-voronoi-diagram</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```
For more info (see <https://jitpack.io/#ArlindNocaj/power-voronoi-diagram>)

License
------------------------

Copyright (c) 2013 Arlind Nocaj, University of Konstanz.

All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html

For distributors of proprietary software, other licensing is possible on request: arlind.nocaj@gmail.com


Citation
-----------------

This work is based on the publication below, please cite on usage.

* Arlind Nocaj, Ulrik Brandes, "Computing Voronoi Treemaps: Faster, Simpler, and Resolution-independent", Computer Graphics Forum, vol. 31, no. 3, June 2012, pp. 855-864
