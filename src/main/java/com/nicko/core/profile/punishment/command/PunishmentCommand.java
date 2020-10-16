package com.nicko.core.profile.punishment.command;

import com.mongodb.client.model.Filters;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.punishment.BanReason;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.util.duration.Duration;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "punishmenttype", permission = "core.staff.punishment", async = true)
public class PunishmentCommand {

    public void execute(Player player) {
        player.sendMessage(CC.RED + "Uso:");
        player.sendMessage(CC.RED + "/punishmenttype add <nome> <tipo> <tempo> <admitir>");
        player.sendMessage(CC.RED + "/punishmenttype remover <nome>");
    }

    @CommandMeta(label = "add")
    public class AddCommand extends PunishmentCommand {
        public void execute(Player player, @CPL("nome") String name,
                            @CPL("tipo") String type, @CPL("tempo") String duration, @CPL("admitir") Boolean admit) {

            if (Duration.fromString(duration).getValue() == -1) {
                player.sendMessage(CC.RED + "A duração é inválida.");
                player.sendMessage(CC.RED + "Exemplo: [perm/1y1m1w1d]");
                return;
            }

            PunishmentType punishmentType;
            if (type.equalsIgnoreCase("tempban") && (!duration.contains("permanente") || !duration.contains("perm"))) {
                punishmentType = PunishmentType.TEMP_BAN;
            } else {
                punishmentType = PunishmentType.getByName(type);
                duration = "permanente";
            }

            if (punishmentType == null) {
                player.sendMessage(CC.RED + "Tipo de punição não encontrado");
                return;
            }

            BanReason banReason = new BanReason(name, duration, punishmentType, admit);

            banReason.save();
            player.sendMessage(CC.GREEN + "Tipo de punição criado com sucesso");
        }
    }

    @CommandMeta(label = "remover")
    public class RemoveCommand extends PunishmentCommand {
        public void execute(Player player, @CPL("nome") String name) {

            BanReason banReason = BanReason.getByReason(name);

            if (banReason == null) {
                player.sendMessage(CC.RED + "Tipo de punição não encontrado.");
                return;
            }

            BanReason.getBanReasons().remove(banReason);
            BanReason.getCollection().deleteOne(Filters.eq("reason", banReason.getReason()));
            player.sendMessage(CC.GREEN + "Tipo de punição removido com sucesso.");
        }
    }

}
