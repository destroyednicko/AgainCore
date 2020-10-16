package com.nicko.shinigami;

import com.nicko.shinigami.command.CommandOption;
import com.nicko.shinigami.command.adapter.CommandTypeAdapter;
import com.nicko.shinigami.map.MethodData;
import com.nicko.shinigami.map.ParameterData;
import com.nicko.shinigami.map.CommandData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ShinigamiExecutor {
    private final Shinigami shinigami;
    private final CommandSender sender;
    private final String label;
    private final CommandData commandData;
    private String[] args;

    public ShinigamiExecutor(final Shinigami shinigami, final CommandSender sender, final String label, final CommandData commandData, final String[] args) {
        this.shinigami = shinigami;
        this.sender = sender;
        this.label = label;
        this.commandData = commandData;
        this.args = args;
    }

    public void execute() {
        if (!this.commandData.getMeta().permission().equalsIgnoreCase("") && !this.sender.hasPermission(this.commandData.getMeta().permission())) {
            this.sender.sendMessage(ChatColor.RED + " • Você não tem permissão para executar este comando.");
            return;
        }
        Label_0828:
        for (final MethodData methodData : this.commandData.getMethodData()) {
            Label_0822:
            {
                if (methodData.getMethod().getDeclaringClass().equals(this.commandData.getInstance().getClass())) {
                    if (methodData.getParameterData().length - 1 > this.args.length) {
                        boolean doContinue = true;
                        for (final ParameterData parameterData : methodData.getParameterData()) {
                            if (parameterData.getType().equals(CommandOption.class) && methodData.getParameterData().length - 2 <= this.args.length) {
                                doContinue = false;
                                break;
                            }
                        }
                        if (doContinue) {
                            break Label_0822;
                        }
                    }
                    for (final MethodData otherMethodData : this.commandData.getMethodData()) {
                        if (!otherMethodData.equals(methodData)) {
                            if (methodData.getParameterData().length == otherMethodData.getParameterData().length && methodData.getParameterData()[0].getType().equals(CommandSender.class) && otherMethodData.getParameterData()[0].getType().equals(Player.class) && this.sender instanceof Player) {
                                break Label_0822;
                            }
                            if (this.args.length != methodData.getParameterData().length - 1 && this.args.length - methodData.getParameterData().length > this.args.length - otherMethodData.getParameterData().length) {
                                break Label_0822;
                            }
                        }
                    }
                    if (methodData.getParameterData().length > 0 && (methodData.getParameterData()[0].getType().equals(CommandSender.class) || methodData.getParameterData()[0].getType().equals(Player.class))) {
                        final List<Object> arguments = new ArrayList<>();
                        final ParameterData[] parameters = methodData.getParameterData();
                        arguments.add(this.sender);
                        if (!methodData.getParameterData()[0].getType().equals(Player.class) || this.sender instanceof Player) {
                            for (int i = 1; i < parameters.length; ++i) {
                                final ParameterData parameterData2 = parameters[i];
                                final CommandTypeAdapter adapter = this.shinigami.getTypeAdapter(parameterData2.getType());
                                if (adapter == null) {
                                    arguments.add(null);
                                } else {
                                    Object object;
                                    if (i == parameters.length - 1) {
                                        object = adapter.convert(StringUtils.join(this.args, " ", i - 1, this.args.length), (Class<Object>) parameterData2.getType());
                                    } else {
                                        object = adapter.convert(this.args[i - 1], parameterData2.getType());
                                    }
                                    if (parameterData2.getType().equals(CommandOption.class) && object == null) {
                                        final List<String> replacement = new ArrayList<String>(Arrays.asList(this.args));
                                        replacement.add(i - 1, null);
                                        this.args = replacement.toArray(new String[0]);
                                    }
                                    if (object instanceof CommandOption) {
                                        final CommandOption option = (CommandOption) object;
                                        if (!Arrays.asList(this.commandData.getMeta().options()).contains(option.getTag().toLowerCase())) {
                                            this.sender.sendMessage(ChatColor.RED + "Opção de comando desconhecida \"-" + option.getTag().toLowerCase() + "\"!");
                                            break Label_0828;
                                        }
                                    }
                                    arguments.add(object);
                                }
                            }
                            if (arguments.size() == parameters.length) {
                                try {
                                    methodData.getMethod().invoke(this.commandData.getInstance(), arguments.toArray());
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
        this.sender.sendMessage(this.getUsage());
    }

    private String getUsage() {
        final StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.RED).append(" • Sintaxe correta: /").append(this.label);
        if (this.commandData.getMeta().options().length > 0) {
            final List<String> options = new ArrayList<>();
            for (final String option : this.commandData.getMeta().options()) {
                options.add("-" + option.toLowerCase());
            }
            builder.append(" [");
            builder.append(StringUtils.join(options, ","));
            builder.append("]");
        }
        final Map<Integer, List<String>> arguments = new HashMap<>();
        for (final MethodData methodData : this.commandData.getMethodData()) {
            final ParameterData[] parameters = methodData.getParameterData();
            for (int i = 1; i < parameters.length; ++i) {
                final List<String> argument = arguments.getOrDefault(i - 1, new ArrayList<String>());
                final ParameterData parameterData = parameters[i];
                if (parameterData.getType().equals(CommandOption.class)) {
                    arguments.put(i - 1, null);
                } else {
                    if (parameterData.getCpl() != null) {
                        argument.add(parameterData.getCpl().value().toLowerCase());
                    } else {
                        final String name = parameterData.getName();
                        if (!argument.contains(name)) {
                            argument.add(name);
                        }
                    }
                    arguments.put(i - 1, argument);
                }
            }
        }
        for (int j = 0; j < arguments.size(); ++j) {
            final List<String> argument2 = arguments.get(j);
            if (argument2 != null) {
                builder.append(" <").append(StringUtils.join(argument2, "/")).append(">");
            }
        }
        return builder.toString();
    }
}
