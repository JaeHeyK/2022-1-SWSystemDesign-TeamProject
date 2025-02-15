package com.discord.bot.commands.musiccommands;

import com.discord.bot.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicHelpCommand implements ISlashCommand {
    ChannelValidation channelValidation;

    public MusicHelpCommand(ChannelValidation channelValidation) {
        this.channelValidation = channelValidation;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        showDetailOption(embedBuilder);
        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private void showDetailOption(EmbedBuilder embedBuilder) {
        embedBuilder.setTitle("Music Commands").setDescription("""
                        - /play
                        - /skip
                        - /pause
                        - /resume
                        - /leave
                        - /queue
                        - /swap
                        - /shuffle
                        - /loop
                        """)
                .setFooter("Bot can't play shorts.");
    }
}
