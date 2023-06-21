package de.ezeise;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String TOKEN = "token";
    private static final String PREFIX = "!";

    private Map<String, String> languageCodes;

    public Main() {
        languageCodes = new HashMap<>();
        languageCodes.put("en", "Englisch");
    }

    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault(TOKEN)
                .addEventListeners(new Main())
                .build();
        jda.awaitReady();
        System.out.println("Bot is online!");
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (author.isBot() || !content.startsWith(PREFIX))
            return;

        String[] parts = content.substring(PREFIX.length()).split(" ", 2);
        String command = parts[0].toLowerCase();

        if (command.equals("translate") && parts.length == 2) {
            String text = parts[1];
            String translation = translateText(text);
            TextChannel channel = event.getGuildChannel().asTextChannel();
            channel.sendMessageFormat("Übersetzung: %s", translation).queue();
        }
    }

    private String translateText(String text) {
        Translate translate = TranslateOptions.newBuilder().setApiKey("api_key").build().getService();
        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("en"));
        return translation.getTranslatedText();
    }
}