/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ac.big.circos.util;

import java.io.StringReader;
import java.io.StringWriter;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * 
 * @author sweeter
 */
public class XmlHander {

	public static Object xmlString2Object(String xmlFile, String className) {

		File file = new File(xmlFile);
		Object obj = null;
		if (file.exists() == false) {
			return obj;
		} else {

			BeanReader beanReader = new BeanReader();
			beanReader.getXMLIntrospector().getConfiguration()
					.setAttributesForPrimitives(false);

			beanReader.getBindingConfiguration().setMapIDs(false);


			Class clazz = null;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				beanReader.registerBeanClass(clazz);

			} catch (Exception e) {
				e.printStackTrace();
			}


			try {

				InputStream in = new FileInputStream(new File(xmlFile));
				InputStreamReader isr  =   new  InputStreamReader(in,  "UTF-8"); 

				
				obj = beanReader.parse(isr);
				in.close();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		return obj;
	}

	/**
	 * object to string
	 */
	public static String Object2XmlString(Object object) {
		String xmlString = null;

		StringWriter outputWriter = new StringWriter();

		try {
			BeanWriter beanWriter = new BeanWriter(outputWriter);

			beanWriter
					.writeXmlDeclaration("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

			beanWriter.getXMLIntrospector().getConfiguration()
					.setAttributesForPrimitives(false);
			beanWriter.getBindingConfiguration().setMapIDs(false); 
			beanWriter.setWriteEmptyElements(true); 
			beanWriter.enablePrettyPrint(); // 

			beanWriter.write(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		xmlString = outputWriter.toString();

		try {
			outputWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlString;
	}

	/************************************************************
	 * write xml bean
	 * 
	 * @param path
	 * @param object
	 * @return
	 */
	public static int writeObject2Xml(String path, Object object)
			throws Exception {

		 String xmlstr = Object2XmlString(object);
		 FileOutputStream fos  =   new  FileOutputStream(path); 

		 OutputStreamWriter osw  =   new  OutputStreamWriter(fos,  "UTF-8" ); 
		 osw.write(xmlstr);
		 
		 osw.close();
		 
		return 0;
	}

	public static void main(String[] args) {
		
		
	}
}
