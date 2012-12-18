package org.jboss.forge.addon.facets;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MockFaceted extends BaseFaceted
{
   @Override
   public boolean supports(Class<? extends Facet<?>> type)
   {
      return MockFacet.class.isAssignableFrom(type);
   }
}