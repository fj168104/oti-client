package com.mr.sac.oti.pack;

import com.mr.framework.core.util.XmlUtil;
import com.mr.sac.oti.DataType;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by feng on 18-5-7
 */
public class XmlParser implements Parser {

	@Override
	public Object packMessage(Message message) {
		Document document = XmlUtil.createXml(message.getId());
		Element rootElement = XmlUtil.getRootElement(document);
		transFormMessageToXML(document, rootElement, message);

		return XmlUtil.toStr(document);

	}

	/**
	 * @param node    message对应的节点
	 * @param message 需要解析的message
	 * @return
	 */
	private void transFormMessageToXML(Document document, Node node, Message message) {
		//
		for (Map.Entry<String, Field> entry : message.getFieldMap().entrySet()) {
			String fieldTag = entry.getKey();
			Field field = entry.getValue();
			String value = null;

			switch (DataType.getCodeByName(field.getDataType())) {
				case 1:
					if (Objects.isNull(field.getValue())) continue;
					value = String.valueOf(field.getValue());
					//TODO length 处理
					Node sTxtChild = document.createElement(fieldTag);
					sTxtChild.setTextContent(value);
					node.appendChild(sTxtChild);
					break;
				case 2:
					if (Objects.isNull(field.getValue())) continue;
					value = String.valueOf(field.getValue());
					//TODO length 处理
					Node iTxtChild = document.createElement(fieldTag);
					iTxtChild.setTextContent(value);
					node.appendChild(iTxtChild);
					break;
				case 3:
					if (Objects.isNull(field.getValue())) continue;
					value = String.valueOf(field.getValue());
					//TODO length 处理
					Node dTxtChild = document.createElement(fieldTag);
					dTxtChild.setTextContent(value);
					node.appendChild(dTxtChild);
					break;
				case 6:
					if (Objects.isNull(field.getValue())) continue;
					value = String.valueOf(field.getValue());
					//TODO length 处理
					Node bTxtChild = document.createElement(fieldTag);
					bTxtChild.setTextContent(value);
					node.appendChild(bTxtChild);
					break;

				case 4:
					Element oElement = document.createElement(fieldTag);
					transFormMessageToXML(document, oElement, field.getObjectMessage());
					node.appendChild(oElement);
					break;
				case 5:
					for (Message m : field.getArrayMessage()) {
						Element element = document.createElement(fieldTag);
						transFormMessageToXML(document, element, m);
						node.appendChild(element);
					}
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void unpackMessage(Object receivedMsg, Message message) {
		Document document = XmlUtil.parseXml((String) receivedMsg);
		Element elementDoc = XmlUtil.getRootElement(document);
		String encoding = document.getXmlEncoding();
		transFormXMLToMessage(elementDoc, message);

	}

	private void transFormXMLToMessage(Element element, Message message) {
		LinkedHashMap<String, Field> fieldMap = message.getFieldMap();
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			String fieldTag = entry.getKey();
			Field field = entry.getValue();
			String value = null;

			Element sElement = XmlUtil.getElement(element, fieldTag);
			if (Objects.isNull(sElement)) continue;
			switch (DataType.getCodeByName(field.getDataType())) {
				case 1:

					value = String.valueOf(sElement.getTextContent());
					field.setValue(value);
					break;
				case 2:
					value = String.valueOf(sElement.getTextContent());
					field.setValue(value);
					break;
				case 3:
					value = String.valueOf(sElement.getTextContent());
					field.setValue(value);
					break;
				case 6:
					value = String.valueOf(sElement.getTextContent());
					field.setValue(value);
					break;

				case 4:
					field.setObjectMessage(field.getMessageTemplete().clone());
					transFormXMLToMessage(sElement, field.getObjectMessage());
					break;
				case 5:
					List<Element> arrElement = XmlUtil.getElements(element, fieldTag);
					for (Element em : arrElement) {
						Message msg = field.getMessageTemplete().clone();
						transFormXMLToMessage(em, msg);
						field.getArrayMessage().add(msg);
					}
					break;
				default:
					break;
			}
		}
	}
}
