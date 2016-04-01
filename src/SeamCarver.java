import java.awt.Color;

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
			color = new Color[pHeight][pWidth];
			for(int i = 0; i < pHeight ; i++) {
				for(int j = 0; j < pWidth; j++) {
					color[i][j] = picture.get(j, i);
				}
			}
		}              
		/**
		 * Method: picture
		 * Usage: Picture p = sc.picture();
		 * @return object of Picture after image processing.
		 */
		public Picture picture() {
			Picture pic = new Picture(pWidth, pHeight);
			for (int col = 0; col < pWidth; col++) {
				for (int row = 0; row < pHeight; row++) {
					pic.set(col, row, color[row][col]);
				}
			}
			return pic;
		}                         
		/**
		 * Method: width
		 * Usage: int width = sc.width();
		 * @return the current picture width
		 */
		public     int width() {
			return pWidth;
		}                           
		/**
		 * Method: height
		 * Usage: int height = sc.height();
		 * @return the current picture height
		 */
		public     int height() {
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
			if (x < 0 || y < 0 || x >= pWidth|| y >= pHeight)
				throw new IndexOutOfBoundsException();
			if (x == 0 || y == 0 || x == pWidth - 1 || y == pHeight - 1)
				return 1000.0;
			else {
				return Math.sqrt(getDelta2(x - 1, y, x + 1, y) 
					+ getDelta2(x, y - 1, x, y + 1));
			}
		}              
		/**
		 * Method: findHorizontalSeam
		 * Usage: int[] seam = sc.findHorizontalseam();
		 * @return the seam array feedback with least energy path.
		 */
		public   int[] findHorizontalSeam() {
//			calEnergy();
			int[] seam = new int[pWidth];
			boolean[][] marked = new boolean[pWidth][pHeight];
			double[][] sumEnergy = new double[pWidth][pHeight];
			int[][] nextTo = new int[pWidth][pHeight];
			for (int i = 0; i < pHeight; i++) {
				dfsEnergy(i, 0, nextTo, sumEnergy, marked, false);
			}
			int minSumEnergyIndex = getMinIndex(sumEnergy[0]);
			seam[0] = minSumEnergyIndex;
			for (int i = 1; i < seam.length; i++) {
				seam[i] = nextTo[i - 1][seam[i - 1]];
			}
			return seam;
		}              
		/**
		 * Method: findVerticalSeam
		 * Usage: int[] seam = sc.findVerticalSeam();
		 * @return the seam array feedback with least energy path.
		 */
		public   int[] findVerticalSeam() {
//			calEnergy();
			int[] seam = new int[pHeight];
			boolean[][] marked = new boolean[pHeight][pWidth];
			double[][] sumEnergy = new double[pHeight][pWidth];
			int[][] nextTo = new int[pHeight][pWidth];
			for(int i = 0; i < pWidth; i++) {
				dfsEnergy(i, 0, nextTo, sumEnergy, marked, true);
			}
			int minSumEnergyIndex = getMinIndex(sumEnergy[0]);
			seam[0] = minSumEnergyIndex;
			for (int i = 1; i < seam.length; i++) {
				seam[i] = nextTo[i - 1][seam[i - 1]];
			}
			return seam;
		}
		/**
		 * Method: removeHorizontalseam
		 * Usage: sc.removeHorizontalSeam();
		 * @param seam the array from findHorizontalseam
		 * shift the element in the color array according to the seam.
		 */
		public void removeHorizontalSeam(int[] seam) {
			/* check null pointer */
			if (seam == null)
				throw new java.lang.NullPointerException();
			/* check length */
			if (seam.length != pWidth)
				throw new java.lang.IllegalArgumentException();
			int seamIndex = 0,lastIndex = 0;
			for(int i = 0; i < pWidth; i++) {
				/* check if difference between -1 ~ 1 */
				if (i != 0) {
					if(Math.abs(lastIndex - seamIndex) > 1) {
						throw new java.lang.IllegalArgumentException();
					}
				}
				seamIndex = seam[i];
				/* check bound */
				if (seamIndex < 0 || seamIndex >= pHeight)
					throw new java.lang.IllegalArgumentException();
				for (int j = seamIndex; j < color.length - 1; j++) {
					color[j][i] = color[j + 1][i];
				}
				lastIndex = seamIndex;
			}
			pHeight--;
		}  
		/**
		 * Method: removeVertivalSeam
		 * Usage: sc.removeVerticalseam();
		 * @param seam the array from findVerticalSeam
		 * shift the element in color array according to seam.
		 */
		public void removeVerticalSeam(int[] seam) {
			/* check null pointer */
			if (seam == null)
				throw new java.lang.NullPointerException();
			/* check length */
			if (seam.length != pHeight)
				throw new java.lang.IllegalArgumentException();
			int seamIndex = 0,lastIndex = 0;
			for(int i = 0; i < pHeight; i++) {
				/* check if difference between -1 ~ 1 */
				if (i != 0) {
					if(Math.abs(lastIndex - seamIndex) > 1) {
						throw new java.lang.IllegalArgumentException();
					}
				}
				seamIndex = seam[i];
				/* check bound */
				if (seamIndex < 0 || seamIndex >= pWidth)
					throw new java.lang.IllegalArgumentException();
				if (seamIndex < pWidth - 1)
					System.arraycopy(color[i], seamIndex + 1, color[i], seamIndex, pWidth - 1 - seamIndex);
				lastIndex = seamIndex;
			}
			pWidth--;
		}


		
		/* Private Section */
		/**
		 * Method: dfsEnergy
		 * @param x isVer -> width, otherwise -> height.
		 * @param y isVer -> height, otherwise -> width.
		 * @param nextTo the next x value to go through.
		 * @param sumEnergy the sum array of current energy.
		 * @param marked turn true when the (x,y) is done.
		 * @param isVer boolean value to decide vertical or horizontal.
		 */
		private void dfsEnergy(int x, int y, int[][] nextTo, 
				double[][] sumEnergy, boolean[][] marked, boolean isVer) {
			if (marked[y][x]) return;
			if (isVer && (y == pHeight - 1)) {
				sumEnergy[y][x] = energy(x, y);
				marked[y][x] = true;
				return;
			}
			if (!isVer && (y == pWidth - 1)) {
				sumEnergy[y][x] = energy(y, x);
				marked[y][x] = true;
				return;
			}
			int py = y + 1;
			if (isVer) {
				for(int px : getNextX(x, y)) {
					dfsEnergy(px, py, nextTo, sumEnergy, marked, isVer);
					double cur_ene = energy(x, y);
					if (sumEnergy[y][x] == 0 || (sumEnergy[y][x] > sumEnergy[py][px] + cur_ene)) {
						sumEnergy[y][x] = sumEnergy[py][px] + cur_ene;
						nextTo[y][x] = px;
					}
				}
			} else {
				for(int px : getNextY(x, y)) {
					dfsEnergy(px, py, nextTo, sumEnergy, marked, isVer);
					double cur_ene = energy(y, x);
					if (sumEnergy[y][x] == 0 || (sumEnergy[y][x] > sumEnergy[py][px] + cur_ene)) {
						sumEnergy[y][x] = sumEnergy[py][px] + cur_ene;
						nextTo[y][x] = px;
					}
				}	
			}
			marked[y][x] = true;
		}
		/**
		 * Method: getNextX
		 * @param x the pixel x
		 * @param y the pixel y
		 * @return contains the next reachable value 
		 */
		private int[] getNextX(int x, int y) {
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
		 * Method: getNextY
		 * @param x the pixel x
		 * @param y the pixel y
		 * @return contains the next reachable value.
		 */
		private int[] getNextY(int x, int y) {
			int[] result;
			if (pHeight == 1){
				result = new int[1];
				result[0] = x;
			} else if (x == 0) {
				result = new int[2];
				result[0] = x;
				result[1] = x + 1;
			} else if (x == pHeight - 1) {
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
		// private double[][] calEnergy() {
		// 	double energy = new double[pHeight][pWidth];
		// 	for (int i = 0; i < energy.length; i++) {
		// 		for (int j = 0; j < energy[0].length; j++) {
		// 			energy[i][j] = this.energy(j, i);
		// 		}
		// 	}
		// 	return energy;
		// }
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
			Color c1 = color[y1][x1];
			Color c2 = color[y2][x2];
			return Math.pow((c1.getRed() - c2.getRed()), 2) + 
			Math.pow((c1.getGreen() - c2.getGreen()), 2) +
			Math.pow((c1.getBlue() - c2.getBlue()), 2);
		}

		/* Private instance variables */
		private int pHeight;			/* current height */
		private int pWidth;				/* current width */
		private Color[][] color;		/* current color array */
}
