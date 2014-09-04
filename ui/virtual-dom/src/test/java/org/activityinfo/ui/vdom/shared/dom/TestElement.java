package org.activityinfo.ui.vdom.shared.dom;

import java.util.HashMap;
import java.util.Map;

public class TestElement extends TestNode implements DomElement {

    private String tagName;
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, String> properties = new HashMap<>();
    private Map<String, String> style = new HashMap<>();

    public TestElement(String tagName) {
        this.tagName = tagName.toLowerCase();
    }

    @Override
    public void removeAttribute(String attrName) {
        this.attributes.remove(attrName);
    }

    @Override
    public void setPropertyString(String propName, String propValue) {
        this.properties.put(propName, propValue);
    }

    @Override
    public void clearStyleProperty(String name) {
        this.style.remove(name);
    }

    @Override
    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    @Override
    public void setStyleProperty(String key, String value) {
        this.style.put(key, value);
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public int getNodeType() {
        return ELEMENT_NODE;
    }

    @Override
    public String toString() {
        if(children.isEmpty()) {
            return "<" + tagName + "></"+ tagName + ">";
        } else {
            StringBuilder html = new StringBuilder();
            html.append("<" + tagName + ">");
            for(DomNode node : children) {
                html.append(node);
            }
            html.append("</" + tagName + ">");
            return html.toString();
        }
    }
}
