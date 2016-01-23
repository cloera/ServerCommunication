package com.dyn.server.packets.client;

import java.io.IOException;
import com.dyn.achievements.achievement.AchievementPlus;
import com.dyn.achievements.achievement.AchievementPlus.AchievementType;
import com.dyn.achievements.achievement.Requirements.BaseRequirement;
import com.dyn.achievements.handlers.AchievementHandler;
import com.dyn.server.packets.PacketDispatcher;
import com.dyn.server.packets.server.AwardAchievementMessage;
import com.dyn.server.packets.AbstractMessage.AbstractClientMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.relauncher.Side;

public class SyncAchievementsMessage extends AbstractClientMessage<SyncAchievementsMessage> {

	// the info needed to increment a requirement
	private String data;

	// The basic, no-argument constructor MUST be included for
	// automated handling
	public SyncAchievementsMessage() {
	}

	// We need to initialize our data, so provide a suitable constructor:
	public SyncAchievementsMessage(String s) {
		data = s;
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {
		data = buffer.readStringFromBuffer(500);
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
		buffer.writeStringToBuffer(data);
	}

	@Override
	public void process(EntityPlayer player, Side side) {
		if (side.isClient()) {

			String[] values = this.data.split(" ");
			int ach_id = Integer.parseInt(values[0]);
			AchievementType type = null;
			if (values[1].equals("CRAFT")) {
				type = AchievementType.CRAFT;
			} else if (values[1].equals("SMELT")) {
				type = AchievementType.SMELT;
			} else if (values[1].equals("PICKUP")) {
				type = AchievementType.PICKUP;
			} else if (values[1].equals("KILL")) {
				type = AchievementType.KILL;
			} else if (values[1].equals("BREW")) {
				type = AchievementType.BREW;
			} else if (values[1].equals("STAT")) {
				type = AchievementType.STAT;
			} else if (values[1].equals("SPAWN")) {
				type = AchievementType.SPAWN;
			} else if (values[1].equals("PLACE")) {
				type = AchievementType.PLACE;
			}
			int req_id = Integer.parseInt(values[2]);

			AchievementPlus a = AchievementHandler.findAchievementById(ach_id);
			if (!a.isAwarded()) {
				if (!a.hasParent()) {
					for (BaseRequirement r : a.getRequirements().getRequirementsByType(type)) {
						if (r.getRequirementID() == req_id) {
							if (r.getTotalAquired() < r.getTotalNeeded()) {
								r.incrementTotal();
							}
						}
					}
					if(a.meetsRequirements()){
						PacketDispatcher.sendToServer(new AwardAchievementMessage(a.getId()));
						a.setAwarded(true);
					}
				} else if (a.getParent().isAwarded()) {
					for (BaseRequirement r : a.getRequirements().getRequirementsByType(type)) {
						if (r.getRequirementID() == req_id) {
							if (r.getTotalAquired() < r.getTotalNeeded()) {
								r.incrementTotal();
							}
						}
					}
					if(a.meetsRequirements()){
						PacketDispatcher.sendToServer(new AwardAchievementMessage(a.getId()));
						a.setAwarded(true);
					}
				}
			}
		}
	}
}
