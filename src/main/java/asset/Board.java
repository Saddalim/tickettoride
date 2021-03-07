package asset;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import configuration.BoardParser;

public class Board implements Serializable
{
	private static final long serialVersionUID = -7231296098521147092L;

	String title;
	String fileName;

	Set<Geographic> geos = new HashSet<>();
	Set<City> cities = new HashSet<>();
	Set<Rail> rails = new HashSet<>();

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public Board(String title)
	{
		this.title = title;
		this.fileName = StringUtils.stripAccents(StringUtils.deleteWhitespace(title));
	}

	public Board(String title, String fileName)
	{
		this(title);
		this.fileName = fileName;
	}

	public Board(String title, String fileName, Set<City> cities, Set<Rail> segments)
	{
		this(title, fileName);

		this.cities = cities;
		this.rails = segments;

		geos.addAll(cities);
		geos.addAll(segments);
	}

	public Set<Geographic> getGeos()
	{
		return geos;
	}

	public Set<City> getCities()
	{
		return cities;
	}

	public Set<Rail> getRails()
	{
		return rails;
	}

	public void addCity(City city)
	{
		cities.add(city);
		geos.add(city);
	}

	public void addRail(Rail rail)
	{
		rails.add(rail);
		geos.add(rail);
	}

	public void removeGeo(Geographic geo)
	{
		geos.remove(geo);
		cities.remove(geo);
		rails.remove(geo);
	}

	public void saveToFile()
	{
		BoardParser.writeBoardToFile(this);
	}

	@Override
	public String toString()
	{
		return title + " (@" + fileName + ")";
	}

}
