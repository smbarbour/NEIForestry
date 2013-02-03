package org.smbarbour.nei.forestry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import codechicken.nei.api.API;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;

@Mod(modid="NEIForestry", name="NEI Forestry Shaped Crafting", version="1.0", dependencies="required-after:NotEnoughItems")
public class NEIForestry {

	public static final String VERSION = "1.0";
	public static Logger logger = Logger.getLogger("NEIForestry");

	@Mod.Instance("NEIForestry")
	public static NEIForestry instance;
	private File sourceFile;
	private Map<String, ArtifactVersion> modVersions;

	@Mod.PreInit
	public void preLoad(FMLPreInitializationEvent event)
	{
		this.sourceFile = event.getSourceFile();
	}

	@Mod.PostInit
	public void postLoad(FMLPostInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide().isClient())
		{
			//ModContainer forestryMod = Loader.instance().getIndexedModList().get("Forestry");
			//if (forestryMod.getProcessedVersion().compareTo(VersionParser.parseVersionReference("Forestry@,2.1")) == 0) {
				API.registerRecipeHandler(new ForestryRecipeHandler());
				API.registerUsageHandler(new ForestryRecipeHandler());
			//}
		} else {
			logger.log(Level.WARNING, "NEIForestry is client-side only mod ");
		}
	}
}
