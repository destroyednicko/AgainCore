package com.nicko.shinigami;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.nicko.core.Core;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import com.nicko.shinigami.command.CommandOption;
import com.nicko.shinigami.command.adapter.CommandTypeAdapter;
import com.nicko.shinigami.command.adapter.impl.*;
import com.nicko.shinigami.map.CommandData;
import com.nicko.shinigami.map.MethodData;
import com.nicko.shinigami.map.ParameterData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Shinigami implements Listener {
    private JavaPlugin plugin;
    private Map<Class, CommandTypeAdapter> adapters;
    private Map<String, CommandData> commands;

    public Shinigami(JavaPlugin plugin) {
        this.adapters = new HashMap<>();
        this.commands = new HashMap<>();
        this.plugin = plugin;
        this.registerTypeAdapter(Player.class, new PlayerTypeAdapter());
        this.registerTypeAdapter(OfflinePlayer.class, new OfflinePlayerTypeAdapter());
        this.registerTypeAdapter(String.class, new StringTypeAdapter());
        this.registerTypeAdapter(Integer.TYPE, new IntegerTypeAdapter());
        this.registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
        this.registerTypeAdapter(Number.class, new IntegerTypeAdapter());
        this.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        this.registerTypeAdapter(Boolean.TYPE, new BooleanTypeAdapter());
        this.registerTypeAdapter(World.class, new WorldTypeAdapter());
        this.registerTypeAdapter(GameMode.class, new GameModeTypeAdapter());
        this.registerTypeAdapter(CommandOption.class, new CommandOptionTypeAdapter());
        Bukkit.getPluginManager().registerEvents(this, plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.TAB_COMPLETE) {
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                String text = packet.getStrings().read(0);
                if (text.startsWith("/")) {
                    List<String> completed = Shinigami.this.handleTabCompletion(event.getPlayer(), text);
                    if (completed != null) {
                        event.setCancelled(true);
                        PacketContainer response = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);
                        response.getStringArrays().write(0, completed.toArray(new String[completed.size()]));
                        try {
                            ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onServerCommandEvent(ServerCommandEvent event) {
        event.setCancelled(this.handleExecution(event.getSender(), "/" + event.getCommand()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (Core.get().getEssentials().getCommandsBlocked().contains(event.getMessage()) && !event.getPlayer().hasPermission("core.commanblock.bypass")) {
            return;
        }
        event.setCancelled(this.handleExecution(event.getPlayer(), event.getMessage()));
    }

    private List<String> handleTabCompletion(Player player, String message) {
        String[] messageSplit = message.substring(1).split(" ");
        String cmdLine = message.substring(1);
        int spaceIndex = cmdLine.indexOf(' ');
        Set<String> completions = new HashSet<>();
        for (CommandData command : this.commands.values()) {
            if (!command.getMeta().permission().equalsIgnoreCase("") && !player.hasPermission(command.getMeta().permission())) {
                continue;
            }
            for (String alias : command.getMeta().label()) {
                String split = alias.split(" ")[0];

                if (spaceIndex != -1) {
                    split = alias;
                }

                if (StringUtil.startsWithIgnoreCase(split.trim(), cmdLine.trim()) ||
                    StringUtil.startsWithIgnoreCase(cmdLine.trim(), split.trim())) {
                    if (spaceIndex == -1 && cmdLine.length() < alias.length()) {
                        // Completa o comando
                        completions.add("/" + split.toLowerCase());
                    }
                }
            }

        }

        List<String> completionList = new ArrayList<>(completions);

        completionList.sort((o1, o2) -> (o2.length() - o1.length()));

        completionList.remove("w");

        if (!completionList.isEmpty()) {
            return (completionList);
        }
        CommandData commandData = null;
        String label = null;
        for (int remaining = messageSplit.length; remaining > 0; --remaining) {
            label = StringUtils.join(messageSplit, " ", 0, remaining);
            String finalLabel = label;
            if (this.commands.keySet().stream().anyMatch(command -> command.toLowerCase().startsWith(finalLabel.toLowerCase()))) {
                String key = this.commands.keySet().stream().filter(command ->
                    command.toLowerCase().startsWith(finalLabel.toLowerCase())).findFirst().orElse(null);
                CommandData possibleCommand = this.commands.get(key);
                if (label.split(" ").length != messageSplit.length + (message.endsWith(" ") ? 1 : 0)) {
                    commandData = possibleCommand;
                    break;
                }
            }
        }
        if (commandData != null) {
            String[] labelSplit = label.split(" ");
            String[] args = new String[0];
            if (messageSplit.length != labelSplit.length) {
                int numArgs = messageSplit.length - labelSplit.length;
                args = new String[numArgs];
                System.arraycopy(messageSplit, labelSplit.length, args, 0, numArgs);
            }
            return new ShinigamiTabCompleter(this, player, commandData, message, args).execute();
        }
        return null;
    }

    private boolean handleExecution(CommandSender commandSender, String message) {
        String[] messageSplit = message.substring(1).split(" ");
        CommandData commandData = null;
        String label = null;
        for (int remaining = messageSplit.length; remaining > 0; --remaining) {
            label = StringUtils.join(messageSplit, " ", 0, remaining);
            if (this.commands.get(label.toLowerCase()) != null) {
                commandData = this.commands.get(label.toLowerCase());
                break;
            }
        }
        if (commandData != null) {
            String[] labelSplit = label.split(" ");
            String[] args = new String[0];
            if (messageSplit.length != labelSplit.length) {
                int numArgs = messageSplit.length - labelSplit.length;
                args = new String[numArgs];
                System.arraycopy(messageSplit, labelSplit.length, args, 0, numArgs);
            }
            ShinigamiExecutor executor = new ShinigamiExecutor(this, commandSender, label.toLowerCase(), commandData, args);
            if (commandData.getMeta().async()) {
                new BukkitRunnable() {
                    public void run() {
                        executor.execute();
                    }
                }.runTaskAsynchronously(this.plugin);
            } else {
                executor.execute();
            }
            return true;
        }
        return false;
    }

    public void forceCommand(Player player, String command) {
        if (!command.startsWith("/")) {
            command = "/" + command;
        }
        this.handleExecution(player, command);
    }

    public void registerTypeAdapter(Class clazz, CommandTypeAdapter adapter) {
        this.adapters.put(clazz, adapter);
    }

    public CommandTypeAdapter getTypeAdapter(Class clazz) {
        return this.adapters.get(clazz);
    }

    public void registerCommand(Object object) {
        CommandMeta meta = object.getClass().getAnnotation(CommandMeta.class);
        if (meta == null) {
            throw new RuntimeException(new ClassNotFoundException(object.getClass().getName() + " faltando o @CommandMeta"));
        }
        List<MethodData> methodDataList = new ArrayList<>();
        for (Method method : object.getClass().getMethods()) {
            if (method.getParameterCount() != 0) {
                if (CommandSender.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    ParameterData[] parameterData = new ParameterData[method.getParameters().length];
                    for (int i = 0; i < method.getParameterCount(); ++i) {
                        Parameter parameter = method.getParameters()[i];
                        parameterData[i] = new ParameterData(parameter.getName(), parameter.getType(), parameter.getAnnotation(CPL.class));
                    }
                    methodDataList.add(new MethodData(method, parameterData));
                }
            }
        }
        CommandData commandData = new CommandData(object, meta, methodDataList.toArray(new MethodData[methodDataList.size()]));
        for (String label : this.getLabels(object.getClass(), new ArrayList<String>())) {
            this.commands.put(label.toLowerCase(), commandData);
        }
        if (meta.autoAddSubCommands()) {
            for (Class<?> clazz : object.getClass().getDeclaredClasses()) {
                if (clazz.getSuperclass().equals(object.getClass())) {
                    try {
                        this.registerCommand(clazz.getDeclaredConstructor(object.getClass()).newInstance(object));
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private List<String> getLabels(Class clazz, List<String> list) {
        List<String> toReturn = new ArrayList<String>();
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            CommandMeta meta = (CommandMeta) superClass.getAnnotation(CommandMeta.class);
            if (meta != null) {
                list = this.getLabels(superClass, list);
            }
        }
        CommandMeta meta = (CommandMeta) clazz.getAnnotation(CommandMeta.class);
        if (meta == null) {
            return list;
        }
        if (list.isEmpty()) {
            toReturn.addAll(Arrays.asList(meta.label()));
        } else {
            for (String prefix : list) {
                for (String label : meta.label()) {
                    toReturn.add(prefix + " " + label);
                }
            }
        }
        return toReturn;
    }
}
