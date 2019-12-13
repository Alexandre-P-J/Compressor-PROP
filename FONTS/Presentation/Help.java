package Presentation;

public class Help {
    /**
     * User manual
     */
    private static String Help = 
        "<html>"+
        "<body>"+
        "<h1>I'm a Help panel!</h1>"+
        "<p>Hello Isaac.</p>"+
        "<p style='color:red;'>This is a paragraph in red, but it could be any other colour! even bold or in italics.</p>"+
        "</body>"+
        "</html>";

    /**
     * @return the help
     */
    public static String getHelp() {
        return Help;
    }
}