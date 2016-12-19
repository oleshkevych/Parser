package com.parser.parsers;

import com.parser.entity.ListImpl;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rolique_pc on 12/19/2016.
 */
public class PrintDescription {

    public List<ListImpl> generateListImpl(Elements tablesDescription){

        List<ListImpl> list = new ArrayList<ListImpl>();

        for (int i = 0; i < tablesDescription.size(); i++) {

            if(!tablesDescription.get(i).hasClass("top_section")) {
                if (tablesDescription.get(i).tagName().equals("div")) {
                    for (Element e : tablesDescription.get(i).children()) {
                        if (e.tagName().equals("p")) {
                            list.add(addParagraph(e));
                        } else if (e.tagName().equals("ul")) {
                            list.add(addList(e));
                        } else if (e.tagName().contains("h")) {
                            list.add(addHead(e));
                        } else if (e.tagName().contains("di")) {
                            for (Element e1 : e.children()) {
                                if (e1.tagName().equals("p")) {
                                    list.add(addParagraph(e1));
                                } else if (e1.tagName().equals("ul")) {
                                    list.add(addList(e1));
                                } else if (e1.tagName().contains("h")) {
                                    list.add(addHead(e1));
                                } else if (e1.tagName().equals("div")) {
                                    ListImpl list1 = new ListImpl();
                                    list1.setTextFieldImpl(e1.text());
                                    list.add(list1);
                                }
                            }
                            ListImpl list1 = new ListImpl();
                            list1.setTextFieldImpl(e.ownText());
                            list.add(list1);
                        }
                    }
                }
                if (tablesDescription.get(i).tagName().equals("p")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("span")) {
                    list.add(addParagraph(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("ul")) {
                    list.add(addList(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().contains("h")) {
                    list.add(addHead(tablesDescription.get(i)));
                } else if (tablesDescription.get(i).tagName().equals("div")) {
                    ListImpl list1 = new ListImpl();
                    list1.setTextFieldImpl(tablesDescription.get(i).ownText());
                    list.add(list1);
                }
            }
        }
        return list;
    }

    private static ListImpl addHead(Element element) {
        ListImpl list = new ListImpl();
        list.setListHeader(element.ownText());
        return list;
    }

    private static ListImpl addParagraph(Element element) {
        if (element.select("strong").size() > 0) {
            return addHead(element.select("strong").first());
        } else {


            ListImpl list = new ListImpl();
            list.setTextFieldImpl(element.text());
            return list;
        }
    }

    private static ListImpl addList(Element element) {
        ListImpl list = new ListImpl();
        List<String> strings = new ArrayList<String>();
        for (Element e : element.getAllElements()) {
            strings.add(e.text());
        }
        list.setListItem(strings);
        return list;
    }
}
