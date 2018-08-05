//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.01.21 at 01:52:19 PM IST
//
package io.github.jeddict.jpa.spec;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import static io.github.jeddict.jcode.JPAConstants.TEMPORAL_FQN;
import io.github.jeddict.source.JavaSourceParserUtil;

/**
 * <p>
 * Java class for temporal-type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * <
 * pre>
 * &lt;simpleType name="temporal-type"> &lt;restriction
 * base="{http://www.w3.org/2001/XMLSchema}token"> &lt;enumeration
 * value="DATE"/> &lt;enumeration value="TIME"/> &lt;enumeration
 * value="TIMESTAMP"/> &lt;/restriction> &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "temporal-type")
@XmlEnum
public enum TemporalType {

    DATE,
    TIME,
    TIMESTAMP;

    public String value() {
        return name();
    }

    public static TemporalType fromValue(String v) {
        return valueOf(v);
    }

    public static TemporalType load(Element element, AnnotationMirror annotationMirror) {
        if (annotationMirror == null) {
            annotationMirror = JavaSourceParserUtil.findAnnotation(element, TEMPORAL_FQN);
        }
        TemporalType temporalType = null;
        if (annotationMirror != null) {
            Object value = JavaSourceParserUtil.findAnnotationValue(annotationMirror, "value");
            if (value != null) {
                temporalType = TemporalType.valueOf(value.toString());
            }
        }
        return temporalType;

    }

}