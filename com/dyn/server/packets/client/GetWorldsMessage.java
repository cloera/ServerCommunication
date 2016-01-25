package com.dyn.server.packets.client;

import java.io.IOException;
import java.util.List;

import com.dyn.server.ServerMod;
import com.dyn.server.packets.AbstractMessage.AbstractClientMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.relauncher.Side;

public class GetWorldsMessage extends AbstractClientMessage<GetWorldsMessage> {

	// the info needed to increment a requirement
		private String data;

		// The basic, no-argument constructor MUST be included for
		// automated handling
		public GetWorldsMessage() {
		}

		// We need to initialize our data, so provide a suitable constructor:
		public GetWorldsMessage(List<String> users) {
			for(String s: users){
				this.data += " " + s;
			}
		}

		@Override
		protected void read(PacketBuffer buffer) throws IOException {
			data = buffer.readStringFromBuffer(10000);
		}

		@Override
		protected void write(PacketBuffer buffer) throws IOException {
			buffer.writeStringToBuffer(data);
		}

		@Override
		public void process(EntityPlayer player, Side side) {
			if (side.isClient()) {
				String[] users = this.data.split(" ");			
				ServerMod.worlds.clear();
				for(String u : users){
					player.addChatMessage(new ChatComponentText(u));
					if(u != null && !u.equals("null")){
						ServerMod.worlds.add(u);
					}
				}
				ServerMod.worlds.remove(null);
			}
		}
}
