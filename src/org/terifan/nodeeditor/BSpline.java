package org.terifan.nodeeditor;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;


/**
 * Interpolates points given in the 2D plane. The resulting spline is a function s: R -> R^2 with parameter t in [0,1].
 */
public class BSpline
{
	/**
	 * Array representing the relative proportion of the total distance of each point in the line ( i.e. first point is 0.0, end point is
	 * 1.0, a point halfway on line is 0.5 ).
	 */
	private double[] t;
	private Spline splineX;
	private Spline splineY;
	private double length;


	public BSpline(Point2D[] aPoints)
	{
		double[] x = new double[aPoints.length];
		double[] y = new double[aPoints.length];

		for (int i = 0; i < aPoints.length; i++)
		{
			x[i] = aPoints[i].getX();
			y[i] = aPoints[i].getY();
		}

		init(x, y);
	}


	public BSpline(double[] aX, double[] aY)
	{
		init(aX, aY);
	}


	private void init(double[] aX, double[] aY)
	{
		if (aX.length != aY.length)
		{
			throw new IllegalArgumentException("Arrays must have the same length.");
		}

		if (aX.length < 2)
		{
			throw new IllegalArgumentException("Spline edges must have at least two points.");
		}

		t = new double[aX.length];
		t[0] = 0.0; // start point is always 0.0

		// Calculate the partial proportions of each section between each set of points and the total length of sum of all sections
		for (int i = 1; i < t.length; i++)
		{
			double lx = aX[i] - aX[i - 1];
			double ly = aY[i] - aY[i - 1];

			// If either diff is zero there is no point performing the square root
			if (0.0 == lx)
			{
				t[i] = Math.abs(ly);
			}
			else if (0.0 == ly)
			{
				t[i] = Math.abs(lx);
			}
			else
			{
				t[i] = Math.sqrt(lx * lx + ly * ly);
			}

			length += t[i];
			t[i] += t[i - 1];
		}

		for (int i = 1; i < (t.length) - 1; i++)
		{
			t[i] = t[i] / length;
		}

		t[(t.length) - 1] = 1.0; // end point is always 1.0

		splineX = new Spline(t, aX);
		splineY = new Spline(t, aY);
	}


	/**
	 * @param aT 0 <= t <= 1
	 */
	public Point2D.Double getPoint(double aT)
	{
		return new Point.Double(splineX.getValue(aT), splineY.getValue(aT));
	}


	public boolean checkValues()
	{
		return splineX.checkValues() && splineY.checkValues();
	}


	public double getDx(double aT)
	{
		return splineX.getDx(aT);
	}


	public double getDy(double aT)
	{
		return splineY.getDx(aT);
	}



	static class Spline
	{
		private double[] x;
		private double[] y;
		private double[] a;
		private double[] b;
		private double[] c;
		private double[] d;

		/**
		 * tracks the last index found since that is mostly commonly the next one used
		 */
		private int storageIndex = 0;


		public Spline(double[] aX, double[] aY)
		{
			setValues(aX, aY);
		}


		public void setValues(double[] aX, double[] aY)
		{
			this.x = aX;
			this.y = aY;
			if (aX.length > 1)
			{
				calculateCoefficients();
			}
		}


		public double getValue(double aX)
		{
			if (x.length == 0)
			{
				return Double.NaN;
			}

			if (x.length == 1)
			{
				if (x[0] == aX)
				{
					return y[0];
				}
				else
				{
					return Double.NaN;
				}
			}

			int index = Arrays.binarySearch(x, aX);
			if (index > 0)
			{
				return y[index];
			}

			index = -(index + 1) - 1;

			//TODO linear interpolation or extrapolation
			if (index < 0)
			{
				return y[0];
			}

			return a[index]
				+ b[index] * (aX - x[index])
				+ c[index] * Math.pow(aX - x[index], 2)
				+ d[index] * Math.pow(aX - x[index], 3);
		}


		/**
		 * Returns an interpolated value. To be used when a long sequence of values are required in order, but ensure checkValues() is called
		 * beforehand to ensure the boundary checks from getValue() are made
		 */
		public double getFastValue(double aX)
		{
			// Fast check to see if previous index is still valid
			if (!(storageIndex > -1 && storageIndex < x.length - 1 && aX > x[storageIndex] && aX < x[storageIndex + 1]))
			{
				int index = Arrays.binarySearch(x, aX);
				if (index > 0)
				{
					return y[index];
				}
				index = -(index + 1) - 1;
				storageIndex = index;
			}

			//TODO linear interpolation or extrapolation
			if (storageIndex < 0)
			{
				return y[0];
			}

			double value = aX - x[storageIndex];

			return a[storageIndex]
				+ b[storageIndex] * value
				+ c[storageIndex] * (value * value)
				+ d[storageIndex] * (value * value * value);
		}


		public boolean checkValues()
		{
			return x.length >= 2;
		}


		public double getDx(double aX)
		{
			if (x.length == 0 || x.length == 1)
			{
				return 0;
			}

			int index = Arrays.binarySearch(x, aX);
			if (index < 0)
			{
				index = -(index + 1) - 1;
			}

			return b[index]
				+ 2 * c[index] * (aX - x[index])
				+ 3 * d[index] * Math.pow(aX - x[index], 2);
		}


		private void calculateCoefficients()
		{
			int N = y.length;
			a = new double[N];
			b = new double[N];
			c = new double[N];
			d = new double[N];

			if (N == 2)
			{
				a[0] = y[0];
				b[0] = y[1] - y[0];
				return;
			}

			double[] h = new double[N - 1];
			for (int i = 0; i < N - 1; i++)
			{
				a[i] = y[i];
				h[i] = x[i + 1] - x[i];
				// h[i] is used for division later, avoid a NaN
				if (h[i] == 0.0)
				{
					h[i] = 0.01;
				}
			}
			a[N - 1] = y[N - 1];

			double[][] A = new double[N - 2][N - 2];
			double[] y = new double[N - 2];
			for (int i = 0; i < N - 2; i++)
			{
				y[i]
					= 3
					* ((y[i + 2] - y[i + 1]) / h[i
					+ 1]
					- (y[i + 1] - y[i]) / h[i]);

				A[i][i] = 2 * (h[i] + h[i + 1]);

				if (i > 0)
				{
					A[i][i - 1] = h[i];
				}

				if (i < N - 3)
				{
					A[i][i + 1] = h[i + 1];
				}
			}
			solve(A, y);

			for (int i = 0; i < N - 2; i++)
			{
				c[i + 1] = y[i];
				b[i] = (a[i + 1] - a[i]) / h[i] - (2 * c[i] + c[i + 1]) / 3 * h[i];
				d[i] = (c[i + 1] - c[i]) / (3 * h[i]);
			}
			b[N - 2]
				= (a[N - 1] - a[N - 2]) / h[N
				- 2]
				- (2 * c[N - 2] + c[N - 1]) / 3 * h[N
				- 2];
			d[N - 2] = (c[N - 1] - c[N - 2]) / (3 * h[N - 2]);
		}


		public void solve(double[][] aA, double[] aB)
		{
			int n = aB.length;
			for (int i = 1; i < n; i++)
			{
				aA[i][i - 1] = aA[i][i - 1] / aA[i - 1][i - 1];
				aA[i][i] = aA[i][i] - aA[i - 1][i] * aA[i][i - 1];
				aB[i] = aB[i] - aA[i][i - 1] * aB[i - 1];
			}

			aB[n - 1] = aB[n - 1] / aA[n - 1][n - 1];
			for (int i = aB.length - 2; i >= 0; i--)
			{
				aB[i] = (aB[i] - aA[i][i + 1] * aB[i + 1]) / aA[i][i];
			}
		}
	}
}