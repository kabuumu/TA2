package org.kabuumu.entities.items;

import org.kabuumu.entities.Spell;
import org.kabuumu.enums.ItemType;

public class MagicItem extends Item {
	private final Spell spell;
	
	public MagicItem(String name, ItemType type, int level, int value, Spell spell) {
		super(name, type, level, value);
		this.spell = spell;
	}

	public Spell getSpell() {
		return spell;
	}

}
