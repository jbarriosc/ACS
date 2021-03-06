/*
 * $Id: PropertyInfo.java,v 1.2 2006/09/25 08:52:36 acaproni Exp $
 *
 * $Date: 2006/09/25 08:52:36 $
 * $Revision: 1.2 $
 * $Author: acaproni $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.gp.beans;

import java.beans.PropertyDescriptor;

/**
 * This interface defines information that can be returned for a given property. It is similar to the <code>java.beans.
 * PropertyDescriptor</code> but it aims to be a complementary and ligther version of it.
 * <p>
 * The properties of a JavaBean are describe using the <code>PropertyDescriptor</code> returned by the
 * <code>BeanInfo</code>. If you provide an explicit <code>BeanInfo</code> you can use the
 * <code>PropertyDescriptor</code> to specify the needed information (expert, hidden, PropertyEditor ...) and you do
 * not need to use this interface.
 * </p><p>
 * If you do not provide an explicit <code>BeanInfo</code> you cannot specify that information as the
 * <code>PropertyDescriptor</code> will be automatically generated by the <code>Introspector</code>. In this case, you
 * can returned an array of <code>PropertyInfo</code> specifying the proper information directly from your bean.
 * The node representing the bean will invoke the method to get that array of <code>PropertyInfo</code> and use the
 * information from it.
 * </p>
 * 
 * @version $Revision: 1.2 $  $Date: 2006/09/25 08:52:36 $
 * @author Lionel Mestre
 */
public interface PropertyInfo {
  
  /**
   * Returns the name of the property
   * @return the name of the property
   */
  public String getName();

  /**
   * Returns the display name of the property
   * @return the display name of the property
   */
  public String getDisplayName();

  /**
   * Returns if this property is expert. The "expert" flag is used to distinguish between those features that are
   * intended for expert users from those that are intended for normal users.
   * @return true if this property is intended for use by experts only
   */
  public boolean isExpert();

  /**
   * Returns if this property is hidden. The "hidden" flag is used to identify features that are intended only for tool
   * use, and which should not be exposed to humans.
   * @return true if this property should be hidden from human users.
   */
  public boolean isHidden();

  /**
   * Returns a boolean indicating the caching strategy for this property. Three value can be returned :
   * <ul>
   * <li>null : no caching strategy specified, the property inherit from the one defined for the bean or from the
   * default one if the bean does not specified one.</li>
   * <li>True : the property can be cached. This will override the caching strategy defined at the bean level. It is the
   * responsibility of the bean to send PropertyChangeEvent to update the cached value</li>
   * <li>False : the property cannot be cached. This will override the caching strategy defined at the bean level.</li>
   * </ul>
   * @return null, True or False depending the caching strategy specified.
   */
  public Boolean isCacheable();

  /**
   * Gets an explicit PropertyEditor Class for this property.
   * The method will  return "null" for indicating that no special editor has been registered, so the
   * PropertyEditorManager should be used to locate a suitable PropertyEditor.
   * @return An explicit PropertyEditor Class for this property or null.
   */
  public Class getPropertyEditorClass();

  /**
   * Update the given <code>PropertyDescriptor</code> with the information contained in this <code>PropertyInfo</code>
   * @param propertyDescriptor the <code>PropertyDescriptor</code> to update
   */
  public void updatePropertyDescriptor(PropertyDescriptor propertyDescriptor);
}