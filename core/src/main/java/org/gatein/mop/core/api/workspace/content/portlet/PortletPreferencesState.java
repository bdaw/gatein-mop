/**
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.gatein.mop.core.api.workspace.content.portlet;

import org.chromattic.api.annotations.NodeMapping;
import org.chromattic.api.annotations.OneToMany;
import org.chromattic.api.annotations.Create;
import org.gatein.mop.core.api.workspace.content.CustomizationState;
import org.gatein.mop.core.content.portlet.Preferences;
import org.gatein.mop.core.content.portlet.Preference;
import org.gatein.mop.core.content.portlet.PreferencesBuilder;

import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
@NodeMapping(name = "mop:portletpreferences")
public abstract class PortletPreferencesState extends CustomizationState {

  /** . */
  private Preferences payload;

  @OneToMany
  public abstract Map<String, PortletPreferenceState> getChildren();

  @Create
  public abstract PortletPreferenceState create();

  private void setPayload(Preferences payload) {

    this.payload = payload;

    //
    Map<String, PortletPreferenceState> entries = getChildren();
    entries.clear();

    for (Preference pref : payload) {
      PortletPreferenceState prefState = create();
      entries.put(pref.getName(), prefState);
      prefState.setValue(pref.getValues());
      prefState.setReadOnly(pref.isReadOnly());
    }
  }

  public void setPayload(Object payload) {
    setPayload((Preferences)payload);
  }

  public Object getPayload() {
    if (payload == null) {
      PreferencesBuilder builder = new PreferencesBuilder();
      for (PortletPreferenceState entry : getChildren().values()) {
        builder.add(entry.getName(), entry.getValues(), entry.getReadOnly());
      }
      payload = builder.build();
    }
    return payload;
  }
}