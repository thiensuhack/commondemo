package com.orange.studio.bobo.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.orange.studio.bobo.objects.ProductDTO;

public class ProductXmlParser {

	public void startParserXml(InputStream in_s) {
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in_s, null);

			parseXML(parser);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseXML(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		ArrayList<ProductDTO> products = null;
		int eventType = parser.getEventType();
		ProductDTO currentProduct = null;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = null;
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				products = new ArrayList<ProductDTO>();
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if (name == "product") {
					currentProduct = new ProductDTO();
				} else if (currentProduct != null) {
					if (name == "name") {
						currentProduct.name = parser.nextText();	
					} else if (name == "price") {
						currentProduct.price = parser.nextText();
					} else if (name == "quantity") {
						currentProduct.quantity = parser.nextText();
					}
				}
				break;
			case XmlPullParser.END_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase("product") && currentProduct != null) {
					products.add(currentProduct);
				}
			}
			eventType = parser.next();
		}

		printProducts(products);
	}

	private void printProducts(ArrayList<ProductDTO> products) {
		String content = "";
		Iterator<ProductDTO> it = products.iterator();

		while (it.hasNext()) {
			ProductDTO currProduct = it.next();
			content = content + "nnnProduct :" + currProduct.name + "n";
			content = content + "Quantity :" + currProduct.quantity + "n";
		}
	}
}