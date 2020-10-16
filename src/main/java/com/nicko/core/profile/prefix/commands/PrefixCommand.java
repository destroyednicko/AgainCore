package com.nicko.core.profile.prefix.commands;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.prefix.Category;
import com.nicko.core.profile.prefix.Prefix;
import com.nicko.core.profile.prefix.menu.PrefixMenu;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"prefix", "prefixo"})
public class PrefixCommand {

    public void execute(Player player) {
        new PrefixMenu().openMenu(player);
    }

    @CommandMeta(label = {"ajuda", "?", "help"}, permission = "core.prefix.command")
    public class HelpCommand extends PrefixCommand {
        public void execute(Player player) {
            player.sendMessage(CC.translate("&7/prefixo criar <nome>"));
            player.sendMessage(CC.translate("&7/prefix deletar <nome>"));
            player.sendMessage(CC.translate("&7/prefixo set <nome> <prefixo>"));
            player.sendMessage(CC.translate("&7/setprefix <jogador> <prefixo>"));
        }
    }

    @CommandMeta(label = "criar", permission = "core.prefix.command")
    public class CreateCommand extends PrefixCommand {
        public void execute(Player player, @CPL("nome") String name, @CPL("categoria") String categoryName) {
            if (Prefix.getPrefixByName(name) != null) {
                player.sendMessage(CC.RED + " • Um prefixo com este nome já existe.");
                return;
            }
            Category category = Category.getByName(categoryName);
            if (category == null) {
                player.sendMessage(CC.RED + " • Categoria não encontrada.");
                return;
            }
            final Prefix prefix = new Prefix(name, category);
            prefix.save();
            player.sendMessage(CC.GREEN + " • Novo prefixo criado: " + prefix.getName());
        }
    }

    @CommandMeta(label = "deletar", permission = "core.prefix.command")
    public class DeleteCommand extends PrefixCommand {
        public void execute(Player player, @CPL("prefixo") String name) {
            Prefix prefix = Prefix.getPrefixByName(name);
            if (prefix == null) {
                player.sendMessage(CC.RED + " • Um prefixo com este nome não foi encontrado.");
                return;
            }

            prefix.delete();
            player.sendMessage(CC.GREEN + " • Prefixo removido: " + prefix.getName());
        }
    }

    @CommandMeta(label = "set", permission = "core.prefix.command")
    public class AddPrefixCommand extends PrefixCommand {
        public void execute(Player player, @CPL("nome") String name, @CPL("prefixo") String prefixDisplay) {
            Prefix prefix = Prefix.getPrefixByName(name);
            if (prefix == null) {
                player.sendMessage(CC.RED + " • Um prefixo com este nome não foi encontrado.");
                return;
            }
            prefix.setPrefix(CC.translate(prefixDisplay));
            prefix.save();
            player.sendMessage(CC.GREEN + " • Prefixo " + prefix.getName() + CC.GREEN + " atualizado para: " + prefix.getPrefix() + player.getName());
        }
    }

    @CommandMeta(label = "categoria", permission = "core.prefix.command")
    public class CategoryCommand extends PrefixCommand {
        public void execute(Player player, @CPL("nome") String name, @CPL("categoria") String categoryName) {
            Prefix prefix = Prefix.getPrefixByName(name);
            if (prefix == null) {
                player.sendMessage(CC.RED + " • Um prefixo com este nome não foi encontrado.");
                return;
            }
            Category category = Category.getByName(categoryName);
            if (category == null) {
                player.sendMessage(CC.RED + " • Categoria não encontrada.");
                return;
            }
            prefix.setCategory(category);
            prefix.save();
            player.sendMessage(CC.GREEN + " • Categoria " + prefix.getName() + " atualizada para: " + category.getName());
        }
    }

    @CommandMeta(label = "listar", permission = "core.prefix.command")
    public class ListCommand extends PrefixCommand {

        public void execute(Player player, @CPL("jogador") Player target) {
            player.sendMessage(CC.translate("&7 • Lista de prefixos de " + target.getName() + ": "));
            Prefix.getPrefixes().forEach(prefix -> {
                if (target.hasPermission(prefix.getPermission())) {
                    player.sendMessage(CC.translate(prefix.getName()));
                }
            });
        }

    }
}
