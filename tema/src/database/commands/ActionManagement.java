package database.commands;

import common.Constants;
import database.Action;
import database.Database;

/**
 *  Class which will pass forward the command to other
 *  executional classes depending on their ActionType
 */
public final class ActionManagement {
    /**
     * For coding style
     */
    private ActionManagement() {
    }

    /**
     *  Method redirecting the command requested by the user
     *  according to the action type it implies
     *  If the action is undefined the user will only get a blank message
     */
    public static String manageActions(final Action command, final Database database) {
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
