/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2014 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.osgi.service.tracker;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.pentaho.di.osgi.OSGIPluginTracker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.pentaho.di.osgi.service.lifecycle.LifecycleEvent.*;

/**
 * Created by bryan on 8/15/14.
 */
public class OSGIServiceTrackerTest {
  private BundleContext bundleContext;
  private OSGIPluginTracker pluginTracker;

  @Before
  public void setup() {
    bundleContext = mock( BundleContext.class );
    pluginTracker = mock( OSGIPluginTracker.class );
    when( pluginTracker.getBundleContext() ).thenReturn( bundleContext );
  }

  @Test
  public void testAddingService() {
    ServiceReference serviceReference = mock( ServiceReference.class );
    Object service = mock( Object.class );
    when( bundleContext.getService( serviceReference ) ).thenReturn( service );
    assertEquals( service, new OSGIServiceTracker( pluginTracker, Object.class ).addingService( serviceReference ) );
    verify( pluginTracker ).serviceChanged( Object.class, START, serviceReference );
  }

  @Test
  public void testRemovedService() {
    ServiceReference serviceReference = mock( ServiceReference.class );
    Object service = mock( Object.class );
    new OSGIServiceTracker( pluginTracker, Object.class ).removedService( serviceReference, service );
    verify( pluginTracker ).serviceChanged( Object.class, STOP, serviceReference );
    verify( bundleContext ).ungetService( serviceReference );
  }

  @Test
  public void testModifiedService() {
    ServiceReference serviceReference = mock( ServiceReference.class );
    Object service = mock( Object.class );
    new OSGIServiceTracker( pluginTracker, Object.class ).modifiedService( serviceReference,
      service );
    verify( pluginTracker ).serviceChanged( Object.class, MODIFY, serviceReference );
  }
}
