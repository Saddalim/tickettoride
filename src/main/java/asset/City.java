package asset;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class City extends Geographic
{
	private static final long serialVersionUID = 2238819380523294578L;

	private static final double cityRadius = 12.0;
	private static final int fontSize = 18;
	private static final Color cityColor = Color.DARK_GRAY;

	private String name;

	public City(double x, double y)
	{
		this(x, y, "");
	}

	public City(double x, double y, String name)
	{
		super(x, y);
		this.name = name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof City)) return false;
		return this.name.equals(((City) obj).name);
	}

	@Override
	public void draw(Graphics2D g)
	{
		Ellipse2D ellipse = new Ellipse2D.Double(getX() - cityRadius, getY() - cityRadius, cityRadius * 2.0, cityRadius * 2.0);

		g.setColor(cityColor);
		g.fill(ellipse);

		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		Rectangle2D numberRect = g.getFontMetrics().getStringBounds(name, g);
		g.drawString(name, (int) (getX() - (numberRect.getWidth() / 2.0)),
		        (int) (getY() - (numberRect.getHeight() / 2.0) + cityRadius * 3.0 + 3));
	}

	@Override
	public boolean containsPoint(double x, double y)
	{
		return Math.sqrt(Math.pow(getX() - x, 2) + Math.pow(getY() - y, 2)) <= cityRadius;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
