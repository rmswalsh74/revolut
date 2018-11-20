package com.revolut.event.command;

public abstract class CommandEvent {
    public static final String INTRABANKTRANSFER="IntraBankTransfer";
    public static final String CREATE_ACCOUNT_COMMAND_EVENT ="CREATE_ACCOUNT_COMMAND_EVENT";
    public static final String CREATE_BANK_COMMAND_EVENT="CREATE_BANK_COMMAND_EVENT";
    public static final String CREATE_USER_COMMAND_EVENT="CREATE_USER_COMMAND_EVENT";

    public abstract String getType();
}
