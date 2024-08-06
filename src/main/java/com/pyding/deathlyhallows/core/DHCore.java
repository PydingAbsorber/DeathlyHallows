package com.pyding.deathlyhallows.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("com.pyding.deathlyhallows.*")
public class DHCore implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{DHClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> map) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
