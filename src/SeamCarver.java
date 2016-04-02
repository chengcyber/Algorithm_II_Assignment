import java.awt.Color;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;


public class SeamCarver {
		/**
		 * Constructor
		 * Usage: SeamCarver sc = new SeamCarver(picture);
		 * @param picture object of algs4_stdlib Picture Class
		 * color 2D array to store the RGB information.
		 */
		public SeamCarver(Picture picture) {
			pHeight = picture.height();
			pWidth = picture.width();
			color = new int[pHeight][pWidth];
			for(int i = 0; i < pHeight ; i++) {
				for(int j = 0; j < pWidth; j++) {
					color[i][j] = picture.get(j, i).getRGB();;
				}
			}
			calEnergy();
			lastHorizontal = false;
		}              
		/**
		 * Method: picture
		 * Usage: Picture p = sc.picture();
		 * @return object of Picture after image processing.
		 */
		public Picture picture() {
			if (lastHorizontal) transpose();
			Picture pic = new Picture(pWidth, pHeight);
			for (int col = 0; col < pWidth; col++) {
				for (int row = 0; row < pHeight; row++) {
					pic.set(col, row, (new Color(color[row][col])));
				}
			}
			lastHorizontal = false;
			return pic;
		}                         
		/**
		 * Method: width
		 * Usage: int width = sc.width();
		 * @return the current picture width
		 */
		public int width() {
			if (lastHorizontal) {
				return pHeight;
			}
			return pWidth;
		}                           
		/**
		 * Method: height
		 * Usage: int height = sc.height();
		 * @return the current picture height
		 */
		public int height() {
			if (lastHorizontal) {
				return pWidth;
			}
			return pHeight;
		}                          
		/**
		 * Method: energy
		 * Usage: double e = sc.energy(x, y);
		 * @param x between 0 ~ width - 1
		 * @param y between 0 ~ height - 1
		 * @return the energy is 
		 * 		sqrt(R^2(dx, y) + G^2(dx, y) + B^2(dx, y)) + R^2(x, dy) + G^2(x, dy) + B^2(x, dy))
		 */
		public  double energy(int x, int y) {
			// if (x < 0 || y < 0 || x >= pWidth|| y >= pHeight)
			// 	throw new IndexOutOfBoundsException();
			// if (x == 0 || y == 0 || x == pWidth - 1 || y == pHeight - 1)
			// 	return 1000.0;
			// else {
			// 	return Math.sqrt(getDelta2(x - 1, y, x + 1, y) 
			// 		+ getDelta2(x, y - 1, x, y + 1));
			// }
			if (lastHorizontal) {
				return getE(y, x);
			}
			return getE(x, y);
		}              
		/**
		 * Method: findHorizontalSeam
		 * Usage: int[] seam = sc.findHorizontalseam();
		 * @return the seam array feedback with least energy path.
		 */
		public   int[] findHorizontalSeam() {
			if (!lastHorizontal)  {
				lastHorizontal = true;
				transpose();
			}
			return fakeFindVerticalSeam();
		}              
		/**
		 * Method: findVerticalSeam
		 * Usage: int[] seam = sc.findVerticalSeam();
		 * @return the seam array feedback with least energy path.
		 */
		public   int[] findVerticalSeam() {
			if (lastHorizontal) {
				lastHorizontal = false;
				transpose();
			}
			return fakeFindVerticalSeam();
		}
		/**
		 * Method: removeHorizontalseam
		 * Usage: sc.removeHorizontalSeam();
		 * @param seam the array from findHorizontalseam
		 * shift the element in the color array according to the seam.
		 */
		public void removeHorizontalSeam(int[] seam) {
			if (!lastHorizontal)  {
				lastHorizontal = true;
				transpose();
			}
			fakeRemoveVerticalSeam(seam);
		}  
		/**
		 * Method: removeVertivalSeam
		 * Usage: sc.removeVerticalseam();
		 * @param seam the array from findVerticalSeam
		 * shift the element in color array according to seam.
		 */
		public void removeVerticalSeam(int[] seam) {
			if (lastHorizontal) {
				lastHorizontal = false;
				transpose();
			}
			fakeRemoveVerticalSeam(seam);
		}

		
		/* Private Section */
		/**
		 * Method: fakeRemoveVerticalSeam
		 * The idea is reuse removeVerticalSeam when removeHorizontalSeam
		 * The fake function is to cut the relationship when horizontal things called.
		 * @param seam the array of least energy seam.
		 */
		private void fakeRemoveVerticalSeam(int[] seam) {
			if (pWidth <= 1) 
				throw new java.lang.IllegalArgumentException();
			/* check null pointer */
			if (seam == null)
				throw new java.lang.NullPointerException();
			/* check length */
			if (seam.length != pHeight)
				throw new java.lang.IllegalArgumentException();
			int seamIndex = 0,lastIndex = 0;
			for(int i = 0; i < pHeight; i++) {
				seamIndex = seam[i];
				/* check if difference between -1 ~ 1 */
				if (i != 0) {
					if(Math.abs(lastIndex - seamIndex) > 1) {
						throw new java.lang.IllegalArgumentException();
					}
				}
				/* check bound */
				if (seamIndex < 0 || seamIndex >= pWidth)
					throw new java.lang.IllegalArgumentException();
				if (seamIndex != pWidth - 1)
					System.arraycopy(color[i], seamIndex + 1, color[i], seamIndex, pWidth - 1 - seamIndex);
				lastIndex = seamIndex;
			}
			pWidth--;
			maintainEnergy(seam);
		}
		/**
		 * Method: fakeFindVerticalSeam
		 * The idea is to reuse the findVeritcalSeam when findHorizontalSeam
		 * the fake function is to cut the relationship when the horizontal things called. 
		 * @return
		 */
		private int[] fakeFindVerticalSeam() {
			Bag<Point> topo = topoColor();
			double[][] distTo = new double[pHeight][pWidth];
			int[][] edgeTo = new int[pHeight][pWidth];
			for(Point p : topo) {
				if (p.y == 0) distTo[0][p.x] = 1000;
				double cur_dist = distTo[p.y][p.x];
				int py = p.y + 1;
				if (py != pHeight) {
					for (int px : getReachable(p.x, p.y)) {
						double cur_ene = energy[py][px];
						if (distTo[py][px] == 0 || distTo[py][px] >= cur_dist + cur_ene) {
							distTo[py][px] = cur_dist + cur_ene;
							edgeTo[py][px] = p.x;
						}
					}
				}
			}
			int minIndex = getMinIndex(distTo[pHeight - 1]);
			int[] seam = new int[pHeight];
			seam[pHeight - 1] = minIndex;
			for (int i = pHeight - 2; i >= 0; i--) {
				seam[i] = edgeTo[i + 1][seam[i + 1]];	
			}
			return seam;
		}
		/**
		 * get the topological order in color array.
		 * @return topological order
		 */
		private Bag<Point> topoColor() {
			boolean[][] marked = new boolean[pHeight][pWidth];
			Bag<Point> topo = new Bag<Point>();
			dfs(0, -1, marked, topo);
			return topo;
		}
		/**
		 * The DFS strategy to get the topological order.
		 * @param x width
		 * @param y height
		 * @param marked mark the visited point
		 * @param topo the topological order returned.
		 */
		private void dfs(int x, int y, boolean[][] marked, Bag<Point> topo) {
			int py = y + 1;
			if (y == -1) {
				for (int i = 0; i < pWidth; i++) {
					dfs(i, 0, marked, topo);
				}
			} else if (marked[y][x]) {
				
			} else if (y == pHeight - 1) {
				topo.add(new Point(x, y));
				marked[y][x] = true;
			} else {
				for (int px : getReachable(x, y)) {
					dfs(px, py, marked, topo);
				}
				topo.add(new Point(x, y));
				marked[y][x] = true;
			}
		}
		/**
		 * Method: getReachable
		 * @param x the pixel x
		 * @param y the pixel y
		 * @return contains the next reachable value.
		 */
		private int[] getReachable(int x, int y) {
			int[] result;
			if (pWidth == 1){
				result = new int[1];
				result[0] = x;
			} else if (x == 0) {
				result = new int[2];
				result[0] = x;
				result[1] = x + 1;
			} else if (x == pWidth - 1) {
				result = new int[2];
				result[0] = x - 1;
				result[1] = x;
			} else {
				result = new int[3];
				result[0] = x - 1;
				result[1] = x;
				result[2] = x + 1;
			}
			return result;
		}
		/**
		 * Method: getMinIndex
		 * @param candidate the array to find minIndex.
		 * @return get the minimum value's index in candidate array.
		 */
		private int getMinIndex(double[] candidate) {
			int index = -1;
			double minNumber = Double.MAX_VALUE;
			for (int i = 0; i < candidate.length; i++) {
				if (candidate[i] < minNumber) {
					index = i;
					minNumber = candidate[i];
				}
			}
			return index;
		}
		/**
		 * Method: calEnergy
		 * calculates each pixel's energy, and store in energy array. 
		 */
		private void calEnergy() {
			energy = new double[pHeight][pWidth];
			for (int i = 0; i < energy.length; i++) {
				for (int j = 0; j < energy[0].length; j++) {
					energy[i][j] = getE(j, i);
				}
			}
		}
		/**
		 * Trying to do less calculation when removedSeam,
		 * finally it is faster to avoid shifting element in array!
		 * @param seam the array of least energy seam.
		 */
		private void maintainEnergy(int[] seam) {
			// for(int i = 1; i < pHeight - 1; i++) {
			// 	int seamIndex = seam[i];
			// 	if (seamIndex < pWidth - 1) {
			// 		System.arraycopy(energy[i], seamIndex + 1, energy[i], seamIndex, pWidth - 1 - seamIndex);
			// 	}
			// 	if (seamIndex != 0) {
			// 		energy[i][seamIndex - 1] = getE(seamIndex - 1, i);
			// 	}
			// 	if (seamIndex != pWidth - 1) {
			// 		energy[i][seamIndex] = getE(seamIndex, i);
			// 	}
			// }
			calEnergy();
		}
		/**
		 * Method: getDelta2
		 * @param x1 the x of pixel one
		 * @param y1 the y of pixel one
		 * @param x2 the x of pixel two
		 * @param y2 the y of pixel two
		 * @return (R(x1, y1) - R(x2, y2)) ^ 2 + (G(x1, y1) - G(x2, y2)) ^ 2 +
		 * (B(x1, y1) - B(x2, y2)) ^ 2
		 */
		private double getDelta2(int x1, int y1, int x2, int y2) {
			int r1 = getColor(x1, y1, R);
			int g1 = getColor(x1, y1, G);
			int b1 = getColor(x1, y1, B);
			int r2 = getColor(x2, y2, R);
			int g2 = getColor(x2, y2, G);
			int b2 = getColor(x2, y2, B);
			return Math.pow((r1 - r2), 2) + 
			Math.pow((g1 - g2), 2) +
			Math.pow((b1 - b2), 2);
		}
		private int getColor(int x, int y, int channel) {
			return color[y][x] >> channel & 255;
		}
		private void transpose() {
			int tmp = pWidth;
			pWidth = pHeight;
			pHeight = tmp;
			int[][] transposedColor = new int[pHeight][pWidth];
			for(int i = 0; i < pHeight ; i++) {
				for(int j = 0; j < pWidth; j++) {
					transposedColor[i][j] = color[j][i];
				}
			}
			color = transposedColor;
			calEnergy();
		}
		private double getE(int x, int y) {
			if (x < 0 || y < 0 || x >= pWidth|| y >= pHeight)
				throw new IndexOutOfBoundsException( x + ", " + y);
			if (x == 0 || y == 0 || x == pWidth - 1 || y == pHeight - 1)
				return 1000.0;
			else {
				return Math.sqrt(getDelta2(x - 1, y, x + 1, y) 
					+ getDelta2(x, y - 1, x, y + 1));
			}
		}
		/* Private instance variables */
		private int pHeight;			/* current height */
		private int pWidth;				/* current width */
		private double[][] energy;		/* energy of each pixel */
		private int[][] color;			/* current color array */
		private boolean lastHorizontal; /* record last Operation is Horizontal */
		private static final int R = 16;
		private static final int G = 8;
		private static final int B = 0;
}

/*
 * Package the x, y information in Point class.
 */
class Point {
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x;
	public int y;
}
