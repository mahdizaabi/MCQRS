package com.techbank.account.cmd.dto;

import com.techbank.account.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class OpenAccountResponse extends BaseResponse {
 private String id;
 public OpenAccountResponse(String message, String id){
     super(message);
     this.id = id;
 }
}
