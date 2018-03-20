package cz.wake.corgibot;

import com.jagrosh.jdautilities.waiter.EventWaiter;
import cz.wake.corgibot.commands.CommandHandler;
import cz.wake.corgibot.listener.ChannelDeleteEvent;
import cz.wake.corgibot.listener.ChatListener;
import cz.wake.corgibot.listener.JoinEvent;
import cz.wake.corgibot.listener.LeaveEvent;
import cz.wake.corgibot.managers.BotManager;
import cz.wake.corgibot.runnable.ReminderTask;
import cz.wake.corgibot.runnable.SpamHandler;
import cz.wake.corgibot.runnable.StatusChanger;
import cz.wake.corgibot.sql.SQLManager;
import cz.wake.corgibot.utils.LoadingProperties;
import cz.wake.corgibot.utils.statuses.Checker;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class CorgiBot {

    private static CorgiBot instance;
    private static JDA jda;
    private CommandHandler ch = new CommandHandler();
    private SQLManager sql;
    private ChatListener chatListener;
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("MMMM yyyy HH:mm:ss");
    public static long startUp;
    private static final Map<String, Logger> LOGGERS;
    public static final Logger LOGGER;
    public static int commands = 0;
    private static boolean isBeta = true;

    static {
        new File("logs/latest.log").renameTo(new File("logs/log-" + getCurrentTimeStamp() + ".log"));
        LOGGERS = new ConcurrentHashMap<>();
        LOGGER = getLog(CorgiBot.class);
    }

    public static void main(String[] args) throws LoginException, InterruptedException {

        // Logo on start
        bootLogo();

        // Properties from config
        LoadingProperties config = new LoadingProperties();
        isBeta = config.isBeta();

        // JDA Event Waiter
        EventWaiter waiter = new EventWaiter();

        // Startup time
        startUp = System.currentTimeMillis();

        // JDA Build
        jda = new JDABuilder(AccountType.BOT)
                .setToken(config.getBotToken())
                .addEventListener(new ChatListener(waiter))
                .addEventListener(new LeaveEvent())
                .addEventListener(new JoinEvent())
                .addEventListener(new ChannelDeleteEvent())
                .addEventListener(waiter)
                .setGame(Game.playing("Starting..."))
                .setStatus(OnlineStatus.IDLE)
                .buildBlocking();

        // Instances
        (instance = new CorgiBot()).init();
        (instance = new CorgiBot()).initDatabase();

        // Load configuration for guilds
        BotManager.loadGuilds();

        // Startup timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new StatusChanger(), 10, 120000);
        timer.scheduleAtFixedRate(new SpamHandler(), 10, 1500); // 1.5s clear, higher = disaster

        // Is Corgi beta?
        if (!isBeta) {
            timer.scheduleAtFixedRate(new Checker(), 10, 60000);
            timer.scheduleAtFixedRate(new ReminderTask(getInstance()), 10, 20000);
        } else {
            LOGGER.info("Corgi spuštěn jako BETA! Některé funkce budou vypnuty!");
        }

        /* NASTAVENI NOVY PROFILOVKY
        jda.getSelfUser().getManager().setAvatar(Icon.from(
                new URL("https://i.imgur.com/N9wftHn.jpg").openStream())).complete();*/
    }

    public static CorgiBot getInstance() {
        return instance;
    }

    public static JDA getJda() {
        return jda;
    }

    public ChatListener getChatListener(){
        return chatListener;
    }

    public CommandHandler getCommandHandler() {
        return ch;
    }

    public SQLManager getSql() {
        return sql;
    }

    private void init() {
        ch.register();
    }

    private void initDatabase() {
        sql = new SQLManager(this);
    }

    public String formatTime(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth() + ". " + dateTime.format(timeFormat);
    }

    private static Logger getLog(String name) {
        return LOGGERS.computeIfAbsent(name, LoggerFactory::getLogger);
    }

    public static Logger getLog(Class<?> clazz) {
        return getLog(clazz.getName());
    }

    public TextChannel getGuildLogChannel() {
        return getJda().getGuildById("255045073887166475").getTextChannelById("361636711585021953");
    }

    public static Guild getDefaultGuild() {
        return getJda().getGuildById("255045073887166475");
    }

    private static void bootLogo() {
        LOGGER.info("Spousteni bota...");
        LOGGER.info("");
        LOGGER.info("   ______                 _ ");
        LOGGER.info("  / ____/___  _________ _(_)");
        LOGGER.info(" / /   / __ \\/ ___/ __ `/ / ");
        LOGGER.info("/ /___/ /_/ / /  / /_/ / /  ");
        LOGGER.info("\\____/\\____/_/   \\__, /_/   ");
        LOGGER.info("                /____/      ");
        LOGGER.info("");
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date now = new Date();
        return sdfDate.format(now);
    }
}
