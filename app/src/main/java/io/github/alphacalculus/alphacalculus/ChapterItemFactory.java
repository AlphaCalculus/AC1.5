package io.github.alphacalculus.alphacalculus;


import android.content.Context;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

class ChapterItemFactory {
    private static ChapterItemFactory __factory = new ChapterItemFactory();

    public static ChapterItemFactory getInstance() {
        return __factory;
    }

    private ChapterItemFactory() {
        Context context = TheApp.getInstance().getApplicationContext();
        readXML(context);
    }

    private NodeList parts;

    public void readXML(Context context) {
        InputSource __inputSrc = new InputSource(context.getResources().openRawResource(R.raw.itemdata));
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            parts = ((NodeList) xpath.evaluate("//Data/Part", __inputSrc, XPathConstants.NODESET));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public int getChapterCount(int part_idx) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            return ((Double) xpath.evaluate("count(./Chapter)", parts.item(part_idx), XPathConstants.NUMBER)).intValue();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ChapterItem getChapter(int part_idx, int chapter_idx) {
        ChapterItem ch = null;
        Context context = TheApp.getInstance().getApplicationContext();
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            Node node = (Node) xpath.evaluate("(./Chapter)[" + chapter_idx + "]", parts.item(part_idx), XPathConstants.NODE);
            String chidx = "ch" + (String) xpath.evaluate("./@index", node);
            String content = xpath.evaluate("./text()", node);
            String[] contentLines = content.split("[\r\n]");
            content = "";
            for (String s : contentLines) {
                content += s.trim() + "\n";
            }
            ch = new ChapterItem((String) xpath.evaluate("./@title", node, XPathConstants.STRING),
                    context.getResources().getIdentifier(chidx,
                            "drawable", context.getPackageName()), content);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return ch;
    }
}
