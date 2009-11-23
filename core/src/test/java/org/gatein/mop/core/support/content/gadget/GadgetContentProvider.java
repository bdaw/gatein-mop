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
package org.gatein.mop.core.support.content.gadget;

import org.gatein.mop.spi.content.ContentProvider;
import org.gatein.mop.spi.content.StateContainer;
import org.gatein.mop.core.api.workspace.content.AbstractCustomization;
import org.chromattic.api.ChromatticSession;
import org.chromattic.api.UndeclaredRepositoryException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class GadgetContentProvider implements ContentProvider<Gadget>
{

   public Gadget combine(List<Gadget> states)
   {
      throw new UnsupportedOperationException();
   }

   public void setState(StateContainer container, Gadget state)
   {
      try
      {
         ChromatticSession session = ((AbstractCustomization)container).session;
         String containerId = session.getId(container);
         Node node = session.getJCRSession().getNodeByUUID(containerId);

         //
         GadgetState prefs;
         if (node.hasNode("state"))
         {
            Node stateNode = node.getNode("state");
            prefs = (GadgetState)session.findById(Object.class, stateNode.getUUID());
            if (state == null)
            {
               session.remove(prefs);
               return;
            }
         }
         else
         {
            if (state == null)
            {
               return;
            }
            else
            {
               Node stateNode = node.addNode("state", "mop:gadget");
               prefs = (GadgetState)session.findById(Object.class, stateNode.getUUID());
            }
         }

         //
         prefs.setUserPrefs(state.getUserPref());
      }
      catch (RepositoryException e)
      {
         throw new UndeclaredRepositoryException(e);
      }
   }

   public Gadget getState(StateContainer container)
   {
      try
      {
         ChromatticSession session = ((AbstractCustomization)container).session;
         String containerId = session.getId(container);
         Node node = session.getJCRSession().getNodeByUUID(containerId);

         //
         GadgetState prefs;
         if (node.hasNode("state"))
         {
            Node stateNode = node.getNode("state");
            prefs = (GadgetState)session.findById(Object.class, stateNode.getUUID());
            Gadget gadget = new Gadget();
            gadget.setUserPref(prefs.getUserPrefs());
            return gadget;
         }
         else
         {
            return null;
         }
      }
      catch (RepositoryException e)
      {
         throw new UndeclaredRepositoryException(e);
      }
   }

   public Class<Gadget> getStateType()
   {
      return Gadget.class;
   }
}
