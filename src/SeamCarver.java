import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
		// create a seam carver object based on the given picture
		public SeamCarver(Picture picture) {
			p = new Picture(picture);
			energy = new double[p.height()][p.width()];
		}              
		// current picture
		public Picture picture() {
			return p;
		}                         
		// width of current picture
		public     int width() {
			return p.width();
		}                           
		// height of current picture
		public     int height() {
			return p.height();
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
			calEnergy();
			return null;
		}              
		// sequence of indices for vertical seam
		public   int[] findVerticalSeam() {
			return null;
		}                
		// remove horizontal seam from current picture
		public    void removeHorizontalSeam(int[] seam) {}  
		// remove vertical seam from current picture
		public    void removeVerticalSeam(int[] seam) {}


		
		/* Private Section */
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
		double[][] energy;
}
