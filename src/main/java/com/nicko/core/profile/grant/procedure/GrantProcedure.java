package com.nicko.core.profile.grant.procedure;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.grant.Grant;

import java.util.HashSet;
import java.util.Set;

public class GrantProcedure {

    @Getter
    private static final Set<GrantProcedure> procedures = new HashSet<>();

    @Getter
    private final Player issuer;
    @Getter
    private final Profile recipient;
    @Getter
    private final GrantProcedureType type;
    @Getter
    private GrantProcedureStage stage;
    @Getter
    @Setter
    private Grant grant;

    public GrantProcedure(Player issuer, Profile recipient, GrantProcedureType type, GrantProcedureStage stage) {
        this.issuer = issuer;
        this.recipient = recipient;
        this.type = type;
        this.stage = stage;

        procedures.add(this);
    }

    public static GrantProcedure getByPlayer(Player player) {
        for (GrantProcedure procedure : procedures) {
            if (procedure.issuer.equals(player)) {
                return procedure;
            }
        }

        return null;
    }

    public void finish() {
        recipient.save();
        recipient.checkGrants();

        procedures.remove(this);
    }

    public void cancel() {
        procedures.remove(this);
    }

}
