package database;

import common.Constants;

public class ActionManagement {
    public static String manageActions(Action command, Database database) {
        switch (command.getActionType()) {
            case Constants.COMMAND:
                return CommandActions.executeCommand(database, command);
            case Constants.RECOMMENDATION:
                return RecommendationAction.executeRec(database, command);
            case Constants.QUERY:
                return QueryAction.executeQuery(database, command);
            default:
                return "";
        }
    }
}
