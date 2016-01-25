package com.dyn.server.packets.client;

import java.io.IOException;

import com.dyn.achievements.AchievementsMod;
import com.dyn.server.packets.AbstractMessage.AbstractClientMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.relauncher.Side;

public class SyncWorldMessage extends AbstractClientMessage<SyncWorldMessage> {

	// the info needed to increment a requirement
		private String data;

		// The basic, no-argument constructor MUST be included for
		// automated handling
		public SyncWorldMessage() {
		}

		// We need to initialize our data, so provide a suitable constructor:
		public SyncWorldMessage(String world) {
				this.data = world;
		}

		@Override
		protected void read(PacketBuffer buffer) throws IOException {
			data = buffer.readStringFromBuffer(50);
		}

		@Override
		protected void write(PacketBuffer buffer) throws IOException {
			buffer.writeStringToBuffer(data);
		}

		@Override
		public void process(EntityPlayer player, Side side) {
			if (side.isClient()) {
				AchievementsMod.currentWorld = data;
			}
		}
}
