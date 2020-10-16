package com.nicko.core.profile.punishment.command;

import com.nicko.core.profile.punishment.procedure.PunishmentProcedure;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedureStage;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"cancelban", "cancelar"})
public class CancelBanCommand {

    public void execute(Player player) {
        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(player);

        if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
            PunishmentProcedure.getProcedures().remove(procedure);
            player.sendMessage(CC.RED + " • Você cancelou o procedimento de punição.");
        }
    }
}
