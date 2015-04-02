package com.github.otbproject.otbproject.commands;


import com.github.otbproject.otbproject.App;
import com.github.otbproject.otbproject.commands.loader.LoadedCommand;
import com.github.otbproject.otbproject.database.DatabaseWrapper;
import com.github.otbproject.otbproject.users.UserLevel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Command {

    public static LoadedCommand get(DatabaseWrapper db, String commandName) {
        LoadedCommand loadedCommand = new LoadedCommand();
        if (db.exists(CommandFields.TABLE_NAME, commandName, CommandFields.NAME)) {
            ResultSet rs = db.getRecord(CommandFields.TABLE_NAME, commandName, CommandFields.NAME);
            try {
                loadedCommand.setName(rs.getString(CommandFields.NAME));
                loadedCommand.setResponse(rs.getString(CommandFields.RESPONSE));
                loadedCommand.setCount(Integer.parseInt(rs.getString(CommandFields.COUNT)));
                loadedCommand.setEnabled(Boolean.valueOf(rs.getString(CommandFields.ENABLED)));
                loadedCommand.setDebug((Boolean.valueOf(rs.getString(CommandFields.DEBUG))));
                loadedCommand.setExecUserLevel(UserLevel.valueOf(rs.getString(CommandFields.EXEC_USER_LEVEL)));
                loadedCommand.setMinArgs(Integer.parseInt(rs.getString(CommandFields.MIN_ARGS)));
                loadedCommand.setScript(rs.getString(CommandFields.SCRIPT));
                loadedCommand.modifyingUserLevels = loadedCommand.new ModifyingUserLevels();
                loadedCommand.modifyingUserLevels.setNameModifyingUL(UserLevel.valueOf(rs.getString(CommandFields.NAME_MODIFYING_UL)));
                loadedCommand.modifyingUserLevels.setResponseModifyingUL(UserLevel.valueOf(rs.getString(CommandFields.RESPONSE_MODIFYING_UL)));
                loadedCommand.modifyingUserLevels.setUserLevelModifyingUL(UserLevel.valueOf(rs.getString(CommandFields.USER_LEVEL_MODIFYING_UL)));
            } catch (SQLException e) {
                App.logger.catching(e);
            } finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    App.logger.catching(e);
                }
            }
        }
        return loadedCommand;
    }

    public static ArrayList<String> getCommands(DatabaseWrapper db) {
        return db.getRecordsList(CommandFields.TABLE_NAME, CommandFields.NAME);
    }

    public static boolean update(DatabaseWrapper db, HashMap map) {
        return db.updateRecord(CommandFields.TABLE_NAME, map.get(CommandFields.NAME), CommandFields.NAME, map);
    }

    public static boolean exists(DatabaseWrapper db, String commandName) {
        return db.exists(CommandFields.TABLE_NAME, commandName, CommandFields.NAME);
    }

    public static boolean add(DatabaseWrapper db, HashMap map) {
        return db.insertRecord(CommandFields.TABLE_NAME, map);
    }

    public static boolean remove(DatabaseWrapper db, String commandName) {
        return db.removeRecord(CommandFields.TABLE_NAME, commandName, CommandFields.NAME);
    }

    public static void incrementCount(DatabaseWrapper db, String commandName) throws SQLException {
        LoadedCommand loadedCommand = get(db, commandName);
        loadedCommand.setCount(loadedCommand.getCount() + 1);
        addCommandFromLoadedCommand(db, loadedCommand);
    }

    public static void resetCount(DatabaseWrapper db, String commandName) throws SQLException {
        LoadedCommand loadedCommand = get(db, commandName);
        loadedCommand.setCount(0);
        addCommandFromLoadedCommand(db, loadedCommand);
    }

    public static boolean addCommandFromLoadedCommand(DatabaseWrapper db, LoadedCommand loadedCommand) {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(CommandFields.NAME, loadedCommand.getName());
        map.put(CommandFields.RESPONSE, loadedCommand.getResponse());
        map.put(CommandFields.EXEC_USER_LEVEL, loadedCommand.getExecUserLevel().name());
        map.put(CommandFields.MIN_ARGS, String.valueOf(loadedCommand.getMinArgs()));
        map.put(CommandFields.COUNT, String.valueOf(loadedCommand.getCount()));
        map.put(CommandFields.NAME_MODIFYING_UL, loadedCommand.modifyingUserLevels.getNameModifyingUL().name());
        map.put(CommandFields.RESPONSE_MODIFYING_UL, loadedCommand.modifyingUserLevels.getResponseModifyingUL().name());
        map.put(CommandFields.USER_LEVEL_MODIFYING_UL, loadedCommand.modifyingUserLevels.getUserLevelModifyingUL().name());
        map.put(CommandFields.SCRIPT, loadedCommand.getScript());
        map.put(CommandFields.ENABLED, String.valueOf(loadedCommand.isEnabled()));
        map.put(CommandFields.DEBUG, String.valueOf(loadedCommand.isDebug()));

        if (Command.exists(db, loadedCommand.getName())) {
            return Command.update(db, map);
        } else {
            return Command.add(db, map);
        }
    }
}
