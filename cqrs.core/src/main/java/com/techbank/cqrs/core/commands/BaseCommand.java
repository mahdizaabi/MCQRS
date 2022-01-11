package com.techbank.cqrs.core.commands;

import com.techbank.cqrs.core.messages.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public abstract class BaseCommand extends Message {
    public BaseCommand(String id){
        super(id);
    }

}
