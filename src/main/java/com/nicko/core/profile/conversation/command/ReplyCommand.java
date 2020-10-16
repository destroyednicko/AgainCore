package com.nicko.core.profile.conversation.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.conversation.Conversation;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"reply", "r", "responder"})
public class ReplyCommand {

    public void execute(Player player, @CPL("mensagem") String message) {
        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Conversation conversation = playerProfile.getConversations().getLastRepliedConversation();

        if (conversation != null) {
            if (conversation.validate()) {
                conversation.sendMessage(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())), message);
            } else {
                player.sendMessage(CC.RED + " • Você não pode mais responder a esse jogador.");
            }
        } else {
            player.sendMessage(CC.RED + " • Não há ninguém para responder :c.");
        }
    }

}
