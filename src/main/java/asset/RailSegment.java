package asset;

import java.awt.Graphics2D;

public class RailSegment extends Geographic
{
	private static final long serialVersionUID = 1094005289128194631L;

	private static final double segmentWidth = 2.0;
	private static final double segmentLength = 4.5;

	private double angle;

	public RailSegment(double x, double y, double angle)
	{
		super(x, y);
		this.angle = angle;
	}

	public double getAngle()
	{
		return angle;
	}

	@Override
	public void draw(Graphics2D g)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean containsPoint(double x, double y)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
