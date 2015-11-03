/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * √Ë ˆ jfinalmodel
 * 
 * @author Mars zhang
 * @created 2015-11-3 …œŒÁ10:00:13
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class JfModel<M extends JfModel> implements Serializable {


    /**
     * √Ë ˆ
     */
    private static final long serialVersionUID = -712772823165366009L;
    /**
     * Attributes of this model
     */
    private HashMap<String, Object> attrs = new HashMap<String, Object>(); // getConfig().containerFactory.getAttrsMap();
                                                                       // // new
                                                                       // HashMap<String,
                                                                       // Object>();
    
    public M set(String attr, Object value) {
        attrs.put(attr, value);
        return (M) this;
    }

    /**
     * Put key value pair to the model when the key is not attribute of the
     * model.
     */
    public M put(String key, Object value) {
        attrs.put(key, value);
        return (M) this;
    }

    /**
     * Get attribute of any mysql type
     */
    public <T> T get(String attr) {
        return (T) (attrs.get(attr));
    }

    /**
     * Get attribute of any mysql type. Returns defaultValue if null.
     */
    public <T> T get(String attr, Object defaultValue) {
        Object result = attrs.get(attr);
        return (T) (result != null ? result : defaultValue);
    }

    /**
     * Get attribute of mysql type: varchar, char, enum, set, text, tinytext,
     * mediumtext, longtext
     */
    public String getStr(String attr) {
        return (String) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: int, integer, tinyint(n) n > 1, smallint,
     * mediumint
     */
    public Integer getInt(String attr) {
        return (Integer) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: bigint, unsign int
     */
    public Long getLong(String attr) {
        return (Long) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: unsigned bigint
     */
    public java.math.BigInteger getBigInteger(String attr) {
        return (java.math.BigInteger) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: date, year
     */
    public java.util.Date getDate(String attr) {
        return (java.util.Date) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: time
     */
    public java.sql.Time getTime(String attr) {
        return (java.sql.Time) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: timestamp, datetime
     */
    public java.sql.Timestamp getTimestamp(String attr) {
        return (java.sql.Timestamp) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: real, double
     */
    public Double getDouble(String attr) {
        return (Double) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: float
     */
    public Float getFloat(String attr) {
        return (Float) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: bit, tinyint(1)
     */
    public Boolean getBoolean(String attr) {
        return (Boolean) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: decimal, numeric
     */
    public java.math.BigDecimal getBigDecimal(String attr) {
        return (java.math.BigDecimal) attrs.get(attr);
    }

    /**
     * Get attribute of mysql type: binary, varbinary, tinyblob, blob,
     * mediumblob, longblob
     */
    public byte[] getBytes(String attr) {
        return (byte[]) attrs.get(attr);
    }

    /**
     * Get attribute of any type that extends from Number
     */
    public Number getNumber(String attr) {
        return (Number) attrs.get(attr);
    }

    /**
     * Return attribute Map.
     * <p>
     * Danger! The update method will ignore the attribute if you change it
     * directly. You must use set method to change attribute that update method
     * can handle it.
     */
    public HashMap<String, Object> getAttrs() {
        return attrs;
    }

    /**
     * Return attribute Set.
     */
    public Set<Entry<String, Object>> getAttrsEntrySet() {
        return attrs.entrySet();
    }

    /**
     * Set attributes with other model.
     * 
     * @param model
     *            the Model
     * @return this Model
     */
    public M setAttrs(M model) {
        return setAttrs(model.getAttrs());
    }

    /**
     * Set attributes with Map.
     * 
     * @param attrs
     *            attributes of this model
     * @return this Model
     */
    public M setAttrs(Map<String, Object> attrs) {
        for (Entry<String, Object> e : attrs.entrySet())
            set(e.getKey(), e.getValue());
        return (M) this;
    }

    /**
     * Remove attribute of this model.
     * 
     * @param attr
     *            the attribute name of the model
     * @return this model
     */
    public M remove(String attr) {
        attrs.remove(attr);
        return (M) this;
    }

    /**
     * Remove attributes of this model.
     * 
     * @param attrs
     *            the attribute names of the model
     * @return this model
     */
    public M remove(String... attrs) {
        if (attrs != null)
            for (String a : attrs) {
                this.attrs.remove(a);
            }
        return (M) this;
    }

    /**
     * Remove attributes if it is null.
     * 
     * @return this model
     */
    public M removeNullValueAttrs() {
        for (Iterator<Entry<String, Object>> it = attrs.entrySet().iterator(); it.hasNext();) {
            Entry<String, Object> e = it.next();
            if (e.getValue() == null) {
                it.remove();
            }
        }
        return (M) this;
    }

    /**
     * Remove all attributes of this model.
     * 
     * @return this model
     */
    public M clear() {
        attrs.clear();
        return (M) this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(" {");
        boolean first = true;
        for (Entry<String, Object> e : attrs.entrySet()) {
            if (first)
                first = false;
            else
                sb.append(", ");

            Object value = e.getValue();
            if (value != null)
                value = value.toString();
            sb.append(e.getKey()).append(":").append(value);
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof JfModel))
            return false;
        if (o == this)
            return true;
        return this.attrs.equals(((JfModel) o).attrs);
    }

    /**
     * Return attribute names of this model.
     */
    public String[] getAttrNames() {
        Set<String> attrNameSet = attrs.keySet();
        return attrNameSet.toArray(new String[attrNameSet.size()]);
    }

    /**
     * Return attribute values of this model.
     */
    public Object[] getAttrValues() {
        java.util.Collection<Object> attrValueCollection = attrs.values();
        return attrValueCollection.toArray(new Object[attrValueCollection.size()]);
    }

}
