
/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model.xml.clicktt;

import javax.xml.bind.annotation.*;
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
 *       &lt;attribute name="firstname" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="lastname" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="birthyear" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="internal-nr" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="licence-nr" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="sex" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="0"/>
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="club-name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="club-nr" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="club-federation-nickname" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="ttr" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="ttr-match-count" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="nationality" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="foreigner-eq-state" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="region" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="sub-region" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "person")
public class Person {

    @XmlAttribute(name = "firstname", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String firstname;
    @XmlAttribute(name = "lastname", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String lastname;
    @XmlAttribute(name = "birthyear", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String birthyear;
    @XmlAttribute(name = "internal-nr", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String internalNr;
    @XmlAttribute(name = "licence-nr", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String licenceNr;
    @XmlAttribute(name = "sex", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String sex;
    @XmlAttribute(name = "club-name")
    @XmlSchemaType(name = "anySimpleType")
    protected String clubName;
    @XmlAttribute(name = "club-nr")
    @XmlSchemaType(name = "anySimpleType")
    protected String clubNr;
    @XmlAttribute(name = "club-federation-nickname")
    @XmlSchemaType(name = "anySimpleType")
    protected String clubFederationNickname;
    @XmlAttribute(name = "ttr")
    @XmlSchemaType(name = "anySimpleType")
    protected String ttr;
    @XmlAttribute(name = "ttr-match-count")
    @XmlSchemaType(name = "anySimpleType")
    protected String ttrMatchCount;
    @XmlAttribute(name = "nationality")
    @XmlSchemaType(name = "anySimpleType")
    protected String nationality;
    @XmlAttribute(name = "foreigner-eq-state")
    @XmlSchemaType(name = "anySimpleType")
    protected String foreignerEqState;
    @XmlAttribute(name = "region")
    @XmlSchemaType(name = "anySimpleType")
    protected String region;
    @XmlAttribute(name = "sub-region")
    @XmlSchemaType(name = "anySimpleType")
    protected String subRegion;

    /**
     * Ruft den Wert der firstname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Legt den Wert der firstname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstname(String value) {
        this.firstname = value;
    }

    /**
     * Ruft den Wert der lastname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Legt den Wert der lastname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastname(String value) {
        this.lastname = value;
    }

    /**
     * Ruft den Wert der birthyear-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthyear() {
        return birthyear;
    }

    /**
     * Legt den Wert der birthyear-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthyear(String value) {
        this.birthyear = value;
    }

    /**
     * Ruft den Wert der internalNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalNr() {
        return internalNr;
    }

    /**
     * Legt den Wert der internalNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalNr(String value) {
        this.internalNr = value;
    }

    /**
     * Ruft den Wert der licenceNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenceNr() {
        return licenceNr;
    }

    /**
     * Legt den Wert der licenceNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenceNr(String value) {
        this.licenceNr = value;
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
     * Ruft den Wert der clubName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClubName() {
        return clubName;
    }

    /**
     * Legt den Wert der clubName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClubName(String value) {
        this.clubName = value;
    }

    /**
     * Ruft den Wert der clubNr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClubNr() {
        return clubNr;
    }

    /**
     * Legt den Wert der clubNr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClubNr(String value) {
        this.clubNr = value;
    }

    /**
     * Ruft den Wert der clubFederationNickname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClubFederationNickname() {
        return clubFederationNickname;
    }

    /**
     * Legt den Wert der clubFederationNickname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClubFederationNickname(String value) {
        this.clubFederationNickname = value;
    }

    /**
     * Ruft den Wert der ttr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTtr() {
        return ttr;
    }

    /**
     * Legt den Wert der ttr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTtr(String value) {
        this.ttr = value;
    }

    /**
     * Ruft den Wert der ttrMatchCount-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTtrMatchCount() {
        return ttrMatchCount;
    }

    /**
     * Legt den Wert der ttrMatchCount-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTtrMatchCount(String value) {
        this.ttrMatchCount = value;
    }

    /**
     * Ruft den Wert der nationality-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Legt den Wert der nationality-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationality(String value) {
        this.nationality = value;
    }

    /**
     * Ruft den Wert der foreignerEqState-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignerEqState() {
        return foreignerEqState;
    }

    /**
     * Legt den Wert der foreignerEqState-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignerEqState(String value) {
        this.foreignerEqState = value;
    }

    /**
     * Ruft den Wert der region-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion() {
        return region;
    }

    /**
     * Legt den Wert der region-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion(String value) {
        this.region = value;
    }

    /**
     * Ruft den Wert der subRegion-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubRegion() {
        return subRegion;
    }

    /**
     * Legt den Wert der subRegion-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubRegion(String value) {
        this.subRegion = value;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthyear='" + birthyear + '\'' +
                ", internalNr='" + internalNr + '\'' +
                ", licenceNr='" + licenceNr + '\'' +
                ", sex='" + sex + '\'' +
                ", clubName='" + clubName + '\'' +
                ", clubNr='" + clubNr + '\'' +
                ", clubFederationNickname='" + clubFederationNickname + '\'' +
                ", ttr='" + ttr + '\'' +
                ", ttrMatchCount='" + ttrMatchCount + '\'' +
                ", nationality='" + nationality + '\'' +
                ", foreignerEqState='" + foreignerEqState + '\'' +
                ", region='" + region + '\'' +
                ", subRegion='" + subRegion + '\'' +
                '}';
    }
}
