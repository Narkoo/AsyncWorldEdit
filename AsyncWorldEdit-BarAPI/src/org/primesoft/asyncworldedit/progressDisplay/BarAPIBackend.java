/*
 * AsyncWorldEdit a performance improvement plugin for Minecraft WorldEdit plugin.
 * Copyright (c) 2015, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) AsyncWorldEdit contributors
 *
 * All rights reserved.
 *
 * Redistribution in source, use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2.  Redistributions of source code, with or without modification, in any form
 *     other then free of charge is not allowed,
 * 3.  Redistributions of source code, with tools and/or scripts used to build the 
 *     software is not allowed,
 * 4.  Redistributions of source code, with information on how to compile the software
 *     is not allowed,
 * 5.  Providing information of any sort (excluding information from the software page)
 *     on how to compile the software is not allowed,
 * 6.  You are allowed to build the software for your personal use,
 * 7.  You are allowed to build the software using a non public build server,
 * 8.  Redistributions in binary form in not allowed.
 * 9.  The original author is allowed to redistrubute the software in bnary form.
 * 10. Any derived work based on or containing parts of this software must reproduce
 *     the above copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided with the
 *     derived work.
 * 11. The original author of the software is allowed to change the license
 *     terms or the entire license of the software as he sees fit.
 * 12. The original author of the software is allowed to sublicense the software
 *     or its parts using any license terms he sees fit.
 * 13. By contributing to this project you agree that your contribution falls under this
 *     license.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.primesoft.asyncworldedit.progressDisplay;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.primesoft.asyncworldedit.api.IAsyncWorldEdit;
import org.primesoft.asyncworldedit.api.inner.IAsyncWorldEditCore;
import org.primesoft.asyncworldedit.api.inner.IAwePlugin;
import org.primesoft.asyncworldedit.api.progressDisplay.IProgressDisplay;
import org.primesoft.asyncworldedit.api.progressDisplay.IProgressDisplayManager;

/**
 *
 * @author SBPrime
 */
public class BarAPIBackend extends JavaPlugin implements IAwePlugin {
    private static final Logger s_log = Logger.getLogger("Minecraft.AWE.BarAPI");
    private static String s_prefix = null;
    private static final String s_logFormat = "%s %s";
    
    /**
     * Send message to the log
     *
     * @param msg
     */
    public static void log(String msg) {
        if (s_log == null || msg == null || s_prefix == null) {
            return;
        }

        s_log.log(Level.INFO, String.format(s_logFormat, s_prefix, msg));
    }

    
    /**
     * The AWE API
     */
    private IAsyncWorldEdit m_awe;
    
    /**
     * The progress display manager
     */
    private IProgressDisplayManager m_progressManager;
    
    /**
     * The API integrator
     */
    private IProgressDisplay m_integrator;

    @Override
    public void onEnable() {
        PluginDescriptionFile desc = getDescription();
        s_prefix = String.format("[%s]", desc.getName());
        
        super.onEnable();
        log("Enabled.");
    }

    @Override
    public void initialize(IAsyncWorldEditCore awe) {        
        m_awe = awe;
        
        if (m_awe == null) {
            log("AsyncWorldEdit API not found.");
            return;
        }
        
        m_progressManager = m_awe.getProgressDisplayManager();
        
        if (m_progressManager == null) {
            log("No progress display manager found.");
            return;
        }
        
        m_integrator = new BarAPIIntegrator(this, m_progressManager);               
        m_progressManager.registerProgressDisplay(m_integrator);

        log("Initialized.");
    }

    @Override
    public void onDisable() {
        m_progressManager.unregisterProgressDisplay(m_integrator);
        
        super.onDisable();
    }
}
