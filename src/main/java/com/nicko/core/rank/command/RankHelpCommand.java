package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = {"rank", "rank help", "rank ajuda"}, permission = "core.admin.rank")
public class RankHelpCommand {

    private static final String[][] HELP;

    static {
        HELP = new String[][]{
            new String[]{"ranks", "Lista todos os rankings existentes"},
            new String[]{"rank criar <nome>", "Cria um novo ranking"},
            new String[]{"rank deletar <rank>", "Deleta um ranking existente"},
            new String[]{"rank info <rank>", "Exibe as informações sobre um ranking"},
            new String[]{"rank setcor <rank> <cor>", "Define a cor do ranking"},
            new String[]{"rank setprefix <rank> <prefixo>", "Define o prefixo do ranking"},
            new String[]{"rank prioridade <rank> <peso>", "Define a prioridade do ranking"},
            new String[]{"rank addperm <rank> <permissão>", "Adiciona uma permissão ao ranking"},
            new String[]{"rank delperm <rank> <permissão>", "Remove uma permissão do ranking"},
            new String[]{"rank herdeiro <pai> <herdeiro>", "Define um herdeiro para o ranking 'pai'"},
            new String[]{"rank delherdeiro <pai> <herdeiro>", "Remove um herdeiro do ranking 'pai'"}
        };
    }

    public void execute(CommandSender sender) {
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.GOLD + " • Ranking - Ajuda");

        for (String[] help : HELP) {
            sender.sendMessage(CC.BLUE + "/" + help[0] + CC.GRAY + " - " + CC.RESET + help[1] + ".");
        }

        sender.sendMessage(CC.CHAT_BAR);
    }

}
