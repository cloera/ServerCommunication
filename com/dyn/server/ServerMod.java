package com.dyn.server;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import com.dyn.server.packets.PacketDispatcher;
import com.dyn.server.proxy.Proxy;
import com.dyn.server.reference.Reference;



@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class ServerMod {
	
	@Mod.Instance(Reference.MOD_ID)
	public static ServerMod instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static Proxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PacketDispatcher.registerPackets();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
	
	}
}
