package com.nicko.core.profile.punishment.command;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCursor;
import com.nicko.shinigami.command.CommandMeta;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.hastebin.HasteBin;
import com.nicko.core.util.string.CC;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@CommandMeta(label = "banlist", permission = "core.staff.banlist", async = true)
public class BanListCommand {

    public void execute(CommandSender sender) {
        Map<UUID, Punishment> punishments = Maps.newHashMap();

        try (MongoCursor<Document> cursor = Profile.getCollection().find().iterator()) {
            cursor.forEachRemaining(document -> {
                UUID uuid = UUID.fromString(document.getString("uuid"));
                if (document.getString("punishments") != null) {
                    JsonArray punishmentList = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();

                    for (JsonElement punishmentData : punishmentList) {
                        // Transforma num objeto
                        Punishment punishment = Punishment.DESERIALIZER.deserialize(punishmentData.getAsJsonObject());

                        if (punishment != null && !punishment.isRemoved() && !punishment.hasExpired()) {
                            punishments.put(uuid, punishment);
                        }
                    }
                }
            });
        }

        if (punishments.isEmpty()) {
            sender.sendMessage(CC.translate("&cNenhuma punição encontrada."));
            return;
        }
        StringBuilder string = new StringBuilder("Lista de punições (" + punishments.size() + "): \n");
        punishments.forEach((uuid, punishment) -> {
            Profile profile = Profile.getByUuid(uuid);

            String addedBy = "Console";

            if (punishment.getAddedBy() != null) {
                try {
                    Profile addedByProfile = Profile.getByUuid(punishment.getAddedBy());
                    addedBy = addedByProfile.getName();
                } catch (Exception e) {
                    addedBy = "Não encontrado...";
                }
            }

            string.append("Nome: ").append(profile.getName())
                .append(", Tipo: ").append(punishment.getType())
                .append(", Duração: ").append(punishment.getDurationText())
                .append(", Por: ").append(addedBy)
                .append(", Motivo: ").append(punishment.getAddedReason())
                .append(", Adicionada em: ").append(TimeUtil.dateToString(new Date(punishment.getAddedAt()), null))
                .append(", Servidor: ").append(Bukkit.getServerName())
                .append("\n");
        });

        for (String endpoint : HasteBin.ENDPOINTS) {
            try {
                String pasteURL = HasteBin.paste(string.toString(), endpoint);
                sender.sendMessage(CC.translate("&eURL da lista de punições:&7 " + pasteURL));
                return;
            } catch (IOException e) {
                continue;
            }
        }
        sender.sendMessage(CC.translate("&c(Falha no Upload)"));
    }
}
