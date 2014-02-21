//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.26 at 01:54:43 AM CET 
//


package icaro.aplicaciones.informacion.dominioClases.asistente;

import icaro.infraestructura.entidadesBasicas.descEntidadesOrganizacion.jaxb.Nodo;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Gestores complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Gestores">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InstanciaGestor" type="{urn:icaro:aplicaciones:descripcionOrganizaciones}InstanciaGestor" maxOccurs="unbounded"/>
 *         &lt;element name="nodoComun" type="{urn:icaro:aplicaciones:descripcionOrganizaciones}Nodo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Gestores", propOrder = {
    "instanciaGestor",
    "nodoComun"
})
public class Gestores {

    @XmlElement(name = "InstanciaGestor", required = true)
    protected List<InstanciaGestor> instanciaGestor;
    protected Nodo nodoComun;

    /**
     * Gets the value of the instanciaGestor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instanciaGestor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstanciaGestor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanciaGestor }
     * 
     * 
     */
    public List<InstanciaGestor> getInstanciaGestor() {
        if (instanciaGestor == null) {
            instanciaGestor = new ArrayList<InstanciaGestor>();
        }
        return this.instanciaGestor;
    }

    public void setInstanciaGestor(List<InstanciaGestor> instanciaGestor) {
        this.instanciaGestor = instanciaGestor;
    }

    /**
     * Gets the value of the nodoComun property.
     * 
     * @return
     *     possible object is
     *     {@link Nodo }
     *     
     */
    public Nodo getNodoComun() {
        return nodoComun;
    }

    /**
     * Sets the value of the nodoComun property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nodo }
     *     
     */
    public void setNodoComun(Nodo value) {
        this.nodoComun = value;
    }

}
