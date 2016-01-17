package com.dyn.server.proxy;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class Server implements Proxy {

        /**
         * @see forge.reference.proxy.Proxy#renderGUI()
         */
        @Override
        public void renderGUI() {
                // Actions on render GUI for the server (logging)
                
        }

        
        /**
    	 * Returns a side-appropriate EntityPlayer for use during message handling
    	 */
    	@Override
		public EntityPlayer getPlayerEntity(MessageContext ctx) {
    		return ctx.getServerHandler().playerEntity;
    	}
}