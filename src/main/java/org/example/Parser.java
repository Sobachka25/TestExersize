package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Parser {
    public static String[] getInfo(Document doc){
        String info[] = new String[3];
        Element element = doc.getDocumentElement();
        //System.out.println(element.getTagName());
        info[0] = element.getAttribute("uuid");
        info[1] = element.getAttribute("date");
        info[2] = element.getAttribute("company");
        return info;
    }

    public static ArrayList<Zapis> getSpisok(Document doc) {
        ArrayList<Zapis> zapisiPervoyTablici = new ArrayList<>();
        ArrayList<String> dlyaZapisey = new ArrayList<>();
        Element element = doc.getDocumentElement();
        Node node;
        NodeList nodeList2;
        //System.out.println(element.getChildNodes().getLength());
        NodeList nodeList = element.getChildNodes();
        for(int i=0; i< nodeList.getLength();i++){
            if(nodeList.item(i) instanceof Element){
                node = nodeList.item(i);
                nodeList2 = node.getChildNodes();
                for(int j=0; j< nodeList2.getLength();j++){
                    if(nodeList2.item(j) instanceof Element){
                        dlyaZapisey.add(nodeList2.item(j).getTextContent());
                        //System.out.println(nodeList2.item(j).getTextContent());
                    }
                }
                dlyaZapisey.add(element.getAttribute("uuid"));
                zapisiPervoyTablici.add(new Zapis(dlyaZapisey));
                dlyaZapisey.clear();
            }
        }
        return zapisiPervoyTablici;
    }

    private int getLength(Document doc){
        int schet=0;
        Element element = doc.getDocumentElement();
        NodeList nodeList = element.getChildNodes();
        for(int i=0; i< nodeList.getLength();i++){
            if(nodeList.item(i) instanceof Element){

                schet++;
            }
        }
        return schet;
    }

}
