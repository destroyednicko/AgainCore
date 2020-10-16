package com.nicko.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nicko.hollow.Hollow;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.nicko.core.cache.RedisCache;
import com.nicko.core.chat.Chat;
import com.nicko.core.chat.ChatListener;
import com.nicko.core.chat.command.ClearChatCommand;
import com.nicko.core.chat.command.MuteChatCommand;
import com.nicko.core.chat.command.SlowChatCommand;
import com.nicko.core.config.ConfigValidation;
import com.nicko.core.config.utils.BasicConfigurationFile;
import com.nicko.core.essentials.Essentials;
import com.nicko.core.essentials.EssentialsListener;
import com.nicko.core.essentials.command.*;
import com.nicko.core.essentials.tasks.AnnouncementTask;
import com.nicko.core.network.NetworkPacketListener;
import com.nicko.core.network.command.ReportCommand;
import com.nicko.core.network.command.RequestCommand;
import com.nicko.core.network.packet.*;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.ProfileListener;
import com.nicko.core.profile.ProfileTypeAdapter;
import com.nicko.core.profile.conversation.command.MessageCommand;
import com.nicko.core.profile.conversation.command.ReplyCommand;
import com.nicko.core.profile.conversation.command.ignore.IgnoreAddCommand;
import com.nicko.core.profile.conversation.command.ignore.IgnoreCommand;
import com.nicko.core.profile.conversation.command.ignore.IgnoreListCommand;
import com.nicko.core.profile.conversation.command.ignore.IgnoreRemoveCommand;
import com.nicko.core.profile.grant.GrantListener;
import com.nicko.core.profile.grant.command.GrantCommand;
import com.nicko.core.profile.grant.command.RevokeCommand;
import com.nicko.core.profile.notes.commands.*;
import com.nicko.core.profile.option.command.OptionsCommand;
import com.nicko.core.profile.option.command.ToggleGlobalChatCommand;
import com.nicko.core.profile.option.command.TogglePrivateMessagesCommand;
import com.nicko.core.profile.option.command.ToggleSoundsCommand;
import com.nicko.core.profile.permissions.PermissionsCommand;
import com.nicko.core.profile.prefix.Prefix;
import com.nicko.core.profile.prefix.commands.PrefixCommand;
import com.nicko.core.profile.prefix.commands.SetPrefixCommand;
import com.nicko.core.profile.prefix.commands.UnSetPrefixCommand;
import com.nicko.core.profile.punishment.BanReason;
import com.nicko.core.profile.punishment.command.*;
import com.nicko.core.profile.punishment.listener.PunishmentListener;
import com.nicko.core.profile.staff.command.*;
import com.nicko.core.profile.staff.listeners.StaffModeListener;
import com.nicko.core.rank.Rank;
import com.nicko.core.rank.RankTypeAdapter;
import com.nicko.core.rank.command.*;
import com.nicko.core.util.adapter.ChatColorTypeAdapter;
import com.nicko.core.util.duration.Duration;
import com.nicko.core.util.duration.DurationTypeAdapter;
import com.nicko.core.util.geoip.GeoIPAPI;
import com.nicko.core.util.hotbar.HotbarListener;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.MenuListener;
import com.nicko.core.util.string.CC;
import com.nicko.shinigami.Shinigami;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Getter
public class Core extends JavaPlugin {

    public static final Gson GSON = new Gson();
    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();

    private static Core core;

    private BasicConfigurationFile mainConfig;

    private GeoIPAPI geoIPAPI;

    private Shinigami shinigami;
    private Hollow hollow;

    private MongoDatabase mongoDatabase;
    private JedisPool jedisPool;
    private RedisCache redisCache;

    private Essentials essentials;
    private Chat chat;

    public BasicConfigurationFile getMainConfig() {
        return mainConfig;
    }

    @Override
    public void onEnable() {
        core = this;

        mainConfig = new BasicConfigurationFile(this, "config");

        new ConfigValidation(mainConfig.getFile(), mainConfig.getConfiguration(), 4).check();

        loadMongo();
        loadRedis();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        redisCache = new RedisCache(this);
        essentials = new Essentials(this);
        chat = new Chat(this);

        geoIPAPI = new GeoIPAPI(this);

        shinigami = new Shinigami(this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Prefix.load();
        BanReason.load();

        Arrays.asList(
            // Comandos 'Essentials'
            new HubCommand(),
            new BroadcastCommand(),
            new ClearCommand(),
            new DayCommand(),
            new FlyCommand(),
            new InvModify(),
            new NoteCommand(),
            new NoteAddCommand(),
            new NoteRemoveCommand(),
            new NoteUpdateCommand(),
            new NotesCommand(),
            new EnchantmentCommand(),
            new GameModeCommand(),
            new GMCCommand(),
            new GMSCommand(),
            new VoteCommand(),
            new VoteClaimCommand(),
            new FeedCommand(),
            new HelpCommand(),
            new HealCommand(),
            new HidePlayerCommand(),
            new InvseeCommand(),
            new ListCommand(),
            new LagCommand(),
            new KillallCommand(),
            new KoreReloadCommand(),
            new LocationCommand(),
            new MoreCommand(),
            new NightCommand(),
            new PingCommand(),
            new RenameCommand(),
            new SetSlotsCommand(),
            new SetSpawnCommand(),
            new ShowAllPlayersCommand(),
            new ShowPlayerCommand(),
            new SkullCommand(),
            new TPHereCommand(),
            new SpawnCommand(),
            new SudoAllCommand(),
            new SudoCommand(),
            new SunsetCommand(),
            new TeleportWorldCommand(),
            new OptionsCommand(),
            new ToggleGlobalChatCommand(),
            new TogglePrivateMessagesCommand(),
            new ToggleSoundsCommand(),
            new ReportCommand(),
            new RequestCommand(),
            new ToggleDonorOnlyCommand(),
            new RestartCommand(),

            // Comandos 'Staff'
            new WhoisCommand(),
            new JoinCommand(),
            new FreezeCommand(),
            new ClearChatCommand(),
            new SlowChatCommand(),
            new AltsCommand(),
            new AuthCommand(),
            new InvalidateAuth(),
            new ResetAuth(),
            new SocialSpyCommand(),
            new BanCommand(),
            new BlacklistCommand(),
            new UnblacklistCommand(),
            new CheckCommand(),
            new KickCommand(),
            new CancelBanCommand(),
            new MuteCommand(),
            new PunishmentCommand(),
            new UnbanCommand(),
            new UnmuteCommand(),
            new WarnCommand(),
            new MuteChatCommand(),
            new StaffChatCommand(),
            new ToggleStaffChat(),
            new VanishCommand(),
            new HideStaffCommand(),
            new BanListCommand(),

            // Comandos 'Permissões'
            new GrantCommand(),
            new RevokeCommand(),
            new RankAddPermissionCommand(),
            new RankCreateCommand(),
            new RankDeleteCommand(),
            new RankHelpCommand(),
            new RankInfoCommand(),
            new RankInheritCommand(),
            new RankRemovePermissionCommand(),
            new RanksCommand(),
            new RankSetColorCommand(),
            new RankSetPrefixCommand(),
            new RankSetWeightCommand(),
            new RankUninheritCommand(),
            new ClearPunishmentsCommand(),
            new PermissionsCommand(),

            // Comandos 'Mensagens'
            new MessageCommand(),
            new ReplyCommand(),
            new IgnoreCommand(),
            new IgnoreAddCommand(),
            new IgnoreListCommand(),
            new IgnoreRemoveCommand(),

            new PrefixCommand(),
            new SetPrefixCommand(),
            new UnSetPrefixCommand()
        ).forEach(shinigami::registerCommand);

        shinigami.registerTypeAdapter(Rank.class, new RankTypeAdapter());
        shinigami.registerTypeAdapter(Profile.class, new ProfileTypeAdapter());
        shinigami.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        shinigami.registerTypeAdapter(ChatColor.class, new ChatColorTypeAdapter());

        hollow = new Hollow("core",
            mainConfig.getString("REDIS.HOST"),
            mainConfig.getInteger("REDIS.PORT"),
            mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
                mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
        );

        Arrays.asList(
            PacketAddGrant.class,
            PacketAnticheatAlert.class,
            PacketBroadcastPunishment.class,
            PacketDeleteGrant.class,
            PacketDeleteRank.class,
            PacketRefreshRank.class,
            PacketStaffAuth.class,
            PacketStaffChat.class,
            PacketStaffJoinNetwork.class,
            PacketStaffLeaveNetwork.class,
            PacketStaffSwitchServer.class,
            PacketStaffReport.class,
            PacketStaffRequest.class,
            PacketClearGrants.class,
            PacketClearPunishments.class
        ).forEach(hollow::registerPacket);

        hollow.registerListener(new NetworkPacketListener(this));

        Arrays.asList(
            new ProfileListener(),
            new MenuListener(),
            new EssentialsListener(),
            new ChatListener(),
            new GrantListener(),
            new PunishmentListener(),
            new HotbarListener(),
            new StaffModeListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        if (mainConfig.getBoolean("STAFF.TP_COMMAND")) {
            shinigami.registerCommand(new TeleportCommand());
        }

        if (mainConfig.getBoolean("STAFF.MOD_COMMAND")) {
            shinigami.registerCommand(new StaffModeCommand());
        }


        Rank.init();
        new AnnouncementTask();
        ItemBuilder.registerGlow();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ProfileListener.leavePlayer(player, true);
        });
        try {
            jedisPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMongo() {
        if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(mainConfig.getString("MONGO.HOST"),
                mainConfig.getInteger("MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
                mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
                .getDatabase("core");
        } else {
            mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"),
                mainConfig.getInteger("MONGO.PORT")).getDatabase("core");
        }
    }

    private void loadRedis() {
        jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

        if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
            }
        }
    }


    /**
     * Envia uma mensagem para todos os 'OPs'.
     *
     * @param message A mensagem.
     */
    public static void broadcastOps(String message) {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(CC.translate(message)));
    }

    public static Core get() {
        return core;
    }

}
