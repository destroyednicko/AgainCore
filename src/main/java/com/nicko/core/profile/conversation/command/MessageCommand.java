package com.nicko.core.profile.conversation.command;

import com.nicko.core.CoreAPI;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.conversation.Conversation;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"message", "msg", "whisper", "tell", "t", "m"})
public class MessageCommand {

    public void execute(Player player, @CPL("jogador") Player target, @CPL("mensagem") String message) {
        if (player.equals(target)) {
            player.sendMessage(CC.RED + " • Você não pode enviar uma mensagem para si mesmo!");
            //return;
        }

        if (target == null) {
            player.sendMessage(CC.RED + " • Nenhum jogador de nome " + target + " foi encontrado.");
            return;
        }

        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Profile targetProfile = Profile.getByUuid(target.getUniqueId());

        if (targetProfile.getConversations().canBeMessagedBy(player)) {
            Conversation conversation = playerProfile.getConversations().getOrCreateConversation(target);

            if (conversation.validate()) {
                conversation.sendMessage(player, target, message);
            } else {
                player.sendMessage(CC.RED + "Esse jogador não está recebendo mensagens privadas agora.");
            }
        } else {
            if ((CoreAPI.isVanish(target) && !player.hasPermission("core.staff"))) {
                player.sendMessage(CC.RED + " • Nenhum jogador de nome " + target + " foi encontrado.");
            } else {
                player.sendMessage(CC.RED + "Esse jogador não está recebendo mensagens privadas agora.");
            }
        }
    }

}
