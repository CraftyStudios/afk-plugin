package me.Incbom.afk.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class Logger {
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void log(LogLevel level, String message) {
        if (message == null) return;

        switch (level) {
            case ERROR:
                console.sendMessage(ChatColor.RED + "[ERROR] " + ChatColor.RESET + message);
                break;
            case WARNING:
                console.sendMessage(ChatColor.YELLOW + "[WARNING] " + ChatColor.RESET + message);
                break;
            case INFO:
                console.sendMessage(ChatColor.BLUE + "[INFO] " + ChatColor.RESET + message);
                break;
            case SUCCESS:
                console.sendMessage(ChatColor.GREEN + "[SUCCESS] " + ChatColor.RESET + message);
                break;
            case OUTLINE:
                console.sendMessage(ChatColor.GRAY + message);
                break;
        }
    }

    public enum LogLevel { ERROR, WARNING, INFO, SUCCESS, OUTLINE }
}
