package com.dyn.server.packets.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import com.dyn.achievements.handlers.AchievementHandler;
import com.dyn.server.packets.AbstractMessage.AbstractServerMessage;

import cpw.mods.fml.relauncher.Side;

public class AwardAchievementMessage extends AbstractServerMessage<AwardAchievementMessage>
{
	private int id;

	// The basic, no-argument constructor MUST be included to use the new automated handling
	public AwardAchievementMessage() {}

	// if there are any class fields, be sure to provide a constructor that allows
	// for them to be initialized, and use that constructor when sending the packet
	public AwardAchievementMessage(int id) {
		this.id = id;
	}

	@Override
	protected void read(PacketBuffer buffer) {
		// basic Input/Output operations, very much like DataInputStream
		id = buffer.readInt();
	}

	@Override
	protected void write(PacketBuffer buffer) {
		// basic Input/Output operations, very much like DataOutputStream
		buffer.writeInt(id);
	}

	@Override
	public void process(EntityPlayer player, Side side) {
		// using the message instance gives access to 'this.id'
		if(side.isServer())
			AchievementHandler.findAchievementById(this.id).awardAchievement(player);
	}
}
