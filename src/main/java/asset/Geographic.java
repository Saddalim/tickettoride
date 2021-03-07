package asset;

import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class Geographic implements Serializable
{
	private static final long serialVersionUID = 2281515690673681676L;

	private double x, y;

	protected Geographic(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getX()
	{
		return x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public double getY()
	{
		return y;
	}

	public abstract void draw(Graphics2D g);

	public abstract boolean containsPoint(double x, double y);

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}

}
