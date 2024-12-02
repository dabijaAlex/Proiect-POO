package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

@Getter @Setter
class PrintUsers extends Command {
     public PrintUsers(CommandInput command) {
         this.cmdName = command.getCommand();
         this.timestamp = command.getTimestamp();
     }
     public void execute() {}
}

@Getter @Setter
class PrintTransactions extends Command {
    public PrintTransactions(CommandInput command) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.email = command.getEmail();
    }
    public void execute() {
    }
}




