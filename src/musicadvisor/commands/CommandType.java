package advisor.commands;

import java.util.Objects;

public enum CommandType {
    EXIT("exit", ExitCommand.class),
    AUTH("auth", AuthCommand.class),
    CATEGORIES("categories", CategoriesCommand.class),
    FEATURED("featured", FeaturedCommand.class),
    NEW_SONGS("new", NewSongsCommand.class),
    PLAYLISTS("playlists", PlaylistsCommand.class),
    NEXT("next", NextCommand.class),
    PREV("prev", PrevCommand.class),
    WRONG_COMMAND("", WrongCommand.class);

    private String typeCode;
    private Class<? extends CommandTemplate> commandTemplate;

    CommandType(String typeCode, Class<? extends CommandTemplate> commandTemplate) {
        this.typeCode = typeCode;
        this.commandTemplate = commandTemplate;
    }

    public static CommandType getTemplateByCode(String code) {
        for (CommandType type : CommandType.values()) {
            if (Objects.equals(type.typeCode, code)) {
                return type;
            }
        }
        return WRONG_COMMAND;
    }

    public CommandTemplate getCommandTemplate() throws InstantiationException, IllegalAccessException {
        return commandTemplate.newInstance();
    }
}