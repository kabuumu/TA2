package org.kabuumu.factories;

import org.junit.Test;

import org.kabuumu.entities.Spell;


public class SpellFactoryTest {
	@Test
	public void Test01(){


		Spell spell = SpellFactory.GetSpell(10);
		System.out.println("Name:     " + spell.getName());
		System.out.println("Damage:   " + spell.getDamage());
		System.out.println("Range:    " + spell.getRange());
		System.out.println("Mana:     " + spell.getManaCost());
		if(spell.getStatusEffect()!=null){
			System.out.println("Status Effect: ");
			System.out.println("   Name:  " + spell.getStatusEffect().getEffect());
			System.out.println("   Amount:" + spell.getStatusEffect().getAmount());
			System.out.println("   Time:  " + spell.getStatusEffect().getTime());
		}
	}
}
