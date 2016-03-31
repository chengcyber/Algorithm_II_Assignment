import java.awt.Color;

import edu.princeton.cs.algs4.Picture;


public class SeamCarver {
		// create a seam carver object based on the given picture
		public SeamCarver(Picture picture) {
			p = new Picture(picture);
			energy = new double[p.height()][p.width()];
			color = new Color[p.height()][p.width()];
			for(int i = 0; i < p.height() ; i++) {
				for(int j = 0; j < p.width(); j++) {
					color[i][j] = p.get(j, i);
				}
			}
		}              
		// current picture
		public Picture picture() {
			return p;
		}                         
		// width of current picture
		public     int width() {
//			return p.width();
			return color[0].length;
		}                           
		// height of current picture
		public     int height() {
//			return p.height();
			return color.length;
		}                          
		// energy of pixel at column x and row y
		public  double energy(int x, int y) {
			if (x < 0 || y < 0 || x >= p.width()|| y >= p.height())
				throw new IndexOutOfBoundsException();
			if (x == 0 || y == 0 || x == p.width() - 1 || y == p.height() - 1)
				return 1000.0;
			else {
				return Math.sqrt(getDelta2(x - 1, y, x + 1, y) 
					+ getDelta2(x, y - 1, x, y + 1));
			}
		}              
		// sequence of indices for horizontal seam
		public   int[] findHorizontalSeam() {
			
			return null;
		}              
		// sequence of indices for vertical seam
		public   int[] findVerticalSeam() {
			calEnergy();
			int[] seam = new int[p.height()];
			boolean[][] marked = new boolean[p.height()][p.width()];
			double[][] sumEnergy = new double[p.height()][p.width()];
			int[][] nextTo = new int[p.height()][p.width()];
			for(int i = 0; i < p.width(); i++) {
				dfsEnergy(i, 0, nextTo, sumEnergy, marked);
			}
			int minSumEnergyIndex = getMinIndex(sumEnergy[0]);
			seam[0] = minSumEnergyIndex;
			for (int i = 1; i < seam.length; i++) {
				seam[i] = nextTo[i - 1][seam[i - 1]];
			}
			return seam;
		}                
		// remove horizontal seam from current picture
		public void removeHorizontalSeam(int[] seam) {
			
		}  
		// remove vertical seam from current picture
		public void removeVerticalSeam(int[] seam) {
			Picture pic = new Picture(p.width() - 1, p.height());
			for (int col = 0; col < p.width() - 1; col++) {
				for (int row = 0; row < p.height(); row++) {
					
				}
			}
		}


		
		/* Private Section */
		private void dfsEnergy(int x, int y, int[][] nextTo, 
				double[][] sumEnergy, boolean[][] marked) {
			if (marked[y][x]) return;
			if (y == p.height() - 1) {
				sumEnergy[y][x] = energy[y][x];
				marked[y][x] = true;
				return;
			}
			int py = y + 1;
			for(int px : getNextX(x, y)) {
				dfsEnergy(px, py, nextTo, sumEnergy, marked);
				if (sumEnergy[y][x] == 0 || (sumEnergy[y][x] > sumEnergy[py][px] + energy[y][x])) {
					sumEnergy[y][x] = sumEnergy[py][px] + energy[y][x];
					nextTo[y][x] = px;
				}
			}
			marked[y][x] = true;
		}
		
		private int[] getNextX(int x, int y) {
			int[] result;
			if (x == 0) {
				result = new int[2];
				result[0] = x;
				result[1] = x + 1;
			} else if (x == p.width() - 1) {
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
		
		private void calEnergy() {
			for (int i = 0; i < energy.length; i++) {
				for (int j = 0; j < energy[0].length; j++) {
					energy[i][j] = this.energy(j, i);
				}
			}
		}
		
		private double getDelta2(int x1, int y1, int x2, int y2) {
			Color c1 = p.get(x1, y1);
			Color c2 = p.get(x2, y2);
			return Math.pow((c1.getRed() - c2.getRed()), 2) + 
			Math.pow((c1.getGreen() - c2.getGreen()), 2) +
			Math.pow((c1.getBlue() - c2.getBlue()), 2);
		}

		/* Private instance variables */
		Picture p;
		Color[][] color;
		double[][] energy;
}
