package com.discord.bot.commands.musiccommands;

import com.discord.bot.audioplayer.GuildMusicManager;
import com.discord.bot.service.audioplayer.PlayerManagerService;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SwapCommand extends MusicPlayerCommand {

    public SwapCommand(PlayerManagerService playerManagerService, ChannelValidation channelValidation) {
        this.playerManagerService = playerManagerService;
        this.channelValidation = channelValidation;
    }

    @Override
    void operate(SlashCommandInteractionEvent event, EmbedBuilder embedBuilder) {
        GuildMusicManager musicManager = playerManagerService.getMusicManager(event);
        List<AudioTrack> trackList = new ArrayList<>(musicManager.scheduler.queue);

        if (trackList.size() > 1) {
            swapTrack(event, musicManager, trackList);
        } else if (trackList.size() == 1) {
            event.replyEmbeds(embedBuilder.setDescription("There is only one song in queue.")
                    .setColor(Color.RED).build()).queue();
        } else {
            event.replyEmbeds(embedBuilder.setDescription("Queue is empty.")
                    .setColor(Color.RED).build()).queue();
        }
    }

    @Override
    boolean isValidState(SlashCommandInteractionEvent event) {
        return channelValidation.isValid(event);
    }

    @Override
    String getFailDescription() {
        return "Please be in a same voice channel as bot.";
    }

    private void swapTrack(SlashCommandInteractionEvent event, GuildMusicManager musicManager, List<AudioTrack> trackList) {
        int first = event.getOption("songnum1").getAsInt() - 1;
        int second = event.getOption("songnum2").getAsInt() - 1;

        try {
            AudioTrack temp = trackList.get(first);
            trackList.set(first, trackList.get(second));
            trackList.set(second, temp);
        } catch (Exception e) {
            event.replyEmbeds(new EmbedBuilder()
                    .setDescription("Please enter a valid queue ids for both of the songs.")
                    .setColor(Color.RED).build()).queue();
            return;
        }

        musicManager.scheduler.queue.clear();
        for (AudioTrack track : trackList) {
            musicManager.scheduler.queue(track);
        }

        event.replyEmbeds(new EmbedBuilder()
                .setDescription("Successfully swapped order of two songs")
                .setColor(Color.GREEN).build()).queue();
    }
}
