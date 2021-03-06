/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2002 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2002-2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby;

import org.jruby.anno.JRubyMethod;
import org.jruby.anno.JRubyModule;
import org.jruby.common.IRubyWarnings.ID;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * GC (Garbage Collection) Module
 *
 * Note: Since we rely on Java's memory model we can't provide the
 * kind of control over garbage collection that MRI provides.  Also note
 * that since all Ruby libraries make GC assumptions based on MRI's GC
 * that we decided to no-op explicit collection through these APIs.
 * You can use Java Integration in your libraries to force a Java
 * GC (assuming you really want to).
 *
 */
@JRubyModule(name="GC")
public class RubyGC {
    private static volatile boolean gcDisabled = false;
    private static volatile boolean stress = false;

    public static RubyModule createGCModule(Ruby runtime) {
        RubyModule result = runtime.defineModule("GC");
        runtime.setGC(result);
        
        result.defineAnnotatedMethods(RubyGC.class);
        
        return result;        
    }

    @JRubyMethod(module = true, visibility = Visibility.PRIVATE)
    public static IRubyObject start(ThreadContext context, IRubyObject recv) {
        return context.getRuntime().getNil();
    }

    @JRubyMethod
    public static IRubyObject garbage_collect(ThreadContext context, IRubyObject recv) {
        return context.getRuntime().getNil();
    }

    @JRubyMethod(module = true, visibility = Visibility.PRIVATE)
    public static IRubyObject enable(ThreadContext context, IRubyObject recv) {
        Ruby runtime = context.getRuntime();
        emptyImplementationWarning(runtime, "GC.enable");
        boolean old = gcDisabled;
        gcDisabled = false;
        return runtime.newBoolean(old);
    }

    @JRubyMethod(module = true, visibility = Visibility.PRIVATE)
    public static IRubyObject disable(ThreadContext context, IRubyObject recv) {
        Ruby runtime = context.getRuntime();
        emptyImplementationWarning(runtime, "GC.disable");
        boolean old = gcDisabled;
        gcDisabled = true;
        return runtime.newBoolean(old);
    }

    @JRubyMethod(module = true, visibility = Visibility.PRIVATE)
    public static IRubyObject stress(ThreadContext context, IRubyObject recv) {
        return context.getRuntime().newBoolean(stress);
    }

    @JRubyMethod(name = "stress=", module = true, visibility = Visibility.PRIVATE)
    public static IRubyObject stress_set(ThreadContext context, IRubyObject recv, IRubyObject arg) {
        Ruby runtime = context.getRuntime();
        emptyImplementationWarning(runtime, "GC.stress=");
        stress = arg.isTrue();
        return runtime.newBoolean(stress);
    }

    private static void emptyImplementationWarning(Ruby runtime, String name) {
        runtime.getWarnings().warn(ID.EMPTY_IMPLEMENTATION,
                name + " does nothing on JRuby", name);
    }
}
