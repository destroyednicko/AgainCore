package com.nicko.shinigami;

import com.nicko.shinigami.command.CommandOption;
import com.nicko.shinigami.command.adapter.CommandTypeAdapter;
import com.nicko.shinigami.map.CommandData;
import com.nicko.shinigami.map.MethodData;
import com.nicko.shinigami.map.ParameterData;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ShinigamiTabCompleter {
    private final Shinigami shinigami;
    private final CommandSender sender;
    private final CommandData commandData;
    private final String fullMessage;
    private String[] args;

    public ShinigamiTabCompleter(final Shinigami shinigami, final CommandSender sender, final CommandData commandData, final String fullMessage, final String[] args) {
        this.shinigami = shinigami;
        this.sender = sender;
        this.commandData = commandData;
        this.fullMessage = fullMessage;
        this.args = args;
    }

    public List<String> execute() {
        if (!this.commandData.getMeta().permission().equalsIgnoreCase("") && !this.sender.hasPermission(this.commandData.getMeta().permission())) {
            return null;
        }
        for (final MethodData methodData : this.commandData.getMethodData()) {
            if (methodData.getParameterData().length != 1) {
                final int paramsLength = methodData.getParameterData().length - 1;
                if (this.args.length <= paramsLength) {
                    int offset = 1;
                    final String[] args = this.args;
                    if (args.length == 0) {
                        ++offset;
                    }
                    if (paramsLength >= 2 && methodData.getParameterData()[1].getType() == CommandOption.class && args.length != 0 && !args[0].startsWith("-")) {
                        ++offset;
                    }
                    if (args.length <= paramsLength) {
                        final String source = this.fullMessage.endsWith(" ") ? "" : args[args.length - 1];
                        final ParameterData parameterData = methodData.getParameterData()[args.length + offset - 1];
                        final CommandTypeAdapter adapter = this.shinigami.getTypeAdapter(parameterData.getType());
                        if (adapter != null) {
                            return adapter.tabComplete(source, (Class<Object>) parameterData.getType());
                        }
                    }
                }
            }
        }
        return null;
    }
}
