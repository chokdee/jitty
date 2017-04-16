
package com.jmelzer.jitty.model.xml.playerimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}players"/>
 *         &lt;element ref="{}matches" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="age-group" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="Mixed"/>
 *             &lt;enumeration value="Doppel"/>
 *             &lt;enumeration value="Einzel"/>
 *             &lt;enumeration value="Mannschaft"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="start-date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="ttr-from" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="ttr-to" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="ttr-remarks" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="entry-fee" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="age-from" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="age-to" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="sex" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="preliminary-round-playmode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="final-round-playmode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="max-persons" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "players",
    "matches"
})
@XmlRootElement(name = "competition")
public class Competition {

    @XmlElement(required = true)
    protected Players players;
    protected Matches matches;
    @XmlAttribute(name = "age-group", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String ageGroup;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "start-date", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String startDate;
    @XmlAttribute(name = "ttr-from")
    @XmlSchemaType(name = "anySimpleType")
    protected String ttrFrom;
    @XmlAttribute(name = "ttr-to")
    @XmlSchemaType(name = "anySimpleType")
    protected String ttrTo;
    @XmlAttribute(name = "ttr-remarks")
    @XmlSchemaType(name = "anySimpleType")
    protected String ttrRemarks;
    @XmlAttribute(name = "entry-fee")
    @XmlSchemaType(name = "anySimpleType")
    protected String entryFee;
    @XmlAttribute(name = "age-from")
    @XmlSchemaType(name = "anySimpleType")
    protected String ageFrom;
    @XmlAttribute(name = "age-to")
    @XmlSchemaType(name = "anySimpleType")
    protected String ageTo;
    @XmlAttribute(name = "sex")
    @XmlSchemaType(name = "anySimpleType")
    protected String sex;
    @XmlAttribute(name = "preliminary-round-playmode")
    @XmlSchemaType(name = "anySimpleType")
    protected String preliminaryRoundPlaymode;
    @XmlAttribute(name = "final-round-playmode")
    @XmlSchemaType(name = "anySimpleType")
    protected String finalRoundPlaymode;
    @XmlAttribute(name = "max-persons")
    @XmlSchemaType(name = "anySimpleType")
    protected String maxPersons;

    /**
     * Ruft den Wert der players-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Players }
     *     
     */
    public Players getPlayers() {
        return players;
    }

    /**
     * Legt den Wert der players-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Players }
     *     
     */
    public void setPlayers(Players value) {
        this.players = value;
    }

    /**
     * Ruft den Wert der matches-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Matches }
     *     
     */
    public Matches getMatches() {
        return matches;
    }

    /**
     * Legt den Wert der matches-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Matches }
     *     
     */
    public void setMatches(Matches value) {
        this.matches = value;
    }

    /**
     * Ruft den Wert der ageGroup-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgeGroup() {
        return ageGroup;
    }

    /**
     * Legt den Wert der ageGroup-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgeGroup(String value) {
        this.ageGroup = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Ruft den Wert der startDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Legt den Wert der startDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Ruft den Wert der ttrFrom-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTtrFrom() {
        return ttrFrom;
    }

    /**
     * Legt den Wert der ttrFrom-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTtrFrom(String value) {
        this.ttrFrom = value;
    }

    /**
     * Ruft den Wert der ttrTo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTtrTo() {
        return ttrTo;
    }

    /**
     * Legt den Wert der ttrTo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTtrTo(String value) {
        this.ttrTo = value;
    }

    /**
     * Ruft den Wert der ttrRemarks-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTtrRemarks() {
        return ttrRemarks;
    }

    /**
     * Legt den Wert der ttrRemarks-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTtrRemarks(String value) {
        this.ttrRemarks = value;
    }

    /**
     * Ruft den Wert der entryFee-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryFee() {
        return entryFee;
    }

    /**
     * Legt den Wert der entryFee-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryFee(String value) {
        this.entryFee = value;
    }

    /**
     * Ruft den Wert der ageFrom-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgeFrom() {
        return ageFrom;
    }

    /**
     * Legt den Wert der ageFrom-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgeFrom(String value) {
        this.ageFrom = value;
    }

    /**
     * Ruft den Wert der ageTo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgeTo() {
        return ageTo;
    }

    /**
     * Legt den Wert der ageTo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgeTo(String value) {
        this.ageTo = value;
    }

    /**
     * Ruft den Wert der sex-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSex() {
        return sex;
    }

    /**
     * Legt den Wert der sex-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSex(String value) {
        this.sex = value;
    }

    /**
     * Ruft den Wert der preliminaryRoundPlaymode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreliminaryRoundPlaymode() {
        return preliminaryRoundPlaymode;
    }

    /**
     * Legt den Wert der preliminaryRoundPlaymode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreliminaryRoundPlaymode(String value) {
        this.preliminaryRoundPlaymode = value;
    }

    /**
     * Ruft den Wert der finalRoundPlaymode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalRoundPlaymode() {
        return finalRoundPlaymode;
    }

    /**
     * Legt den Wert der finalRoundPlaymode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalRoundPlaymode(String value) {
        this.finalRoundPlaymode = value;
    }

    /**
     * Ruft den Wert der maxPersons-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxPersons() {
        return maxPersons;
    }

    /**
     * Legt den Wert der maxPersons-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxPersons(String value) {
        this.maxPersons = value;
    }

}
