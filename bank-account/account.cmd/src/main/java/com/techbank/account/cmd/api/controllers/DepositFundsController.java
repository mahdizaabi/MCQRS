package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/depositfunds")
public class DepositFundsController {
    @Autowired
    private CommandDispatcher commandDispatcher;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(DepositFundsController.class);

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> depositFund(@PathVariable(value = "id") String id,
                                                    @RequestBody DepositFundsCommand command) {
        var SUCCESSFUL_MESSAGE_FUND_DEPOSIT = "Deposit found request completed successfully!";

        try {
            command.setIdentidier(id);
            commandDispatcher.send(command);
            return new ResponseEntity<BaseResponse>(new OpenAccountResponse(SUCCESSFUL_MESSAGE_FUND_DEPOSIT, id), HttpStatus.OK);

        } catch (IllegalStateException exc) {
            exc.printStackTrace();
            logger.error(exc.toString());
            return ResponseEntity.badRequest().body(new OpenAccountResponse(exc.toString()));
        } catch (Exception exc) {
            var safeMessageError = "Error while deposit fund request to open a new bank account for id: " + id;
            logger.error(safeMessageError);
            return new ResponseEntity<>(new BaseResponse(safeMessageError), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
