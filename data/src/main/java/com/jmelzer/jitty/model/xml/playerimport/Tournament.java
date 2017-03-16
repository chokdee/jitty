
package com.jmelzer.jitty.model.xml.playerimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}tournament-location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}competition" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="start-date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="end-date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="tournament-id" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="winning-sets" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="winning-sets-text" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="multiple-participations-same-day" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="multiple-participations-same-time" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="table-count" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="team-formation" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tournamentLocation",
    "competition"
})
@XmlRootElement(name = "tournament")
public class Tournament {

    @XmlElement(name = "tournament-location")
    protected List<TournamentLocation> tournamentLocation;
    @XmlElement(required = true)
    protected List<Competition> competition;
    @XmlAttribute(name = "name", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute(name = "start-date", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String startDate;
    @XmlAttribute(name = "end-date", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String endDate;
    @XmlAttribute(name = "tournament-id", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String tournamentId;
    @XmlAttribute(name = "winning-sets")
    @XmlSchemaType(name = "anySimpleType")
    protected String winningSets;
    @XmlAttribute(name = "winning-sets-text")
    @XmlSchemaType(name = "anySimpleType")
    protected String winningSetsText;
    @XmlAttribute(name = "multiple-participations-same-day")
    @XmlSchemaType(name = "anySimpleType")
    protected String multipleParticipationsSameDay;
    @XmlAttribute(name = "multiple-participations-same-time")
    @XmlSchemaType(name = "anySimpleType")
    protected String multipleParticipationsSameTime;
    @XmlAttribute(name = "table-count")
    @XmlSchemaType(name = "anySimpleType")
    protected String tableCount;
    @XmlAttribute(name = "team-formation")
    @XmlSchemaType(name = "anySimpleType")
    protected String teamFormation;

    /**
     * Gets the value of the tournamentLocation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tournamentLocation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTournamentLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TournamentLocation }
     * 
     * 
     */
    public List<TournamentLocation> getTournamentLocation() {
        if (tournamentLocation == null) {
            tournamentLocation = new ArrayList<TournamentLocation>();
        }
        return this.tournamentLocation;
    }

    /**
     * Gets the value of the competition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the competition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompetition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Competition }
     * 
     * 
     */
    public List<Competition> getCompetition() {
        if (competition == null) {
            competition = new ArrayList<Competition>();
        }
        return this.competition;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
     * Ruft den Wert der endDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Legt den Wert der endDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Ruft den Wert der tournamentId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTournamentId() {
        return tournamentId;
    }

    /**
     * Legt den Wert der tournamentId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTournamentId(String value) {
        this.tournamentId = value;
    }

    /**
     * Ruft den Wert der winningSets-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWinningSets() {
        return winningSets;
    }

    /**
     * Legt den Wert der winningSets-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWinningSets(String value) {
        this.winningSets = value;
    }

    /**
     * Ruft den Wert der winningSetsText-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWinningSetsText() {
        return winningSetsText;
    }

    /**
     * Legt den Wert der winningSetsText-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWinningSetsText(String value) {
        this.winningSetsText = value;
    }

    /**
     * Ruft den Wert der multipleParticipationsSameDay-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultipleParticipationsSameDay() {
        return multipleParticipationsSameDay;
    }

    /**
     * Legt den Wert der multipleParticipationsSameDay-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultipleParticipationsSameDay(String value) {
        this.multipleParticipationsSameDay = value;
    }

    /**
     * Ruft den Wert der multipleParticipationsSameTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultipleParticipationsSameTime() {
        return multipleParticipationsSameTime;
    }

    /**
     * Legt den Wert der multipleParticipationsSameTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultipleParticipationsSameTime(String value) {
        this.multipleParticipationsSameTime = value;
    }

    /**
     * Ruft den Wert der tableCount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableCount() {
        return tableCount;
    }

    /**
     * Legt den Wert der tableCount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableCount(String value) {
        this.tableCount = value;
    }

    /**
     * Ruft den Wert der teamFormation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeamFormation() {
        return teamFormation;
    }

    /**
     * Legt den Wert der teamFormation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeamFormation(String value) {
        this.teamFormation = value;
    }

}
