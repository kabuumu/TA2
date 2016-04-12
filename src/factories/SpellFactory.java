package factories;

import java.util.Random;

import entities.Spell;
import entities.Spell.SpellType;
import entities.StatusEffect;
import entities.StatusEffect.Effect;
import enums.DamageType;

public class SpellFactory {
	public static Spell GetSpell(int itemLevel){
		return GetSpell(null,itemLevel);
	}

	public static Spell GetSpell(SpellType spellType, int itemLevel){

		String name;
		DamageType damageType;
		int damage;
		float manaCost;
		StatusEffect statusEffect = null;
		int range;

		int spellLevel = (itemLevel*10)+20; //Setting a random spell level, this is a multiple of 30;

		if(spellType==null){
			spellType = getSpellType(); //Getting the spell type, offensive, or buffing.
		}

		if(new Random().nextInt(2)==0){
			statusEffect = getStatusEffect(spellType, spellLevel); //Getting the status effect (if any) for the spell.
		}

		if(statusEffect!=null){ //If a status effect was added.
			spellLevel-=statusEffect.getAmount()*statusEffect.getTime(); //Subtract the status effect's amount multiplied by the time it remains for.
		}

		damageType = getDamageType(spellType);

		damage = getDamage(damageType, spellLevel);

		if(spellType==SpellType.SELF){
			range = 1;
			if(statusEffect!=null){
				damage = 0;
			}
		}
		else{
			range = new Random().nextInt(3)+3;
		}

		manaCost = Math.max(Math.abs((int)(damage*6)),1)
		+
		Math.max((range/2),1);
		if(statusEffect!=null){
			manaCost+=(statusEffect.getAmount()*4)+(statusEffect.getTime()/2);
		}
		
		manaCost*=1.2;

		name = getName(spellType, statusEffect, damageType, damage);

		Spell spell = new Spell(name, damageType, damage, (int)manaCost, statusEffect, spellType, range);
		return spell;
	}

	private static String getName(SpellType spellType, StatusEffect statusEffect, DamageType damageType, int damage) {
		String name = "";

		if(statusEffect!=null){
			name+=getSpellPrefix(statusEffect);

		}
		else{
			name+=getSpellPrefix();
		}

		name+=" ";

		if(damage!=0){
			name+=getSpellSuffix(damageType);
		}
		else name+=getSpellSuffix(spellType);

		return name;
	}

	private static int getDamage(DamageType damageType, int spellLevel) {
		int damage = spellLevel/8;
		if (damageType==DamageType.CURE){
			damage = -damage;
		}
		return damage;
	}

	private static DamageType getDamageType(SpellType spellType) {
		if(spellType == SpellType.SELF){
			return DamageType.CURE;
		}
		switch(new Random().nextInt(3)){
		case 0:
			return DamageType.MAGIC;
		case 1:
			return DamageType.FIRE;
		case 2:
			return DamageType.ICE;
		}
		return null;
	}

	private static StatusEffect getStatusEffect(SpellType spellType, int spellLevel) {
		//Getting status effect type
		StatusEffect.Effect effectType = null; // Will never actually be null.
		switch(spellType){
		case SELF: //Get buffing spell.
			spellLevel*=1.3;
			switch (new Random().nextInt(2)){
			case 0:
				effectType = Effect.STRENGTH;
				break;
			case 1:
				effectType = Effect.AGILITY;
				break;
			}
			break;
		case TARGET: //Get offensive spell.
			switch (new Random().nextInt(2)){
			case 0:
				effectType = Effect.SLOW;
				break;
			case 1:
				effectType = Effect.WEAKEN;
				break;
			}
			break;
		default:
			return null;
		}

		int amount = new Random().nextInt(spellLevel/8)+(spellLevel/12); //A random number between 1 and spellLevel/10.
		int time = new Random().nextInt(spellLevel/amount)+3; //A random number between 3 and spellLevel/Amount(minimum 1).
		//These two multiplied together will determine how much of the spell level is left for damage.

		StatusEffect statusEffect = new StatusEffect(effectType,amount,time);
		return statusEffect;

	}

	private static SpellType getSpellType(){
		switch (new Random().nextInt(2)){
		case 0:
			return SpellType.SELF;
		case 1:
			return SpellType.TARGET;
		default:
			return null;
		}
	}

	private static String getSpellPrefix(StatusEffect statusEffect){
		int random = new Random().nextInt(4);
		switch(statusEffect.getEffect()){
		case AGILITY:
			switch (random){
			case 0:
				return "SWIFT";
			case 1:
				return "SPEEDY";
			case 2:
				return "AGILE";
			case 3:
				return "FAST";
			}
		case STRENGTH:
			switch (random){
			case 0:
				return "STRONG";
			case 1:
				return "BRUTISH";
			case 2:
				return "POTENT";
			case 3:
				return "MIGHTY";
			}
		case SLOW:
			switch (random){
			case 0:
				return "SLUDGY";
			case 1:
				return "SLOWING";
			case 2:
				return "BINDING";
			case 3:
				return "SLUGGISH";
			}
		case WEAKEN:
			switch (random){
			case 0:
				return "CRIPPLING";
			case 1:
				return "WEAKENING";
			case 2:
				return "ATROPHIC";
			case 3:
				return "LANGUID";
			}
		default:
			return null;
		}
	}

	private static String getSpellSuffix(DamageType damageType) {
		int random = new Random().nextInt(4);
		switch (damageType){
		case CURE:
			switch (random){
			case 0:
				return "CURE";
			case 1:
				return "HEAL";
			case 2:
				return "LIFE";
			case 3:
				return "REVIVE";
			}
		case FIRE:
			switch (random){
			case 0:
				return "FLAME";
			case 1:
				return "EMBER";
			case 2:
				return "FIRE";
			case 3:
				return "BURN";
			}
		case ICE:
			switch (random){
			case 0:
				return "SHARD";
			case 1:
				return "SNOW";
			case 2:
				return "ICE";
			case 3:
				return "FROST";
			}
		case MAGIC:
			switch (random){
			case 0:
				return "BOLT";
			case 1:
				return "GRASP";
			case 2:
				return "FORCE";
			case 3:
				return "BEAM";
			}
		default:
			return null;
		}

	}


	private static String getSpellSuffix(SpellType spellType) {
		int random = new Random().nextInt(4);
		switch (spellType){
		case SELF:
			switch (random){
			case 0:
				return "CANTRIP";
			case 1:
				return "CHARM";
			case 2:
				return "ENCHANT";
			case 3:
				return "SPELL";
			}
		case TARGET:
			switch (random){
			case 0:
				return "JINX";
			case 1:
				return "HEX";
			case 2:
				return "CURSE";
			case 3:
				return "VOODOO";
			}
		}
		return null;
	}


	private static String getSpellPrefix() {

		switch(new Random().nextInt(4)){
		case 0:
			return "ARCANE";
		case 1:
			return "ELDRITCH";
		case 2:
			return "MYSTIC";
		case 3:
			return "MAGIC";
		default:
			return null;
		}

	}
}
