package cz.wake.corgibot.commands.user;

import com.jagrosh.jdautilities.waiter.EventWaiter;
import cz.wake.corgibot.CorgiBot;
import cz.wake.corgibot.annotations.SinceCorgi;
import cz.wake.corgibot.commands.CommandCategory;
import cz.wake.corgibot.objects.GuildWrapper;
import cz.wake.corgibot.utils.Constants;
import cz.wake.corgibot.utils.MessageUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

@SinceCorgi(version = "1.3.0")
public class Donate implements Command {

    @Override
    public void onCommand(MessageChannel channel, Message message, String[] args, Member member, EventWaiter w, GuildWrapper gw) {
        channel.sendMessage(MessageUtils.getEmbed(Constants.WHITE).setTitle("Podpoř Corgiho")
                .setDescription("Corgi byl, je a vždy bude zdarma. Ovšem bez kvalitních serverů nelze provozovat kvalitního bota. Pokuď nám tedy chceš pomoc, můžeš nám přispět. Jako odměnu dostaneš speciální rank na Support serveru u Corgiho.\n\n**Donate**: https://donorbox.org/corgibot")
                .setFooter("Všem, kteří přispějí děkujeme!", null)
                .setThumbnail(CorgiBot.getJda().getSelfUser().getAvatarUrl()).build()).queue();
    }

    @Override
    public String getCommand() {
        return "donate";
    }

    @Override
    public String getDescription() {
        return "Podpoř Corgiho v jeho růstu!";
    }

    @Override
    public String getHelp() {
        return "%donate - Zobrazí odkaz na donate pro Corgiho";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERAL;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"donator"};
    }
}
