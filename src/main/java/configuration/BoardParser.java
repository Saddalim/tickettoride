package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import asset.Board;
import asset.City;
import asset.Rail;

public class BoardParser
{
	private static final String CITY_NODE_TAG = "city";
	private static final String SEGMENT_NODE_TAG = "segment";
	private static final String BOARD_FILE_EXT = "fos";

	@Deprecated
	public static Board parse(String mapName)
	{
		Set<City> cities = null;
		Set<Rail> segments = null;

		try
		{
			// TODO this is now a copypaste of readFromFile. Separate binary and XML read/writers
			// Check if there is an already serialized Board

			FileInputStream fin = new FileInputStream(mapName + ".fos");
			ObjectInputStream ois = new ObjectInputStream(fin);

			try
			{

				Object readout = ois.readObject();

				if (readout instanceof Board)
				{
					return (Board) readout;
				}
			}
			catch (FileNotFoundException ex)
			{
				System.out.println("No serialized board file, parsing XML...");
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				ois.close();
				fin.close();
			}

			// Parse XML

			cities = new HashSet<>();
			segments = new HashSet<>();

			// ClassLoader classLoader = BoardParser.class.getClassLoader();
			// File fXmlFile = new File(classLoader.getResource(mapName + ".xml").getFile());
			File fXmlFile = new File(mapName + ".xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList cityList = doc.getElementsByTagName(CITY_NODE_TAG);

			for (int i = 0; i < cityList.getLength(); i++)
			{

				Node nNode = cityList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					try
					{
						Element eElement = (Element) nNode;
						double x = Double.parseDouble(eElement.getAttribute("x"));
						double y = Double.parseDouble(eElement.getAttribute("y"));
						String name = eElement.getAttribute("name");
						cities.add(new City(x, y, name));
					}
					catch (NumberFormatException ex)
					{
						ex.printStackTrace();
					}
				}
				else
				{
					System.out.println("Node is not an element at item " + i);
				}
			}

			NodeList segmentList = doc.getElementsByTagName(SEGMENT_NODE_TAG);

			for (int i = 0; i < segmentList.getLength(); i++)
			{

				Node nNode = segmentList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					// TODO parse segments
				}
				else
				{
					System.out.println("Node is not an element at item " + i);
				}
			}

			String title = "";
			String fileName = "";

			Node nameNode = doc.getElementsByTagName("Name").item(0);
			if (nameNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nameNode;
				title = eElement.getAttribute("Title");
				fileName = eElement.getAttribute("FileName");
			}

			System.out.println("Found " + cities.size() + " cities and " + segments.size() + " routes inbetween");

			Board parsedBoard = new Board(title, fileName, cities, segments);
			writeBoardToFile(parsedBoard);
			return parsedBoard;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("Unable to parse board file");
		return null;
	}

	public static void writeBoardToFile(Board board)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(board.getFileName() + "." + BOARD_FILE_EXT);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(board);
			oos.flush();
			fout.flush();
			oos.close();
			fout.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static Board loadBoardFromFile(String mapName)
	{
		try
		{
			FileInputStream fin = new FileInputStream(mapName + "." + BOARD_FILE_EXT);

			try
			{
				ObjectInputStream ois = new ObjectInputStream(fin);

				try
				{
					Object readout = ois.readObject();

					if (readout instanceof Board)
					{
						return (Board) readout;
					}
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						ois.close();
					}
					catch (IOException ex)
					{
						System.out.println("Unable to f-Stop OIS");
						ex.printStackTrace();
					}
				}

			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				try
				{
					fin.close();

				}
				catch (IOException ex)
				{
					System.out.println("Unable to f-Stop FIN");
					ex.printStackTrace();
				}
			}
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("No serialized board file, parsing XML...");
		}

		return null;

	}

	public static String[] getSavedMaps()
	{
		List<String> savedMaps = new LinkedList<>();
		File dir = new File(".");
		File[] files = dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith("." + BOARD_FILE_EXT);
			}
		});

		for (File saveFile : files)
		{
			String fileName = saveFile.getName();
			// Remove extension
			savedMaps.add(fileName.substring(0, fileName.length() - BOARD_FILE_EXT.length() - 1));
		}

		return (String[]) savedMaps.toArray(new String[savedMaps.size()]);
	}
}
