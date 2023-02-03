package org.terifan.nodeeditor.graphics;

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
	private double[] mT;
	private Spline mSplineX;
	private Spline mSplineY;
	private double mLength;


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

		mT = new double[aX.length];
		mT[0] = 0.0; // start point is always 0.0

		// Calculate the partial proportions of each section between each set of points and the total length of sum of all sections
		for (int i = 1; i < mT.length; i++)
		{
			double lx = aX[i] - aX[i - 1];
			double ly = aY[i] - aY[i - 1];

			// If either diff is zero there is no point performing the square root
			if (0.0 == lx)
			{
				mT[i] = Math.abs(ly);
			}
			else if (0.0 == ly)
			{
				mT[i] = Math.abs(lx);
			}
			else
			{
				mT[i] = Math.sqrt(lx * lx + ly * ly);
			}

			mLength += mT[i];
			mT[i] += mT[i - 1];
		}

		for (int i = 1; i < (mT.length) - 1; i++)
		{
			mT[i] = mT[i] / mLength;
		}

		mT[(mT.length) - 1] = 1.0; // end point is always 1.0

		mSplineX = new Spline(mT, aX);
		mSplineY = new Spline(mT, aY);
	}


	/**
	 * @param aT 0 <= t <= 1
	 */
	public Point2D.Double getPoint(double aT)
	{
		return new Point.Double(mSplineX.getValue(aT), mSplineY.getValue(aT));
	}


	public boolean checkValues()
	{
		return mSplineX.checkValues() && mSplineY.checkValues();
	}


	public double getDx(double aT)
	{
		return mSplineX.getDx(aT);
	}


	public double getDy(double aT)
	{
		return mSplineY.getDx(aT);
	}



	static class Spline
	{
		private double[] mX;
		private double[] mY;
		private double[] mA;
		private double[] mB;
		private double[] mC;
		private double[] mD;

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
			this.mX = aX;
			this.mY = aY;
			if (aX.length > 1)
			{
				calculateCoefficients();
			}
		}


		public double getValue(double aX)
		{
			if (mX.length == 0)
			{
				return Double.NaN;
			}

			if (mX.length == 1)
			{
				if (mX[0] == aX)
				{
					return mY[0];
				}
				else
				{
					return Double.NaN;
				}
			}

			int index = Arrays.binarySearch(mX, aX);
			if (index > 0)
			{
				return mY[index];
			}

			index = -(index + 1) - 1;

			//TODO linear interpolation or extrapolation
			if (index < 0)
			{
				return mY[0];
			}

			return mA[index]
				+ mB[index] * (aX - mX[index])
				+ mC[index] * Math.pow(aX - mX[index], 2)
				+ mD[index] * Math.pow(aX - mX[index], 3);
		}


		/**
		 * Returns an interpolated value. To be used when a long sequence of values are required in order, but ensure checkValues() is called
		 * beforehand to ensure the boundary checks from getValue() are made
		 */
		public double getFastValue(double aX)
		{
			// Fast check to see if previous index is still valid
			if (!(storageIndex > -1 && storageIndex < mX.length - 1 && aX > mX[storageIndex] && aX < mX[storageIndex + 1]))
			{
				int index = Arrays.binarySearch(mX, aX);
				if (index > 0)
				{
					return mY[index];
				}
				index = -(index + 1) - 1;
				storageIndex = index;
			}

			//TODO linear interpolation or extrapolation
			if (storageIndex < 0)
			{
				return mY[0];
			}

			double value = aX - mX[storageIndex];

			return mA[storageIndex]
				+ mB[storageIndex] * value
				+ mC[storageIndex] * (value * value)
				+ mD[storageIndex] * (value * value * value);
		}


		public boolean checkValues()
		{
			return mX.length >= 2;
		}


		public double getDx(double aX)
		{
			if (mX.length == 0 || mX.length == 1)
			{
				return 0;
			}

			int index = Arrays.binarySearch(mX, aX);
			if (index < 0)
			{
				index = -(index + 1) - 1;
			}

			return mB[index]
				+ 2 * mC[index] * (aX - mX[index])
				+ 3 * mD[index] * Math.pow(aX - mX[index], 2);
		}


		private void calculateCoefficients()
		{
			int N = mY.length;
			mA = new double[N];
			mB = new double[N];
			mC = new double[N];
			mD = new double[N];

			if (N == 2)
			{
				mA[0] = mY[0];
				mB[0] = mY[1] - mY[0];
				return;
			}

			double[] h = new double[N - 1];
			for (int i = 0; i < N - 1; i++)
			{
				mA[i] = mY[i];
				h[i] = mX[i + 1] - mX[i];
				// h[i] is used for division later, avoid a NaN
				if (h[i] == 0.0)
				{
					h[i] = 0.01;
				}
			}
			mA[N - 1] = mY[N - 1];

			double[][] A = new double[N - 2][N - 2];
			double[] y = new double[N - 2];
			for (int i = 0; i < N - 2; i++)
			{
				y[i]
					= 3
					* ((mY[i + 2] - mY[i + 1]) / h[i
					+ 1]
					- (mY[i + 1] - mY[i]) / h[i]);

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
				mC[i + 1] = y[i];
				mB[i] = (mA[i + 1] - mA[i]) / h[i] - (2 * mC[i] + mC[i + 1]) / 3 * h[i];
				mD[i] = (mC[i + 1] - mC[i]) / (3 * h[i]);
			}
			mB[N - 2]
				= (mA[N - 1] - mA[N - 2]) / h[N
				- 2]
				- (2 * mC[N - 2] + mC[N - 1]) / 3 * h[N
				- 2];
			mD[N - 2] = (mC[N - 1] - mC[N - 2]) / (3 * h[N - 2]);
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