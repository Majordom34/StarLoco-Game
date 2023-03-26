package org.starloco.locos.script;

import org.classdump.luna.Table;
import org.classdump.luna.exec.CallException;
import org.classdump.luna.exec.CallPausedException;
import org.classdump.luna.impl.NonsuspendableFunctionException;
import org.classdump.luna.load.LoaderException;
import org.classdump.luna.runtime.*;
import org.starloco.locos.entity.npc.NpcTemplate;
import org.starloco.locos.game.world.World;

import java.io.IOException;
import java.nio.file.Paths;

public final class NpcScriptVM extends ScriptVM {
    private static NpcScriptVM instance;

    private NpcScriptVM() throws LoaderException, IOException, CallException, CallPausedException, InterruptedException {
        super("NPC");
    }

    public void loadData() throws CallException, LoaderException, IOException, CallPausedException, InterruptedException {
        super.loadData();
        this.customizeEnv();
        this.runFile(Paths.get("scripts", "Npc.lua"));
        this.runDirectory(Paths.get("scripts", "npcs"));
    }

    public void safeLoadData() {
        try {
            this.loadData();
        } catch (Exception e) {
            ScriptVM.logger.error("Failed to load NPC data", e);
        }
    }

    private void customizeEnv() {
        this.env.rawset("RegisterNPCDef", new RegisterNpcTemplate());
    }

    public static synchronized void init() throws LoaderException, IOException, CallException, CallPausedException, InterruptedException {
        if(instance != null) return;

        instance = new NpcScriptVM();
    }

    public static NpcScriptVM getInstance()  {
        return instance;
    }

    static class RegisterNpcTemplate extends AbstractFunction1<Table> {
        @Override
        public void invoke(ExecutionContext context, Table val) {
            World.world.addNpcTemplate(new NpcTemplate(val));
            context.getReturnBuffer().setTo();
        }

        @Override
        public void resume(ExecutionContext context, Object suspendedState) {
            throw new NonsuspendableFunctionException();
        }
    }
}
