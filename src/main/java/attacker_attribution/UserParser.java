package attacker_attribution;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import parser.XMLParser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserParser {
    public static Set<User> parse(File file) {
        Set<User> users = new HashSet<>();
        try {
            // get file URL
            URL url = file.toURI().toURL();
            // get dom4j Document object
            Document document = XMLParser.parse(url);
            // root xml element
            Element root = document.getRootElement();

            // type of root element needs to be equal to emfta:FTAModel; otherwise xml is no emfta model
            if (!root.getQualifiedName().equals("users"))
                throw new DocumentException("XML is not valid Users XML.");

            List<Element> usersXML = root.elements("user");
            for (Element userXML: usersXML) {
                User user = parseUser(userXML);
                if (user != null)
                    users.add(user);
            }

        } catch (MalformedURLException | DocumentException e) {
            e.printStackTrace();
        }

        return users;
    }

    private static User parseUser(Element element) throws DocumentException {
        String ID = element.attributeValue("ID", null);
        String name = element.attributeValue("name", null);
        String role = element.attributeValue("role", null);
        String scoreStr = element.attributeValue("score", "-1");

        int score = -1;
        try {
            score = Integer.parseInt(scoreStr);
        } catch (NumberFormatException e) {
            System.err.println("Score of user " + name + " is not an integer. Setting it to -1.");
        }

        User user = new User(ID, name, role, score, null);

        // TODO attacks
        if (ID == null || name == null || role == null)
            return null;
        else
            return user;
    }
}
