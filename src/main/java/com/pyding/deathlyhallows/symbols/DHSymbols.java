package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.StrokeSet;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;

import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.DOWN;
import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.LEFT;
import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.RIGHT;
import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.UP;

public final class DHSymbols {
	
	private DHSymbols() {
		
	}
	
	public static void init() {
		register(new SymbolAnimaInteritus(), stroke(1, UP, LEFT, UP));
		register(new SymbolHorcrux(), stroke(1, UP, LEFT, DOWN));
		register(new SymbolSectumsempra(), stroke(1, UP, LEFT, RIGHT));
		register(new SymbolLumosTempestas(), stroke(1, UP,RIGHT, UP));
		register(new SymbolBombardaMaxima(), stroke(1, UP, RIGHT, DOWN));
		register(new SymbolGraviole(), stroke(1, UP, RIGHT, LEFT));
		register(new SymbolPowerDestruction(), stroke(1, UP, RIGHT, RIGHT));
		register(new SymbolOphiuchus(), stroke(1, UP, LEFT, LEFT, UP));	
	}
	
	private static void register(SymbolEffect symbol, StrokeSet... strokes) {
		EffectRegistry.instance().addEffect(symbol, strokes);
	}
	
	private static StrokeSet stroke(int level, byte... strokes) {
		return new StrokeSet(level, strokes);
	}
	
}
