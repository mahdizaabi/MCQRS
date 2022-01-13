package com.techbank.account.cmd.api.controllers;


import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/openbankaccount")
public class OpenAccountContoller {
    private final Logger logger = Logger.getLogger(OpenAccountContoller.class.getName());
    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping(path = "/")
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand openAccountCommand) {
        var SUCCESSFUL_MESSAGE_ACCOUNT_CREATION = "Bank account cretion request completed successfully!";
        var id = UUID.randomUUID().toString();
        openAccountCommand.setIdentidier(id);
        try {
            commandDispatcher.send(openAccountCommand);

            return new ResponseEntity<BaseResponse>(new OpenAccountResponse(SUCCESSFUL_MESSAGE_ACCOUNT_CREATION, id), HttpStatus.CREATED);
        } catch (IllegalStateException exc) {
            exc.printStackTrace();
            logger.log(Level.WARNING, MessageFormat.format("client made a bad request {}", exc.toString()));
            return ResponseEntity.badRequest().body(new OpenAccountResponse(exc.toString()));
        } catch (Exception exc) {
            var safeMessageError = "Error while processin request to open a new bank account for id: " + id;
            logger.warning(safeMessageError);
            return new ResponseEntity<>(new BaseResponse(safeMessageError), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


}
