package org.smbarbour.nei.forestry;

import codechicken.nei.api.API;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mod(modid="NEIForestry", name="NEI Forestry", version="1.1", dependencies="required-after:NotEnoughItems")
public class NEIForestry {

	public static final String VERSION = "1.1";
	public static Logger logger = Logger.getLogger("NEIForestry");

	@Mod.Instance("NEIForestry")
	public static NEIForestry instance;
	private File sourceFile;
	private Map<String, ArtifactVersion> modVersions;

	@Mod.EventHandler
	public void preLoad(FMLPreInitializationEvent event)
	{
		this.sourceFile = event.getSourceFile();
	}

	@Mod.EventHandler
	public void postLoad(FMLPostInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide().isClient())
		{
			API.registerRecipeHandler(new ForestryShapedHandler());
			API.registerUsageHandler(new ForestryShapedHandler());
		} else {
			logger.log(Level.WARNING, "NEIForestry is client-side only mod ");
		}
	}
}
