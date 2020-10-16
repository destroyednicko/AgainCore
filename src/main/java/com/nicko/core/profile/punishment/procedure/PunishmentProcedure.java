package com.nicko.core.profile.punishment.procedure;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;

import java.util.HashSet;
import java.util.Set;

public class PunishmentProcedure {

    @Getter
    private static final Set<PunishmentProcedure> procedures = new HashSet<>();

    @Getter
    private final Player issuer;
    @Getter
    private final Profile recipient;
    @Getter
    private final PunishmentProcedureType type;
    @Getter
    private PunishmentProcedureStage stage;
    @Getter
    @Setter
    private Punishment punishment;

    public PunishmentProcedure(Player issuer, Profile recipient, PunishmentProcedureType type, PunishmentProcedureStage stage) {
        this.issuer = issuer;
        this.recipient = recipient;
        this.type = type;
        this.stage = stage;

        procedures.add(this);
    }

    public static PunishmentProcedure getByPlayer(Player player) {
        for (PunishmentProcedure procedure : procedures) {
            if (procedure.issuer.equals(player)) {
                return procedure;
            }
        }

        return null;
    }

    public void finish() {
        this.recipient.save();
        procedures.remove(this);
    }

    public void cancel() {
        procedures.remove(this);
    }


}
