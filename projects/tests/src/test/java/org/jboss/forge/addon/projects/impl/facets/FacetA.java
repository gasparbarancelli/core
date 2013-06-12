package org.jboss.forge.addon.projects.impl.facets;

import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.facets.RequiresFacet;

@RequiresFacet(FacetB.class)
public class FacetA extends AbstractFacet<Project> implements ProjectFacet
{
   @Override
   public boolean install()
   {
      return true;
   }

   @Override
   public boolean isInstalled()
   {
      return getOrigin().hasFacet(getClass());
   }

}