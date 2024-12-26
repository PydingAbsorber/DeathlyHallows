package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.StrokeSet;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;

import java.util.ArrayList;

import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.DOWN;
import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.LEFT;
import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.RIGHT;
import static com.emoniph.witchery.infusion.infusions.symbols.StrokeSet.Stroke.UP;

public final class DHSymbols {

	public static final ArrayList<SymbolEffect> ELDER_SYMBOLS = new ArrayList<>();

	private DHSymbols() {

	}

	public static void init() {
		register(new SymbolAnimaInteritus(), true,
				stroke(1, RIGHT, RIGHT, DOWN, RIGHT, UP),
				stroke(2, RIGHT, RIGHT, RIGHT, DOWN, RIGHT, UP),
				stroke(3, RIGHT, RIGHT, RIGHT, RIGHT, DOWN, RIGHT, UP)
		);
		register(new SymbolHorcrux(), true,
				stroke(1, UP, UP, LEFT, LEFT, DOWN, DOWN, RIGHT, RIGHT, RIGHT, RIGHT, DOWN, DOWN, LEFT, LEFT, UP),
				stroke(1, DOWN, DOWN, RIGHT, RIGHT, UP, UP, LEFT, LEFT, LEFT, LEFT, UP, UP, RIGHT, RIGHT, DOWN),
				stroke(1, DOWN, DOWN, LEFT, LEFT, UP, UP, RIGHT, RIGHT, RIGHT, RIGHT, UP, UP, LEFT, LEFT, DOWN)
		);
		register(new SymbolSectumsempra(), true,
				stroke(1, UP, LEFT, UP, LEFT),
				stroke(2, UP, UP, LEFT, LEFT, UP, UP, LEFT, LEFT)

		);
		register(new SymbolLumosTempestas(), true,
				stroke(1, UP, UP, LEFT, LEFT, DOWN, RIGHT),
				stroke(2, UP, UP, LEFT, LEFT, LEFT, DOWN, RIGHT)
		);
		register(new SymbolBombardaMaxima(), true,
				stroke(1, UP, RIGHT, RIGHT),
				stroke(2, UP, UP, RIGHT, RIGHT, RIGHT),
				stroke(3, UP, UP, UP, RIGHT, RIGHT, RIGHT, RIGHT)
		);
		register(new SymbolGraviole(), true,
				stroke(1, LEFT, DOWN, DOWN, DOWN, DOWN)
		);
		register(new SymbolPowerDestruction(), true,
				stroke(1, DOWN, DOWN, RIGHT, DOWN, DOWN, LEFT, LEFT, UP, UP),
				stroke(1, DOWN, DOWN, LEFT, DOWN, DOWN, RIGHT, RIGHT, UP, UP)
		);
		register(new SymbolOphiuchus(), true,
				stroke(1, UP, RIGHT, DOWN, DOWN, LEFT),
				stroke(1, RIGHT, DOWN, LEFT, LEFT, UP),
				stroke(1, DOWN, LEFT, UP, UP, RIGHT)
		);
	}

	private static void register(SymbolEffect symbol, boolean putInElderWands, StrokeSet... strokes) {
		EffectRegistry.instance().addEffect(symbol, strokes);
		if(putInElderWands) {
			ELDER_SYMBOLS.add(symbol);
		}
	}

	private static StrokeSet stroke(int level, byte... strokes) {
		return new StrokeSet(level, strokes);
	}

}
