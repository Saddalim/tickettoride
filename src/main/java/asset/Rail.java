package asset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Rail extends Geographic
{
	private static final long serialVersionUID = -7288563523591399822L;

	private static final float lineWidth = 9.5F;
	private static final float lengthCircRad = 25.0F;
	private static final int fontSize = 16;

	public enum RailColor
	{
		Red, Green, Blue, Yellow, Black, White, Pink, Orange, Any
	}

	City n1, n2;
	RailColor railColor;
	int length = 1;
	boolean isTunnel;
	int jokersNeeded;

	List<RailSegment> segments = new ArrayList<>();

	public Rail(City n1, City n2)
	{
		this(n1, n2, RailColor.Any);
	}

	public Rail(City n1, City n2, RailColor color)
	{
		super(n1.getX(), n1.getY());

		this.n1 = n1;
		this.n2 = n2;
		this.railColor = color;
	}

	public RailColor getColor()
	{
		return railColor;
	}

	public void setColor(RailColor color)
	{
		this.railColor = color;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public boolean isTunnel()
	{
		return isTunnel;
	}

	public void setTunnel(boolean isTunnel)
	{
		this.isTunnel = isTunnel;
	}

	public int getJokersNeeded()
	{
		return jokersNeeded;
	}

	public void setJokersNeeded(int jokersNeeded)
	{
		this.jokersNeeded = jokersNeeded;
	}

	public List<RailSegment> getSegments()
	{
		return segments;
	}

	public City getN1()
	{
		return n1;
	}

	public City getN2()
	{
		return n2;
	}

	protected static Color getGraphicsColorFor(RailColor railColor)
	{
		switch (railColor)
		{
			case Any:
				return Color.GRAY;
			case Black:
				return Color.DARK_GRAY;
			case Blue:
				return Color.BLUE;
			case Green:
				return Color.GREEN;
			case Red:
				return Color.RED;
			case White:
				return Color.WHITE;
			case Yellow:
				return Color.YELLOW;
			default:
				return Color.BLACK;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Rail)) return false;
		Rail o = (Rail) obj;
		return this.railColor.equals(o.railColor)
		        && ((this.n1.equals(o.n1) && this.n2.equals(o.n2)) || (this.n1.equals(o.n2) && this.n2.equals(o.n1)));
	}

	@Override
	public void draw(Graphics2D g)
	{
		for (RailSegment segment : segments)
		{
			segment.draw(g);
		}

		// While segments are not done...

		// Tunnel
		if (isTunnel)
		{
			g.setStroke(new BasicStroke((float) (lineWidth * 1.8)));
			g.setColor(Color.BLACK);
			g.drawLine((int) n1.getX(), (int) n1.getY(), (int) n2.getX(), (int) n2.getY());

			// Draw tunnel humps
			Stroke dashed = new BasicStroke((float) (lineWidth * 2.2), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 11 },
			        0);
			g.setStroke(dashed);
			g.drawLine((int) n1.getX(), (int) n1.getY(), (int) n2.getX(), (int) n2.getY());
		}

		g.setStroke(new BasicStroke(lineWidth));
		g.setColor(getGraphicsColorFor(railColor));
		g.drawLine((int) n1.getX(), (int) n1.getY(), (int) n2.getX(), (int) n2.getY());

		// Write length in middle
		double middleX = n1.getX() + (n2.getX() - n1.getX()) / 2.0;
		double middleY = n1.getY() + (n2.getY() - n1.getY()) / 2.0;
		double innerCircHRad = lengthCircRad * 2.0;
		double innerCircVRad = lengthCircRad;
		double outerCircHRad = innerCircHRad * 1.2;
		double outerCircVRad = innerCircVRad * 1.2;
		Ellipse2D ellipse = new Ellipse2D.Double(middleX - outerCircHRad / 2.0, middleY - outerCircVRad / 2.0, outerCircHRad,
		        outerCircVRad);
		g.setColor(Color.BLACK);
		g.fill(ellipse);
		ellipse = new Ellipse2D.Double(middleX - innerCircHRad / 2.0, middleY - innerCircVRad / 2.0, innerCircHRad, innerCircVRad);
		g.setColor(Color.WHITE);
		g.fill(ellipse);

		g.setColor(Color.BLACK);
		String lengthStr = "" + length + " / " + jokersNeeded;
		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		Rectangle2D numberRect = g.getFontMetrics().getStringBounds(lengthStr, g);
		g.drawString(lengthStr, (int) (middleX - (numberRect.getWidth() / 2.0)),
		        (int) (middleY - (numberRect.getHeight() / 2.0) + innerCircVRad / 2.0));
	}

	@Override
	public boolean containsPoint(double x, double y)
	{
		for (RailSegment segment : segments)
		{
			if (segment.containsPoint(x, y)) return true;
		}

		// While segments are not done...
		double middleX = n1.getX() + (n2.getX() - n1.getX()) / 2.0;
		double middleY = n1.getY() + (n2.getY() - n1.getY()) / 2.0;
		return Math.sqrt(Math.pow(middleX - x, 2) + Math.pow(middleY - y, 2)) <= lengthCircRad;
	}

	@Override
	public String toString()
	{
		return n1.getName() + "-" + n2.getName() + "(" + railColor + ")";
	}
}
