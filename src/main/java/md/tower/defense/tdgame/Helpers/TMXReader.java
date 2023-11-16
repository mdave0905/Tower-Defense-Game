package md.tower.defense.tdgame.Helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TMXReader {

    public static List<Point> pathPositions;
    public static int startTile;
    public static int endTile;

    public static List<Point> read(String mapFile) {
        InputStream inputStream = TMXReader.class.getResourceAsStream(mapFile);
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList layerList = doc.getElementsByTagName("layer");

            for (int i = 0; i < layerList.getLength(); i++) {
                Node layerNode = layerList.item(i);
                if (layerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element layerElement = (Element) layerNode;
                    String layerName = layerElement.getAttribute("name");

                    if (layerName.equals("Path")) {
                        NodeList propertiesList = layerElement.getElementsByTagName("properties");

                        if (propertiesList.getLength() > 0) {
                            Element propertiesElement = (Element) propertiesList.item(0);
                            Element endTileElement = (Element) propertiesElement.getElementsByTagName("property").item(0);
                            String endTilePropertyName = endTileElement.getAttribute("name");
                            if ("endTile".equals(endTilePropertyName)) {
                                endTile = Integer.parseInt(endTileElement.getAttribute("value"));
                            }

                            Element startTileElement = (Element) propertiesElement.getElementsByTagName("property").item(1);
                            String startTilePropertyName = startTileElement.getAttribute("name");
                            if ("startTile".equals(startTilePropertyName)) {
                                startTile = Integer.parseInt(startTileElement.getAttribute("value"));
                            }
                        }

                        NodeList dataList = layerElement.getElementsByTagName("data");
                        if (dataList.getLength() > 0) {
                            Element dataElement = (Element) dataList.item(0);
                            String encoding = dataElement.getAttribute("encoding");
                            if ("csv".equals(encoding)) {
                                String csvData = dataElement.getTextContent().trim();
                                pathPositions = extractPathPositions(csvData);
                                return pathPositions;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Point> extractPathPositions(String csvData) {
        List<Point> pathPositions = new ArrayList<>();
        String[] rows = csvData.split("\n");

        for (int y = 0; y < rows.length; y++) {
            String[] columns = rows[y].split(",");
            for (int x = 0; x < columns.length; x++) {
                int value = Integer.parseInt(columns[x].trim());
                if (value != 0) {
                    Point p = new Point(x, y);
                    if (value == startTile) {
                        p.setStartTile(true);
                    } else if (value == endTile) {
                        p.setEndTile(true);
                    }
                    pathPositions.add(p);

                }
            }
        }
        return pathPositions;
    }
}
