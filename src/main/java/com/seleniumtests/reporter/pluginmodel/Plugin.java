/*
 * Copyright 2021 www.seleniumtests.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seleniumtests.reporter.pluginmodel;

import java.util.ArrayList;
import java.util.List;

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
 * <p/>Java class for anonymous complex type.
 *
 * <p/>
 * <p/>The following schema fragment specifies the expected content contained within this class.
 *
 * <p/>
 * <pre>
   &lt;complexType>
     &lt;complexContent>
       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         &lt;sequence>
           &lt;element ref="{}test" maxOccurs="unbounded"/>
         &lt;/sequence>
         &lt;attribute name="purpose" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
         &lt;attribute name="class-name" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
       &lt;/restriction>
     &lt;/complexContent>
   &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"test"})
@XmlRootElement(name = "plugin")
public class Plugin {

    @XmlElement(required = true)
    protected List<Test> test;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String purpose;
    @XmlAttribute(name = "class-name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String className;

    /**
     * Gets the value of the className property.
     *
     * @return  possible object is {@link String }
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the value of the purpose property.
     *
     * @return  possible object is {@link String }
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * Gets the value of the test property.
     *
     * <p/>
     * <p/>This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
     * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
     * method for the test property.
     *
     * <p/>
     * <p/>For example, to add a new item, do as follows:
     *
     * <p/>
     * <pre>
       getTest().add(newItem);
     * </pre>
     *
     * <p/>
     * <p/>
     * <p/>Objects of the following type(s) are allowed in the list {@link Test }
     */
    public List<Test> getTest() {
        if (test == null) {
            test = new ArrayList<Test>();
        }

        return this.test;
    }

    /**
     * Sets the value of the className property.
     *
     * @param  value  allowed object is {@link String }
     */
    public void setClassName(final String value) {
        this.className = value;
    }

    /**
     * Sets the value of the purpose property.
     *
     * @param  value  allowed object is {@link String }
     */
    public void setPurpose(final String value) {
        this.purpose = value;
    }

}
