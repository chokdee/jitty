
package com.jmelzer.jitty.model.xml.playerimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse f�r anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="nr" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="group" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="scheduled" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="player-a" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="player-b" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="state">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="retired-a"/>
 *             &lt;enumeration value="retired-b"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="set-a-1" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-a-2" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-a-3" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-a-4" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-a-5" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-a-6" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-a-7" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="sets-a" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="0"/>
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *             &lt;enumeration value="4"/>
 *             &lt;enumeration value="5"/>
 *             &lt;enumeration value="6"/>
 *             &lt;enumeration value="7"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="matches-a" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="0"/>
 *             &lt;enumeration value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="games-a" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-b-1" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-b-2" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-b-3" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-b-4" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-b-5" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-b-6" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="set-b-7" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="sets-b" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="0"/>
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *             &lt;enumeration value="4"/>
 *             &lt;enumeration value="5"/>
 *             &lt;enumeration value="6"/>
 *             &lt;enumeration value="7"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="matches-b" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="0"/>
 *             &lt;enumeration value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="games-b" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "match")
public class Match {

    @XmlAttribute(name = "nr")
    @XmlSchemaType(name = "anySimpleType")
    protected String nr;
    @XmlAttribute(name = "group")
    @XmlSchemaType(name = "anySimpleType")
    protected String group;
    @XmlAttribute(name = "scheduled")
    @XmlSchemaType(name = "anySimpleType")
    protected String scheduled;
    @XmlAttribute(name = "player-a", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object playerA;
    @XmlAttribute(name = "player-b", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object playerB;
    @XmlAttribute(name = "state")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String state;
    @XmlAttribute(name = "set-a-1", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setA1;
    @XmlAttribute(name = "set-a-2", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setA2;
    @XmlAttribute(name = "set-a-3", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setA3;
    @XmlAttribute(name = "set-a-4", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setA4;
    @XmlAttribute(name = "set-a-5", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setA5;
    @XmlAttribute(name = "set-a-6", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setA6;
    @XmlAttribute(name = "set-a-7", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setA7;
    @XmlAttribute(name = "sets-a", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String setsA;
    @XmlAttribute(name = "matches-a", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String matchesA;
    @XmlAttribute(name = "games-a", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String gamesA;
    @XmlAttribute(name = "set-b-1", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setB1;
    @XmlAttribute(name = "set-b-2", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setB2;
    @XmlAttribute(name = "set-b-3", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setB3;
    @XmlAttribute(name = "set-b-4", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setB4;
    @XmlAttribute(name = "set-b-5", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setB5;
    @XmlAttribute(name = "set-b-6", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setB6;
    @XmlAttribute(name = "set-b-7", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String setB7;
    @XmlAttribute(name = "sets-b", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String setsB;
    @XmlAttribute(name = "matches-b", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String matchesB;
    @XmlAttribute(name = "games-b", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String gamesB;

    /**
     * Ruft den Wert der nr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNr() {
        return nr;
    }

    /**
     * Legt den Wert der nr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNr(String value) {
        this.nr = value;
    }

    /**
     * Ruft den Wert der group-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Legt den Wert der group-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
    }

    /**
     * Ruft den Wert der scheduled-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduled() {
        return scheduled;
    }

    /**
     * Legt den Wert der scheduled-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduled(String value) {
        this.scheduled = value;
    }

    /**
     * Ruft den Wert der playerA-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPlayerA() {
        return playerA;
    }

    /**
     * Legt den Wert der playerA-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPlayerA(Object value) {
        this.playerA = value;
    }

    /**
     * Ruft den Wert der playerB-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPlayerB() {
        return playerB;
    }

    /**
     * Legt den Wert der playerB-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPlayerB(Object value) {
        this.playerB = value;
    }

    /**
     * Ruft den Wert der state-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Legt den Wert der state-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Ruft den Wert der setA1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetA1() {
        return setA1;
    }

    /**
     * Legt den Wert der setA1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetA1(String value) {
        this.setA1 = value;
    }

    /**
     * Ruft den Wert der setA2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetA2() {
        return setA2;
    }

    /**
     * Legt den Wert der setA2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetA2(String value) {
        this.setA2 = value;
    }

    /**
     * Ruft den Wert der setA3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetA3() {
        return setA3;
    }

    /**
     * Legt den Wert der setA3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetA3(String value) {
        this.setA3 = value;
    }

    /**
     * Ruft den Wert der setA4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetA4() {
        return setA4;
    }

    /**
     * Legt den Wert der setA4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetA4(String value) {
        this.setA4 = value;
    }

    /**
     * Ruft den Wert der setA5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetA5() {
        return setA5;
    }

    /**
     * Legt den Wert der setA5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetA5(String value) {
        this.setA5 = value;
    }

    /**
     * Ruft den Wert der setA6-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetA6() {
        return setA6;
    }

    /**
     * Legt den Wert der setA6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetA6(String value) {
        this.setA6 = value;
    }

    /**
     * Ruft den Wert der setA7-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetA7() {
        return setA7;
    }

    /**
     * Legt den Wert der setA7-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetA7(String value) {
        this.setA7 = value;
    }

    /**
     * Ruft den Wert der setsA-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetsA() {
        return setsA;
    }

    /**
     * Legt den Wert der setsA-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetsA(String value) {
        this.setsA = value;
    }

    /**
     * Ruft den Wert der matchesA-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchesA() {
        return matchesA;
    }

    /**
     * Legt den Wert der matchesA-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchesA(String value) {
        this.matchesA = value;
    }

    /**
     * Ruft den Wert der gamesA-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGamesA() {
        return gamesA;
    }

    /**
     * Legt den Wert der gamesA-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGamesA(String value) {
        this.gamesA = value;
    }

    /**
     * Ruft den Wert der setB1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetB1() {
        return setB1;
    }

    /**
     * Legt den Wert der setB1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetB1(String value) {
        this.setB1 = value;
    }

    /**
     * Ruft den Wert der setB2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetB2() {
        return setB2;
    }

    /**
     * Legt den Wert der setB2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetB2(String value) {
        this.setB2 = value;
    }

    /**
     * Ruft den Wert der setB3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetB3() {
        return setB3;
    }

    /**
     * Legt den Wert der setB3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetB3(String value) {
        this.setB3 = value;
    }

    /**
     * Ruft den Wert der setB4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetB4() {
        return setB4;
    }

    /**
     * Legt den Wert der setB4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetB4(String value) {
        this.setB4 = value;
    }

    /**
     * Ruft den Wert der setB5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetB5() {
        return setB5;
    }

    /**
     * Legt den Wert der setB5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetB5(String value) {
        this.setB5 = value;
    }

    /**
     * Ruft den Wert der setB6-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetB6() {
        return setB6;
    }

    /**
     * Legt den Wert der setB6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetB6(String value) {
        this.setB6 = value;
    }

    /**
     * Ruft den Wert der setB7-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetB7() {
        return setB7;
    }

    /**
     * Legt den Wert der setB7-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetB7(String value) {
        this.setB7 = value;
    }

    /**
     * Ruft den Wert der setsB-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetsB() {
        return setsB;
    }

    /**
     * Legt den Wert der setsB-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetsB(String value) {
        this.setsB = value;
    }

    /**
     * Ruft den Wert der matchesB-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchesB() {
        return matchesB;
    }

    /**
     * Legt den Wert der matchesB-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchesB(String value) {
        this.matchesB = value;
    }

    /**
     * Ruft den Wert der gamesB-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGamesB() {
        return gamesB;
    }

    /**
     * Legt den Wert der gamesB-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGamesB(String value) {
        this.gamesB = value;
    }

}
