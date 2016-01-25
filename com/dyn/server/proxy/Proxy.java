package com.dyn.server.proxy;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public interface Proxy {
        public void renderGUI();
        
        /**
    	 * Returns a side-appropriate EntityPlayer for use during message handling
    	 */
    	public EntityPlayer getPlayerEntity(MessageContext ctx);
    	
    	public void init();
}