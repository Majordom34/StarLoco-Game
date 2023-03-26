package org.starloco.locos.fight;

import org.starloco.locos.area.map.GameCase;
import org.starloco.locos.client.BasePlayer;
import org.starloco.locos.client.other.Stats;
import org.starloco.locos.common.Formulas;
import org.starloco.locos.common.PathFinding;
import org.starloco.locos.common.SocketManager;
import org.starloco.locos.entity.Collector;
import org.starloco.locos.entity.Prism;
import org.starloco.locos.entity.monster.MonsterGrade;
import org.starloco.locos.fight.spells.LaunchedSpell;
import org.starloco.locos.fight.spells.Spell;
import org.starloco.locos.fight.spells.SpellEffect;
import org.starloco.locos.game.world.World;
import org.starloco.locos.kernel.Config;
import org.starloco.locos.kernel.Constant;
import org.starloco.locos.guild.Guild;
import org.starloco.locos.util.TimerWaiter;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Fighter implements Comparable<Fighter> {

    public int nbrInvoc;
    private boolean trapped = false, glyphed = false;
    public boolean isStatique = false;
    private int id = 0;
    private boolean canPlay = false;
    private Fight fight;
    private int type = 0;                                // 1 : Personnage, 2 : Mob, 5 : Perco
    private MonsterGrade mob = null;
    private BasePlayer player = null;
    private BasePlayer _double = null;
    private Collector collector = null;
    private Prism prism = null;
    private int team = -2;
    private GameCase cell;
    private int pdvMax;
    private int pdv;
    private boolean isDead;
    private boolean hasLeft;
    private int gfxId;
    private Fighter isHolding;
    private Fighter holdedBy;
    private Fighter oldCible = null;
    private Fighter invocator;
    private boolean levelUp = false;
    private boolean isDeconnected = false;
    private int turnRemaining = 0;
    private int nbrDisconnection = 0;
    private boolean isTraqued = false;
    private Stats stats;
    private Map<Integer, Integer> state = new HashMap<>();
    private ArrayList<SpellEffect> fightBuffs = new ArrayList<>();
    private Map<Integer, Integer> chatiValue = new HashMap<>();
    private ArrayList<LaunchedSpell> launchedSpell = new ArrayList<>();
    public World.Couple<Byte, Long> killedBy;

    public Fighter(Fight f, MonsterGrade mob) {
        setId(mob.getInFightID());
        this.fight = f;
        this.type = 2;
        this.mob = mob;
        this.pdvMax = mob.getPdvMax();
        this.pdv = mob.getPdv();
        this.gfxId = getDefaultGfx();
        if(this.mob.getTemplate().getId() == 423) // Kralamour géant
            this.state.put(Constant.ETAT_ENRACINE, 9999);
    }

    public Fighter(Fight f, BasePlayer player) {
        this.fight = f;
        if (player._isClone) {
            this.type = 10;
            setDouble(player);
        } else {
            this.type = 1;
            this.player = player;
        }
        setId(player.getId());
        this.pdvMax = player.getMaxPdv();
        this.pdv = player.getCurPdv();
        this.gfxId = getDefaultGfx();
    }

    public Fighter(Fight f, Collector Perco) {
        this.fight = f;
        this.type = 5;
        setCollector(Perco);
        setId(-1);
        this.pdvMax = (World.world.getGuild(Perco.getGuildId()).getLvl() * 100);
        this.pdv = (World.world.getGuild(Perco.getGuildId()).getLvl() * 100);
        this.gfxId = 6000;
    }

    public Fighter(Fight Fight, Prism Prisme) {
        this.fight = Fight;
        this.type = 7;
        setPrism(Prisme);
        setId(-1);
        this.pdvMax = Prisme.getLevel() * 10000;
        this.pdv = Prisme.getLevel() * 10000;
        this.gfxId = Prisme.getAlignment() == 1 ? 8101 : 8100;
        Prisme.refreshStats();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean canPlay() {
        return this.canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public Fight getFight() {
        return this.fight;
    }

    public int getType() {
        return this.type;
    }

    public MonsterGrade getMob() {
        if (this.type == 2)
            return this.mob;
        return null;
    }

    public boolean isMob() {
        return (this.mob != null);
    }

    public BasePlayer getPlayer() {
        if (this.type == 1)
            return this.player;
        return null;
    }

    public BasePlayer getDouble() {
        return _double;
    }

    public boolean isDouble() {
        return (this._double != null);
    }

    public void setDouble(BasePlayer _double) {
        this._double = _double;
    }

    public Collector getCollector() {
        if (this.type == 5)
            return this.collector;
        return null;
    }

    public boolean isCollector() {
        return (this.collector != null);
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public Prism getPrism() {
        if (this.type == 7)
            return this.prism;
        return null;
    }

    public void setPrism(Prism prism) {
        this.prism = prism;
    }

    public boolean isPrisme() {
        return (this.prism != null);
    }

    public int getTeam() {
        return this.team;
    }

    public void setTeam(int i) {
        this.team = i;
    }

    public int getTeam2() {
        return this.fight.getTeamId(getId());
    }

    public int getOtherTeam() {
        return this.fight.getOtherTeamId(getId());
    }

    public GameCase getCell() {
        return this.cell;
    }

    public void setCell(GameCase cell) {
        this.cell = cell;
    }

    public int getPdvMax() {
        return this.pdvMax + getBuffValue(Constant.STATS_ADD_VITA);
    }

    public void removePdvMax(int pdv) {
        this.pdvMax = this.pdvMax - pdv;
        if (this.pdv > this.pdvMax)
            this.pdv = this.pdvMax;
    }

    public int getPdv() {
        return (this.pdv + getBuffValue(Constant.STATS_ADD_VITA));
    }

    public void setPdvMax(int pdvMax) {
        this.pdvMax = pdvMax;
    }

    public void setPdv(int pdv) {
        this.pdv = pdv;
        if(this.pdv > this.pdvMax)
            this.pdv = this.pdvMax;
    }

    public void removePdv(Fighter caster, int pdv) {
        if (pdv > 0)
            this.getFight().getAllChallenges().values().stream().filter(Objects::nonNull).forEach(challenge -> challenge.onFighterAttacked(caster, this));
        this.pdv -= pdv;
    }

    public void fullPdv() {
        this.pdv = this.pdvMax;
    }

    public boolean isFullPdv() {
        return this.pdv == this.pdvMax;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public boolean hasLeft() {
        return this.hasLeft;
    }

    public void setLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }

    public Fighter getIsHolding() {
        return this.isHolding;
    }

    public void setIsHolding(Fighter isHolding) {
        this.isHolding = isHolding;
    }

    public Fighter getHoldedBy() {
        return this.holdedBy;
    }

    public void setHoldedBy(Fighter holdedBy) {
        this.holdedBy = holdedBy;
    }

    public Fighter getOldCible() {
        return this.oldCible;
    }

    public void setOldCible(Fighter cible) {
        this.oldCible = cible;
    }

    public Fighter getInvocator() {
        return this.invocator;
    }

    public void setInvocator(Fighter invocator) {
        this.invocator = invocator;
    }

    public boolean isInvocation() {
        return (this.invocator != null);
    }

    public boolean getLevelUp() {
        return this.levelUp;
    }

    public void setLevelUp(boolean levelUp) {
        this.levelUp = levelUp;
    }

    public void Disconnect() {
        if (this.isDeconnected)
            return;
        this.isDeconnected = true;
        this.turnRemaining = 20;
        this.nbrDisconnection++;
    }

    public void Reconnect() {
        this.isDeconnected = false;
        this.turnRemaining = 0;
    }

    public boolean isDeconnected() {
        return !this.hasLeft && this.isDeconnected;
    }

    public int getTurnRemaining() {
        return this.turnRemaining;
    }

    public void setTurnRemaining() {
        this.turnRemaining--;
    }

    public int getNbrDisconnection() {
        return this.nbrDisconnection;
    }

    public boolean getTraqued() {
        return this.isTraqued;
    }

    public void setTraqued(boolean isTraqued) {
        this.isTraqued = isTraqued;
    }

    public void setState(int id, int t) {
        this.state.remove(id);
        if (t != 0) this.state.put(id, t);
    }

    public int getState(int id) {
        return this.state.get(id) != null ? this.state.get(id) : -1;
    }

    public boolean haveState(int id) {
        Integer turn = this.state.get(id);
        return turn != null && turn != 0;
    }

    public void sendState(BasePlayer p) {
        if (p != null && p.getAccount() != null && p.getGameClient() != null)
            for (Entry<Integer, Integer> state : this.state.entrySet())
                SocketManager.GAME_SEND_GA_PACKET(p.getGameClient(), 7 + "", 950 + "", getId() + "", getId() + "," + state.getKey() + ",1");
    }

    public int nbInvocation() {
        int i = 0;
        for (Entry<Integer, Fighter> entry : this.getFight().getTeam(this.getTeam2()).entrySet()) {
            Fighter f = entry.getValue();
            if (f.isInvocation() && !f.isStatique)
                if (f.getInvocator() == this)
                    i++;
        }
        return i;
    }

    public boolean isTrappedOrGlyphed() {
        return isTrapped() || isGlyphed();
    }

    public boolean isTrapped() {
        return trapped;
    }

    public void setTrapped(boolean trapped) {
        this.trapped = trapped;
    }

    public boolean isGlyphed() {
        return trapped;
    }

    public void setGlyphed(boolean glyphed) {
        this.glyphed = glyphed;
    }

    public ArrayList<SpellEffect> getFightBuff() {
        return this.fightBuffs;
    }

    private Stats getFightBuffStats() {
        Stats stats = new Stats();
        for (SpellEffect entry : this.fightBuffs)
            stats.addOneStat(entry.getEffectID(), entry.getValue());
        return stats;
    }

    public int getBuffValue(int id) {
        int value = 0;
        for (SpellEffect entry : this.fightBuffs)
            if (entry.getEffectID() == id)
                value += entry.getValue();
        return value;
    }

    public SpellEffect getBuff(int id) {
        for (SpellEffect entry : this.fightBuffs)
            if (entry.getEffectID() == id && entry.getTurn() > 0)
                return entry;
        return null;
    }

    public ArrayList<SpellEffect> getBuffsByEffectID(int effectId) {
        return this.fightBuffs.stream().filter(buff -> buff.getEffectID() == effectId).collect(Collectors.toCollection(ArrayList::new));
    }

    public Stats getTotalStatsLessBuff() {
        Stats stats = new Stats(new HashMap<>());
        if (this.type == 1)
            stats = this.player.getTotalStats(true);
        if (this.type == 2) {
            if (this.stats == null) {
                this.stats = this.mob.getStats();
                stats = this.stats;
            } else {
                stats = this.mob.getStats();
            }
        }
        if (this.type == 5)
            stats = new Stats(World.world.getGuild(getCollector().getGuildId()));
        if (this.type == 7)
            stats = getPrism().getStats();
        if (this.type == 10)
            stats = getDouble().getTotalStats(true);
        return stats;
    }

    public boolean hasBuff(int id) {
        for (SpellEffect entry : this.fightBuffs)
            if (entry.getEffectID() == id && entry.getTurn() > 0)
                return true;
        return false;
    }

    public SpellEffect addBuff(int id, int value, int duration, boolean debuff, int spellId, String args, Fighter caster, boolean sendGA, boolean sendGIE) {
        SpellEffect effect = new SpellEffect(id, value, duration, debuff, caster, args, spellId);

        if(this.mob != null)
            for(int id1 : Constant.STATIC_INVOCATIONS)
                if (id1 != 2750 && id1 == this.mob.getTemplate().getId())
                    return effect;

        switch(spellId) {
            case 1099:
                if(this.mob != null && this.mob.getTemplate().getId() == 423)
                    return effect;
                break;
            case 99:case 5:case 20:case 127: case 89:case 126:case 115:case 192: case 4:case 1:case 6:
            case 14:case 18:case 7: case 284:case 197:case 704:case 168:case 45: case 159:case 171:case 167:case 511:case 513:
            case 686: case 701: // Sort pandawa (etat)
            case 431:case 433:case 437:case 443:case 441: // Chatiment
                debuff = true;
                break;
        }

        if(id == 606 || id == 607 || id == 608 || id == 609 || id == 611 || id == 125 || id == 114) debuff = true;
        else if(id == 293) debuff = false;

        switch(spellId) {
            // Feca spells shields
            case 1: case 4: case 5: case 6: case 7: case 14: case 18: case 20:
                if(this.getId() != caster.getId())
                    duration++;
                break;
        }
        //Si c'est le jouer actif qui s'autoBuff, on ajoute 1 a la durée
        if(this.getId() == caster.getId() && !this.isTrapped() && id != 84 && id != 950 && spellId != 446) {
            duration += 1;
            switch(spellId) {
                case 138: // Mot de silence
                case 170: // Fleche d'immo
                case 114: // Rekop
                    duration--;
                    break;
            }
        }
        // Cas infini
        if(this.mob != null && duration == 0)
            duration = -1;

        effect.setTurn(duration);
        this.fightBuffs.add(effect);

        if(Config.debug)
            System.out.println("- Ajout du Buff "+id+" sur le personnage fighter ("+this.getId()+") val : "+value+" duration : "+duration+" debuff : "+debuff+" spellid : "+spellId+" args : "+args+" !");
        switch(spellId) {
            // Feca spells shields
            case 1: case 4: case 5: case 6: case 7: case 14: case 18: case 20:
                if(this.getId() != caster.getId())
                    duration--;
                break;
        }
        if(id != 950 && spellId != 446) {
            switch (spellId) {
                case 170: // Fleche d'immo
                case 114: // Rekop
                case 101:// Roulette
                    if (duration != 0)
                        duration--;
                    break;
                default:
                    if (this.getId() == caster.getId() || this.isTrapped())
                        duration--;
            }
        }

        if(sendGA) {
            SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(fight, 7, effect.getEffectID(), String.valueOf(caster.getId()), this.getId() + "," + effect.getValue() + "," + effect.getTurn());
        }
        if(sendGIE) {
            fight.sendBuffPacket(this, effect, fight.getFighters(7), null, duration);
        }
        return effect;
    }

    public void debuff(SpellEffect effect) {
        Iterator<SpellEffect> it = this.fightBuffs.iterator();
        while (it.hasNext()) {
            SpellEffect spellEffect = it.next();

            if(spellEffect.getEffectID() == 293)
                continue;
            switch (spellEffect.getSpell()) {
                case 437: case 431: case 433: case 443: case 441://Châtiments
                case 1104: case 1105: // Krachau immo / ralenti
                    continue;
                case 197://Puissance sylvestre
                case 52://Cupidité
                case 228://Etourderie mortelle (DC)
                    it.remove();
                    continue;
            }

            if (spellEffect.isDebuffabe()) {
                it.remove();

                if (effect.getCaster() == this) {
                    switch (spellEffect.getEffectID()) {
                        case Constant.STATS_ADD_PA:
                        case Constant.STATS_ADD_PA2:
                            SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 101, getId() + "", getId() + ",-" + spellEffect.getValue());
                            this.setCurPa(this.fight, this.getCurPa(fight) - spellEffect.getValue());
                            break;
                        case Constant.STATS_ADD_PM:
                        case Constant.STATS_ADD_PM2:
                            SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 127, getId() + "", getId() + ",-" + spellEffect.getValue());
                            this.setCurPm(this.fight, this.getCurPm(fight) - spellEffect.getValue());
                            break;
                        //case Constant.STATS_REM_PA:
                        case Constant.STATS_REM_PA2: // Pa non esquivable
                            SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 111, getId() + "", getId() + "," + spellEffect.getValue());
                            this.setCurPa(this.fight, this.getCurPa(fight) + spellEffect.getValue());
                            break;

                        //case Constant.STATS_REM_PM:
                        case Constant.STATS_REM_PM2: // Pm non esquivable (picole)
                            SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 128, getId() + "", getId() + "," + spellEffect.getValue());
                            this.setCurPm(this.fight, this.getCurPm(fight) + spellEffect.getValue());
                            break;
                    }
                }
            }
        }
        ArrayList<SpellEffect> array = new ArrayList<>(this.fightBuffs);
        if (!array.isEmpty()) {
            this.fightBuffs.clear();
            TimerWaiter.addNext(() -> array.stream().filter(Objects::nonNull).forEach(spellEffect -> {
                this.addBuff(spellEffect.getEffectID(), spellEffect.getValue(), spellEffect.getTurn(), spellEffect.isDebuffabe(), spellEffect.getSpell(), spellEffect.getArgs(), spellEffect.getCaster(), false, false);
                int duration = effect.getCaster() != this ? spellEffect.getTurn() : spellEffect.getTurn() - 1;
                fight.sendBuffPacket(this, spellEffect, fight.getFighters(7), null, duration);
            }), 750);
        }

        if (this.player != null && !this.hasLeft) {
            SocketManager.GAME_SEND_STATS_PACKET(this.player);
        }
    }

    private void refreshEndTurnShield() {
        for (Fighter fighter : this.fight.getFighters(7)) {
            Iterator<SpellEffect> iterator = fighter.getFightBuff().iterator();

            while (iterator.hasNext()) {
                SpellEffect effect = iterator.next();

                if (effect != null) {
                    switch (effect.getSpell()) {
                        // Feca spells shields
                        case 1: case 4: case 5: case 6: case 7: case 14: case 18: case 20:
                            if (this.getId() == effect.getCaster().getId()) {
                                if (effect.decrementDuration() == 0) {
                                    iterator.remove();
                                }
                                break;
                            }
                    }
                }
            }
        }
    }

    void refreshEndTurnBuff() {
        this.refreshEndTurnShield();
        Iterator<SpellEffect> iterator = this.fightBuffs.iterator();

        while (iterator.hasNext()) {

            SpellEffect effect = iterator.next();
            if (effect == null)
                continue;
            switch (effect.getSpell()) {
                // Feca spells shields
                case 1: case 4: case 5: case 6: case 7: case 14: case 18: case 20:
                    continue;
            }

            if (effect.decrementDuration() == 0 || effect.getCaster().isDead) {
                iterator.remove();
                switch (effect.getEffectID()) {
                    case 787: // Mot lotof
                        int id = Integer.parseInt(effect.getArgs().split(";")[0]);
                        int level = Integer.parseInt(effect.getArgs().split(";")[1]);
                        Spell spell = World.world.getSort(id);

                        for (SpellEffect e : spell.getStatsByLevel(level).getEffects()) {
                            if (e.getEffectID() == 89)
                                e.applyToFight(fight, this, this.getCell(), PathFinding.getFightersAround(this.getCell().getId(), this.fight.getMap()));
                        }
                        spell.getStatsByLevel(level).applySpellEffectToFight(fight, fight.getFighterByGameOrder(), cell, false, false);
                        break;
                    case 108:
                        if (effect.getSpell() == 441) {
                            //Baisse des pdvs max
                            this.pdvMax = (this.pdvMax - effect.getValue());
                            //Baisse des pdvs actuel
                            if (this.pdv - effect.getValue() <= 0) {
                                this.fight.onFighterDie(this, this.holdedBy);
                                this.fight.verifIfTeamAllDead();
                            } else this.pdv = (this.pdv - effect.getValue());
                        }
                        break;
                    case 150: // Invisibilité
                        SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 150, effect.getCaster().getId() + "", getId() + ",0");
                        SocketManager.GAME_SEND_GIC_PACKET_TO_FIGHT(this.fight, 7, this);
                        break;

                    case 950: // Etat
                        String args = effect.getArgs();
                        id = Integer.parseInt(args.split(";")[2]);

                        if (id != -1) {
                            setState(id, 0);
                            SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 950, effect.getCaster().getId() + "", effect.getCaster().getId() + "," + id + ",0");
                        }
                        break;
                }
            }
        }
    }

    public void initBuffStats() {
        if (this.type == 1)
            this.fightBuffs.addAll(new ArrayList<>(this.player.get_buff().values()));
    }

    void applyBeginningTurnBuff(Fight fight) {
        List<SpellEffect> effects = new ArrayList<>(this.fightBuffs);
        for (int effectID : Constant.BEGIN_TURN_BUFF) {
            effects.stream().filter(entry -> entry.getEffectID() == effectID)
                    .forEach(entry -> entry.applyBeginingBuff(fight, this));
        }
    }

    public ArrayList<LaunchedSpell> getLaunchedSorts() {
        return this.launchedSpell;
    }

    void refreshLaunchedSort() {
        this.launchedSpell.removeIf(launched -> launched.decrementCooldown() <= 0);
    }

    void addLaunchedSort(Fighter target, Spell.SortStats sort, Fighter fighter) {
        LaunchedSpell launched = new LaunchedSpell(target, sort, fighter);
        this.launchedSpell.add(launched);
    }

    public Stats getTotalStats() {
        Stats stats = new Stats(new HashMap<>());
        if (this.type == 1)
            stats = this.player.getTotalStats(false);
        if (this.type == 2)
            stats = this.mob.getStats();
        if (this.type == 5)
            stats = new Stats(World.world.getGuild(getCollector().getGuildId()));
        if (this.type == 7)
            stats = this.getPrism().getStats();
        if (this.type == 10)
            stats = this.getDouble().getTotalStats(false);

        if(this.type != 1)
            stats = Stats.cumulStatFight(stats, getFightBuffStats());

        return stats;
    }

    public int getMaitriseDmg(int id) {
        int value = 0;
        for (SpellEffect entry : this.fightBuffs)
            if (entry.getSpell() == id)
                value += entry.getValue();
        return value;
    }

    public boolean getSpellValueBool(int id) {
        for (SpellEffect entry : this.fightBuffs)
            if (entry.getSpell() == id)
                return true;
        return false;
    }

    boolean testIfCC(int tauxCC) {
        if (tauxCC < 2)
            return false;
        int agi = getTotalStats().getEffect(Constant.STATS_ADD_AGIL);
        if (agi < 0)
            agi = 0;
        tauxCC -= getTotalStats().getEffect(Constant.STATS_ADD_CC);
        tauxCC = (int) ((tauxCC * 2.9901) / Math.log(agi + 12));//Influence de l'agi
        if (tauxCC < 2)
            tauxCC = 2;
        int jet = Formulas.getRandomValue(1, tauxCC);
        return (jet == tauxCC);
    }

    boolean testIfCC(int porcCC, Spell.SortStats sSort, Fighter fighter) {
        BasePlayer perso = fighter.getPlayer();
        if (porcCC < 2)
            return false;
        int agi = getTotalStats().getEffect(Constant.STATS_ADD_AGIL);
        if (agi < 0)
            agi = 0;
        porcCC -= getTotalStats().getEffect(Constant.STATS_ADD_CC);
        if (fighter.getType() == 1
                && perso.getObjectsClassSpell().containsKey(sSort.getSpellID())) {
            int modi = perso.getValueOfClassObject(sSort.getSpellID(), 287);
            porcCC -= modi;
        }
        porcCC = (int) ((porcCC * 2.9901) / Math.log(agi + 12));
        if (porcCC < 2)
            porcCC = 2;
        int jet = Formulas.getRandomValue(1, porcCC);
        return (jet == porcCC);
    }

    int getInitiative() {
        if (this.type == 1)
            return this.player.getInitiative();
        if (this.type == 2)
            return this.mob.getInit();
        if (this.type == 5)
            return World.world.getGuild(getCollector().getGuildId()).getLvl();
        if (this.type == 7)
            return 0;
        if (this.type == 10)
            return getDouble().getInitiative();
        return 0;
    }

    public int getPa() {
        switch (this.type) {
            case 1:
                return getTotalStats().getEffect(Constant.STATS_ADD_PA);
            case 2:
                return getTotalStats().getEffect(Constant.STATS_ADD_PA)
                        + this.mob.getPa();
            case 5:
                return getTotalStats().getEffect(Constant.STATS_ADD_PM) + 6;
            case 7:
                return getTotalStats().getEffect(Constant.STATS_ADD_PM) + 6;
            case 10:
                return getTotalStats().getEffect(Constant.STATS_ADD_PA);
        }
        return 0;
    }

    public int getPm() {
        switch (this.type) {
            case 1: // personnage
                return getTotalStats().getEffect(Constant.STATS_ADD_PM);
            case 2: // mob
                return getTotalStats().getEffect(Constant.STATS_ADD_PM) + this.mob.getPm();
            case 5: // perco
                return getTotalStats().getEffect(Constant.STATS_ADD_PM) + 4;
            case 7: // prisme
                return getTotalStats().getEffect(Constant.STATS_ADD_PM);
            case 10: // clone
                return getTotalStats().getEffect(Constant.STATS_ADD_PM);
        }
        return 0;
    }

    int getPros() {
        switch (this.type) {
            case 1: // personnage
                return (getTotalStats().getEffect(Constant.STATS_ADD_PROS) + Math.round(getTotalStats().getEffect(Constant.STATS_ADD_CHAN) / 10) + Math.round(getBuffValue(Constant.STATS_ADD_CHAN) / 10));
            case 2: // mob
                if (this.isInvocation()) // Si c'est un coffre anim�, la chance est �gale � 1000*(1+lvlinvocateur/100)
                    return (getTotalStats().getEffect(Constant.STATS_ADD_PROS) + (1000 * (1 + this.getInvocator().getLvl() / 100)) / 10);
                else
                    return (getTotalStats().getEffect(Constant.STATS_ADD_PROS) + Math.round(getBuffValue(Constant.STATS_ADD_CHAN) / 10));
        }
        return 0;
    }

    public int getCurPa(Fight fight) {
        return fight.getCurFighterPa();
    }

    public void setCurPa(Fight fight, int pa) {
        fight.setCurFighterPa(fight.getCurFighterPa() + pa);
    }

    public int getCurPm(Fight fight) {
        return fight.getCurFighterPm();
    }

    public void setCurPm(Fight fight, int pm) {
        fight.setCurFighterPm(fight.getCurFighterPm() + pm);
    }

    public boolean canLaunchSpell(int spellID) {
        return this.getPlayer().hasSpell(spellID) && LaunchedSpell.cooldownGood(this, spellID);
    }

    public void unHide(int spellid) {
       //on retire le buff invi
        if (spellid != -1)// -1 : CAC
        {
            switch (spellid) {
                case 66:
                case 71:
                case 181:
                case 196:
                case 200:
                case 219:
                    return;
            }
        }
        ArrayList<SpellEffect> buffs = new ArrayList<>();
        buffs.addAll(getFightBuff());
        for (SpellEffect SE : buffs) {
            if (SE.getEffectID() == 150)
                getFightBuff().remove(SE);
        }
        SocketManager.GAME_SEND_GA_PACKET_TO_FIGHT(this.fight, 7, 150, getId() + "", getId() + ",0");
        //On actualise la position
        SocketManager.GAME_SEND_GIC_PACKET_TO_FIGHT(this.fight, 7, this);
    }

    public boolean isHide() {
        return hasBuff(150);
    }

    public int getPdvMaxOutFight() {
        if (this.player != null)
            return this.player.getMaxPdv();
        if (this.mob != null)
            return this.mob.getPdvMax();
        return 0;
    }

    public Map<Integer, Integer> getChatiValue() {
        return this.chatiValue;
    }

    public int getDefaultGfx() {
        if (this.player != null)
            return this.player.getGfxId();
        if (this.mob != null)
            return this.mob.getTemplate().getGfxId();
        return 0;
    }

    public int getLvl() {
        if (this.type == 1)
            return this.player.getLevel();
        if (this.type == 2)
            return this.mob.getLevel();
        if (this.type == 5)
            return World.world.getGuild(getCollector().getGuildId()).getLvl();
        if (this.type == 7)
            return getPrism().getLevel();
        if (this.type == 10)
            return getDouble().getLevel();
        return 0;
    }

    public String xpString(String str) {
        if (this.player != null) {
            int max = this.player.getLevel() + 1;
            if (max > World.world.getExpLevelSize())
                max = World.world.getExpLevelSize();
            return World.world.getExpLevel(this.player.getLevel()).perso + str
                    + this.player.getExp() + str + World.world.getExpLevel(max).perso;
        }
        return "0" + str + "0" + str + "0";
    }

    public String getPacketsName() {
        if (this.type == 1)
            return this.player.getName();
        if (this.type == 2)
            return this.mob.getTemplate().getId() + "";
        if (this.type == 5)
            return this.getCollector().getFullName();
        if (this.type == 7)
            return (getPrism().getAlignment() == 1 ? 1111 : 1112) + "";
        if (this.type == 10)
            return getDouble().getName();

        return "";
    }

    public String getGmPacket(char c, boolean withGm) {
        StringBuilder str = new StringBuilder();
        str.append(withGm ? "GM|" : "").append(c);
        str.append(getCell().getId()).append(";");
        str.append("1;0;");//1; = Orientation
        str.append(getId()).append(";");
        str.append(getPacketsName()).append(";");

        switch (this.type) {
            case 1://Perso
                str.append(this.player.getClasse()).append(";");
                str.append(this.player.getGfxId()).append("^").append(this.player.get_size()).append(";");
                str.append(this.player.getSexe()).append(";");
                str.append(this.player.getLevel()).append(";");
                str.append(this.player.getAlignment()).append(",");
                str.append("0").append(",");
                str.append((this.player.is_showWings() ? this.player.getGrade() : "0")).append(",");
                str.append(this.player.getLevel() + this.player.getId());
                if (this.player.is_showWings() && this.player.getDeshonor() > 0) {
                    str.append(",");
                    str.append(this.player.getDeshonor() > 0 ? 1 : 0).append(';');
                } else {
                    str.append(";");
                }
                int color1 = this.player.getColor1(),
                        color2 = this.player.getColor2(),
                        color3 = this.player.getColor3();
                if (this.player.getObjetByPos(Constant.ITEM_POS_MALEDICTION) != null)
                    if (this.player.getObjetByPos(Constant.ITEM_POS_MALEDICTION).getTemplate().getId() == 10838) {
                        color1 = 16342021;
                        color2 = 16342021;
                        color3 = 16342021;
                    }
                str.append((color1 == -1 ? "-1" : Integer.toHexString(color1))).append(";");
                str.append((color2 == -1 ? "-1" : Integer.toHexString(color2))).append(";");
                str.append((color3 == -1 ? "-1" : Integer.toHexString(color3))).append(";");
                str.append(this.player.getGMStuffString()).append(";");
                str.append(getPdv()).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_PA)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_PM)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_NEU)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_TER)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_FEU)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_EAU)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_AIR)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_AFLEE)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_MFLEE)).append(";");
                str.append(this.team).append(";");
                if (this.player.isOnMount() && this.player.getMount() != null)
                    str.append(this.player.getMount().getStringColor(this.player.parsecolortomount()));
                str.append(";");
                break;
            case 2://Mob
                str.append("-2;");
                str.append(this.mob.getTemplate().getGfxId()).append("^").append(this.mob.getSize()).append(";");
                str.append(this.mob.getGrade()).append(";");
                str.append(this.mob.getTemplate().getColors().replace(",", ";")).append(";");
                str.append("0,0,0,0;");
                str.append(this.getPdvMax()).append(";");
                str.append(this.mob.getPa()).append(";");
                str.append(this.mob.getPm()).append(";");
                str.append(this.team);
                break;
            case 5://Perco
                str.append("-6;");//Perco
                str.append("6000^100;");//GFXID^Size
                Guild G = World.world.getGuild(this.collector.getGuildId());
                str.append(G.getLvl()).append(";");
                str.append("1;");
                str.append("2;4;");
                int resistance = (int) Math.floor(G.getLvl() / 2);
                if(resistance > 50) resistance = 50;
                str.append(resistance).append(";").append(resistance).append(";").append(resistance).append(";").append(resistance)
                        .append(";").append(resistance).append(";").append(resistance).append(";").append(resistance).append(";");//R�sistances
                str.append(this.team);
                break;
            case 7://Prisme
                str.append("-2;");
                str.append(getPrism().getAlignment() == 1 ? 8101 : 8100).append("^100;");
                str.append(getPrism().getLevel()).append(";");
                str.append("-1;-1;-1;");
                str.append("0,0,0,0;");
                str.append(this.getPdvMax()).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_PA)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_PM)).append(";");
                str.append(getTotalStats().getEffect(214)).append(";");
                str.append(getTotalStats().getEffect(210)).append(";");
                str.append(getTotalStats().getEffect(213)).append(";");
                str.append(getTotalStats().getEffect(211)).append(";");
                str.append(getTotalStats().getEffect(212)).append(";");
                str.append(getTotalStats().getEffect(160)).append(";");
                str.append(getTotalStats().getEffect(161)).append(";");
                str.append(this.team);
                break;
            case 10://Double
                str.append(getDouble().getClasse()).append(";");
                str.append(getDouble().getGfxId()).append("^").append(getDouble().get_size()).append(";");
                str.append(getDouble().getSexe()).append(";");
                str.append(getDouble().getLevel()).append(";");
                str.append(getDouble().getAlignment()).append(",");
                str.append("1,");//TODO
                str.append((getDouble().is_showWings() ? getDouble().getALvl() : "0")).append(",");
                str.append(getDouble().getId()).append(";");

                str.append((getDouble().getColor1() == -1 ? "-1" : Integer.toHexString(getDouble().getColor1()))).append(";");
                str.append((getDouble().getColor2() == -1 ? "-1" : Integer.toHexString(getDouble().getColor2()))).append(";");
                str.append((getDouble().getColor3() == -1 ? "-1" : Integer.toHexString(getDouble().getColor3()))).append(";");
                str.append(getDouble().getGMStuffString()).append(";");
                str.append(getPdv()).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_PA)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_PM)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_NEU)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_TER)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_FEU)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_EAU)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_RP_AIR)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_AFLEE)).append(";");
                str.append(getTotalStats().getEffect(Constant.STATS_ADD_MFLEE)).append(";");
                str.append(this.team).append(";");
                if (getDouble().isOnMount() && getDouble().getMount() != null)
                    str.append(getDouble().getMount().getStringColor(getDouble().parsecolortomount()));
                str.append(";");
                break;
        }

        return str.toString();
    }

    public boolean isStatic() {
        if(this.getMob() != null && this.getMob().getTemplate() != null)
            for (int id : Constant.STATIC_INVOCATIONS)
                if (id == this.getMob().getTemplate().getId())
                    return true;
        return false;
    }

    @Override
    public int compareTo(Fighter t) {
        return ((this.getPros() > t.getPros() && !this.isInvocation()) ? 1 : 0);
    }
}