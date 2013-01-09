package org.jboss.forge.container.impl;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.forge.container.services.RemoteInstance;
import org.jboss.forge.container.services.RemoteInstanceImpl;
import org.jboss.forge.container.services.ServiceRegistry;
import org.jboss.forge.container.util.Sets;

@Singleton
public class ServiceRegistryImpl implements ServiceRegistry
{
   private Set<Class<?>> services = Sets.getConcurrentSet();

   private BeanManager manager;

   private ClassLoader loader;

   @Inject
   public ServiceRegistryImpl(BeanManager manager)
   {
      this.manager = manager;
      this.loader = Thread.currentThread().getContextClassLoader();
   }

   @Override
   public <T> void addService(Class<T> clazz)
   {
      services.add(clazz);
   }

   @Override
   public <T> RemoteInstance<T> getRemoteInstance(Class<T> clazz)
   {
      if (!manager.getBeans(clazz).isEmpty())
         return new RemoteInstanceImpl<T>(loader, manager, clazz);
      return null;
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> RemoteInstance<T> getRemoteInstance(String clazz)
   {
      Class<?> type;
      try
      {
         type = Class.forName(clazz, true, loader);
         return (RemoteInstance<T>) getRemoteInstance(type);
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
   }

   @Override
   public Set<Class<?>> getServices()
   {
      return services;
   }

   @Override
   public boolean hasService(Class<?> clazz)
   {
      return services.contains(clazz);
   }

   @Override
   public String toString()
   {
      return services.toString();
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> Set<RemoteInstance<T>> getRemoteInstances(Class<T> clazz)
   {
      Set<RemoteInstance<T>> result = new HashSet<RemoteInstance<T>>();
      for (Class<?> type : services)
      {
         if (clazz.isAssignableFrom(type))
         {
            result.add((RemoteInstance<T>) getRemoteInstance(type));
         }
      }
      return result;
   }

   @Override
   public <T> Set<RemoteInstance<T>> getRemoteInstances(String clazz)
   {
      try
      {
         @SuppressWarnings("unchecked")
         Class<T> type = (Class<T>) Class.forName(clazz, true, loader);
         return getRemoteInstances(type);
      }
      catch (ClassNotFoundException e)
      {
         return new HashSet<RemoteInstance<T>>();
      }
   }
}