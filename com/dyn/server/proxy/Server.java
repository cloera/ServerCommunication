package com.dyn.server.proxy;

import com.dyn.server.packets.PacketDispatcher;
import com.dyn.server.packets.client.GetWorldsMessage;
import com.dyn.server.packets.client.SyncWorldMessage;
import com.dyn.server.packets.client.TeacherSettingsMessage;
//import com.forgeessentials.api.APIRegistry;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.common.MinecraftForge;

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

	@Override
	public void init() {
		FMLCommonHandler.instance().bus().register(this);

		MinecraftForge.EVENT_BUS.register(this);
	}

	// void dropEvent(ItemTossEvent event){

	// }

	@SubscribeEvent
	public void loginEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (getOpLevel(event.player.getGameProfile()) > 0) {
			PacketDispatcher.sendTo(new TeacherSettingsMessage(getServerUsers(), true), (EntityPlayerMP) event.player);
		}

		//PacketDispatcher.sendTo(new GetWorldsMessage(APIRegistry.namedWorldHandler.getWorldNames()), (EntityPlayerMP) event.player);

	}

	/*@SubscribeEvent
	public void changedDim(PlayerEvent.PlayerLoggedInEvent event) {
		PacketDispatcher.sendTo(
				new SyncWorldMessage(
						APIRegistry.namedWorldHandler.getWorldName(event.player.worldObj.provider.dimensionId)),
				(EntityPlayerMP) event.player);

	}*/

	private String[] getServerUsers() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	private int getOpLevel(GameProfile profile) {
		// does the configuration manager return null on the client side?
		MinecraftServer minecraftServer = MinecraftServer.getServer();
		if (minecraftServer == null)
			return 0;
		if (!minecraftServer.getConfigurationManager().func_152596_g(profile))
			return 0;
		UserListOpsEntry entry = (UserListOpsEntry) minecraftServer.getConfigurationManager().func_152603_m()
				.func_152683_b(profile);
		return entry != null ? entry.func_152644_a() : MinecraftServer.getServer().getOpPermissionLevel();
	}

}