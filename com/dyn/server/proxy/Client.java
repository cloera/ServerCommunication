package com.dyn.server.proxy;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class Client implements Proxy {

        /**
         * @see forge.reference.proxy.Proxy#renderGUI()
         */
        @Override
        public void renderGUI() {
                // Render GUI when on call from client
        } 
        
        @Override
    	public EntityPlayer getPlayerEntity(MessageContext ctx) {
    		// Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
    		// your packets will not work as expected because you will be getting a
    		// client player even when you are on the server!
    		// Sounds absurd, but it's true.

    		// Solution is to double-check side before returning the player:
    		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : null);
    	}
}