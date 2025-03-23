package states;

public enum MainPageStates {
    AUTHENTICATION_LOGIN,
    AUTHENTICATION_REGISTER,

    PROFILE_VIEW,
    PROFILE_EDIT,

    DASHBOARD_MAIN,
    DASHBOARD_ALERTS,
    DASHBOARD_REPORTS,

    INVENTORY_MAIN;

    public static MainPageStates currentState = AUTHENTICATION_LOGIN;

    public static void setCurrentState(MainPageStates newState) {
        currentState = newState;
    }
}
