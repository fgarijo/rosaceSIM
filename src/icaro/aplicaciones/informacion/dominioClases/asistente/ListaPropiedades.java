//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.26 at 01:54:43 AM CET 
//


package icaro.aplicaciones.informacion.dominioClases.asistente;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListaPropiedades complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListaPropiedades">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="propiedad" type="{urn:icaro:aplicaciones:descripcionOrganizaciones}Propiedad" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaPropiedades", propOrder = {
    "propiedad"
})
public class ListaPropiedades {

    @XmlElement(required = true)
    protected List<Propiedad> propiedad;

    /**
     * Gets the value of the propiedad property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propiedad property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropiedad().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Propiedad }
     * 
     * 
     */
    public List<Propiedad> getPropiedad() {
        if (propiedad == null) {
            propiedad = new ArrayList<Propiedad>();
        }
        return this.propiedad;
    }

    public void setPropiedad(List<Propiedad> propiedad) {
        this.propiedad = propiedad;
    }


}
