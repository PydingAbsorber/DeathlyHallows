package com.pyding.deathlyhallows.core.transformers;

import airburn.fasmtel.transformers.MethodData;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.core.DHHooks;
import org.apache.logging.log4j.Logger;

public class ClassTransformerBase extends airburn.fasmtel.transformers.ClassTransformerBase {

	public static final String HOOKS = getPath(DHHooks.class);

	public ClassTransformerBase(byte[] basicClass, int flags, MethodData... methods) {
		super(basicClass, flags, methods);
	}

	public ClassTransformerBase(byte[] basicClass, MethodData... methods) {
		super(basicClass, methods);
	}

	@Override
	protected Logger getLogger() {
		return DeathlyHallows.LOG;
	}
	
}
